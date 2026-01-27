package dev.itaprev.dao;

import java.util.List;

import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.model.Mudanca;

/**
 * Interface para operações de banco de dados relacionadas a mudanças.
 */
public interface MudancaDAO {
    
    /**
     * Salva uma nova mudança no banco de dados.
     * @param mudanca O objeto a ser salvo.
     */
    void salvarMudanca(Mudanca mudanca);

    /**
     * Busca todas as mudanças cadastradas.
     * @param idcompetencia O ID da competência associada.
     * @return Uma lista com todas as mudanças.
     */
    List<MudancaDTO> buscarTodos(int idcompetencia);

    /**
     * Busca uma mudança pelo ID do consignado.
     * @param idconsignado O ID do consignado associado.
     * @return A mudança encontrada, ou null se não existir.
     */
    Mudanca buscarPorConsignado(int idconsignado);

    /**
     * Busca uma mudança pelo ID.
     * @param idmudanca O ID da mudança.
     * @return A mudança encontrada, ou null se não existir.
     */
    Mudanca buscarPorId(int idmudanca);

    /**
     * Busca uma mudança pelo motivo.
     * @param motivo O motivo da mudança.
     * @param idcompetencia O ID da competência associada.
     * @return Uma lista com as mudanças encontradas.
     */
    List<Mudanca> buscarPorMotivo(String motivo, int idcompetencia);
}
