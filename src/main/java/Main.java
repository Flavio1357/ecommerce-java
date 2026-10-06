
import java.sql.SQLException;
import java.util.Optional;
 
import dao.UsuarioDAO;
import model.Cliente;
import model.Usuario;
 
public class Main {
 
    public static void main(String[] args) {
        UsuarioDAO dao = new UsuarioDAO();
 
        // E-mail e CPF mudam a cada execução, porque as duas colunas são UNIQUE no banco.
        long agora = System.currentTimeMillis();
        String email = "teste" + agora + "@email.com";
        String cpf = String.format("%011d", agora % 100_000_000_000L);
 
        Cliente cliente = new Cliente("Rua Teste, 123", 0, "Cliente Teste", email, "senha123", cpf);
 
        try {
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
 
        } catch (SQLException e) {
            System.out.println("Erro de banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
 