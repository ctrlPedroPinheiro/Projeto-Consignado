package dev.itaprev.dto;

import dev.itaprev.model.MotivoMudanca;

public record MudancaDTO (
    ConsignadoDTO consignado,
    MotivoMudanca motivo
) {

}
