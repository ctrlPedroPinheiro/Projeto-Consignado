package dev.itaprev.view;

import dev.itaprev.controller.ConsignadoController;
import dev.itaprev.dto.ResultadoComparacaoDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.Year;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Classe responsável pela visualização da tela de importação.
 */
public class ImportarView extends VBox {

    private ComboBox<Integer> mesCombo;
    private ComboBox<Integer> anoCombo;
    private Button selecionarArquivoBotao;

    /**
     * Construtor da classe ImportarView.
     */
    public ImportarView() {
        Label title = new Label("Importar Consignados");

        Label competenciaLabel = new Label("Selecione a Competência (Obrigatório):");
        
        mesCombo = new ComboBox<>();
        mesCombo.setPromptText("Mês");
        mesCombo.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));

        anoCombo = new ComboBox<>();
        anoCombo.setPromptText("Ano");
        int anoAtual = Year.now().getValue();
        ObservableList<Integer> anos = IntStream.rangeClosed(anoAtual - 10, anoAtual)
                                                .boxed()
                                                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        anoCombo.setItems(anos);
        anoCombo.setValue(anoAtual); 

        HBox competenciaBox = new HBox(10, mesCombo, anoCombo);
        competenciaBox.setAlignment(Pos.CENTER);

        selecionarArquivoBotao = new Button("Selecionar Arquivo...");
        selecionarArquivoBotao.setDisable(true); 

        Label description = new Label("Arquivos Excel (.xlsx, .xls)");

        mesCombo.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        anoCombo.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());

        selecionarArquivoBotao.setOnAction(e -> {
            int mes = mesCombo.getValue();
            int ano = anoCombo.getValue();
            
            try {
                ConsignadoController controller = new ConsignadoController();
                ResultadoComparacaoDTO resultado = controller.compararConsignados(mes, ano);
                MenuView.navegarPara(new ComparacaoView(resultado, mes, ano));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            
        });

        this.getChildren().addAll(title, competenciaLabel, competenciaBox, selecionarArquivoBotao, description);
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15); 
    }

    /**
     * Valida os campos de entrada.
     */
    private void validarCampos() {
        boolean habilitar = (mesCombo.getValue() != null && anoCombo.getValue() != null);
        selecionarArquivoBotao.setDisable(!habilitar);
    }
}