package dev.itaprev.dto;

import dev.itaprev.model.MotivoMudanca;

/**
 * Data Transfer Object para Mudança.
 */
public record MudancaDTO (
    ConsignadoDTO consignado,
    MotivoMudanca motivo
) {

}
