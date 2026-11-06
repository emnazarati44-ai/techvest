package com.techvest.dao;

import com.techvest.model.DocumentKYC;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DocumentKYCDAO {

    public void sauvegarder(DocumentKYC doc, String utilisateurId) throws SQLException {
        String sql = "INSERT OR REPLACE INTO documents_kyc " +
                     "(id, utilisateur_id, type_document, numero_identite, adresse, " +
                     "date_soumission, est_valide) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, doc.getId());
            pstmt.setString(2, utilisateurId);
            pstmt.setString(3, doc.getTypeDocument());
            pstmt.setString(4, doc.getNumeroIdentite());
            pstmt.setString(5, doc.getAdresse());
            pstmt.setString(6, doc.getDateSoumission().toString());
            pstmt.setInt(7, doc.isEstValide() ? 1 : 0);
            
            pstmt.executeUpdate();
        }
    }

    public DocumentKYC trouverParUtilisateur(String utilisateurId) throws SQLException {
        String sql = "SELECT * FROM documents_kyc WHERE utilisateur_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateurId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return construireDocument(rs);
            }
        }
        return null;
    }

    public List<DocumentKYC> listerNonValides() throws SQLException {
        List<DocumentKYC> docs = new ArrayList<>();
        String sql = "SELECT * FROM documents_kyc WHERE est_valide = 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                docs.add(construireDocument(rs));
            }
        }
        return docs;
    }

    public void valider(String docId) throws SQLException {
        String sql = "UPDATE documents_kyc SET est_valide = 1 WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, docId);
            pstmt.executeUpdate();
        }
    }

    public void supprimer(String id) throws SQLException {
        String sql = "DELETE FROM documents_kyc WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    private DocumentKYC construireDocument(ResultSet rs) throws SQLException {
        return new DocumentKYC(
            rs.getString("id"),
            rs.getString("type_document"),
            rs.getString("numero_identite"),
            rs.getString("adresse"),
            LocalDateTime.parse(rs.getString("date_soumission")),
            rs.getInt("est_valide") == 1
        );
    }
}
