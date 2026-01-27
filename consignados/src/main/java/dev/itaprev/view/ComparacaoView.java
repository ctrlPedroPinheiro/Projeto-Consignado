package dev.itaprev.view;

import dev.itaprev.controller.ConsignadoController;
import dev.itaprev.controller.MudancaController;
import dev.itaprev.dto.ConsignadoDTO;
import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.dto.ResultadoComparacaoDTO;
import dev.itaprev.model.MotivoMudanca;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe responsável pela visualização da comparação de consignados.
 */
public class ComparacaoView extends VBox {

    private ObservableList<ConsignadoDTO> listaAnalise;
    private FilteredList<ConsignadoDTO> filteredAnalise;
    private TableView<ConsignadoDTO> tabelaAnalise;

    private ObservableList<MudancaDTO> listaAcatados;
    private ObservableList<MudancaDTO> listaExcluidos;

    private FilteredList<MudancaDTO> filteredAcatados;
    private FilteredList<MudancaDTO> filteredExcluidos;

    private TableView<MudancaDTO> tabelaAcatados;
    private TableView<MudancaDTO> tabelaExcluidos;

    private TextField txtBuscaAnalise;
    private TextField txtBuscaAcatados;
    private TextField txtBuscaExcluidos;

    private Tab tabAnalise;
    private Tab tabAcatados;
    private Tab tabExcluidos;

    private final ResultadoComparacaoDTO resultadoOriginal;
    private final int idCompetencia;

    private List<MudancaDTO> ultimoLoteAcatado;
    
    private Button btnDesfazer;
    private Button btnAcatar;
    private Button btnResetar;
    private Button btnSalvar;
    private Button btnMoverParaExcluidos;

    /** 
     * Construtor da classe ComparacaoView.
     */
    public ComparacaoView(ResultadoComparacaoDTO resultado, int mes, int ano) {
        this.resultadoOriginal = resultado;
        this.idCompetencia = resultado.idCompetencia();

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Resultado da Comparação Competência: " + mes + "/" + ano);
        title.setFont(new Font("Arial", 24));

        List<ConsignadoDTO> divergentesDTO = resultadoOriginal.divergentes().stream()
            .map(c -> new ConsignadoDTO(c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), this.idCompetencia))
            .collect(Collectors.toList());

        this.listaAnalise = FXCollections.observableArrayList(divergentesDTO);

