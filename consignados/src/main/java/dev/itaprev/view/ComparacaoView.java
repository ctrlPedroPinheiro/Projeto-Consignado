package dev.itaprev.view;

import dev.itaprev.controller.ConsignadoController; 
import dev.itaprev.dto.ResultadoComparacaoDTO;
import dev.itaprev.model.Consignado;
import dev.itaprev.model.MotivoMudanca;
import dev.itaprev.model.Mudanca;
import javafx.scene.control.ChoiceDialog;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComparacaoView extends VBox {

    private ObservableList<Consignado> listaAnalise;
    private FilteredList<Consignado> filteredAnalise;
    private TableView<Consignado> tabelaAnalise;

    private ObservableList<Mudanca> listaAcatados;
    private ObservableList<Mudanca> listaExcluidos;
    
    private FilteredList<Mudanca> filteredAcatados;
    private FilteredList<Mudanca> filteredExcluidos;
    
    private TableView<Mudanca> tabelaAcatados;
    private TableView<Mudanca> tabelaExcluidos;
    
    private TextField txtBuscaAnalise;
    private TextField txtBuscaAcatados;
    private TextField txtBuscaExcluidos;
    
    private Tab tabAnalise;
    private Tab tabAcatados;
    private Tab tabExcluidos;
    
    private final ResultadoComparacaoDTO resultadoOriginal; 

    private List<Mudanca> ultimoLoteAcatado; 
    private List<Mudanca> ultimoLoteExcluido;
    
    private Button btnDesfazer; 
    private Button btnAcatar; 
    private Button btnResetar; 
    private Button btnSalvar; 
    private Button btnMoverParaExcluidos;


    public ComparacaoView(ResultadoComparacaoDTO resultado, int mes, int ano) {
        this.resultadoOriginal = resultado; 
        
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Resultado da Comparação Competência: " + mes + "/" + ano);
        title.setFont(new Font("Arial", 24));
        
        this.listaAnalise = FXCollections.observableArrayList(resultadoOriginal.divergentes());

        this.listaAcatados = FXCollections.observableArrayList(
            resultadoOriginal.acatados().stream()
                .map(consignado -> new Mudanca(consignado, MotivoMudanca.NULO))
                .collect(Collectors.toList())
        );
        this.listaExcluidos = FXCollections.observableArrayList(
            resultadoOriginal.paraExcluir().stream()
                .map(consignado -> new Mudanca(consignado, MotivoMudanca.NULO))
                .collect(Collectors.toList())
        );
        
        this.filteredAnalise = new FilteredList<>(this.listaAnalise, p -> true);
        this.filteredAcatados = new FilteredList<>(this.listaAcatados, p -> true);
        this.filteredExcluidos = new FilteredList<>(this.listaExcluidos, p -> true);
        
        VBox painelAnalise = criarPainelAnalise();
        VBox painelAcatados = criarPainelAcatados();
        VBox painelExcluidos = criarPainelExcluidos();
        
        TabPane tabPane = new TabPane();
    
        this.tabAnalise = new Tab("Divergentes (" + this.listaAnalise.size() + ")", painelAnalise);
        this.tabAcatados = new Tab("Acatados (" + this.listaAcatados.size() + ")", painelAcatados);
        this.tabExcluidos = new Tab("Excluídos (" + this.listaExcluidos.size() + ")", painelExcluidos); // <-- Modificado
        
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

    private VBox criarPainelAnalise() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));
        
        Label lblTitulo = new Label("Lista de Consignados Para Análise");
        lblTitulo.setFont(new Font("Arial", 16));
        
        this.txtBuscaAnalise = new TextField();
        this.txtBuscaAnalise.setPromptText("Buscar por nome...");
        this.txtBuscaAnalise.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredAnalise.setPredicate(consignado -> filtroPorNome(consignado, newVal));
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

    private VBox criarPainelAcatados() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));
        Label lblTitulo = new Label("Lista de Consignados Acatados");
        lblTitulo.setFont(new Font("Arial", 16));
        this.txtBuscaAcatados = new TextField();
        this.txtBuscaAcatados.setPromptText("Buscar por nome...");
        this.txtBuscaAcatados.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredAcatados.setPredicate(consignado -> filtroPorNome(consignado, newVal));
        });
        this.tabelaAcatados = criarTabelaMudancas(this.filteredAcatados);
        
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

    private VBox criarPainelExcluidos() {
        VBox painel = new VBox(10);
        painel.setPadding(new Insets(10));
        Label lblTitulo = new Label("Lista de Consignados Excluídos");
        lblTitulo.setFont(new Font("Arial", 16));
        this.txtBuscaExcluidos = new TextField();
        this.txtBuscaExcluidos.setPromptText("Buscar por nome...");
        this.txtBuscaExcluidos.textProperty().addListener((obs, oldVal, newVal) -> {
            this.filteredExcluidos.setPredicate(consignado -> filtroPorNome(consignado, newVal));
        });
        
        this.tabelaExcluidos = criarTabelaMudancas(this.filteredExcluidos);
        painel.getChildren().addAll(lblTitulo, txtBuscaExcluidos, this.tabelaExcluidos);
        return painel;
    }

    private boolean filtroPorNome(Mudanca consignado, String textoBusca) {
        if (textoBusca == null || textoBusca.isEmpty() || textoBusca.isBlank()) {
            return true;
        }
        String filtro = textoBusca.toLowerCase().trim();
        if (consignado.getConsignado().getNome() == null) {
            return false;
        }
        return consignado.getConsignado().getNome().toLowerCase().contains(filtro);
    }

    private Optional<MotivoMudanca> perguntarMotivo() {
        List<MotivoMudanca> motivos = List.of(MotivoMudanca.values());
        
        ChoiceDialog<MotivoMudanca> dialog = new ChoiceDialog<>(MotivoMudanca.NULO, motivos);
        dialog.setTitle("Justificar Mudança");
        dialog.setHeaderText("Por favor, selecione o motivo da alteração:");
        dialog.setContentText("Motivo:");

        return dialog.showAndWait();
    }

    private void moverParaExcluidos() {
        List<Consignado> selecionados = new ArrayList<>(this.tabelaAcatados.getSelectionModel().getSelectedItems());
        
        if (selecionados.isEmpty()) return;

        this.listaAcatados.removeAll(selecionados);
        this.listaExcluidos.addAll(selecionados);

        atualizarContagemAbas();

        this.tabelaAcatados.getSelectionModel().clearSelection();
    }

    private void moverParaAcatados() {
        List<Consignado> selecionados = new ArrayList<>(this.tabelaAnalise.getSelectionModel().getSelectedItems());
        if (selecionados.isEmpty()) return;

        Optional<MotivoMudanca> motivoOpt = perguntarMotivo();
        if (motivoOpt.isEmpty()) {
            System.out.println("Movimentação cancelada (nenhum motivo selecionado).");
            return; 
        }
        MotivoMudanca motivo = motivoOpt.get();

        List<Mudanca> novasMudancas = selecionados.stream()
            .map(consignado -> new Mudanca(consignado, motivo))
            .collect(Collectors.toList());

        this.ultimoLoteAcatado = novasMudancas; 

        this.listaAnalise.removeAll(selecionados);
        this.listaAcatados.addAll(novasMudancas);
        
        this.btnDesfazer.setDisable(false);
        atualizarContagemAbas();
        this.tabelaAnalise.getSelectionModel().clearSelection();
    }

    private void desfazerUltimaAcao() {
        if (this.ultimoLoteMovido == null || this.ultimoLoteMovido.isEmpty()) return;
        this.listaAcatados.removeAll(this.ultimoLoteMovido);
        this.listaAnalise.addAll(this.ultimoLoteMovido);
        this.ultimoLoteMovido = null;
        this.btnDesfazer.setDisable(true);
        atualizarContagemAbas();
    }
    private void resetarListas() {
        this.txtBuscaAnalise.clear();
        this.txtBuscaAcatados.clear();
        this.txtBuscaExcluidos.clear();
        
        this.listaAnalise.setAll(resultadoOriginal.divergentes());
        this.listaAcatados.setAll(resultadoOriginal.acatados());
        this.listaExcluidos.setAll(resultadoOriginal.paraExcluir()); 
        
        this.ultimoLoteMovido = null;
        this.btnDesfazer.setDisable(true);
        
        this.tabelaAnalise.getSelectionModel().clearSelection();
        this.tabelaAcatados.getSelectionModel().clearSelection();
        this.tabelaExcluidos.getSelectionModel().clearSelection(); 
        
        atualizarContagemAbas();
    }
    
    private void atualizarContagemAbas() {
        this.tabAnalise.setText("Divergentes (" + this.listaAnalise.size() + ")");
        this.tabAcatados.setText("Acatados (" + this.listaAcatados.size() + ")");
        this.tabExcluidos.setText("Excluídos (" + this.listaExcluidos.size() + ")"); 
    }
    
    private void salvarAlteracoes() {

        List<Mudanca> paraAcatar = new ArrayList<>(this.listaAcatados);
        List<Mudanca> paraExcluir = new ArrayList<>(this.listaExcluidos);
        List<Consignado> paraIgnorar = new ArrayList<>(this.listaAnalise);

        Alert alertConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmacao.setTitle("Confirmar Alterações");
        alertConfirmacao.setHeaderText("Você confirma o salvamento destas alterações?");

        String resumo = String.format(
            "Você está prestes a salvar as seguintes alterações:\n\n" +
            "Total de Acatados (Salvar/Atualizar): %d\n" +
            "Total de Excluídos (Deletar): %d\n" +
            "Total Ignorados (permanecem em análise): %d\n\n" +
            "Deseja continuar?",
            paraAcatar.size(),
            paraExcluir.size(),
            paraIgnorar.size()
        );
        alertConfirmacao.setContentText(resumo);

        VBox expandableContent = new VBox();
        expandableContent.setPadding(new Insets(10));
        
        StringBuilder detalhes = new StringBuilder();
        
        detalhes.append("--- ACATADOS (PARA SALVAR/ATUALIZAR) ---\n");
        if (paraAcatar.isEmpty()) {
            detalhes.append("Nenhum.\n");
        } else {
            for (Consignado c : paraAcatar) {
                detalhes.append(String.format("Contrato: %s | Nome: %s\n", c.getContrato(), c.getNome()));
            }
        }
        
        detalhes.append("\n--- EXCLUÍDOS (PARA DELETAR) ---\n");
        if (paraExcluir.isEmpty()) {
            detalhes.append("Nenhum.\n");
        } else {
            for (Consignado c : paraExcluir) {
                detalhes.append(String.format("Contrato: %s | Nome: %s\n", c.getContrato(), c.getNome()));
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
            ResultadoComparacaoDTO dadosFinais = new ResultadoComparacaoDTO(
                paraIgnorar,  
                paraAcatar,   
                paraExcluir   
            );
            
            System.out.println("Enviando para salvar...");
            
            try {
                ConsignadoController controller = new ConsignadoController();
                controller.salvarResultadosApurados(dadosFinais);
                System.out.println("SIMULAÇÃO: controller.salvarResultadosApurados() chamado.");

                Alert alertSucesso = new Alert(Alert.AlertType.INFORMATION);
                alertSucesso.setTitle("Sucesso");
                alertSucesso.setHeaderText("Alterações salvas com sucesso!");
                alertSucesso.setContentText("O banco de dados foi atualizado com os dados apurados.");
                alertSucesso.showAndWait();
                
                travarBotoesAposSalvar();

            } catch (Exception ex) {
                System.err.println("Erro ao tentar salvar: " + ex.getMessage());
                ex.printStackTrace();
                
                Alert alertErro = new Alert(Alert.AlertType.ERROR);
                alertErro.setTitle("Erro ao Salvar");
                alertErro.setHeaderText("Não foi possível salvar as alterações.");
                alertErro.setContentText("Ocorreu um erro: " + ex.getMessage());
                alertErro.showAndWait();
            }
        } else {
            System.out.println("Salvamento cancelado pelo usuário.");
        }
    }

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
    
    private TableView<Consignado> criarTabelaConsignados(ObservableList<Consignado> lista) {
        TableView<Consignado> tabela = new TableView<>();
        tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Consignado, String> colContrato = new TableColumn<>("Contrato");
        colContrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        TableColumn<Consignado, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Consignado, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        TableColumn<Consignado, String> colMatricula = new TableColumn<>("Matrícula");
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        TableColumn<Consignado, Integer> colPrazo = new TableColumn<>("Prazo Total");
        colPrazo.setCellValueFactory(new PropertyValueFactory<>("prazoTotal"));
        TableColumn<Consignado, Integer> colPrestacao = new TableColumn<>("Nº Prestação");
        colPrestacao.setCellValueFactory(new PropertyValueFactory<>("numeroPrestacao"));
        TableColumn<Consignado, Double> colValor = new TableColumn<>("Valor Prestação");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorPrestacao"));

        tabela.getColumns().addAll(colContrato, colNome, colCpf, colMatricula, colPrazo, colPrestacao, colValor);
        tabela.setItems(lista);
        tabela.setPlaceholder(new Label("Nenhum item encontrado."));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPrefHeight(400); 

        return tabela;
    }

    private TableView<Mudanca> criarTabelaMudancas(ObservableList<Mudanca> lista) {
        TableView<Mudanca> tabela = new TableView<>();
        tabela.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        TableColumn<Mudanca, String> colContrato = new TableColumn<>("Contrato");
        colContrato.setCellValueFactory(new PropertyValueFactory<>("contrato"));
        TableColumn<Mudanca, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        TableColumn<Mudanca, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        TableColumn<Consignado, String> colMatricula = new TableColumn<>("Matrícula");
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        TableColumn<Consignado, Integer> colPrazo = new TableColumn<>("Prazo Total");
        colPrazo.setCellValueFactory(new PropertyValueFactory<>("prazoTotal"));
        TableColumn<Consignado, Integer> colPrestacao = new TableColumn<>("Nº Prestação");
        colPrestacao.setCellValueFactory(new PropertyValueFactory<>("numeroPrestacao"));
        TableColumn<Consignado, Double> colValor = new TableColumn<>("Valor Prestação");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valorPrestacao"));
        TableColumn<Mudanca, MotivoMudanca> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));

        tabela.getColumns().addAll(colContrato, colNome, colCpf, colMatricula, colPrazo, colPrestacao, colValor, colMotivo); 
        
        tabela.setItems(lista);
        tabela.setPlaceholder(new Label("Nenhum item encontrado."));
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        tabela.setPrefHeight(400); 

        return tabela;
    }
}