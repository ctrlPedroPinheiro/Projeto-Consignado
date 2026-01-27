package dev.itaprev.dto;

import dev.itaprev.model.Consignado;
import java.util.List;

/**
 * Data Transfer Object para o resultado da comparação de consignados.
 */
public record ResultadoComparacaoDTO(
    List<Consignado> divergentes,
    List<Consignado> acatados,
    List<Consignado> paraExcluir,
    int idCompetencia
) {
}