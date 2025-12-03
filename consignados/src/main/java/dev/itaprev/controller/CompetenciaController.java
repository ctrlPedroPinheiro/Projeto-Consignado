package dev.itaprev.controller;

import dev.itaprev.dao.CompetenciaDAOImpl;
import dev.itaprev.dto.CompetenciaDTO;

public class CompetenciaController {

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

    public CompetenciaDTO buscarPorId (int idcompetencia) {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        return dao.buscarPorId(idcompetencia);
    }

    public void salvarCompetencia(int id, int mes, int ano) {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        dao.salvarCompetencia(id, mes, ano);
    }

    public CompetenciaDTO buscarUltimaCompetencia() {
        CompetenciaDAOImpl dao = new CompetenciaDAOImpl();
        return dao.buscarUltimaCompetencia();
    }
}
