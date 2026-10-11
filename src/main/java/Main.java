import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import dao.ProdutoDAO;
import dao.UsuarioDAO;
import enums.CategoriaProduto;
import model.Cliente;
import model.Produto;
import model.ProdutoInternacional;
import model.ProdutoNacional;
import model.Usuario;

public class Main {

    public static void main(String[] args) {
        try {
            testarUsuario();
            testarProdutos();
        } catch (SQLException e) {
            System.out.println("Erro de banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testarUsuario() throws SQLException {
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
    }

    private static void testarProdutos() throws SQLException {
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

        // Busca o internacional de volta e confere se o percentual de importação voltou do banco.
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

        // Testa a atualização de estoque.
        dao.atualizarEstoque(nacional.getId(), 45);
        dao.buscarPorId(nacional.getId()).ifPresent(
            p -> System.out.println("Estoque do " + p.getNome() + " depois da atualização: " + p.getEstoque())
        );

        List<Produto> todos = dao.listarTodos();
        System.out.println("Total de produtos no banco: " + todos.size());
    }
}