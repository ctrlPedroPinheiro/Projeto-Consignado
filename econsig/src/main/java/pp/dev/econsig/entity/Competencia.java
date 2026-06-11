package pp.dev.econsig.entity;

import java.util.Objects;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Representa uma Competência com mês, ano e ID.
 */
@Entity
@Table(name = "competencia")
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "O mês deve ser entre 1 e 12")
    @Max(value = 12, message = "O mês deve ser entre 1 e 12")
    @Column(nullable = false)
    private int mes;

    @Min(value = 1994, message = "O ano deve ser maior ou igual a 1994")
    @Column(nullable = false)
    private int ano;

    @OneToMany(mappedBy = "competencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consignado> consignados;


    public Competencia() {
    }

    public Competencia(int mes, int ano, Long id, List<Consignado> consignados) {
        this.mes = mes;
        this.ano = ano;
        this.id = id;
        this.consignados = consignados;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public List<Consignado> getConsignados() {
        return consignados;
    }
    public void setConsignados(List<Consignado> consignados) {
        this.consignados = consignados;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Competencia that = (Competencia) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Competencia{" +
                "id=" + id +
                ", mes=" + mes +
                ", ano=" + ano +
                '}';
    }
}
