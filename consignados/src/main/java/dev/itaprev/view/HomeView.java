package dev.itaprev.view;

import java.util.List;
import dev.itaprev.controller.CompetenciaController;
import dev.itaprev.controller.ConsignadoController;
import dev.itaprev.dto.CompetenciaDTO;
import dev.itaprev.dto.ConsignadoDTO;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Classe responsável pela visualização da tela inicial.
 */
public class HomeView extends StackPane {

    /**
     * Construtor da classe HomeView.
     */
    public HomeView() {

        this.getStylesheets().add(getClass().getResource("/home-style.css").toExternalForm());
        this.getStyleClass().add("main-background");

        CompetenciaController competenciaController = new CompetenciaController();
        ConsignadoController consignadoController = new ConsignadoController();
        CompetenciaDTO atualCompetencia = competenciaController.buscarUltimaCompetencia();
        List<ConsignadoDTO> consignados = consignadoController.listarConsignados(atualCompetencia.idcompetencia());
        int totalConsignados = consignados.size();

        VBox conteinerPrincipal = new VBox(20);
        conteinerPrincipal.setAlignment(Pos.CENTER);
        conteinerPrincipal.setMaxWidth(600);    

        Label welcomeLabel = new Label("Bem-vindo!");
        welcomeLabel.getStyleClass().add("header-title");

        Label subTituloLabel = new Label("Painel de Controle de Consignados");
        subTituloLabel.getStyleClass().add("header-subtitle");

        VBox infoCard = new VBox(15);
        infoCard.getStyleClass().add("card");
        infoCard.setAlignment(Pos.CENTER);

        VBox competenciaBox = itemEstatistica("Competência Atual", atualCompetencia.mes() + "/" + atualCompetencia.ano());
        VBox totalBox = itemEstatistica("Total de Consignados", String.valueOf(totalConsignados));

        HBox statsRow = new HBox(40, competenciaBox, totalBox);
        statsRow.setAlignment(Pos.CENTER);
        
        infoCard.getChildren().add(statsRow);

        Label instructionsLabel = new Label("Para iniciar uma nova importação, acesse o menu superior.");
        instructionsLabel.getStyleClass().add("instruction-text");
        
        Label actionLabel = new Label("Arquivo > Importar");
        actionLabel.getStyleClass().add("action-highlight");

        conteinerPrincipal.getChildren().addAll(
            welcomeLabel, 
            subTituloLabel, 
            infoCard, 
            instructionsLabel, 
            actionLabel
        );

        this.getChildren().add(conteinerPrincipal);
    }

    /**
     * Cria um item de estatística para exibir no painel.
     * @param titulo
     * @param valor
     * @return
     */
    private VBox itemEstatistica(String titulo, String valor) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("stat-title");

        Label lblValor = new Label(valor);
        lblValor.getStyleClass().add("stat-value");

        VBox box = new VBox(5, lblTitulo, lblValor);
        box.setAlignment(Pos.CENTER);
        return box;
    }
}