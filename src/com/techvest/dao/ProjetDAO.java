package com.techvest.dao;

import com.techvest.model.ProjetTech;
import com.techvest.model.enums.CategorieProjet;
import com.techvest.model.enums.StatutProjet;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjetDAO {

    public void sauvegarder(ProjetTech projet) throws SQLException {
        String sql = "INSERT OR REPLACE INTO projets " +
                     "(id, titre, description, categorie, montant_demande, montant_collecte, " +
                     "date_creation, date_limite, statut, porteur_id, adresse_crypto, " +
                     "contact_externe, contrat_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, projet.getId());
            pstmt.setString(2, projet.getTitre());
            pstmt.setString(3, projet.getDescription());
            pstmt.setString(4, projet.getCategorie().name());
            pstmt.setDouble(5, projet.getMontantDemande());
            pstmt.setDouble(6, projet.getMontantCollecte());
            pstmt.setString(7, projet.getDateCreation().toString());
            pstmt.setString(8, projet.getDateLimite().toString());
            pstmt.setString(9, projet.getStatut().name());
            pstmt.setString(10, projet.getPorteurId());
            pstmt.setString(11, projet.getAdresseCrypto());
            pstmt.setString(12, projet.getContactExterne());
            pstmt.setString(13, projet.getContratId());
            
            pstmt.executeUpdate();
        }
    }

    public ProjetTech trouverParId(String id) throws SQLException {
        String sql = "SELECT * FROM projets WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireProjet(rs);
            }
        }
        return null;
    }

    public List<ProjetTech> listerTous() throws SQLException {
        List<ProjetTech> projets = new ArrayList<>();
        String sql = "SELECT * FROM projets ORDER BY date_creation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                projets.add(construireProjet(rs));
            }
        }
        return projets;
    }

    public List<ProjetTech> listerParStatut(StatutProjet statut) throws SQLException {
        List<ProjetTech> projets = new ArrayList<>();
        String sql = "SELECT * FROM projets WHERE statut = ? ORDER BY date_creation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, statut.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                projets.add(construireProjet(rs));
            }
        }
        return projets;
    }

    public List<ProjetTech> listerParPorteur(String porteurId) throws SQLException {
        List<ProjetTech> projets = new ArrayList<>();
        String sql = "SELECT * FROM projets WHERE porteur_id = ? ORDER BY date_creation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, porteurId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                projets.add(construireProjet(rs));
            }
        }
        return projets;
    }

    public List<ProjetTech> listerParCategorie(CategorieProjet categorie) throws SQLException {
        List<ProjetTech> projets = new ArrayList<>();
        String sql = "SELECT * FROM projets WHERE categorie = ? AND statut = 'EN_FINANCEMENT' ORDER BY date_creation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, categorie.name());
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                projets.add(construireProjet(rs));
            }
        }
        return projets;
    }

    public List<ProjetTech> listerOuvertsAuxInvestissements() throws SQLException {
        List<ProjetTech> projets = new ArrayList<>();
        String sql = "SELECT * FROM projets WHERE statut = 'EN_FINANCEMENT' " +
                     "AND date_limite >= date('now') ORDER BY date_creation DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                projets.add(construireProjet(rs));
            }
        }
        return projets;
    }

    public void mettreAJourMontantCollecte(String projetId, double montant) throws SQLException {
        String sql = "UPDATE projets SET montant_collecte = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, montant);
            pstmt.setString(2, projetId);
            pstmt.executeUpdate();
        }
    }

    public void mettreAJourStatut(String projetId, StatutProjet statut) throws SQLException {
        String sql = "UPDATE projets SET statut = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, statut.name());
            pstmt.setString(2, projetId);
            pstmt.executeUpdate();
        }
    }

    public void mettreAJourContrat(String projetId, String contratId) throws SQLException {
        String sql = "UPDATE projets SET contrat_id = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, contratId);
            pstmt.setString(2, projetId);
            pstmt.executeUpdate();
        }
    }

    public void supprimer(String id) throws SQLException {
        String sql = "DELETE FROM projets WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    private ProjetTech construireProjet(ResultSet rs) throws SQLException {
        return new ProjetTech(
            rs.getString("id"),
            rs.getString("titre"),
            rs.getString("description"),
            CategorieProjet.valueOf(rs.getString("categorie")),
            rs.getDouble("montant_demande"),
            rs.getDouble("montant_collecte"),
            LocalDate.parse(rs.getString("date_creation")),
            LocalDate.parse(rs.getString("date_limite")),
            StatutProjet.valueOf(rs.getString("statut")),
            rs.getString("porteur_id"),
            rs.getString("adresse_crypto"),
            rs.getString("contact_externe"),
            rs.getString("contrat_id")
        );
    }
}
