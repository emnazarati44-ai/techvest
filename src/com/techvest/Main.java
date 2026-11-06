package com.techvest;

import com.techvest.dao.DatabaseConnection;
import com.techvest.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        // Initialiser la base de données
        DatabaseConnection.initDatabase();
        
        // Démarrer l'interface console
        ConsoleUI ui = new ConsoleUI();
        ui.demarrer();
        
        // Fermer la connexion à la fin
        DatabaseConnection.closeConnection();
    }
}
