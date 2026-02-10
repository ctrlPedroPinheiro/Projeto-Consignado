package dev.itaprev.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static String urlEscolhida;
    private static String usuarioLogado;
    private static String senhaLogada;

    /**
     * Define as credenciais do usuário para conexão com o banco de dados.
     * @param user
     * @param password
     */
    public static void setCredentials(String url, String user, String password) {
        urlEscolhida = url;
        usuarioLogado = user;
        senhaLogada = password;
    }

    /**
     * Obtém uma conexão com o banco de dados usando as credenciais do usuário logado.
     * @return
     */
    public static Connection getConnection() {
        try {
            if (usuarioLogado == null) throw new SQLException("Nenhum usuário autenticado.");
            return DriverManager.getConnection(urlEscolhida, usuarioLogado, senhaLogada);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar ao banco: Verifique usuário e senha.", e);
        }
    }
}