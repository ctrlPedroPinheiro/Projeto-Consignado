package pp.dev.econsig.dto;

import java.util.List;

public record CompetenciaDetalhesDTO(
    Long id,
    int mes,
    int ano,
    List<ConsignadoDTO> consignados
) {

}
