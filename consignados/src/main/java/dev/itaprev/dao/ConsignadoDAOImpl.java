package dev.itaprev.dao;

import dev.itaprev.factory.ConnectionFactory;
import dev.itaprev.model.Consignado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface ConsignadoDAO.
 */
public class ConsignadoDAOImpl implements ConsignadoDAO {

    /**
     * Salva um novo consignado no banco de dados.
     * @param consignado O objeto a ser salvo.
     */
    @Override
    public void salvar(Consignado consignado) {
        String sql = "INSERT INTO consignado (contrato_numero, nome, cpf, matricula, prazo_total, numero_prestacao, valor_prestacao, competencia_idcompetencia) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, consignado.getContrato());
            pstmt.setString(2, consignado.getNome());
            pstmt.setString(3, consignado.getCpf());
            pstmt.setString(4, consignado.getMatricula());
            pstmt.setInt(5, consignado.getPrazoTotal());
            pstmt.setInt(6, consignado.getNumeroPrestacao());
            pstmt.setDouble(7, consignado.getValorPrestacao());
            pstmt.setInt(8, consignado.getIdCompetencia());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao salvar consignado: " + e.getMessage());
        }
    }

    /**
     * Atualiza um consignado existente no banco de dados.
     * @param consignado O objeto com os dados atualizados.
     */
    @Override
    public void atualizar(Consignado consignado) {
        String sql = "UPDATE consignado SET nome = ?, cpf = ?, matricula = ?, prazo_total = ?, " +
                     "numero_prestacao = ?, valor_prestacao = ? WHERE contrato_numero = ? AND competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, consignado.getNome());
            pstmt.setString(2, consignado.getCpf());
            pstmt.setString(3, consignado.getMatricula());
            pstmt.setInt(4, consignado.getPrazoTotal());
            pstmt.setInt(5, consignado.getNumeroPrestacao());
            pstmt.setDouble(6, consignado.getValorPrestacao());
            pstmt.setString(7, consignado.getContrato());
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar consignado: " + e.getMessage());
        }
    }

    /**
     * Deleta um consignado do banco de dados pelo seu contrato.
     * @param contrato O número do contrato a ser deletado.
     * @param idcompetencia O ID da competência associada.
     */
    @Override
    public void deletar(String contrato, int idcompetencia) {
        String sql = "DELETE FROM consignado WHERE contrato_numero = ? AND competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contrato);
            pstmt.setInt(2, idcompetencia);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erro ao deletar consignado: " + e.getMessage());
        }
    }

    /**
     * Busca um consignado pelo seu contrato e competência.
     * @param contrato O número do contrato.
     * @param idcompetencia O ID da competência.
     * @return Um Optional contendo o Consignado se encontrado, ou vazio se não.
     */
    @Override
    public Optional<Consignado> buscarPorContrato(String contrato, int idcompetencia) {
        String sql = "SELECT * FROM consignado WHERE contrato_numero = ? AND competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contrato);
            pstmt.setInt(2, idcompetencia);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearResultadoParaConsignado(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consignado por contrato: " + e.getMessage());
        }
        
        return Optional.empty(); 
    }

    /**
     * Busca consignados pelo nome.
     * @param nome O nome do consignado.
     * @param idcompetencia O ID da competência associada.
     * @return Uma lista com os consignados encontrados.
     */
    public List<Consignado> buscarPorNome(String nome, int idcompetencia) {
        List<Consignado> consignados = new ArrayList<>();
        String sql = "SELECT * FROM consignado WHERE nome LIKE ? AND competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + nome + "%");
            pstmt.setInt(2, idcompetencia);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    consignados.add(mapearResultadoParaConsignado(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consignados por nome: " + e.getMessage());
        }
        
        return consignados;
    }

    /**
     * Busca consignados pelo CPF.
     * @param cpf O CPF do consignado.
     * @param idcompetencia O ID da competência associada.
     * @return Uma lista com os consignados encontrados.
     */
    public List<Consignado> buscarPorCpf(String cpf, int idcompetencia) {
        List<Consignado> consignados = new ArrayList<>();
        String sql = "SELECT * FROM consignado WHERE cpf = ? AND competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cpf);
            pstmt.setInt(2, idcompetencia);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    consignados.add(mapearResultadoParaConsignado(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consignados por CPF: " + e.getMessage());
        }
        
        return consignados;
    }

    /**
     * Busca todos os consignados cadastrados.
     * @param idcompetencia O ID da competência associada.
     * @return Uma lista com todos os consignados.
     */
    @Override
    public List<Consignado> buscarTodos(int idcompetencia) {
        List<Consignado> consignados = new ArrayList<>();
        String sql = "SELECT * FROM consignado WHERE competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idcompetencia);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    consignados.add(mapearResultadoParaConsignado(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os consignados: " + e.getMessage());
        }
        
        return consignados;
    }

    /**
     * Busca o ID de um consignado pelo seu contrato e competência.
     * @param contrato O número do contrato.
     * @param idcompetencia O ID da competência.
     * @return O ID do consignado, ou -1 se não encontrado.
     */
    @Override
    public int buscarIdConsignado(String contrato, int idcompetencia) {
        String sql = "SELECT idconsignado FROM consignado WHERE contrato_numero = ? AND competencia_idcompetencia = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contrato);
            pstmt.setInt(2, idcompetencia);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idconsignado");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar ID do consignado: " + e.getMessage());
        }
        
        return -1;
    }

    /**
     * Busca um consignado pelo seu ID.
     * @param idconsignado O ID do consignado.
     * @return O consignado encontrado, ou null se não encontrado.
     */
    public Consignado buscarPorIdconsignado(int idconsignado) {
        String sql = "SELECT * FROM consignado WHERE idconsignado = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idconsignado);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultadoParaConsignado(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erro ao buscar consignado por ID: " + e.getMessage());
        }
        
        return null; 
    }

    /**
     * Mapeia um ResultSet para um objeto Consignado.
     * @param rs O ResultSet a ser mapeado.
     * @return O objeto Consignado mapeado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Consignado mapearResultadoParaConsignado(ResultSet rs) throws SQLException {
        return new Consignado(
            rs.getString("contrato_numero"),
            rs.getString("nome"),
            rs.getString("cpf"),
            rs.getString("matricula"),
            rs.getInt("prazo_total"),
            rs.getInt("numero_prestacao"),
            rs.getDouble("valor_prestacao"),
            rs.getInt("competencia_idcompetencia")
        );
    }

    
    
}