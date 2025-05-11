package biblioteca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=Biblioteca;encrypt=true;trustServerCertificate=true";
    private static final String USUARIO = "sa";
    private static final String SENHA = "sua_senha";
    
    private static Connection conexao;
    
    public static Connection getConexao() {
        if (conexao == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
            }
        }
        return conexao;
    }
    
    public static void fecharConexao() {
        if (conexao != null) {
            try {
                conexao.close();
                conexao = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} 