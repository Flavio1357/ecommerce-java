package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import enums.StatusPedido;
import exception.ProdutoIndisponivelException;
import interfaces.FormaPagamento;
import model.Carrinho;
import model.Cliente;
import model.ItemCarrinho;
import model.Pedido;
import model.Produto;
import pagamento.PagamentoBoleto;
import pagamento.PagamentoCredito;
import pagamento.PagamentoDebito;
import pagamento.PagamentoMIX;

public class PedidoDAO {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    // Linhas lidas do banco, guardadas antes de montar os objetos do modelo.
    private record LinhaItem(int produtoId, int quantidade, double precoUnitario) {
    }

    private record LinhaPagamento(String forma, double valor) {
    }

    /**
     * Grava o pedido e os itens do carrinho numa transação só.
     * O cliente e os produtos precisam já estar gravados no banco (id maior que zero).
     */
    public void inserir(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (cliente_id, status) VALUES (?, ?)";
        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexaofactory.getConnection()) {
            con.setAutoCommit(false);

            try {
                try (PreparedStatement ps = con.prepareStatement(sqlPedido, new String[] {"id"})) {
                    ps.setInt(1, pedido.getCliente().getId());
                    ps.setString(2, pedido.getStatus().name());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            pedido.setId(rs.getInt(1));
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(sqlItem)) {
                    for (ItemCarrinho item : pedido.getCarrinho().getItens()) {
                        ps.setInt(1, pedido.getId());
                        ps.setInt(2, item.getProduto().getId());
                        ps.setInt(3, item.getQtd());
                        // Preço do momento da compra: se o produto mudar de preço depois, o pedido não muda.
                        ps.setDouble(4, item.getProduto().getPreco());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                pedido.setId(0);
                throw e;
            }
        }
    }

    /**
     * Paga o pedido. Primeiro roda a regra de negócio do próprio Pedido (valida, processa e baixa o
     * estoque em memória). Depois grava tudo no banco numa transação: baixa de estoque, pagamentos
     * e status. Se o banco falhar, nada é gravado e o objeto em memória volta ao estado anterior.
     */
    public void pagar(Pedido pedido) throws SQLException {
        if (pedido.getId() <= 0) {
            throw new IllegalStateException("Grave o pedido no banco antes de pagar");
        }

        pedido.pagar();

        try {
            gravarPagamento(pedido);
        } catch (SQLException | RuntimeException e) {
            desfazerEmMemoria(pedido);
            throw e;
        }
    }

    private void gravarPagamento(Pedido pedido) throws SQLException {
        try (Connection con = Conexaofactory.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1) Baixa de estoque no banco. O "AND estoque >= ?" impede estoque negativo
                //    mesmo que outro pedido tenha comprado o produto nesse meio tempo.
                String sqlEstoque = "UPDATE produto SET estoque = estoque - ? WHERE id = ? AND estoque >= ?";
                try (PreparedStatement ps = con.prepareStatement(sqlEstoque)) {
                    for (ItemCarrinho item : pedido.getCarrinho().getItens()) {
                        ps.setInt(1, item.getQtd());
                        ps.setInt(2, item.getProduto().getId());
                        ps.setInt(3, item.getQtd());

                        if (ps.executeUpdate() == 0) {
                            throw new ProdutoIndisponivelException(
                                    "Estoque insuficiente no banco para o produto " + item.getProduto().getNome());
                        }
                    }
                }

                // 2) Uma linha por forma de pagamento (o MIX gera duas).
                inserirPagamentos(con, pedido.getId(), pedido.getFormaPagamento(), pedido.calcularTotal());

                // 3) Status do pedido.
                try (PreparedStatement ps = con.prepareStatement("UPDATE pedido SET status = ? WHERE id = ?")) {
                    ps.setString(1, StatusPedido.PAGO.name());
                    ps.setInt(2, pedido.getId());
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        }
    }

    private void inserirPagamentos(Connection con, int pedidoId, FormaPagamento forma, double valor)
            throws SQLException {

        if (forma instanceof PagamentoMIX mix) {
            inserirPagamentos(con, pedidoId, mix.getPagamento1(), mix.getValorPagamento1());
            inserirPagamentos(con, pedidoId, mix.getPagamento2(), mix.getValorPagamento2());
            return;
        }

        String sql = "INSERT INTO pagamento (pedido_id, forma, valor) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            ps.setString(2, nomeDaForma(forma));
            ps.setDouble(3, valor);
            ps.executeUpdate();
        }
    }

