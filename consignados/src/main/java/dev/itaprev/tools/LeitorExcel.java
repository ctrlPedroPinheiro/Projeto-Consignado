package dev.itaprev.tools;

import org.apache.poi.ss.usermodel.*;
import dev.itaprev.dto.ConsignadoDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeitorExcel {

    public List<ConsignadoDTO> lerArquivo(File arquivo, int idCompetencia) {

        ArrayList<ConsignadoDTO> listaDeConsignados = new ArrayList<>();

        if (arquivo == null || !arquivo.exists()) {
            throw new IllegalArgumentException("Arquivo inválido ou nulo.");
        }

        int COLUNA_PRAZO_TOTAL = 6;
        int COLUNA_CONTRATO = 8;
        int COLUNA_N_PRESTACAO = 9;
        int COLUNA_MATRICULA = 10;
        int COLUNA_NOME = 11;
        int COLUNA_CPF = 14;
        int COLUNA_VALOR_PRESTACAO = 15;

        System.out.println("Iniciando a leitura do arquivo: " + arquivo.getAbsolutePath());

        try (FileInputStream fis = new FileInputStream(arquivo);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet planilha = workbook.getNumberOfSheets() > 1 ? workbook.getSheetAt(1) : workbook.getSheetAt(0);
            System.out.println("Lendo a planilha: " + planilha.getSheetName());

            for (Row linha : planilha) {
                if (linha.getRowNum() == 0) continue; 

                String contrato = getCellValue(linha.getCell(COLUNA_CONTRATO));
                String nome = getCellValue(linha.getCell(COLUNA_NOME));
                String cpf = getCellValue(linha.getCell(COLUNA_CPF));
                String matricula = getCellValue(linha.getCell(COLUNA_MATRICULA));
                
                int prazoTotal = (int) getNumericValue(linha.getCell(COLUNA_PRAZO_TOTAL));
                int numeroPrestacao = (int) getNumericValue(linha.getCell(COLUNA_N_PRESTACAO));
                double valorPrestacao = getNumericValue(linha.getCell(COLUNA_VALOR_PRESTACAO));

                if (!contrato.isEmpty() || !nome.isEmpty()) {
                    ConsignadoDTO consignado = new ConsignadoDTO(contrato, nome, cpf, matricula, prazoTotal, numeroPrestacao, valorPrestacao, idCompetencia);
                    listaDeConsignados.add(consignado);
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo Excel: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erro na leitura do arquivo", e);
        }
        return listaDeConsignados;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue()); 
            default: return "";
        }
    }

    private double getNumericValue(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
             try {
                 return Double.parseDouble(cell.getStringCellValue().replace(",", "."));
             } catch (NumberFormatException e) {
                 return 0.0;
             }
        }
        return 0.0;
    }
}