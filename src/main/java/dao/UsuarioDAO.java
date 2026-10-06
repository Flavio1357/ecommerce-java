package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Optional;

import model.Cliente;
import model.Funcionario;
import model.Usuario;

public class UsuarioDAO {

    public void inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (tipo, nome, email, senha, cpf, endereco, cargo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexaofactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"})) {

            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenha());
            ps.setString(5, usuario.getCpf());

            if (usuario instanceof Cliente c) {
                ps.setString(1, "CLIENTE");
                ps.setString(6, c.getEndereco());
                ps.setNull(7, Types.VARCHAR);
            } else if (usuario instanceof Funcionario f) {
                ps.setString(1, "FUNCIONARIO");
                ps.setNull(6, Types.VARCHAR);
                ps.setString(7, f.getCargo());
            } else {
                throw new IllegalArgumentException("Tipo de usuário não suportado: " + usuario.getClass());
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        return buscarUm("SELECT * FROM usuario WHERE id = ?", id);
    }

    public Optional<Usuario> buscarPorEmail(String email) throws SQLException {
        return buscarUm("SELECT * FROM usuario WHERE email = ?", email);
    }

    private Optional<Usuario> buscarUm(String sql, Object parametro) throws SQLException {
        try (Connection con = Conexaofactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, parametro);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");
        String cpf = rs.getString("cpf");

        if ("CLIENTE".equals(rs.getString("tipo"))) {
            return new Cliente(rs.getString("endereco"), id, nome, email, senha, cpf);
        }
        return new Funcionario(rs.getString("cargo"), id, nome, email, senha, cpf);
    }
}