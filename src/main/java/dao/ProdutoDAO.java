package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import enums.CategoriaProduto;
import model.Produto;
import model.ProdutoInternacional;
import model.ProdutoNacional;

public class ProdutoDAO {

    public void inserir(Produto produto) throws SQLException {
        String sql = "INSERT INTO produto "
                + "(tipo, nome, descricao, preco, estoque, distancia, categoria, percentual_importacao) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexaofactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, new String[] {"id"})) {

            ps.setString(2, produto.getNome());
            ps.setString(3, produto.getDescricao());
            ps.setDouble(4, produto.getPreco());
            ps.setInt(5, produto.getEstoque());
            ps.setDouble(6, produto.getDistancia());
            ps.setString(7, produto.getCategoria().name());

            if (produto instanceof ProdutoInternacional pi) {
                ps.setString(1, "INTERNACIONAL");
                ps.setDouble(8, pi.getPercentualImportacao());
            } else if (produto instanceof ProdutoNacional) {
                ps.setString(1, "NACIONAL");
                ps.setNull(8, Types.NUMERIC);
            } else {
                throw new IllegalArgumentException("Tipo de produto não suportado: " + produto.getClass());
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Produto> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";

        try (Connection con = Conexaofactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        String sql = "SELECT * FROM produto ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        try (Connection con = Conexaofactory.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(mapear(rs));
            }
        }
        return produtos;
    }

    public boolean atualizarEstoque(int id, int novoEstoque) throws SQLException {
        String sql = "UPDATE produto SET estoque = ? WHERE id = ?";

        try (Connection con = Conexaofactory.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, novoEstoque);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String descricao = rs.getString("descricao");
        double preco = rs.getDouble("preco");
        int estoque = rs.getInt("estoque");
        double distancia = rs.getDouble("distancia");
        CategoriaProduto categoria = CategoriaProduto.valueOf(rs.getString("categoria"));

        if ("INTERNACIONAL".equals(rs.getString("tipo"))) {
            double percentual = rs.getDouble("percentual_importacao");
            return new ProdutoInternacional(id, nome, descricao, preco, estoque, distancia, percentual, categoria);
        }
        return new ProdutoNacional(id, nome, descricao, preco, estoque, distancia, categoria);
    }
}