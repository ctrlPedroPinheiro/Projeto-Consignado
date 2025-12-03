package dev.itaprev.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SobreView extends VBox {

    public SobreView() {
        Label info = new Label("Comparador de Consignados v1.0");
        Label dev = new Label("Desenvolvido por PEDRO PINHEIRO DE ARAÚJO");
        
        this.getChildren().addAll(info, dev);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
    }
}