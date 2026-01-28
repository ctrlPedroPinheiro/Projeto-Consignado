package dev.itaprev.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Fábrica de conexões com o banco de dados.
 */
public class ConnectionFactory {
    private static final String NOME_BANCO = "consignados";
    private static final String URL = "jdbc:mysql://localhost:3306/" + NOME_BANCO + "?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String SENHA = "dev@prev";

    /**
     * Obtém uma conexão com o banco de dados.
     * @return A conexão com o banco de dados.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados", e);
        }
    }
}
