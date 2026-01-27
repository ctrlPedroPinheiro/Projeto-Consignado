package dev.itaprev.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import dev.itaprev.view.HomeView;
import dev.itaprev.view.ImportarView;
import dev.itaprev.view.ConsultaView;
import dev.itaprev.view.SobreView;
import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Classe responsável pela visualização do menu principal.
 */
@SuppressWarnings("unused")
public class MenuView extends Application {

    private static BorderPane borderPane;

    /**
     * Inicia a aplicação.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Comparador de Consignados");
        borderPane = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu menuArquivo = new Menu("Arquivo");
        MenuItem importarItem = new MenuItem("Importar Consignados");
        MenuItem exportarItem = new MenuItem("Consultar Consignados");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        MenuItem sairItem = new MenuItem("Sair");
        menuArquivo.getItems().addAll(importarItem, exportarItem, separator, sairItem);

        Menu menuAjuda = new Menu("Ajuda");
        MenuItem sobreItem = new MenuItem("Sobre");
        menuAjuda.getItems().add(sobreItem);
        menuBar.getMenus().addAll(menuArquivo, menuAjuda);
        
        sairItem.setOnAction(e -> Platform.exit());

        importarItem.setOnAction(e -> {
            navegarPara(new ImportarView()); 
        });

        exportarItem.setOnAction(e -> {
            navegarPara(new ConsultaView());
        });

        sobreItem.setOnAction(e -> {
            navegarPara(new SobreView());
        });

        borderPane.setTop(menuBar);
        
        navegarPara(new HomeView()); 

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        Scene scene = new Scene(borderPane, screenWidth*0.8, screenHeight*0.7);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Navega para uma nova tela.
     * @param novaTela A nova tela a ser exibida.
     */
    public static void navegarPara(Node novaTela) {
        if (borderPane != null) {
            borderPane.setCenter(novaTela);
        } else {
            System.err.println("Erro de navegação: BorderPane principal é nulo.");
        }
    }

    /**
     * Inicia a aplicação.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}