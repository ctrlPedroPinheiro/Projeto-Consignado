package dev.itaprev.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeView extends VBox {

    public HomeView() {
        Label welcomeLabel = new Label("Bem-vindo!");
        Label instructionsLabel = new Label("Selecione 'Arquivo > Importar' para começar.");
        
        welcomeLabel.setFont(new Font("Arial", 24));

        this.getChildren().addAll(welcomeLabel, instructionsLabel);
        

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10); 
    }
}