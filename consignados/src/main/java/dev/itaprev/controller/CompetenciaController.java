package dev.itaprev.controller;

import java.util.List;

import dev.itaprev.dao.CompetenciaDAOImpl;
import dev.itaprev.dto.CompetenciaDTO;

/**
 * Classe responsável pela lógica de negócios das competências.
 */
public class CompetenciaController {

    /**
     * Busca uma competência pelo mês e ano.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     * @return A competência encontrada ou uma nova competência criada.
     */
    public CompetenciaDTO buscarIdCompetencia(int mes, int ano) {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        CompetenciaDTO competencia = dao.buscarPorMesAno(mes, ano);
        if (competencia == null) {
            CompetenciaDTO ultimaCompetencia = dao.buscarUltimaCompetencia();
            int idcompetencia = ultimaCompetencia.idcompetencia() + 1;
            dao.salvarCompetencia(idcompetencia, mes, ano);
            competencia = dao.buscarPorMesAno(mes, ano);
            return competencia;
            
        }
        return competencia;
    }

    /**
     * Busca uma competência pelo ID.
     * @param idcompetencia O ID da competência.
     * @return A competência encontrada.
     */
    public CompetenciaDTO buscarPorId (int idcompetencia) {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        return dao.buscarPorId(idcompetencia);
    }

    /**
     * Salva uma nova competência.
     * @param id O ID da competência.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     */
    public void salvarCompetencia(int id, int mes, int ano) {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        dao.salvarCompetencia(id, mes, ano);
    }

    /**
     * Busca a última competência.
     * @return A última competência encontrada.
     */
    public CompetenciaDTO buscarUltimaCompetencia() {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        return dao.buscarUltimaCompetencia();
    }

    /**
     * Lista todas as competências.
     * @return A lista de competências.
     */
    public List<CompetenciaDTO> listarCompetencias() {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        return dao.buscarTodos();
    }
}
