package dev.itaprev.controller;

import dev.itaprev.dao.ConsignadoDAOImpl;
import dev.itaprev.dto.CompetenciaDTO;
import dev.itaprev.dto.ConsignadoDTO;
import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.dto.ResultadoComparacaoDTO;
import dev.itaprev.model.Consignado;
import dev.itaprev.tools.LeitorExcel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConsignadoController {

    public ResultadoComparacaoDTO compararConsignados(int mes, int ano) {
        CompetenciaController competenciaController = new CompetenciaController();
        CompetenciaDTO competencia = competenciaController.buscarIdCompetencia(mes, ano);
        int idCompetencia = competencia.idcompetencia();
        System.out.println("Comparando consignados para Competência ID: " + idCompetencia + " (Mês: " + mes + ", Ano: " + ano + ")");

        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();

        Map<String, Consignado> mapImportados = new HashMap<>();
        for (Consignado c : importarConsignados(idCompetencia)) {
            mapImportados.put(c.getContrato(), c);
        }

        Map<String, Consignado> mapBanco = new HashMap<>();
        for (Consignado c : dao.buscarTodos(idCompetencia-1)) {
            mapBanco.put(c.getContrato(), c);
        }
        
        System.out.println("Dados do Banco: " + mapBanco.size() + " registros.");
        System.out.println("Dados Importados: " + mapImportados.size() + " registros.");

        List<Consignado> divergencias = new ArrayList<>();
        List<Consignado> acatados = new ArrayList<>();
        List<Consignado> exclusoes = new ArrayList<>();

        for (Consignado importado : mapImportados.values()) {
            String chaveContrato = importado.getContrato();
            Consignado doBanco = mapBanco.get(chaveContrato);

            if (doBanco == null) {
                divergencias.add(importado);
            } else {
                if (dadosSaoDivergentes(importado, doBanco)) {
                    acatados.add(importado);
                }
            }
        }

        for (Consignado doBanco : mapBanco.values()) {
            String chaveContrato = doBanco.getContrato();
            if (!mapImportados.containsKey(chaveContrato)) {
                exclusoes.add(doBanco);
            }
        }

        divergencias.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        acatados.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        exclusoes.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));

        System.out.println("--- Resultados da Comparação ---");
        System.out.println("Divergências: " + divergencias.size());
        System.out.println("Acatados: " + acatados.size());
        System.out.println("Exclusões: " + exclusoes.size());
        System.out.println("----------------------------------");

        return new ResultadoComparacaoDTO(divergencias, acatados, exclusoes, idCompetencia);
    }

    public ArrayList<Consignado> importarConsignados(int idCompetencia) {
        ArrayList<ConsignadoDTO> consignadosDTO = LeitorExcel.lerExcel(idCompetencia);

        ArrayList<Consignado> consignados = new ArrayList<>();
        for (ConsignadoDTO dto : consignadosDTO) {
            consignados.add(new Consignado(
                dto.contrato(),
                dto.nome(),
                dto.cpf(),
                dto.matricula(),
                dto.prazoTotal(),
                dto.numeroPrestacao(),
                dto.valorPrestacao(),
                dto.idCompetencia()
            ));
        }
        System.out.println("Total de consignados importados: " + consignados.size());

        return consignados;
    }

    private boolean dadosSaoDivergentes(Consignado importado, Consignado doBanco) {
        if (!Objects.equals(importado.getNome(), doBanco.getNome())) {
            return true;
        }
        if (!Objects.equals(importado.getCpf(), doBanco.getCpf())) {
            return true;
        }
        if (!Objects.equals(importado.getMatricula(), doBanco.getMatricula())) {
            return true;
        }
        if (importado.getPrazoTotal() != doBanco.getPrazoTotal()) {
            return true;
        }
        if (importado.getNumeroPrestacao() != doBanco.getNumeroPrestacao()) {
            return true;
        }
        
        double epsilon = 0.001;
        if (Math.abs(importado.getValorPrestacao() - doBanco.getValorPrestacao()) > epsilon) {
            return true;
        }
        return false;
    }

    public ArrayList<ConsignadoDTO> listarConsignados(int idCompetencia) {
        ArrayList<Consignado> consignados = new ArrayList<>();
        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();
        consignados.addAll(dao.buscarTodos(idCompetencia));
        ArrayList<ConsignadoDTO> consignadosDTO = new ArrayList<>();
        for (Consignado consignado : consignados) {
            consignadosDTO.add(converterParaDTO(consignado));
        }
        consignadosDTO.sort((a, b) -> a.nome().compareToIgnoreCase(b.nome()));
        return consignadosDTO;
    }

    public ArrayList<ConsignadoDTO> buscarPorNome(String nome, int idCompetencia) {
        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();
        ArrayList<Consignado> resultados = new ArrayList<>();
        resultados.addAll(dao.buscarPorNome(nome, idCompetencia));
        ArrayList<ConsignadoDTO> resultadosDTO = new ArrayList<>();
        for (Consignado consignado : resultados) {
            resultadosDTO.add(converterParaDTO(consignado));
        }
        resultadosDTO.sort((a, b) -> a.nome().compareToIgnoreCase(b.nome()));
        return resultadosDTO;
    }

    public ArrayList<ConsignadoDTO> buscarPorCpf(String cpf, int idCompetencia) {
        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();
        ArrayList<Consignado> resultados = new ArrayList<>();
        resultados.addAll(dao.buscarPorCpf(cpf, idCompetencia));
        ArrayList<ConsignadoDTO> resultadosDTO = new ArrayList<>();
        for (Consignado consignado : resultados) {
            resultadosDTO.add(converterParaDTO(consignado));
        }
        resultadosDTO.sort((a, b) -> a.nome().compareToIgnoreCase(b.nome()));
        return resultadosDTO;
    }

    public void salvarMudancasDTO(List<MudancaDTO> mudancaDTOs) {
        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();
        for (MudancaDTO dto : mudancaDTOs) {
            Consignado consignado = converterParaModel(dto.consignado());
            dao.salvar(consignado);
        }
    }

    public int buscarIdConsignado(String contrato, int idcompetencia) {
        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();
        int id = dao.buscarIdConsignado(contrato, idcompetencia);
        if (id == -1) {
            System.out.println("Consignado não encontrado: Contrato " + contrato + ", Competência ID " + idcompetencia);
            return 0;
        } else {
            System.out.println("Consignado encontrado: Contrato " + contrato + ", Competência ID " + idcompetencia + ", ID Consignado " + id);
            return id;
        }
    }

    public ConsignadoDTO buscarPorId(int idconsignado) {
        ConsignadoDAOImpl dao = new ConsignadoDAOImpl();
        Consignado consignado = dao.buscarPorIdconsignado(idconsignado);
        return converterParaDTO(consignado);
    }

    public ConsignadoDTO converterParaDTO(Consignado consignado) {
        return new ConsignadoDTO(
            consignado.getContrato(),
            consignado.getNome(),
            consignado.getCpf(),
            consignado.getMatricula(),
            consignado.getPrazoTotal(),
            consignado.getNumeroPrestacao(),
            consignado.getValorPrestacao(),
            consignado.getIdCompetencia()
        );
    }

    public Consignado converterParaModel(ConsignadoDTO dto) {
        return new Consignado(
            dto.contrato(),
            dto.nome(),
            dto.cpf(),
            dto.matricula(),
            dto.prazoTotal(),
            dto.numeroPrestacao(),
            dto.valorPrestacao(),
            dto.idCompetencia()
        );
    }
}
