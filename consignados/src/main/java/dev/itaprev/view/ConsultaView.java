package dev.itaprev.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ConsultaView extends VBox {

    public ConsultaView() {
        this.getChildren().add(new Label("Esta é a Tela de Exportação de Relatórios."));
        this.setAlignment(Pos.CENTER);
    }
}