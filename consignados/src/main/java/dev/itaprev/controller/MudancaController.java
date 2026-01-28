package dev.itaprev.controller;

import java.util.List;

import dev.itaprev.dao.MudancaDAO;
import dev.itaprev.dao.MudancaDAOImpl;
import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.model.MotivoMudanca;
import dev.itaprev.model.Mudanca;

/**
 * Controlador responsável pela lógica de negócios das mudanças.
 */
public class MudancaController {
    private MudancaDAO mudancaDAO;
    private ConsignadoController consignadoController;

    /**
     * Construtor da classe MudancaController.
     */
    public MudancaController() {
        this.mudancaDAO = new MudancaDAOImpl();
        this.consignadoController = new ConsignadoController();
    }

    /**
     * Salva as mudanças a serem acatadas e excluídas.
     * @param paraAcatar As mudanças a serem acatadas.
     * @param paraExcluir As mudanças a serem excluídas.
     */
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

    /**
     * Converte um DTO de mudança para um modelo.
     * @param mudancaDTO O DTO de mudança a ser convertido.
     * @return O modelo de mudança correspondente.
     */
    public Mudanca converterMudancaDTO(MudancaDTO mudancaDTO) {
        Mudanca mudanca = new Mudanca();
        mudanca.setMotivo(mudancaDTO.motivo());
        mudanca.setConsignado(consignadoController.converterParaModel(mudancaDTO.consignado()));
        return mudanca;
    }

    /**
     * Lista as mudanças de uma determinada competência.
     * @param idcompetencia O ID da competência.
     * @return A lista de mudanças da competência.
     */
    public List<MudancaDTO> listarMudancasPorCompetencia(int idcompetencia) {
        return mudancaDAO.buscarTodos(idcompetencia);
    }
}
