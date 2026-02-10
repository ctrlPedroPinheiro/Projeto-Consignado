package dev.itaprev.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import java.awt.Desktop;
import java.net.URI;

/**
 * Classe responsável pela visualização da tela "Sobre".
 */
public class SobreView extends StackPane {

    public SobreView() {
        this.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        this.getStyleClass().add("main-background");

        VBox conteinerPrincipal = new VBox(15);
        conteinerPrincipal.setAlignment(Pos.CENTER);
        conteinerPrincipal.setMaxWidth(600);

        Label info = new Label("Comparador de Consignados v1.0");
        info.getStyleClass().add("header-title");

        Label dev = new Label("Desenvolvido por PEDRO PINHEIRO DE ARAÚJO");
        dev.getStyleClass().add("header-subtitle");

        // --- Descrição ---
        Label descricao = new Label(
            "Este software foi desenvolvido para facilitar a comparação de consignados. "
            + "Ele permite importar dados, realizar consultas e gerar relatórios de forma eficiente e automatizada."
        );
        descricao.getStyleClass().add("description");
        descricao.setWrapText(true);
        descricao.setTextAlignment(TextAlignment.CENTER); 

        Label linkGitHub = new Label("Acessar Repositório no GitHub");
        linkGitHub.getStyleClass().add("link"); 
        
        linkGitHub.setOnMouseClicked(event -> {
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI("https://github.com/ctrlPedroPinheiro/Projeto-Consignado"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // --- Contato (E-mail) ---
        Label lblContato = new Label("Contato / Suporte:");
        lblContato.getStyleClass().add("info-label");

        Label eMailInfo = new Label("pp.programacoes@hotmail.com");
        eMailInfo.getStyleClass().add("info");

        conteinerPrincipal.getChildren().addAll(
            info, 
            dev, 
            descricao, 
            new Label(""), 
            linkGitHub,
            new Label(""), 
            lblContato,
            eMailInfo
        );

        this.getChildren().add(conteinerPrincipal);
    }
}