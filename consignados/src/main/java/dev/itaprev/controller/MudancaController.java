package dev.itaprev.controller;

import java.util.List;

import dev.itaprev.dao.MudancaDAO;
import dev.itaprev.dao.MudancaDAOImpl;
import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.model.MotivoMudanca;
import dev.itaprev.model.Mudanca;

public class MudancaController {
    private MudancaDAO mudancaDAO;
    private ConsignadoController consignadoController;

    public MudancaController() {
        this.mudancaDAO = new MudancaDAOImpl();
        this.consignadoController = new ConsignadoController();
    }

    public void salvarMudancasDTO(List<MudancaDTO> paraAcatar, List<MudancaDTO> paraExcluir) {

        for (MudancaDTO mudanca : paraAcatar) {
            if (mudanca.motivo() != MotivoMudanca.NULO) {
                mudancaDAO.salvarMudanca(converterMudancaDTO(mudanca));
            }
        }
        for (MudancaDTO mudanca : paraExcluir) {
            if (mudanca.motivo() != MotivoMudanca.NULO) {
                mudancaDAO.salvarMudanca(converterMudancaDTO(mudanca));
            }
        }
    }

    public Mudanca converterMudancaDTO(MudancaDTO mudancaDTO) {
        Mudanca mudanca = new Mudanca();
        mudanca.setMotivo(mudancaDTO.motivo());
        mudanca.setConsignado(consignadoController.converterParaModel(mudancaDTO.consignado()));
        return mudanca;
    }

    public List<MudancaDTO> listarMudancasPorCompetencia(int idcompetencia) {
        return mudancaDAO.buscarTodos(idcompetencia);
    }
}
