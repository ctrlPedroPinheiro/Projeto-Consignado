package dev.itaprev.dao;

import dev.itaprev.model.Consignado;
import java.util.List;
import java.util.Optional;

public interface ConsignadoDAO {

    /**
     * Salva um novo consignado no banco de dados.
     * @param consignado O objeto a ser salvo.
     */
    void salvar(Consignado consignado);

    /**
     * Atualiza um consignado existente no banco de dados.
     * @param consignado O objeto com os dados atualizados.
     */
    void atualizar(Consignado consignado);

    /**
     * Deleta um consignado do banco de dados pelo seu contrato.
     * @param contrato O número do contrato a ser deletado.
     */
    void deletar(String contrato, int idcompetencia);

    /**
     * Busca um consignado pelo seu contrato (chave primária).
     * @param contrato O número do contrato.
     * @return um Optional contendo o Consignado se encontrado, ou vazio se não.
     */
    Optional<Consignado> buscarPorContrato(String contrato, int idcompetencia);

    /**
     * Busca todos os consignados cadastrados.
     * @return Uma lista com todos os consignados.
     */
    List<Consignado> buscarTodos(int idcompetencia);

    int buscarIdConsignado(String contrato, int idcompetencia);

    Consignado buscarPorIdconsignado(int idconsignado);
}