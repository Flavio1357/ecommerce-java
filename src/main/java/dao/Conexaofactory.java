package dao;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class Conexaofactory {
 
    // Credenciais vêm de variáveis de ambiente para não irem parar no GitHub (repositório público).
    private static final String URL = valor("DB_URL", "jdbc:postgresql://localhost:5432/EcommerceDataBase");
    private static final String USUARIO = valor("DB_USER", "postgres");
    private static final String SENHA = valor("DB_PASSWORD", "");
 
    private Conexaofactory() {
    }
 
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
 
    private static String valor(String nome, String padrao) {
        String v = System.getenv(nome);
        return (v == null || v.isBlank()) ? padrao : v;
    }
}