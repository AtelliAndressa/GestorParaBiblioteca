package biblioteca.database;

import biblioteca.model.Livro;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {
    
    public void criarTabela() {
        String sql = "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Livros' and xtype='U') " +
                    "CREATE TABLE Livros (" +
                    "id INT PRIMARY KEY IDENTITY(1,1), " +
                    "titulo VARCHAR(100) NOT NULL, " +
                    "autor VARCHAR(100) NOT NULL, " +
                    "genero VARCHAR(50) NOT NULL, " +
                    "editora VARCHAR(100) NOT NULL, " +
                    "ano INT NOT NULL, " +
                    "disponivel BIT NOT NULL, " +
                    "data_devolucao DATE)";
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criar tabela: " + e.getMessage());
        }
    }
    
    public void adicionarLivro(Livro livro) {
        String sql = "INSERT INTO Livros (titulo, autor, genero, editora, ano, disponivel) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, livro.getTitulo());
            pstmt.setString(2, livro.getAutor());
            pstmt.setString(3, livro.getGenero());
            pstmt.setString(4, livro.getEditora());
            pstmt.setInt(5, livro.getAno());
            pstmt.setBoolean(6, livro.isDisponivel());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao adicionar livro: " + e.getMessage());
        }
    }
    
    public List<Livro> buscarLivros(String palavraChave) {
        String sql = "SELECT * FROM Livros WHERE titulo LIKE ? OR autor LIKE ? OR genero LIKE ? OR editora LIKE ?";
        List<Livro> livros = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String termo = "%" + palavraChave.toLowerCase() + "%";
            pstmt.setString(1, termo);
            pstmt.setString(2, termo);
            pstmt.setString(3, termo);
            pstmt.setString(4, termo);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("genero"),
                    rs.getString("editora"),
                    rs.getInt("ano")
                );
                livro.setDisponivel(rs.getBoolean("disponivel"));
                Date dataDevolucao = rs.getDate("data_devolucao");
                if (dataDevolucao != null) {
                    livro.setDataDevolucao(dataDevolucao.toLocalDate());
                }
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar livros: " + e.getMessage());
        }
        return livros;
    }
    
    public boolean emprestarLivro(int idLivro) {
        String sql = "UPDATE Livros SET disponivel = 0, data_devolucao = ? WHERE id = ? AND disponivel = 1";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            LocalDate dataDevolucao = LocalDate.now().plusDays(14);
            pstmt.setDate(1, Date.valueOf(dataDevolucao));
            pstmt.setInt(2, idLivro);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao emprestar livro: " + e.getMessage());
        }
    }
    
    public boolean devolverLivro(int idLivro) {
        String sql = "UPDATE Livros SET disponivel = 1, data_devolucao = NULL WHERE id = ? AND disponivel = 0";
        
        try (Connection conn = ConexaoBD.getConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLivro);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao devolver livro: " + e.getMessage());
        }
    }
    
    public List<Livro> getLivrosAtrasados() {
        String sql = "SELECT * FROM Livros WHERE disponivel = 0 AND data_devolucao < GETDATE()";
        List<Livro> livros = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("genero"),
                    rs.getString("editora"),
                    rs.getInt("ano")
                );
                livro.setDisponivel(false);
                livro.setDataDevolucao(rs.getDate("data_devolucao").toLocalDate());
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar livros atrasados: " + e.getMessage());
        }
        return livros;
    }
    
    public List<Livro> getTodosLivros() {
        String sql = "SELECT * FROM Livros";
        List<Livro> livros = new ArrayList<>();
        
        try (Connection conn = ConexaoBD.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getString("genero"),
                    rs.getString("editora"),
                    rs.getInt("ano")
                );
                livro.setDisponivel(rs.getBoolean("disponivel"));
                Date dataDevolucao = rs.getDate("data_devolucao");
                if (dataDevolucao != null) {
                    livro.setDataDevolucao(dataDevolucao.toLocalDate());
                }
                livros.add(livro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao buscar todos os livros: " + e.getMessage());
        }
        return livros;
    }
} 