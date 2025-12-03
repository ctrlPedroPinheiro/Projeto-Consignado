package dev.itaprev.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import dev.itaprev.controller.ConsignadoController;
import dev.itaprev.factory.ConnectionFactory;
import dev.itaprev.model.MotivoMudanca;
import dev.itaprev.model.Mudanca;
import dev.itaprev.model.Consignado;

public class MundacaDAOImpl implements MudancaDAO {

    private static final String INSERT_MUDANCA = "INSERT INTO mudancas (tipo, consignado_idconsignado) VALUES (?, ?)";
    private static final String SELECT_ALL_BY_COMPETENCIA = "SELECT m.idmudanca, m.tipo, c.idconsignado, c.contrato, c.valor, c.competencia_idcompetencia "
            + "FROM mudancas m "
            + "JOIN consignado c ON m.consignado_idconsignado = c.idconsignado "
            + "WHERE c.competencia_idcompetencia = ?";
    private static final String SELECT_BY_CONSIGNADO_ID = "SELECT m.idmudanca, m.tipo, c.idconsignado, c.contrato, c.valor, c.competencia_idcompetencia "
            + "FROM mudancas m "
            + "JOIN consignado c ON m.consignado_idconsignado = c.idconsignado "
            + "WHERE c.idconsignado = ?";
    private static final String SELECT_ALL_BY_MOTIVO = "SELECT m.idmudanca, m.tipo, c.idconsignado, c.contrato, c.valor, c.competencia_idcompetencia "
            + "FROM mudancas m "
            + "JOIN consignado c ON m.consignado_idconsignado = c.idconsignado "
            + "WHERE m.tipo = ? AND c.competencia_idcompetencia = ?";

    @Override
    public void salvarMudanca(Mudanca mudanca) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MUDANCA)) {
            
            ConsignadoController consignadoController = new ConsignadoController();
            int idconsignado = consignadoController.buscarIdConsignado(mudanca.getConsignado().getContrato(), mudanca.getConsignado().getIdCompetencia());
            stmt.setString(1, mudanca.getMotivo().name());
            stmt.setInt(2, idconsignado);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Erro ao salvar mudança: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar mudança", e);
        }
    }

    @Override
    public List<Mudanca> buscarTodos(int idcompetencia) {
        List<Mudanca> mudancas = new java.util.ArrayList<>();
        ConsignadoDAO consignadoDAO = new ConsignadoDAOImpl();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_BY_COMPETENCIA)) {
            
            stmt.setInt(1, idcompetencia);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consignado consignado = consignadoDAO.buscarPorIdconsignado(rs.getInt("consignado_idconsignado"));
                    
                    String tipoString = rs.getString("tipo");
                    
                    MotivoMudanca motivo = MotivoMudanca.valueOf(tipoString);

                    Mudanca mudanca = new Mudanca(consignado, motivo);
                    
                    mudancas.add(mudanca);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar mudanças: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar mudanças", e);
        }
        
        return mudancas;
    }

    @Override
    public Mudanca buscarPorConsignado(int idconsignado) {
        Mudanca mudanca = null;
        ConsignadoDAO consignadoDAO = new ConsignadoDAOImpl();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CONSIGNADO_ID)) {
            
            stmt.setInt(1, idconsignado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Consignado consignado = consignadoDAO.buscarPorIdconsignado(rs.getInt("consignado_idconsignado"));
                    
                    String tipoString = rs.getString("tipo");
                    
                    MotivoMudanca motivo = MotivoMudanca.valueOf(tipoString);

                    mudanca = new Mudanca(consignado, motivo);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar mudança por consignado: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar mudança por consignado", e);
        }
        
        return mudanca;
    }

    @Override
    public Mudanca buscarPorId(int idmudanca) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

    @Override
    public List<Mudanca> buscarPorMotivo(String motivo, int idcompetencia) {
        List<Mudanca> mudancas = new java.util.ArrayList<>();
        ConsignadoDAO consignadoDAO = new ConsignadoDAOImpl();

        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_BY_MOTIVO)) {
            
            stmt.setString(1, motivo);
            stmt.setInt(2, idcompetencia);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consignado consignado = consignadoDAO.buscarPorIdconsignado(rs.getInt("consignado_idconsignado"));
                    
                    String tipoString = rs.getString("tipo");
                    
                    MotivoMudanca motivoMudanca = MotivoMudanca.valueOf(tipoString);

                    Mudanca mudanca = new Mudanca(consignado, motivoMudanca);
                    
                    mudancas.add(mudanca);
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar mudanças por motivo: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar mudanças por motivo", e);
        }
        
        return mudancas;
    }

}
