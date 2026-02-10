package dev.itaprev.controller;

import dev.itaprev.factory.ConnectionFactory;
import dev.itaprev.view.MenuView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import java.sql.Connection;

public class LoginController {

    @FXML private javafx.scene.control.ComboBox<String> comboConexao;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    

    /**
     * Manipula o evento de login, tentando autenticar o usuário e abrir a janela do menu principal.
     * @param event
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        String url = "";
        String server = comboConexao.getValue();
        if (server.isEmpty()) {
            exibirMensagem("Erro de Conexão", "Por favor, selecione uma conexão válida.");
            return;
        }
        if (server.equals("Localhost")) {
            url = "jdbc:mysql://localhost:3306/consignados?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
        } else if (server.equals("LAN")) {
            url = "jdbc:mysql://192.168.1.7:3306/consignados?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true";
        }
        String user = txtUsuario.getText();
        String pass = txtSenha.getText();

        try {
            ConnectionFactory.setCredentials(url, user, pass);
            try (Connection conn = ConnectionFactory.getConnection()) {
                
                MenuView menuView = new MenuView();
                Stage stageMenu = new Stage();
                menuView.start(stageMenu); 

                Node source = (Node) event.getSource();
                Stage stageLogin = (Stage) source.getScene().getWindow();
                stageLogin.close();
                
            }
        } catch (Exception e) {
            exibirMensagem("Erro de Autenticação", "Usuário ou senha inválidos no banco de dados.");
        }
    }

    /**
     * Exibe uma mensagem de alerta para o usuário.
     * @param titulo
     * @param msg
     */
    private void exibirMensagem(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}