        List<MudancaDTO> acatadosDTO = resultadoOriginal.acatados().stream()
            .map(c -> {
                ConsignadoDTO dto = new ConsignadoDTO(c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), this.idCompetencia);
                return new MudancaDTO(dto, MotivoMudanca.NULO);
            })
            .collect(Collectors.toList());
        this.listaAcatados = FXCollections.observableArrayList(acatadosDTO);

        List<MudancaDTO> excluidosDTO = resultadoOriginal.paraExcluir().stream()
            .map(c -> {
                ConsignadoDTO dto = new ConsignadoDTO(c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), this.idCompetencia-1);
                return new MudancaDTO(dto,  MotivoMudanca.NULO);
            })
            .collect(Collectors.toList());
        this.listaExcluidos = FXCollections.observableArrayList(excluidosDTO);

        this.filteredAnalise = new FilteredList<>(this.listaAnalise, p -> true);
        this.filteredAcatados = new FilteredList<>(this.listaAcatados, p -> true);
        this.filteredExcluidos = new FilteredList<>(this.listaExcluidos, p -> true);

        VBox painelAnalise = criarPainelAnalise();
        VBox painelAcatados = criarPainelAcatados();
        VBox painelExcluidos = criarPainelExcluidos();

        TabPane tabPane = new TabPane();

        this.tabAnalise = new Tab("Divergentes (" + this.listaAnalise.size() + ")", painelAnalise);
        this.tabAcatados = new Tab("Acatados (" + this.listaAcatados.size() + ")", painelAcatados);
        this.tabExcluidos = new Tab("Excluídos (" + this.listaExcluidos.size() + ")", painelExcluidos);

        tabAnalise.setClosable(false);
        tabAcatados.setClosable(false);
        tabExcluidos.setClosable(false);

        tabPane.getTabs().addAll(tabAnalise, tabAcatados, tabExcluidos);

        this.btnSalvar = new Button("Confirmar e Salvar Alterações");
        this.btnSalvar.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        this.btnSalvar.setPrefHeight(40);
        this.btnSalvar.setOnAction(e -> salvarAlteracoes());

        HBox painelSalvar = new HBox(this.btnSalvar);
        painelSalvar.setAlignment(Pos.CENTER_RIGHT);
        painelSalvar.setPadding(new Insets(15, 10, 10, 10));

        this.getChildren().addAll(title, tabPane, painelSalvar);
    }

    /**
     * Cria o painel de análise.
     * @return O painel de análise.
     */
    private VBox criarPainelAnalise() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));

        Label lblTitulo = new Label("Lista de Consignados Para Análise");
        lblTitulo.setFont(new Font("Arial", 16));

        this.txtBuscaAnalise = new TextField();
        this.txtBuscaAnalise.setPromptText("Buscar por nome...");
        this.txtBuscaAnalise.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredAnalise.setPredicate(consignado -> filtroConsignado(consignado, newVal));
        });

        this.tabelaAnalise = criarTabelaConsignados(this.filteredAnalise);

        this.btnAcatar = new Button("Acatar Selecionado(s) ➔");
        this.btnAcatar.setOnAction(e -> moverParaAcatados());

        this.btnResetar = new Button("Resetar Listas");
        this.btnResetar.setOnAction(e -> resetarListas());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox barraBotoes = new HBox(10, this.btnResetar, spacer, this.btnAcatar);
        barraBotoes.setPadding(new Insets(10, 0, 0, 0));

        painel.getChildren().addAll(lblTitulo, txtBuscaAnalise, this.tabelaAnalise, barraBotoes);
        return painel;
    }

    /**
     * Cria o painel de acatados.
     * @return O painel de acatados.
     */
    private VBox criarPainelAcatados() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));
        Label lblTitulo = new Label("Lista de Consignados Acatados");
        lblTitulo.setFont(new Font("Arial", 16));
        this.txtBuscaAcatados = new TextField();
        this.txtBuscaAcatados.setPromptText("Buscar por nome...");
        this.txtBuscaAcatados.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredAcatados.setPredicate(mudanca -> filtroMudanca(mudanca, newVal));
        });
        this.tabelaAcatados = criarTabelaMudancas(this.filteredAcatados, this.listaAcatados);

        this.btnDesfazer = new Button("⬅ Desfazer Última Ação");
        this.btnDesfazer.setOnAction(e -> desfazerUltimaAcao());
        this.btnDesfazer.setDisable(true);

        this.btnMoverParaExcluidos = new Button("Mover para Excluídos ➔");
        this.btnMoverParaExcluidos.setOnAction(e -> moverParaExcluidos());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox barraBotoes = new HBox(10, this.btnDesfazer, spacer, this.btnMoverParaExcluidos);

        barraBotoes.setPadding(new Insets(10, 0, 0, 0));
        painel.getChildren().addAll(lblTitulo, txtBuscaAcatados, this.tabelaAcatados, barraBotoes);
        return painel;
    }

    /**
     * Cria o painel de excluídos.
     * @return O painel de excluídos.
     */
    private VBox criarPainelExcluidos() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));
        Label lblTitulo = new Label("Lista de Consignados Excluídos");
        lblTitulo.setFont(new Font("Arial", 16));
        this.txtBuscaExcluidos = new TextField();
        this.txtBuscaExcluidos.setPromptText("Buscar por nome...");
        this.txtBuscaExcluidos.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredExcluidos.setPredicate(mudanca -> filtroMudanca(mudanca, newVal));
        });

        this.tabelaExcluidos = criarTabelaMudancas(this.filteredExcluidos, this.listaExcluidos);
        painel.getChildren().addAll(lblTitulo, txtBuscaExcluidos, this.tabelaExcluidos);
        return painel;
    }

    /**
     * Filtra os consignados com base no texto de busca.
     * @param consignado
     * @param textoBusca
     * @return
     */
    private boolean filtroConsignado(ConsignadoDTO consignado, String textoBusca) {
        if (textoBusca == null || textoBusca.isEmpty() || textoBusca.isBlank()) return true;
        String filtro = textoBusca.toLowerCase().trim();
        if (consignado.nome() == null) return false;
        return consignado.nome().toLowerCase().contains(filtro);
    }

    /**
     * Filtra as mudanças com base no texto de busca.
     * @param mudanca
     * @param textoBusca
     * @return
     */
    private boolean filtroMudanca(MudancaDTO mudanca, String textoBusca) {
        if (textoBusca == null || textoBusca.isEmpty() || textoBusca.isBlank()) return true;
        String filtro = textoBusca.toLowerCase().trim();
        if (mudanca.consignado().nome() == null) return false;
        return mudanca.consignado().nome().toLowerCase().contains(filtro);
    }

    /**
     * Pergunta ao usuário o motivo da mudança.
     * @return O motivo da mudança selecionado pelo usuário.
     */
    private Optional<MotivoMudanca> perguntarMotivo() {
        ChoiceDialog<MotivoMudanca> dialog = new ChoiceDialog<>(MotivoMudanca.OUTROS, MotivoMudanca.values());
        
        dialog.setTitle("Justificar Mudança");
        dialog.setHeaderText("Por favor, selecione o motivo da alteração:");
        dialog.setContentText("Motivo:");

        return dialog.showAndWait();
    }

    /**
     * Move as mudanças selecionadas para a lista de excluídos.
     */
    private void moverParaExcluidos() {
        List<MudancaDTO> selecionados = new ArrayList<>(this.tabelaAcatados.getSelectionModel().getSelectedItems());

        if (selecionados.isEmpty()) return;

        this.listaAcatados.removeAll(selecionados);
        this.listaExcluidos.addAll(selecionados);

        atualizarContagemAbas();
        this.tabelaAcatados.getSelectionModel().clearSelection();
    }

    /**
     * Move os consignados selecionados para a lista de acatados.
     */
    private void moverParaAcatados() {
        List<ConsignadoDTO> selecionados = new ArrayList<>(this.tabelaAnalise.getSelectionModel().getSelectedItems());
        if (selecionados.isEmpty()) return;

        Optional<MotivoMudanca> motivoOpt = perguntarMotivo();
        if (motivoOpt.isEmpty()) return;

        MotivoMudanca motivo = motivoOpt.get();

        List<MudancaDTO> novasMudancas = selecionados.stream().map(dto -> new MudancaDTO(dto, motivo)).collect(Collectors.toList());
        this.ultimoLoteAcatado = novasMudancas;

        this.listaAnalise.removeAll(selecionados);
        this.listaAcatados.addAll(novasMudancas);

        this.btnDesfazer.setDisable(false);
        atualizarContagemAbas();
        this.tabelaAnalise.getSelectionModel().clearSelection();
    }

    /**
     * Desfaz a última ação realizada.
     */
    private void desfazerUltimaAcao() {
        if (this.ultimoLoteAcatado == null || this.ultimoLoteAcatado.isEmpty()) return;

        this.listaAcatados.removeAll(this.ultimoLoteAcatado);

        List<ConsignadoDTO> consignadosOriginais = this.ultimoLoteAcatado.stream()
            .map(MudancaDTO::consignado)
            .collect(Collectors.toList());

        this.listaAnalise.addAll(consignadosOriginais);

        this.ultimoLoteAcatado = null;
        this.btnDesfazer.setDisable(true);
        atualizarContagemAbas();
    }

    /**
     * Reseta os campos de busca.
     */
    private void resetarListas() {
        this.txtBuscaAnalise.clear();
        this.txtBuscaAcatados.clear();
        this.txtBuscaExcluidos.clear();

        List<ConsignadoDTO> divergentesDTO = resultadoOriginal.divergentes().stream()
             .map(c -> new ConsignadoDTO(c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), this.idCompetencia))
             .collect(Collectors.toList());

        this.listaAnalise.setAll(divergentesDTO);

        this.listaAcatados.setAll(
            resultadoOriginal.acatados().stream()
                .map(c -> new MudancaDTO(
                    new ConsignadoDTO(c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), this.idCompetencia), 
                     MotivoMudanca.NULO))
                .collect(Collectors.toList())
        );
        this.listaExcluidos.setAll(
             resultadoOriginal.paraExcluir().stream()
                .map(c -> new MudancaDTO(
                    new ConsignadoDTO(c.getContrato(), c.getNome(), c.getCpf(), c.getMatricula(), c.getPrazoTotal(), c.getNumeroPrestacao(), c.getValorPrestacao(), this.idCompetencia-1), MotivoMudanca.NULO))
                .collect(Collectors.toList())
        );

        this.ultimoLoteAcatado = null;
        this.btnDesfazer.setDisable(true);

        this.tabelaAnalise.getSelectionModel().clearSelection();
        this.tabelaAcatados.getSelectionModel().clearSelection();
        this.tabelaExcluidos.getSelectionModel().clearSelection();

        atualizarContagemAbas();
    }

    /**
     * Atualiza a contagem de abas.
     */
    private void atualizarContagemAbas() {
        this.tabAnalise.setText("Divergentes (" + this.listaAnalise.size() + ")");
        this.tabAcatados.setText("Acatados (" + this.listaAcatados.size() + ")");
        this.tabExcluidos.setText("Excluídos (" + this.listaExcluidos.size() + ")");
    }

    /**
     * Salva as alterações realizadas.
     */
    private void salvarAlteracoes() {
        List<MudancaDTO> paraAcatar = new ArrayList<>(this.listaAcatados);
        List<MudancaDTO> paraExcluir = new ArrayList<>(this.listaExcluidos);
        List<ConsignadoDTO> paraIgnorar = new ArrayList<>(this.listaAnalise);

        Alert alertConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmacao.setTitle("Confirmar Alterações");
        alertConfirmacao.setHeaderText("Você confirma o salvamento destas alterações?");

        String resumo = String.format(
            "Você está prestes a salvar as seguintes alterações:\n\n" +
            "Total de Acatados: %d\n" +
            "Total de Excluídos: %d\n" +
            "Total Ignorados: %d\n\n" +
            "Deseja continuar?",
            paraAcatar.size(),
            paraExcluir.size(),
            paraIgnorar.size()
        );
        alertConfirmacao.setContentText(resumo);

        VBox expandableContent = new VBox();
        expandableContent.setPadding(new Insets(10));

        StringBuilder detalhes = new StringBuilder();

        detalhes.append("ACATADOS\n");
        if (paraAcatar.isEmpty()) {
            detalhes.append("Nenhum.\n");
        } else {
            for (MudancaDTO m : paraAcatar) {
                if (m.motivo() != MotivoMudanca.NULO) {
                    detalhes.append(String.format("Contrato: %s | Nome: %s | Motivo: %s\n",
                        m.consignado().contrato(),
                        m.consignado().nome(),
                        m.motivo()));
                }
            }
        }

        detalhes.append("\nEXCLUÍDOS\n");
        if (paraExcluir.isEmpty()) {
            detalhes.append("Nenhum.\n");
        } else {
            for (MudancaDTO m : paraExcluir) {
                detalhes.append(String.format("Contrato: %s | Nome: %s | Motivo: %s\n",
                    m.consignado().contrato(),
                    m.consignado().nome(),
                    m.motivo()));
            }
        }

        TextArea textArea = new TextArea(detalhes.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        VBox.setVgrow(textArea, Priority.ALWAYS);
        expandableContent.getChildren().add(textArea);

        alertConfirmacao.getDialogPane().setExpandableContent(expandableContent);
        alertConfirmacao.getDialogPane().setExpanded(true);

        Optional<ButtonType> result = alertConfirmacao.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Enviando para salvar...");
            try {
                ConsignadoController controller = new ConsignadoController();
                MudancaController mudancaController = new MudancaController();
                controller.salvarMudancasDTO(paraAcatar);
                mudancaController.salvarMudancasDTO(paraAcatar, paraExcluir);

                Alert alertSucesso = new Alert(Alert.AlertType.INFORMATION);
                alertSucesso.setTitle("Sucesso");
                alertSucesso.setHeaderText("Alterações salvas com sucesso!");
                alertSucesso.setContentText("O banco de dados foi atualizado.");
                alertSucesso.showAndWait();

                // travarBotoesAposSalvar();
                MenuView.navegarPara(new HomeView());

            } catch (Exception ex) {
                System.err.println("Erro ao tentar salvar: " + ex.getMessage());
                ex.printStackTrace();
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Erro ao Salvar");
                alertErro.setHeaderText("Não foi possível salvar as alterações.");
                alertErro.setContentText("Ocorreu um erro: " + ex.getMessage());
                alertErro.showAndWait();
                MenuView.navegarPara(new HomeView());
            }
        }
    }

    /*
    private void travarBotoesAposSalvar() {
        this.btnSalvar.setDisable(true);
        this.btnSalvar.setText("Alterações Salvas");
        this.btnAcatar.setDisable(true);
        this.btnResetar.setDisable(true);
        this.btnDesfazer.setDisable(true);
        this.btnMoverParaExcluidos.setDisable(true);
        this.txtBuscaAnalise.setDisable(true);
        this.txtBuscaAcatados.setDisable(true);
        this.txtBuscaExcluidos.setDisable(true);
        this.tabelaAnalise.setDisable(true);
        this.tabelaAcatados.setDisable(true);
        this.tabelaExcluidos.setDisable(true);
    }
    */
    
    /**
     * Cria uma tabela para exibir os consignados.
     * @param lista
     * @return
     */
    @SuppressWarnings("unchecked")
    private TableView<ConsignadoDTO> criarTabelaConsignados(ObservableList<ConsignadoDTO> lista) {
        TableView<ConsignadoDTO> tabela = new TableView<>();
        tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<ConsignadoDTO, String> colContrato = new TableColumn<>("Contrato");
        colContrato.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().contrato()));
        
        TableColumn<ConsignadoDTO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().nome()));
        
        TableColumn<ConsignadoDTO, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().cpf()));
        
        TableColumn<ConsignadoDTO, String> colMatricula = new TableColumn<>("Matrícula");
        colMatricula.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().matricula()));
        
        TableColumn<ConsignadoDTO, Integer> colPrazo = new TableColumn<>("Prazo Total");
        colPrazo.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().prazoTotal()).asObject());
        
        TableColumn<ConsignadoDTO, Integer> colPrestacao = new TableColumn<>("Nº Prestação");
        colPrestacao.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().numeroPrestacao()).asObject());
        
        TableColumn<ConsignadoDTO, Double> colValor = new TableColumn<>("Valor Prestação");
        colValor.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().valorPrestacao()).asObject());

        tabela.getColumns().addAll(colContrato, colNome, colCpf, colMatricula, colPrazo, colPrestacao, colValor);
        tabela.setItems(lista);
        tabela.setPlaceholder(new Label("Nenhum item encontrado."));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        double screenHeight = screenSize.getHeight();
        tabela.setPrefHeight(screenHeight*0.55);

        return tabela;
    }

    /**
     * Cria uma tabela para exibir as mudanças.
     * @param lista
     * @param listaFonte
     * @return
     */
    @SuppressWarnings("unchecked")
    private TableView<MudancaDTO> criarTabelaMudancas(ObservableList<MudancaDTO> lista, ObservableList<MudancaDTO> listaFonte) {
        TableView<MudancaDTO> tabela = new TableView<>();

        tabela.setEditable(true);

        tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<MudancaDTO, String> colContrato = new TableColumn<>("Contrato");
        colContrato.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().consignado().contrato()));
        colContrato.setEditable(false);
        
        TableColumn<MudancaDTO, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().consignado().nome()));
        colNome.setEditable(false);
        
        TableColumn<MudancaDTO, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().consignado().cpf()));
        colCpf.setEditable(false);
        
        TableColumn<MudancaDTO, String> colMatricula = new TableColumn<>("Matrícula");
        colMatricula.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().consignado().matricula()));
        colMatricula.setEditable(false);
        
        TableColumn<MudancaDTO, Integer> colPrazo = new TableColumn<>("Prazo Total");
        colPrazo.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().consignado().prazoTotal()).asObject());
        colPrazo.setEditable(false);
        
        TableColumn<MudancaDTO, Integer> colPrestacao = new TableColumn<>("Nº Prestação");
        colPrestacao.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().consignado().numeroPrestacao()).asObject());
        colPrestacao.setEditable(false);
        
        TableColumn<MudancaDTO, Double> colValor = new TableColumn<>("Valor Prestação");
        colValor.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().consignado().valorPrestacao()).asObject());
        colValor.setEditable(false);

        TableColumn<MudancaDTO, MotivoMudanca> colMotivo = new TableColumn<>("Motivo");
    
        colMotivo.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().motivo()));
        colMotivo.setCellFactory(ComboBoxTableCell.forTableColumn(MotivoMudanca.values()));
        colMotivo.setOnEditCommit(event -> {
            MudancaDTO itemAntigo = event.getRowValue();
            MotivoMudanca novoMotivo = event.getNewValue();

            MudancaDTO itemNovo = new MudancaDTO(itemAntigo.consignado(), novoMotivo);

            int index = listaFonte.indexOf(itemAntigo);
            if (index >= 0) {
                listaFonte.set(index, itemNovo);
            }
        });

        tabela.getColumns().addAll(colContrato, colNome, colCpf, colMatricula, colPrazo, colPrestacao, colValor, colMotivo);

        tabela.setItems(lista);
        tabela.setPlaceholder(new Label("Nenhum item encontrado."));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        double screenHeight = screenSize.getHeight();
        tabela.setPrefHeight(screenHeight*0.55);

        return tabela;
    }
}