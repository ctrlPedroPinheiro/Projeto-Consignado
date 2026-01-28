package dev.itaprev.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import dev.itaprev.dto.ConsignadoDTO;
import dev.itaprev.dto.MudancaDTO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe responsável pela geração de relatórios em PDF.
 */
public class Exportador {

    /**
     * Gera um relatório de consulta em PDF.
     * @param listaConsignados
     * @param listaMudancas
     */
    public static final void gerarRelatorioConsultaPDF(List<ConsignadoDTO> listaConsignados, List<MudancaDTO> listaMudancas) {

        double valorTotal = 0.0;
        for (ConsignadoDTO consignado : listaConsignados) {
            valorTotal += consignado.valorPrestacao();
        }
        int quantidadeRegistros = listaConsignados.size();
        int quantidadeMudancas = listaMudancas.size();

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/"); 
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("listaConsignados", listaConsignados);
        context.setVariable("valorTotal", valorTotal);
        context.setVariable("quantidadeRegistros", quantidadeRegistros);
        context.setVariable("listaMudancas", listaMudancas);
        context.setVariable("quantidadeMudancas", quantidadeMudancas);

        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        context.setVariable("dataGeracao", dataFormatada);

        String htmlFinal = templateEngine.process("consulta-consignados", context);
        
        String dataFormatada2 = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        File arquivoPdf = new File("relatorio_consignado_" + dataFormatada2 + ".pdf");

        try (OutputStream os = new FileOutputStream(arquivoPdf)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlFinal, null);
            builder.toStream(os);
            builder.run();
            
            System.out.println("PDF gerado em: " + arquivoPdf.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(arquivoPdf);
            }
        } catch (Exception e) {
            System.err.println("Erro ao tentar abrir o PDF automaticamente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gera um relatório de consulta em Excel.
     * @param listaConsignados
     * @param listaMudancas
     * @throws IOException 
     */
    public static final void gerarRelatorioConsultaExcel(List<ConsignadoDTO> listaConsignados, List<MudancaDTO> listaMudancas) {
        try (Workbook relatorioExcel = new XSSFWorkbook()) {
            Sheet planilhaConsignados = relatorioExcel.createSheet("CONSIGNADOS");
            Row headerRow = planilhaConsignados.createRow(0);
            String[] cabecalhos = {"CONTRATO", "NOME", "CPF", "MATRICULA", "PRAZO", "Nº PARC", "VALOR"};

            CellStyle headerStyle = criarEstiloCabecalho(relatorioExcel);

            for (int i = 0; i < cabecalhos.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cabecalhos[i]);
                cell.setCellStyle(headerStyle);
            }

            int iRowC = 1;
            for (ConsignadoDTO c : listaConsignados) {
                Row row = planilhaConsignados.createRow(iRowC++);
                row.createCell(0).setCellValue(c.contrato());
                row.createCell(1).setCellValue(c.nome());
                row.createCell(2).setCellValue(c.cpf());
                row.createCell(3).setCellValue(c.matricula());
                row.createCell(4).setCellValue(c.prazoTotal());
                row.createCell(5).setCellValue(c.numeroPrestacao());
                row.createCell(6).setCellValue(c.valorPrestacao());
            }

            for(int i = 0; i < cabecalhos.length; i++) {
                planilhaConsignados.autoSizeColumn(i);
            }

            Sheet planilhaMudancas = relatorioExcel.createSheet("MUDANÇAS");
            Row headerRow2 = planilhaMudancas.createRow(0);
            String[] cabecalhos2 = {"CONTRATO", "NOME", "CPF", "MATRICULA", "VALOR", "MOTIVO"};

            CellStyle headerStyle2 = criarEstiloCabecalho(relatorioExcel);

            for (int i = 0; i < cabecalhos2.length; i++) {
                Cell cell = headerRow2.createCell(i);
                cell.setCellValue(cabecalhos2[i]);
                cell.setCellStyle(headerStyle2);
            }

            int iRowM = 1;
            for (MudancaDTO m : listaMudancas) {
                Row row = planilhaMudancas.createRow(iRowM++);
                row.createCell(0).setCellValue(m.consignado().contrato());
                row.createCell(1).setCellValue(m.consignado().nome());
                row.createCell(2).setCellValue(m.consignado().cpf());
                row.createCell(3).setCellValue(m.consignado().matricula());
                row.createCell(4).setCellValue(m.consignado().valorPrestacao());
                row.createCell(5).setCellValue(m.motivo().toString());
            }

            for(int i = 0; i < cabecalhos2.length; i++) {
                planilhaMudancas.autoSizeColumn(i);
            }

            String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            File arquivoExcel = new File("relatorio_consignado_" + dataFormatada + ".xlsx");

            try (FileOutputStream fos = new FileOutputStream(arquivoExcel)) {
                relatorioExcel.write(fos);
                System.out.println("Excel gerado em: " + arquivoExcel.getAbsolutePath());
            }

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                Desktop.getDesktop().open(arquivoExcel);
            }
 
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao gerar Excel: " + e.getMessage());
        }

    }

    /**
     * Cria um estilo de cabeçalho para as células do Excel.
     * @param workbook
     * @return
     */
    private static CellStyle criarEstiloCabecalho(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}