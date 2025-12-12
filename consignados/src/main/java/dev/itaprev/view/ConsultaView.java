package dev.itaprev.view;

import dev.itaprev.controller.CompetenciaController;
import dev.itaprev.controller.ConsignadoController;
import dev.itaprev.controller.MudancaController;
import dev.itaprev.dto.CompetenciaDTO;
import dev.itaprev.dto.ConsignadoDTO;
import dev.itaprev.dto.MudancaDTO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

public class ConsultaView extends VBox {

    private ObservableList<ConsignadoDTO> listaConsignados;
    private FilteredList<ConsignadoDTO> filteredConsignados;
    private TableView<ConsignadoDTO> tabelaConsignados;

    private ObservableList<MudancaDTO> listaMudancas;
    private FilteredList<MudancaDTO> filteredMudancas;
    private TableView<MudancaDTO> tabelaMudancas;

    private TextField txtBuscaConsignados;
    private TextField txtBuscaMudancas;

    @SuppressWarnings("unused")
    private CompetenciaDTO competenciaSelecionada;
    private ComboBox<String> cmbCompetencia;
    private Button btnPesquisar;
    private Button btnGerarRelatorio;

    private final ConsignadoController consignadoController;
    private final MudancaController mudancaController;
    private final CompetenciaController competenciaController;

    public ConsultaView() {

        this.consignadoController = new ConsignadoController();
        this.mudancaController = new MudancaController();
        this.competenciaController = new CompetenciaController();

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Consulta de Consignados e Mudanças");
        title.setFont(new Font("Arial", 24));

        HBox boxSelecao = criarAreaSelecaoCompetencia();

        this.listaConsignados = FXCollections.observableArrayList();
        this.filteredConsignados = new FilteredList<>(this.listaConsignados, p -> true);

        this.listaMudancas = FXCollections.observableArrayList();
        this.filteredMudancas = new FilteredList<>(this.listaMudancas, p -> true);

        TabPane tabPane = new TabPane();
        
        Tab tabConsignados = new Tab("Todos Consignados", criarPainelConsignados());
        tabConsignados.setClosable(false);

        Tab tabMudancas = new Tab("Histórico de Mudanças", criarPainelMudancas());
        tabMudancas.setClosable(false);

        tabPane.getTabs().addAll(tabConsignados, tabMudancas);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        HBox boxRodape = criarRodape();

        this.getChildren().addAll(title, boxSelecao, tabPane, boxRodape);
    }

