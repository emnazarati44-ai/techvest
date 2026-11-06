package com.techvest.dao;

import com.techvest.model.*;
import com.techvest.model.enums.StatutContrat;
import com.techvest.model.enums.TypeContrat;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratDAO {

    public void sauvegarder(ContratInvestissement contrat) throws SQLException {
        String sql = "INSERT OR REPLACE INTO contrats " +
                     "(id, projet_id, type_contrat, date_creation, statut, termes, " +
                     "duree_investissement, taux_rendement, pourcentage_equity, " +
                     "conditions_exit, droits_vote) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contrat.getId());
            pstmt.setString(2, contrat.getProjetId());
            pstmt.setString(3, contrat.getTypeContrat().name());
            pstmt.setString(4, contrat.getDateCreation().toString());
            pstmt.setString(5, contrat.getStatut().name());
            pstmt.setString(6, contrat.getTermes());
            
            if (contrat instanceof ContratRevenuFixe) {
                ContratRevenuFixe crf = (ContratRevenuFixe) contrat;
                pstmt.setInt(7, crf.getDureeInvestissement());
                pstmt.setDouble(8, crf.getTauxRendement());
                pstmt.setNull(9, Types.REAL);
                pstmt.setNull(10, Types.VARCHAR);
                pstmt.setNull(11, Types.INTEGER);
            } else if (contrat instanceof ContratEquity) {
                ContratEquity ce = (ContratEquity) contrat;
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setNull(8, Types.REAL);
                pstmt.setDouble(9, ce.getPourcentageEquity());
                pstmt.setString(10, ce.getConditionsExit());
                pstmt.setInt(11, ce.isDroitsVote() ? 1 : 0);
            }
            
            pstmt.executeUpdate();
        }
    }

    public ContratInvestissement trouverParId(String id) throws SQLException {
        String sql = "SELECT * FROM contrats WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireContrat(rs);
            }
        }
        return null;
    }

    public ContratInvestissement trouverParProjet(String projetId) throws SQLException {
        String sql = "SELECT * FROM contrats WHERE projet_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projetId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireContrat(rs);
            }
        }
        return null;
    }

    public List<ContratInvestissement> listerTous() throws SQLException {
        List<ContratInvestissement> contrats = new ArrayList<>();
        String sql = "SELECT * FROM contrats ORDER BY date_creation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                contrats.add(construireContrat(rs));
            }
        }
        return contrats;
    }

    public void mettreAJourStatut(String contratId, StatutContrat statut) throws SQLException {
        String sql = "UPDATE contrats SET statut = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, statut.name());
            pstmt.setString(2, contratId);
            pstmt.executeUpdate();
        }
    }

    public void supprimer(String id) throws SQLException {
        String sql = "DELETE FROM contrats WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    private ContratInvestissement construireContrat(ResultSet rs) throws SQLException {
        String typeStr = rs.getString("type_contrat");
        TypeContrat type = TypeContrat.valueOf(typeStr);
        
        String id = rs.getString("id");
        String projetId = rs.getString("projet_id");
        LocalDate dateCreation = LocalDate.parse(rs.getString("date_creation"));
        StatutContrat statut = StatutContrat.valueOf(rs.getString("statut"));
        String termes = rs.getString("termes");
        
        if (type == TypeContrat.REVENU_FIXE) {
            return new ContratRevenuFixe(id, projetId, dateCreation, statut, termes,
                    rs.getInt("duree_investissement"), rs.getDouble("taux_rendement"));
        } else {
            return new ContratEquity(id, projetId, dateCreation, statut, termes,
                    rs.getDouble("pourcentage_equity"), rs.getString("conditions_exit"),
                    rs.getInt("droits_vote") == 1);
        }
    }
}
