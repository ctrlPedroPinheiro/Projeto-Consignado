package dev.itaprev.dao;

import java.util.List;

import dev.itaprev.dto.CompetenciaDTO;

public interface CompetenciaDAO {
    /**
     * Salva uma nova competência no banco de dados.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     */
    void salvarCompetencia(int id, int mes, int ano);

    /**
     * Busca uma competência pelo seu ID.
     * @param idcompetencia O ID da competência.
     * @return Um objeto CompetenciaDTO representando a competência.
     */
    CompetenciaDTO buscarPorId(int idcompetencia);

    /**
     * Busca uma competência pelo seu mês e ano.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     * @return Um objeto CompetenciaDTO representando a competência.
     */
    CompetenciaDTO buscarPorMesAno(int mes, int ano);

    /**
     * Busca todas as competências no banco de dados.
     * @return Uma lista de objetos CompetenciaDTO representando todas as competências.
     */
    List<CompetenciaDTO> buscarTodos();

    /**
     * Busca a última competência registrada no banco de dados.
     * @return Um objeto CompetenciaDTO representando a última competência.
     */
    CompetenciaDTO buscarUltimaCompetencia();

}
