package pp.dev.econsig.entity;

import java.util.Objects;
import jakarta.persistence.*;

/**
 * Representa uma Mudança no consignado.
 */
@Entity
@Table(name = "mudanca")
public class Mudanca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_consignado", nullable = false, unique = true)
    private Consignado consignado;

    @Enumerated(EnumType.STRING)
    private MotivoMudanca motivo;
    
    public Mudanca(Consignado consignado, MotivoMudanca motivo) {
        this.consignado = consignado;
        this.motivo = motivo;
    }

    public Mudanca() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Consignado getConsignado() {
        return consignado;
    }
    public void setConsignado(Consignado consignado) {
        this.consignado = consignado;
    }
    public MotivoMudanca getMotivo() {
        return motivo;
    }
    public void setMotivo(MotivoMudanca motivo) {
        this.motivo = motivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mudanca mudanca = (Mudanca) o;
        return Objects.equals(id, mudanca.id) && Objects.equals(consignado, mudanca.consignado) && motivo == mudanca.motivo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, consignado, motivo);
    }

    @Override
    public String toString() {
        return "Mudanca{" +
                "id=" + id +
                ", consignado=" + consignado +
                ", motivo=" + motivo +
                '}';
    }
}
