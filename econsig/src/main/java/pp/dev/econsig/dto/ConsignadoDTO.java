package pp.dev.econsig.dto;

public record ConsignadoDTO(
    Long id,
    String contrato,
    String nome,
    String cpf,
    String matricula,
    int prazoTotal,
    int numeroPrestacao,
    double valorPrestacao,
    Long competenciaId
) {

}
