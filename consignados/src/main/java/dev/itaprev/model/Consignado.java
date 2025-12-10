package dev.itaprev.model;

import java.util.Objects;

public class Consignado {
    private String contrato;
    private String nome;
    private String cpf;
    private String matricula;
    private int prazoTotal;
    private int numeroPrestacao;
    private double valorPrestacao;
    private int idCompetencia;

    public Consignado() {
    }

    public Consignado(String contrato, String nome, String cpf, String matricula, int prazoTotal, int numeroPrestacao, double valorPrestacao, int idCompetencia) {
        this.contrato = contrato;
        this.nome = nome;
        this.cpf = cpf;
        this.matricula = matricula;
        this.prazoTotal = prazoTotal;
        this.numeroPrestacao = numeroPrestacao;
        this.valorPrestacao = valorPrestacao;
        this.idCompetencia = idCompetencia;
    }

    public int getPrazoRestante() {
        return prazoTotal - numeroPrestacao;
    }

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
    public int getIdCompetencia() { return idCompetencia; }
    public void setIdCompetencia(int idCompetencia) { this.idCompetencia = idCompetencia; }

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

    public String toString() {
        return "Consignado{" +
                "contrato='" + contrato + '\'' +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", matricula='" + matricula + '\'' +
                ", prazoTotal=" + prazoTotal +
                ", numeroPrestacao=" + numeroPrestacao +
                ", valorPrestacao=" + valorPrestacao +
                ", idCompetencia=" + idCompetencia +
                '}';
    }

    public void setIdConsignado(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setIdConsignado'");
    }
}
