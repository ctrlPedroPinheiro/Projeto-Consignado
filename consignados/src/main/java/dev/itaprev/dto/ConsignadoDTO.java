package dev.itaprev.dto;

/**
 * Data Transfer Object para Consignado.
 */
public record ConsignadoDTO(
    String contrato,
    String nome,
    String cpf,
    String matricula,
    int prazoTotal,
    int numeroPrestacao,
    double valorPrestacao,
    int prazoRestante,
    int idCompetencia
) {
    public ConsignadoDTO(String contrato, String nome, String cpf, String matricula, int prazoTotal, int numeroPrestacao, double valorPrestacao, int idCompetencia) {
        this(contrato, nome, cpf, matricula, prazoTotal, numeroPrestacao, valorPrestacao, prazoTotal - numeroPrestacao, idCompetencia);
    }
}
