package com.techvest.dao;

import com.techvest.model.*;
import com.techvest.model.enums.StatutKYC;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public void sauvegarder(Utilisateur user) throws SQLException {
        String sql = "INSERT OR REPLACE INTO utilisateurs " +
                     "(id, type, nom, prenom, email, mot_de_passe, date_inscription, " +
                     "statut_kyc, adresse_crypto, contact_externe, total_investi) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getType());
            pstmt.setString(3, user.getNom());
            pstmt.setString(4, user.getPrenom());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getMotDePasse());
            pstmt.setString(7, user.getDateInscription().toString());
            
            if (user instanceof PorteurProjet) {
                PorteurProjet pp = (PorteurProjet) user;
                pstmt.setString(8, pp.getStatutKYC().name());
                pstmt.setString(9, pp.getAdresseCrypto());
                pstmt.setString(10, pp.getContactExterne());
                pstmt.setDouble(11, 0);
            } else if (user instanceof Investisseur) {
                Investisseur inv = (Investisseur) user;
                pstmt.setString(8, null);
                pstmt.setString(9, null);
                pstmt.setString(10, null);
                pstmt.setDouble(11, inv.getTotalInvesti());
            }
            
            pstmt.executeUpdate();
        }
    }

    public Utilisateur trouverParId(String id) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireUtilisateur(rs);
            }
        }
        return null;
    }

    public Utilisateur trouverParEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireUtilisateur(rs);
            }
        }
        return null;
    }

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public List<Utilisateur> listerTous() throws SQLException {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(construireUtilisateur(rs));
            }
        }
        return users;
    }

    public List<PorteurProjet> listerPorteursEnAttenteKYC() throws SQLException {
        List<PorteurProjet> porteurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE type = 'PORTEUR_PROJET' AND statut_kyc = 'EN_ATTENTE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                porteurs.add((PorteurProjet) construireUtilisateur(rs));
            }
        }
        return porteurs;
    }

    public void mettreAJourKYC(String userId, StatutKYC statut) throws SQLException {
        String sql = "UPDATE utilisateurs SET statut_kyc = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, statut.name());
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }

    public void mettreAJourTotalInvesti(String userId, double total) throws SQLException {
        String sql = "UPDATE utilisateurs SET total_investi = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, total);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }

    public void supprimer(String id) throws SQLException {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    private Utilisateur construireUtilisateur(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String id = rs.getString("id");
        String nom = rs.getString("nom");
        String prenom = rs.getString("prenom");
        String email = rs.getString("email");
        String mdp = rs.getString("mot_de_passe");
        LocalDate dateInscription = LocalDate.parse(rs.getString("date_inscription"));

        if ("INVESTISSEUR".equals(type)) {
            Investisseur inv = new Investisseur(id, nom, prenom, email, mdp, dateInscription);
            inv.setTotalInvesti(rs.getDouble("total_investi"));
            return inv;
        } else {
            String statutKycStr = rs.getString("statut_kyc");
            StatutKYC statutKyc = statutKycStr != null ? StatutKYC.valueOf(statutKycStr) : StatutKYC.NON_SOUMIS;
            return new PorteurProjet(id, nom, prenom, email, mdp, dateInscription,
                    statutKyc, rs.getString("adresse_crypto"), rs.getString("contact_externe"));
        }
    }
}
