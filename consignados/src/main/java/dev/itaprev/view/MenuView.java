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

@SuppressWarnings("unused")
public class MenuView extends Application {

    private static BorderPane borderPane;

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

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void navegarPara(Node novaTela) {
        if (borderPane != null) {
            borderPane.setCenter(novaTela);
        } else {
            System.err.println("Erro de navegação: BorderPane principal é nulo.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}