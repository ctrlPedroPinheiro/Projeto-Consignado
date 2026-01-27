package dev.itaprev.util;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import dev.itaprev.dto.ConsignadoDTO;
import dev.itaprev.dto.MudancaDTO;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe responsável pela geração de relatórios em PDF.
 */
public class GeradorPDF {

    /**
     * Gera um relatório de consulta em PDF.
     * @param listaConsignados
     * @param listaMudancas
     */
    public static final void gerarRelatorioConsulta(List<ConsignadoDTO> listaConsignados, List<MudancaDTO> listaMudancas) {

        double valorTotal = 0.0;
        for (ConsignadoDTO consignado : listaConsignados) {
            valorTotal += consignado.valorPrestacao();
        }
        int quantidadeRegistros = listaConsignados.size();

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
        
        String dataFormatada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        context.setVariable("dataGeracao", dataFormatada);

        String htmlFinal = templateEngine.process("consulta-consignados", context);
        
        File arquivoPdf = new File("relatorio_consignado.pdf");

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
}