package dev.itaprev.dto;

import dev.itaprev.model.Consignado;
import java.util.List;

public record ResultadoComparacaoDTO(
    List<Consignado> divergentes,
    List<Consignado> acatados,
    List<Consignado> paraExcluir,
    int idCompetencia
) {
}