package dev.itaprev.dao;

import java.util.List;
import dev.itaprev.model.Mudanca;

public interface MudancaDAO {
    
    void salvarMudanca(Mudanca mudanca);

    List<Mudanca> buscarTodos(int idcompetencia);

    Mudanca buscarPorConsignado(int idconsignado);

    Mudanca buscarPorId(int idmudanca);

    List<Mudanca> buscarPorMotivo(String motivo, int idcompetencia);
}
