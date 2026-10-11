import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dao.PedidoDAO;
import dao.ProdutoDAO;
import dao.UsuarioDAO;
import enums.CategoriaProduto;
import exception.PedidoJaPagoException;
import model.Carrinho;
import model.Cliente;
import model.ItemCarrinho;
import model.Pedido;
import model.Produto;
import model.ProdutoInternacional;
import model.ProdutoNacional;
import model.Usuario;
import pagamento.PagamentoCredito;
import pagamento.PagamentoDebito;
import pagamento.PagamentoMIX;

public class Main {

    public static void main(String[] args) {
        try {
            Cliente cliente = testarUsuario();
            int[] idsProdutos = testarProdutos();
            testarPedido(cliente, idsProdutos[0], idsProdutos[1]);
        } catch (SQLException e) {
            System.out.println("Erro de banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Cliente testarUsuario() throws SQLException {
        System.out.println("=== Usuário ===");
        UsuarioDAO dao = new UsuarioDAO();

        // E-mail e CPF mudam a cada execução, porque as duas colunas são UNIQUE no banco.
        long agora = System.currentTimeMillis();
        String email = "teste" + agora + "@email.com";
        String cpf = String.format("%011d", agora % 100_000_000_000L);

        Cliente cliente = new Cliente("Rua Teste, 123", 0, "Cliente Teste", email, "senha123", cpf);
        dao.inserir(cliente);
        System.out.println("Cliente gravado com id: " + cliente.getId());

        Optional<Usuario> encontrado = dao.buscarPorEmail(email);
        encontrado.ifPresentOrElse(
            u -> {
                System.out.println("Cliente encontrado no banco:");
                u.exibirDados();
            },
            () -> System.out.println("Não encontrou o cliente (algo deu errado).")
        );

        return cliente;
    }

    private static int[] testarProdutos() throws SQLException {
        System.out.println();
        System.out.println("=== Produtos ===");
        ProdutoDAO dao = new ProdutoDAO();

        ProdutoNacional nacional = new ProdutoNacional(
                0, "Mouse Gamer", "Mouse com fio", 89.90, 50, 120.0, CategoriaProduto.ELETRONICO);
        ProdutoInternacional internacional = new ProdutoInternacional(
                0, "Fone Bluetooth", "Fone importado", 249.90, 30, 8000.0, 60.0, CategoriaProduto.ELETRONICO);

        dao.inserir(nacional);
        dao.inserir(internacional);
        System.out.println("Produtos gravados com ids: " + nacional.getId() + " e " + internacional.getId());

        Optional<Produto> lido = dao.buscarPorId(internacional.getId());
        lido.ifPresentOrElse(
            p -> {
                System.out.println("Produto lido do banco: " + p.getNome() + " (" + p.getClass().getSimpleName() + ")");
                System.out.println("Preço: " + p.getPreco() + " | Estoque: " + p.getEstoque() + " | Frete: " + p.calcularFrete());
                if (p instanceof ProdutoInternacional pi) {
                    System.out.println("Imposto de importação: " + pi.calcularImpostoImportacao());
                }
            },
            () -> System.out.println("Não encontrou o produto (algo deu errado).")
        );

        dao.atualizarEstoque(nacional.getId(), 45);
        dao.buscarPorId(nacional.getId()).ifPresent(
            p -> System.out.println("Estoque do " + p.getNome() + " depois da atualização: " + p.getEstoque())
        );

        List<Produto> todos = dao.listarTodos();
        System.out.println("Total de produtos no banco: " + todos.size());

        return new int[] {nacional.getId(), internacional.getId()};
    }

    private static void testarPedido(Cliente cliente, int idNacional, int idInternacional) throws SQLException {
        System.out.println();
        System.out.println("=== Pedido ===");
        ProdutoDAO produtoDAO = new ProdutoDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();

        // Relê os produtos do banco, para o estoque em memória bater com o do banco.
        Produto nacional = produtoDAO.buscarPorId(idNacional).orElseThrow();
        Produto internacional = produtoDAO.buscarPorId(idInternacional).orElseThrow();

        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(new ItemCarrinho(nacional, 2));
        carrinho.adicionarItem(new ItemCarrinho(internacional, 1));

        Pedido pedido = new Pedido(0, cliente, carrinho);
        pedidoDAO.inserir(pedido);
        System.out.println("Pedido gravado com id: " + pedido.getId() + " | total: " + pedido.calcularTotal());

        // Divide o total ao meio: metade + metade dá exatamente o total (o MIX compara com !=).
        double total = pedido.calcularTotal();
        double metade = total / 2;
        pedido.setFormaPagamento(new PagamentoMIX(new PagamentoDebito(), new PagamentoCredito(), metade, metade));

        pedidoDAO.pagar(pedido);
        System.out.println("Status depois de pagar: " + pedido.getStatus());

        // Lê o pedido de volta do banco e confere.
        Pedido lido = pedidoDAO.buscarPorId(pedido.getId()).orElseThrow();
        System.out.println("Pedido lido do banco -> status: " + lido.getStatus()
                + " | itens: " + lido.getCarrinho().getItens().size()
                + " | total: " + lido.calcularTotal()
                + " | forma de pagamento: " + lido.getFormaPagamento().getClass().getSimpleName());

        // O estoque no banco tem que ter baixado: 45 - 2 e 30 - 1.
        produtoDAO.buscarPorId(idNacional).ifPresent(
            p -> System.out.println("Estoque do " + p.getNome() + " no banco: " + p.getEstoque() + " (esperado 43)"));
        produtoDAO.buscarPorId(idInternacional).ifPresent(
            p -> System.out.println("Estoque do " + p.getNome() + " no banco: " + p.getEstoque() + " (esperado 29)"));

        // Tentar pagar de novo precisa ser bloqueado.
        try {
            pedidoDAO.pagar(lido);
            System.out.println("ERRO: o pagamento duplicado não foi bloqueado.");
        } catch (PedidoJaPagoException e) {
            System.out.println("Pagamento duplicado bloqueado: " + e.getMessage());
        }
    }
}