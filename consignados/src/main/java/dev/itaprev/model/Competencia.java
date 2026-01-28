package dev.itaprev.model;

/**
 * Representa uma Competência com mês, ano e ID.
 */
public class Competencia {
    private int mes;
    private int ano;
    private int idcompetencia;

    public Competencia() {
    }

    public Competencia(int mes, int ano, int idcompetencia) {
        this.mes = mes;
        this.ano = ano;
        this.idcompetencia = idcompetencia;
    }

    public int getMes() {
        return mes;
    }
    public void setMes(int mes) {
        this.mes = mes;
    }
    public int getAno() {
        return ano;
    }
    public void setAno(int ano) {
        this.ano = ano;
    }
    public int getIdcompetencia() {
        return idcompetencia;
    }
}
