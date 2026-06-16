package pp.dev.econsig.component;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import pp.dev.econsig.dto.ConsignadoDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class LeitorExcel {

    public List<ConsignadoDTO> lerArquivo(InputStream inputStream, Long idCompetencia) throws IOException {
        List<ConsignadoDTO> lista = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // pula cabeçalho

                lista.add(new ConsignadoDTO(
                    row.getCell(0).getStringCellValue(),  // contrato
                    row.getCell(1).getStringCellValue(),  // nome
                    row.getCell(2).getStringCellValue(),  // cpf
                    row.getCell(3).getStringCellValue(),  // matricula
                    (int) row.getCell(4).getNumericCellValue(), // prazoTotal
                    (int) row.getCell(5).getNumericCellValue(), // numeroPrestacao
                    row.getCell(6).getNumericCellValue(),       // valorPrestacao
                    idCompetencia
                ));
            }
        }
        return lista;
    }
}