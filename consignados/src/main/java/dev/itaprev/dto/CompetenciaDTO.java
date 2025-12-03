package dev.itaprev.dto;

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
