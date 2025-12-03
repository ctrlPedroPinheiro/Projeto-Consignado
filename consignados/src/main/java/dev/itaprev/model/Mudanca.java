package dev.itaprev.model;

public class Mudanca {
    private Consignado consignado;
    private MotivoMudanca motivo;
    
    public Mudanca(Consignado consignado, MotivoMudanca motivo) {
        this.consignado = consignado;
        this.motivo = motivo;
    }

    public Consignado getConsignado() {
        return consignado;
    }
    public MotivoMudanca getMotivo() {
        return motivo;
    }
    public void setMotivo(MotivoMudanca motivo) {
        this.motivo = motivo;
    }
}
