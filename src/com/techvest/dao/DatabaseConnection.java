package com.techvest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:database/techvest.db";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Table Utilisateurs
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS utilisateurs (" +
                "id TEXT PRIMARY KEY," +
                "type TEXT NOT NULL," +
                "nom TEXT NOT NULL," +
                "prenom TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "mot_de_passe TEXT NOT NULL," +
                "date_inscription TEXT NOT NULL," +
                "statut_kyc TEXT," +
                "adresse_crypto TEXT," +
                "contact_externe TEXT," +
                "total_investi REAL DEFAULT 0)"
            );

            // Table DocumentsKYC
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS documents_kyc (" +
                "id TEXT PRIMARY KEY," +
                "utilisateur_id TEXT NOT NULL," +
                "type_document TEXT NOT NULL," +
                "numero_identite TEXT NOT NULL," +
                "adresse TEXT NOT NULL," +
                "date_soumission TEXT NOT NULL," +
                "est_valide INTEGER DEFAULT 0," +
                "FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs(id))"
            );

            // Table Projets
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS projets (" +
                "id TEXT PRIMARY KEY," +
                "titre TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "categorie TEXT NOT NULL," +
                "montant_demande REAL NOT NULL," +
                "montant_collecte REAL DEFAULT 0," +
                "date_creation TEXT NOT NULL," +
                "date_limite TEXT NOT NULL," +
                "statut TEXT NOT NULL," +
                "porteur_id TEXT NOT NULL," +
                "adresse_crypto TEXT," +
                "contact_externe TEXT," +
                "contrat_id TEXT," +
                "FOREIGN KEY (porteur_id) REFERENCES utilisateurs(id))"
            );

            // Table Contrats
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS contrats (" +
                "id TEXT PRIMARY KEY," +
                "projet_id TEXT NOT NULL," +
                "type_contrat TEXT NOT NULL," +
                "date_creation TEXT NOT NULL," +
                "statut TEXT NOT NULL," +
                "termes TEXT," +
                "duree_investissement INTEGER," +
                "taux_rendement REAL," +
                "pourcentage_equity REAL," +
                "conditions_exit TEXT," +
                "droits_vote INTEGER," +
                "FOREIGN KEY (projet_id) REFERENCES projets(id))"
            );

            // Table Investissements
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS investissements (" +
                "id TEXT PRIMARY KEY," +
                "investisseur_id TEXT NOT NULL," +
                "projet_id TEXT NOT NULL," +
                "contrat_id TEXT," +
                "montant REAL NOT NULL," +
                "date_investissement TEXT NOT NULL," +
                "transaction_hash TEXT," +
                "confirme INTEGER DEFAULT 0," +
                "FOREIGN KEY (investisseur_id) REFERENCES utilisateurs(id)," +
                "FOREIGN KEY (projet_id) REFERENCES projets(id))"
            );

            System.out.println("✅ Base de données initialisée avec succès!");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur initialisation BD: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Erreur fermeture connexion: " + e.getMessage());
        }
    }
}