    private HBox criarAreaSelecaoCompetencia() {
        Label lblComp = new Label("Selecione a Competência:");
        lblComp.setStyle("-fx-font-weight: bold;");

        this.cmbCompetencia = new ComboBox<>();
        this.cmbCompetencia.setPromptText("Mês/Ano");
        this.cmbCompetencia.setPrefWidth(150);

        try {
            List<CompetenciaDTO> competencias = this.competenciaController.listarCompetencias();
            if (competencias != null) {
                competencias.forEach(c -> {
                    String item = String.format("%02d/%d", c.mes(), c.ano());
                    this.cmbCompetencia.getItems().add(item);
                });
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar lista de competências: " + e.getMessage());
        }

        this.btnPesquisar = new Button("Carregar Dados");
        this.btnPesquisar.setStyle("-fx-base: #4a90e2; -fx-text-fill: white; -fx-font-weight: bold;");
        this.btnPesquisar.setOnAction(e -> carregarDados());

        HBox box = new HBox(10, lblComp, cmbCompetencia, btnPesquisar);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10, 0, 10, 0));
        return box;
    }

    private HBox criarRodape() {
        this.btnGerarRelatorio = new Button("Gerar Relatório PDF");
        this.btnGerarRelatorio.setStyle("-fx-base: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        this.btnGerarRelatorio.setPrefHeight(40);
        this.btnGerarRelatorio.setOnAction(e -> acaoGerarRelatorio());

        HBox box = new HBox(this.btnGerarRelatorio);
        box.setAlignment(Pos.CENTER_RIGHT);
        box.setPadding(new Insets(10, 0, 0, 0));
        return box;
    }

    private VBox criarPainelConsignados() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));

        HBox boxBusca = new HBox(10);
        boxBusca.setAlignment(Pos.CENTER_LEFT);
        Label lblBusca = new Label("Buscar:");
        this.txtBuscaConsignados = new TextField();
        this.txtBuscaConsignados.setPromptText("Nome, Matrícula ou CPF...");
        this.txtBuscaConsignados.setPrefWidth(300);
        
        this.txtBuscaConsignados.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredConsignados.setPredicate(c -> filtroConsignado(c, newVal));
        });

        boxBusca.getChildren().addAll(lblBusca, txtBuscaConsignados);

        this.tabelaConsignados = criarTabelaConsignados(this.filteredConsignados);
        VBox.setVgrow(this.tabelaConsignados, Priority.ALWAYS);

        Label lblTotal = new Label();
        lblTotal.textProperty().bind(javafx.beans.binding.Bindings.size(this.filteredConsignados).asString("Total de registros: %d"));
        
        painel.getChildren().addAll(boxBusca, this.tabelaConsignados, lblTotal);
        return painel;
    }

    private VBox criarPainelMudancas() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));

        HBox boxBusca = new HBox(10);
        boxBusca.setAlignment(Pos.CENTER_LEFT);
        Label lblBusca = new Label("Buscar:");
        this.txtBuscaMudancas = new TextField();
        this.txtBuscaMudancas.setPromptText("Nome ou Motivo...");
        this.txtBuscaMudancas.setPrefWidth(300);

        this.txtBuscaMudancas.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredMudancas.setPredicate(m -> filtroMudanca(m, newVal));
        });

        boxBusca.getChildren().addAll(lblBusca, txtBuscaMudancas);

        this.tabelaMudancas = criarTabelaMudancas(this.filteredMudancas);
        VBox.setVgrow(this.tabelaMudancas, Priority.ALWAYS);

        Label lblTotal = new Label();
        lblTotal.textProperty().bind(javafx.beans.binding.Bindings.size(this.filteredMudancas).asString("Total de alterações: %d"));

        painel.getChildren().addAll(boxBusca, this.tabelaMudancas, lblTotal);
        return painel;
    }

    private void carregarDados() {
        String textoSelecionado = cmbCompetencia.getValue();
        if (textoSelecionado == null || textoSelecionado.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione uma competência para pesquisar.");
            alert.show();
            return;
        }

        try {
            String[] partes = textoSelecionado.split("/");
            int mes = Integer.parseInt(partes[0]);
            int ano = Integer.parseInt(partes[1]);

            CompetenciaDTO competencia = competenciaController.buscarIdCompetencia(mes, ano);

            if (competencia == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Competência não encontrada no banco de dados.");
                alert.show();
                return;
            }

            this.competenciaSelecionada = competencia;
            System.out.println("Carregando dados para competência ID: " + competencia.idcompetencia());

            this.listaConsignados.clear();
            this.listaMudancas.clear();

            List<ConsignadoDTO> dadosConsignados = consignadoController.listarConsignados(competencia.idcompetencia());
            List<MudancaDTO> dadosMudancas = mudancaController.listarMudancasPorCompetencia(competencia.idcompetencia());

            if (dadosConsignados != null) {
                this.listaConsignados.setAll(dadosConsignados);
            }
            if (dadosMudancas != null) {
                this.listaMudancas.setAll(dadosMudancas);
            }

            if (this.listaConsignados.isEmpty() && this.listaMudancas.isEmpty()) {
                Alert info = new Alert(Alert.AlertType.INFORMATION, "Nenhum registro encontrado para esta competência.");
                info.show();
            }

        } catch (NumberFormatException nfe) {
            Alert erro = new Alert(Alert.AlertType.ERROR, "Formato de data inválido.");
            erro.show();
        } catch (Exception ex) {
            Alert erro = new Alert(Alert.AlertType.ERROR, "Erro ao carregar dados: " + ex.getMessage());
            erro.show();
            ex.printStackTrace();
        }
    }

    private void acaoGerarRelatorio() {
        if (this.competenciaSelecionada == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione e carregue uma competência primeiro.");
            alert.show();
            return;
        }

        try {
            consignadoController.relatorioConsultaPDF(competenciaSelecionada);
            Alert success = new Alert(Alert.AlertType.INFORMATION, "Relatório gerado com sucesso!");
            success.show();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Erro ao gerar relatório: " + e.getMessage());
            error.show();
            e.printStackTrace();
        }
    }

    private boolean filtroConsignado(ConsignadoDTO c, String texto) {
        if (texto == null || texto.isBlank()) return true;
        String lower = texto.toLowerCase().trim();
        return (c.nome() != null && c.nome().toLowerCase().contains(lower)) ||
               (c.cpf() != null && c.cpf().contains(lower)) ||
               (c.matricula() != null && c.matricula().toLowerCase().contains(lower));
    }

    private boolean filtroMudanca(MudancaDTO m, String texto) {
        if (texto == null || texto.isBlank()) return true;
        String lower = texto.toLowerCase().trim();
        boolean matchNome = m.consignado() != null && m.consignado().nome() != null && m.consignado().nome().toLowerCase().contains(lower);
        boolean matchMotivo = m.motivo() != null && m.motivo().toString().toLowerCase().contains(lower);
        
        return matchNome || matchMotivo;
    }
    
    @SuppressWarnings("unchecked")
    private TableView<ConsignadoDTO> criarTabelaConsignados(ObservableList<ConsignadoDTO> lista) {
        TableView<ConsignadoDTO> tabela = new TableView<>();
        
        TableColumn<ConsignadoDTO, String> colContrato = new TableColumn<>("Contrato");
        colContrato.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().contrato()));

        TableColumn<ConsignadoDTO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().nome()));
        colNome.setPrefWidth(200);

        TableColumn<ConsignadoDTO, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().cpf()));

        TableColumn<ConsignadoDTO, String> colMatricula = new TableColumn<>("Matrícula");
        colMatricula.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().matricula()));

        TableColumn<ConsignadoDTO, Double> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().valorPrestacao()).asObject());
        
        colValor.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(String.format("R$ %.2f", item));
            }
        });

        tabela.getColumns().addAll(colContrato, colNome, colCpf, colMatricula, colValor);
        tabela.setItems(lista);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        
        configurarAlturaTabela(tabela);
        return tabela;
    }

    @SuppressWarnings("unchecked")
    private TableView<MudancaDTO> criarTabelaMudancas(ObservableList<MudancaDTO> lista) {
        TableView<MudancaDTO> tabela = new TableView<>();

        TableColumn<MudancaDTO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(c -> {
            if (c.getValue().consignado() != null)
                return new SimpleStringProperty(c.getValue().consignado().nome());
            return new SimpleStringProperty("");
        });
        colNome.setPrefWidth(200);

        TableColumn<MudancaDTO, String> colContrato = new TableColumn<>("Contrato");
        colContrato.setCellValueFactory(c -> {
            if (c.getValue().consignado() != null)
                return new SimpleStringProperty(c.getValue().consignado().contrato());
            return new SimpleStringProperty("");
        });

        TableColumn<MudancaDTO, String> colMotivo = new TableColumn<>("Motivo da Mudança");
        colMotivo.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().motivo() != null ? c.getValue().motivo().toString() : ""
        ));
        colMotivo.setStyle("-fx-font-weight: bold; -fx-text-fill: #d32f2f;");

        TableColumn<MudancaDTO, Double> colValor = new TableColumn<>("Valor");
        colValor.setCellValueFactory(c -> {
            if (c.getValue().consignado() != null)
                return new SimpleDoubleProperty(c.getValue().consignado().valorPrestacao()).asObject();
            return new SimpleDoubleProperty(0.0).asObject();
        });
        
        colValor.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(String.format("R$ %.2f", item));
            }
        });

        tabela.getColumns().addAll(colNome, colContrato, colMotivo, colValor);
        tabela.setItems(lista);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        configurarAlturaTabela(tabela);
        return tabela;
    }

    private void configurarAlturaTabela(TableView<?> tabela) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        tabela.setPrefHeight(screenSize.getHeight() * 0.55);
    }
}