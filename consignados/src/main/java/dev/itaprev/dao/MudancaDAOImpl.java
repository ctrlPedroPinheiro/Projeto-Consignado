package dev.itaprev.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.itaprev.controller.ConsignadoController;
import dev.itaprev.dto.ConsignadoDTO;
import dev.itaprev.dto.MudancaDTO;
import dev.itaprev.factory.ConnectionFactory;
import dev.itaprev.model.Consignado;
import dev.itaprev.model.MotivoMudanca;
import dev.itaprev.model.Mudanca;

public class MudancaDAOImpl implements MudancaDAO {

    private static final String INSERT_MUDANCA = 
        "INSERT INTO mudanca (tipo, consignado_idconsignado) VALUES (?, ?)";

    // ADICIONADO: c.nome, c.cpf, c.matricula (Necessários para a View)
    private static final String SELECT_ALL_BY_COMPETENCIA = 
        "SELECT m.idmudanca, m.tipo, " +
        "c.idconsignado, c.contrato_numero, c.valor_prestacao, c.competencia_idcompetencia, " +
        "c.nome, c.cpf, c.matricula " + 
        "FROM mudanca m " +
        "JOIN consignado c ON m.consignado_idconsignado = c.idconsignado " +
        "WHERE c.competencia_idcompetencia = ?";

    private static final String SELECT_BY_CONSIGNADO_ID = 
        "SELECT m.idmudanca, m.tipo, " +
        "c.idconsignado, c.contrato_numero, c.valor_prestacao, c.competencia_idcompetencia, " +
        "c.nome, c.cpf, c.matricula " +
        "FROM mudanca m " +
        "JOIN consignado c ON m.consignado_idconsignado = c.idconsignado " +
        "WHERE c.idconsignado = ?";

    private static final String SELECT_ALL_BY_MOTIVO = 
        "SELECT m.idmudanca, m.tipo, " +
        "c.idconsignado, c.contrato_numero, c.valor_prestacao, c.competencia_idcompetencia, " +
        "c.nome, c.cpf, c.matricula " +
        "FROM mudanca m " +
        "JOIN consignado c ON m.consignado_idconsignado = c.idconsignado " +
        "WHERE m.tipo = ? AND c.competencia_idcompetencia = ?";

    @Override
    public void salvarMudanca(Mudanca mudanca) {
        ConsignadoController consignadoController = new ConsignadoController();
        int idConsignado = consignadoController.buscarIdConsignado(mudanca.getConsignado().getContrato(), mudanca.getConsignado().getIdCompetencia());
        if (idConsignado == 0) {
            throw new RuntimeException("Consignado não encontrado para salvar mudança.");
        }
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MUDANCA)) {
            stmt.setString(1, mudanca.getMotivo().name());
            stmt.setInt(2, idConsignado);
            stmt.executeUpdate();

        } catch (Exception e) {
            System.err.println("Erro ao salvar mudança: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar mudança", e);
        }
    }

    @Override
    public List<MudancaDTO> buscarTodos(int idcompetencia) {
        List<MudancaDTO> mudancas = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_BY_COMPETENCIA)) {
            
            stmt.setInt(1, idcompetencia);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ConsignadoDTO consignadoDTO = new ConsignadoDTO(
                        rs.getString("contrato_numero"), 
                        rs.getString("nome"),             
                        rs.getString("cpf"),              
                        rs.getString("matricula"),        
                        0, 
                        0,
                        rs.getDouble("valor_prestacao"),  
                        rs.getInt("competencia_idcompetencia")
                    );

                    MotivoMudanca motivo = MotivoMudanca.valueOf(rs.getString("tipo"));

                    mudancas.add(new MudancaDTO(consignadoDTO, motivo));
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao buscar mudanças: " + e.getMessage());
            e.printStackTrace(); 
            throw new RuntimeException("Erro ao buscar mudanças", e);
        }
        
        return mudancas;
    }

    @Override
    public Mudanca buscarPorConsignado(int idconsignado) {
        Mudanca mudanca = null;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CONSIGNADO_ID)) {
            
            stmt.setInt(1, idconsignado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Consignado consignado = montarConsignadoDoResultSet(rs);
                    MotivoMudanca motivo = MotivoMudanca.valueOf(rs.getString("tipo"));
                    mudanca = new Mudanca(consignado, motivo);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar mudança por consignado", e);
        }
        return mudanca;
    }

    @Override
    public List<Mudanca> buscarPorMotivo(String motivo, int idcompetencia) {
        List<Mudanca> mudancas = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_BY_MOTIVO)) {
            
            stmt.setString(1, motivo);
            stmt.setInt(2, idcompetencia);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Consignado consignado = montarConsignadoDoResultSet(rs);
                    MotivoMudanca motivoEnum = MotivoMudanca.valueOf(rs.getString("tipo"));
                    mudancas.add(new Mudanca(consignado, motivoEnum));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar mudanças por motivo", e);
        }
        return mudancas;
    }

    @Override
    public Mudanca buscarPorId(int idmudanca) {
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

    private Consignado montarConsignadoDoResultSet(ResultSet rs) throws SQLException {
        Consignado c = new Consignado();
        c.setContrato(rs.getString("contrato_numero"));
        c.setValorPrestacao(rs.getDouble("valor_prestacao"));
        c.setIdCompetencia(rs.getInt("competencia_idcompetencia"));
        
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setMatricula(rs.getString("matricula"));
        return c;
    }
}