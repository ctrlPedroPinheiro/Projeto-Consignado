package dev.itaprev.tools;

import org.apache.poi.ss.usermodel.*;

import dev.itaprev.dto.ConsignadoDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.UIManager;

public class LeitorExcel {

    public static ArrayList<ConsignadoDTO> lerExcel(int idCompetencia) {

        ArrayList<ConsignadoDTO> listaDeConsignados = new ArrayList<>();

        File arquivoSelecionado = escolherArquivo();

        if (arquivoSelecionado == null) {
            System.out.println("Nenhum arquivo foi selecionado. A operação foi cancelada.");
            throw new RuntimeException("Operação de leitura de Excel cancelada pelo usuário.");
        }
        
        String caminhoDoArquivo = arquivoSelecionado.getAbsolutePath();

        int COLUNA_PRAZO_TOTAL = 6;
        int COLUNA_CONTRATO = 8;
        int COLUNA_N_PRESTACAO = 9;
        int COLUNA_MATRICULA = 10;
        int COLUNA_NOME = 11;
        int COLUNA_CPF = 14;
        int COLUNA_VALOR_PRESTACAO = 15;

        System.out.println("Iniciando a leitura do arquivo: " + caminhoDoArquivo);

        try (FileInputStream arquivo = new FileInputStream(new File(caminhoDoArquivo));
             Workbook workbook = WorkbookFactory.create(arquivo)) {

            Sheet planilha = workbook.getSheetAt(1);
            System.out.println("Lendo a planilha: " + planilha.getSheetName());

            for (Row linha : planilha) {
                if (linha.getRowNum() == 0) {
                    continue;
                }
                Cell celulaContrato = linha.getCell(COLUNA_CONTRATO);
                Cell celulaNome = linha.getCell(COLUNA_NOME);
                Cell celulaCpf = linha.getCell(COLUNA_CPF);
                Cell celulaMatricula = linha.getCell(COLUNA_MATRICULA);
                Cell celulaPrazoTotal = linha.getCell(COLUNA_PRAZO_TOTAL);
                Cell celulaNumeroPrestacao = linha.getCell(COLUNA_N_PRESTACAO);
                Cell celulaValorPrestacao = linha.getCell(COLUNA_VALOR_PRESTACAO);

                String contrato = celulaContrato != null ? celulaContrato.getStringCellValue() : "";
                String nome = celulaNome != null ? celulaNome.getStringCellValue() : "";
                String cpf = celulaCpf != null ? celulaCpf.getStringCellValue() : "";
                String matricula = celulaMatricula != null ? celulaMatricula.getStringCellValue() : "";
                int prazoTotal = celulaPrazoTotal != null ? (int) celulaPrazoTotal.getNumericCellValue() : 0;
                int numeroPrestacao = celulaNumeroPrestacao != null ? (int) celulaNumeroPrestacao.getNumericCellValue() : 0;
                double valorPrestacao = celulaValorPrestacao != null ? celulaValorPrestacao.getNumericCellValue() : 0.0;
                ConsignadoDTO consignado = new ConsignadoDTO(contrato, nome, cpf, matricula, prazoTotal, numeroPrestacao, valorPrestacao, idCompetencia);
                listaDeConsignados.add(consignado); 
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo. Verifique se o caminho está correto e se o arquivo não está corrompido.");
            e.printStackTrace();
        }
        return listaDeConsignados;
    }

    private static File escolherArquivo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecione o arquivo Excel para importação");
        
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Arquivos Excel (.xlsx, .xls)", "xlsx", "xls");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        
        return null; 
    }
}
