package dev.itaprev.model;

/**
 * Representa uma Mudança no consignado.
 */
public class Mudanca {
    private Consignado consignado;
    private MotivoMudanca motivo;
    
    public Mudanca(Consignado consignado, MotivoMudanca motivo) {
        this.consignado = consignado;
        this.motivo = motivo;
    }

    public Mudanca() {}

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
}
