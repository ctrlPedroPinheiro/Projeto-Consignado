package dev.itaprev.dao;

import dev.itaprev.dto.CompetenciaDTO;
import dev.itaprev.factory.ConnectionFactory; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implementação da interface CompetenciaDAO.
 */
public class CompetenciaDAOImpl implements CompetenciaDAO {

    private static final String INSERT_COMPETENCIA = "INSERT INTO competencia (idcompetencia, mes, ano) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT idcompetencia, mes, ano FROM competencia WHERE idcompetencia = ?";
    private static final String SELECT_BY_MES_ANO = "SELECT idcompetencia, mes, ano FROM competencia WHERE mes = ? AND ano = ?";
    private static final String SELECT_ALL = "SELECT idcompetencia, mes, ano FROM competencia";

    /**
     * Recebe uma nova competência e a salva no banco de dados.
     * @param id O ID da competência.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     */
    @Override
    public void salvarCompetencia(int id, int mes, int ano) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_COMPETENCIA)) {
            
            stmt.setInt(1, id);
            stmt.setString(2, String.valueOf(mes));
            stmt.setString(3, String.valueOf(ano)); 
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar competência: " + e.getMessage());
            throw new RuntimeException("Erro ao salvar competência", e);
        }
    }

    /**
     * Busca uma competência pelo seu ID.
     * @param idcompetencia O ID da competência.
     * @return A competência encontrada, ou null se não encontrada.
     */
    @Override
    public CompetenciaDTO buscarPorId(int idcompetencia) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setInt(1, idcompetencia);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetParaDTO(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar competência por ID: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar competência por ID", e);
        }
        return null;
    }

    /**
     * Busca uma competência pelo seu mês e ano.
     * @param mes O mês da competência.
     * @param ano O ano da competência.
     * @return A competência encontrada, ou null se não encontrada.
     */
    @Override
    public CompetenciaDTO buscarPorMesAno(int mes, int ano) {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MES_ANO)) {
            
            stmt.setString(1, String.valueOf(mes));
            stmt.setString(2, String.valueOf(ano));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Encontrado competência para Mês: " + mes + " Ano: " + ano + " com ID: " + rs.getInt("idcompetencia"));
                    return mapearResultSetParaDTO(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar competência por Mês/Ano: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar competência por Mês/Ano", e);
        }
        return null;
    }

    /**
     * Busca todas as competências cadastradas.
     * @return Uma lista com todas as competências.
     */
    @Override
    public ArrayList<CompetenciaDTO> buscarTodos() {
        ArrayList<CompetenciaDTO> competencias = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                competencias.add(mapearResultSetParaDTO(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todas as competências: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar todas as competências", e);
        }
        return competencias;
    }

    /**
     * Busca a última competência cadastrada.
     * @return A última competência cadastrada, ou null se não encontrada.
     */
    @Override
    public CompetenciaDTO buscarUltimaCompetencia() {
        String query = "SELECT idcompetencia, mes, ano FROM competencia ORDER BY idcompetencia DESC LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return mapearResultSetParaDTO(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar a última competência: " + e.getMessage());
            throw new RuntimeException("Erro ao buscar a última competência", e);
        }
        return null;
    }

    /**
     * Mapeia a linha atual de um ResultSet para um CompetenciaDTO.
     * @param rs O ResultSet a ser mapeado.
     * @return O CompetenciaDTO mapeado.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private CompetenciaDTO mapearResultSetParaDTO(ResultSet rs) throws SQLException {
        int id = rs.getInt("idcompetencia");
        int mes = Integer.parseInt(rs.getString("mes"));
        int ano = Integer.parseInt(rs.getString("ano"));
        
        return new CompetenciaDTO(mes, ano, id);
    }
}