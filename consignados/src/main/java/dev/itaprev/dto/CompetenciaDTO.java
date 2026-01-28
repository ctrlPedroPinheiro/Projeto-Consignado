package dev.itaprev.dto;

/**
 * Data Transfer Object para Competência.
 */
public record CompetenciaDTO( 
    int mes,
    int ano,
    int idcompetencia
) {
    public CompetenciaDTO(int mes, int ano, int idcompetencia) {
        this.mes = mes;
        this.ano = ano;
        this.idcompetencia = idcompetencia;
    }
}
