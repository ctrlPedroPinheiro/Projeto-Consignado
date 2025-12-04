package dev.itaprev.controller;

import java.util.List;

import dev.itaprev.dao.MudancaDAO;
import dev.itaprev.dao.MundacaDAOImpl;
import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.model.Mudanca;

public class MudancaController {
    private MudancaDAO mudancaDAO;
    private ConsignadoController consignadoController;

    public MudancaController() {
        this.mudancaDAO = new MundacaDAOImpl();
        this.consignadoController = new ConsignadoController();
    }

    public void salvarMudancasDTO(List<MudancaDTO> paraAcatar, List<MudancaDTO> paraExcluir) {
        for (MudancaDTO mudanca : paraAcatar) {
            mudancaDAO.salvarMudanca(converterMudancaDTO(mudanca));
        }
        for (MudancaDTO mudanca : paraExcluir) {
            mudancaDAO.salvarMudanca(converterMudancaDTO(mudanca));
        }
    }

    public Mudanca converterMudancaDTO(MudancaDTO mudancaDTO) {
        Mudanca mudanca = new Mudanca();
        mudanca.setMotivo(mudancaDTO.motivo());
        mudanca.setConsignado(consignadoController.converterParaModel(mudancaDTO.consignado()));
        return mudanca;
    }
}
