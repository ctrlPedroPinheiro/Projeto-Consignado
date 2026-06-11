package pp.dev.econsig.entity;

import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Representa um Consignado com informações detalhadas.
 */
@Entity
@Table(name = "consignado")
public class Consignado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contrato não pode ser vazio")
    @Column(unique = true, nullable = false)
    private String contrato;

    @NotBlank(message = "Nome não pode ser vazio")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "CPF não pode ser vazio")
    @Pattern(regexp = "\\d{11}", message = "CPF deve ter 11 dígitos")
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotBlank(message = "Matrícula não pode ser vazio")
    @Column(nullable = false)
    private String matricula;

    @Min(value = 1, message = "Prazo total deve ser maior que 0")
    @Column(nullable = false)
    private int prazoTotal;

    @Min(value = 0, message = "Número de prestação não pode ser negativo")
    @Column(nullable = false)
    private int numeroPrestacao;

    @DecimalMin(value = "0.0", inclusive = false, message = "Valor da prestação deve ser maior que 0")
    @Column(nullable = false)
    private double valorPrestacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_competencia", nullable = false)
    private Competencia competencia;

    public Consignado() {
    }

    public Consignado(String contrato, String nome, String cpf, String matricula, int prazoTotal, int numeroPrestacao, double valorPrestacao, Competencia competencia) {
        this.contrato = contrato;
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.prazoTotal = prazoTotal;
        this.numeroPrestacao = numeroPrestacao;
        this.valorPrestacao = valorPrestacao;
        this.competencia = competencia;
    }

    public int getPrazoRestante() {
        return prazoTotal - numeroPrestacao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContrato() { return contrato; }
    public void setContrato(String contrato) { this.contrato = contrato; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getMatricula() { return matricula; }  
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public int getPrazoTotal() { return prazoTotal; }
    public void setPrazoTotal(int prazoTotal) { this.prazoTotal = prazoTotal; }
    public int getNumeroPrestacao() { return numeroPrestacao; }
    public void setNumeroPrestacao(int numeroPrestacao) { this.numeroPrestacao = numeroPrestacao; }
    public double getValorPrestacao() { return valorPrestacao; }
    public void setValorPrestacao(double valorPrestacao) { this.valorPrestacao = valorPrestacao; }
    public Competencia getCompetencia() { return competencia; }
    public void setCompetencia(Competencia competencia) { this.competencia = competencia; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consignado that = (Consignado) o;
        return Objects.equals(contrato, that.contrato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contrato);
    }

    @Override
    public String toString() {
        return "Consignado{" +
                "id=" + id +
                ", contrato='" + contrato + '\'' +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", matricula='" + matricula + '\'' +
                ", prazoTotal=" + prazoTotal +
                ", numeroPrestacao=" + numeroPrestacao +
                ", valorPrestacao=" + valorPrestacao +
                ", competencia=" + competencia +
                '}';
    }
}

