package dev.itaprev.dao;

import java.util.List;

import dev.itaprev.dto.CompetenciaDTO;

/**
 * Interface para operações de banco de dados relacionadas a competências.
 */
public interface CompetenciaDAO {
    /**
     * Salva uma nova competência no banco de dados.
     * @param id O ID da competência.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     */
    void salvarCompetencia(int id, int mes, int ano);

    /**
     * Busca uma competência pelo seu ID.
     * @param idcompetencia O ID da competência.
     * @return A competência encontrada, ou null se não encontrada.
     */
    CompetenciaDTO buscarPorId(int idcompetencia);

    /**
     * Busca uma competência pelo seu mês e ano.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     * @return A competência encontrada, ou null se não encontrada.
     */
    CompetenciaDTO buscarPorMesAno(int mes, int ano);

    /**
     * Busca todas as competências no banco de dados.
     * @return Uma lista com todas as competências.
     */
    List<CompetenciaDTO> buscarTodos();

    /**
     * Busca a última competência registrada no banco de dados.
     * @return A última competência encontrada, ou null se não encontrada.
     */
    CompetenciaDTO buscarUltimaCompetencia();

}
