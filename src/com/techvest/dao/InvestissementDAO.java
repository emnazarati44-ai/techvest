package com.techvest.dao;

import com.techvest.model.Investissement;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvestissementDAO {

    public void sauvegarder(Investissement inv) throws SQLException {
        String sql = "INSERT OR REPLACE INTO investissements " +
                     "(id, investisseur_id, projet_id, contrat_id, montant, " +
                     "date_investissement, transaction_hash, confirme) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, inv.getId());
            pstmt.setString(2, inv.getInvestisseurId());
            pstmt.setString(3, inv.getProjetId());
            pstmt.setString(4, inv.getContratId());
            pstmt.setDouble(5, inv.getMontant());
            pstmt.setString(6, inv.getDateInvestissement().toString());
            pstmt.setString(7, inv.getTransactionHash());
            pstmt.setInt(8, inv.isConfirme() ? 1 : 0);
            
            pstmt.executeUpdate();
        }
    }

    public Investissement trouverParId(String id) throws SQLException {
        String sql = "SELECT * FROM investissements WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireInvestissement(rs);
            }
        }
        return null;
    }

    public List<Investissement> listerParInvestisseur(String investisseurId) throws SQLException {
        List<Investissement> invs = new ArrayList<>();
        String sql = "SELECT * FROM investissements WHERE investisseur_id = ? ORDER BY date_investissement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, investisseurId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                invs.add(construireInvestissement(rs));
            }
        }
        return invs;
    }

    public List<Investissement> listerParProjet(String projetId) throws SQLException {
        List<Investissement> invs = new ArrayList<>();
        String sql = "SELECT * FROM investissements WHERE projet_id = ? ORDER BY date_investissement DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projetId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                invs.add(construireInvestissement(rs));
            }
        }
        return invs;
    }

    public double getTotalParInvestisseur(String investisseurId) throws SQLException {
        String sql = "SELECT SUM(montant) FROM investissements WHERE investisseur_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, investisseurId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    public double getTotalParProjet(String projetId) throws SQLException {
        String sql = "SELECT SUM(montant) FROM investissements WHERE projet_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projetId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    public void confirmerTransaction(String invId, String hash) throws SQLException {
        String sql = "UPDATE investissements SET transaction_hash = ?, confirme = 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hash);
            pstmt.setString(2, invId);
            pstmt.executeUpdate();
        }
    }

    public void supprimer(String id) throws SQLException {
        String sql = "DELETE FROM investissements WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    private Investissement construireInvestissement(ResultSet rs) throws SQLException {
        return new Investissement(
            rs.getString("id"),
            rs.getString("investisseur_id"),
            rs.getString("projet_id"),
            rs.getString("contrat_id"),
            rs.getDouble("montant"),
            LocalDateTime.parse(rs.getString("date_investissement")),
            rs.getString("transaction_hash"),
            rs.getInt("confirme") == 1
        );
    }
}