    private void desfazerEmMemoria(Pedido pedido) {
        for (ItemCarrinho item : pedido.getCarrinho().getItens()) {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQtd());
        }
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
    }

    public boolean atualizarStatus(int pedidoId, StatusPedido novoStatus) throws SQLException {
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";

        try (Connection con = Conexaofactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, novoStatus.name());
            ps.setInt(2, pedidoId);
            return ps.executeUpdate() > 0;
        }
    }

    /** Busca o pedido completo: cliente, itens (com o preço da compra) e forma de pagamento. */
    public Optional<Pedido> buscarPorId(int id) throws SQLException {
        int clienteId = 0;
        StatusPedido status = null;
        List<LinhaItem> linhas = new ArrayList<>();
        List<LinhaPagamento> pagamentos = new ArrayList<>();

        try (Connection con = Conexaofactory.getConnection()) {

            try (PreparedStatement ps = con.prepareStatement("SELECT cliente_id, status FROM pedido WHERE id = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return Optional.empty();
                    }
                    clienteId = rs.getInt("cliente_id");
                    status = StatusPedido.valueOf(rs.getString("status"));
                }
            }

            String sqlItens = "SELECT produto_id, quantidade, preco_unitario FROM item_pedido "
                    + "WHERE pedido_id = ? ORDER BY id";
            try (PreparedStatement ps = con.prepareStatement(sqlItens)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        linhas.add(new LinhaItem(
                                rs.getInt("produto_id"), rs.getInt("quantidade"), rs.getDouble("preco_unitario")));
                    }
                }
            }

            String sqlPagamentos = "SELECT forma, valor FROM pagamento WHERE pedido_id = ? ORDER BY id";
            try (PreparedStatement ps = con.prepareStatement(sqlPagamentos)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        pagamentos.add(new LinhaPagamento(rs.getString("forma"), rs.getDouble("valor")));
                    }
                }
            }
        }

        Cliente cliente = usuarioDAO.buscarPorId(clienteId)
                .filter(u -> u instanceof Cliente)
                .map(u -> (Cliente) u)
                .orElseThrow(() -> new IllegalStateException("Cliente do pedido " + id + " não encontrado"));

        Carrinho carrinho = new Carrinho();
        for (LinhaItem linha : linhas) {
            Produto produto = produtoDAO.buscarPorId(linha.produtoId())
                    .orElseThrow(() -> new IllegalStateException("Produto " + linha.produtoId() + " não encontrado"));

            // O produto é um objeto novo, só deste pedido, então dá para usar o preço da compra nele.
            produto.setPreco(linha.precoUnitario());

            // O construtor do ItemCarrinho compara a quantidade com o estoque atual, e num pedido já
            // pago o estoque já foi baixado. Por isso cria com 0 e ajusta a quantidade em seguida.
            ItemCarrinho item = new ItemCarrinho(produto, 0);
            item.setQtd(linha.quantidade());
            carrinho.adicionarItem(item);
        }

        Pedido pedido = new Pedido(id, cliente, carrinho);
        pedido.setStatus(status);
        pedido.setFormaPagamento(montarForma(pagamentos));
        return Optional.of(pedido);
    }

    private FormaPagamento montarForma(List<LinhaPagamento> pagamentos) {
        if (pagamentos.isEmpty()) {
            return null;
        }
        if (pagamentos.size() == 1) {
            return criarForma(pagamentos.get(0).forma());
        }
        if (pagamentos.size() == 2) {
            LinhaPagamento p1 = pagamentos.get(0);
            LinhaPagamento p2 = pagamentos.get(1);
            return new PagamentoMIX(criarForma(p1.forma()), criarForma(p2.forma()), p1.valor(), p2.valor());
        }
        throw new IllegalStateException("Pedido com mais de duas formas de pagamento");
    }

    private FormaPagamento criarForma(String nome) {
        return switch (nome) {
            case "DEBITO" -> new PagamentoDebito();
            case "CREDITO" -> new PagamentoCredito();
            case "BOLETO" -> new PagamentoBoleto();
            default -> throw new IllegalStateException("Forma de pagamento desconhecida: " + nome);
        };
    }

    private String nomeDaForma(FormaPagamento forma) {
        if (forma instanceof PagamentoDebito) {
            return "DEBITO";
        }
        if (forma instanceof PagamentoCredito) {
            return "CREDITO";
        }
        if (forma instanceof PagamentoBoleto) {
            return "BOLETO";
        }
        throw new IllegalArgumentException("Forma de pagamento não suportada: " + forma.getClass());
    }
}