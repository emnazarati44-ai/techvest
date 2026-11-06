package com.techvest.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Investissement {
    private String id;
    private String investisseurId;
    private String projetId;
    private String contratId;
    private double montant;
    private LocalDateTime dateInvestissement;
    private String transactionHash;
    private boolean confirme;

    public Investissement(String investisseurId, String projetId, String contratId, double montant) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.investisseurId = investisseurId;
        this.projetId = projetId;
        this.contratId = contratId;
        this.montant = montant;
        this.dateInvestissement = LocalDateTime.now();
        this.transactionHash = "";
        this.confirme = false;
    }

    public Investissement(String id, String investisseurId, String projetId, String contratId,
                          double montant, LocalDateTime dateInvestissement, 
                          String transactionHash, boolean confirme) {
        this.id = id;
        this.investisseurId = investisseurId;
        this.projetId = projetId;
        this.contratId = contratId;
        this.montant = montant;
        this.dateInvestissement = dateInvestissement;
        this.transactionHash = transactionHash;
        this.confirme = confirme;
    }

    public void confirmerTransaction(String hash) {
        this.transactionHash = hash;
        this.confirme = true;
    }

    public String getId() { return id; }
    public String getInvestisseurId() { return investisseurId; }
    public String getProjetId() { return projetId; }
    public String getContratId() { return contratId; }
    public double getMontant() { return montant; }
    public LocalDateTime getDateInvestissement() { return dateInvestissement; }
    public String getTransactionHash() { return transactionHash; }
    public boolean isConfirme() { return confirme; }

    public void setTransactionHash(String hash) { this.transactionHash = hash; }
    public void setConfirme(boolean confirme) { this.confirme = confirme; }

    public String getDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format(
            "┌─────────────────────────────────────┐\n" +
            "│ INVESTISSEMENT #%s\n" +
            "├─────────────────────────────────────┤\n" +
            "│ Montant: %.4f crypto\n" +
            "│ Date: %s\n" +
            "│ Projet: %s\n" +
            "│ Statut: %s\n" +
            "│ Hash: %s\n" +
            "└─────────────────────────────────────┘",
            id, montant, dateInvestissement.format(formatter), projetId,
            confirme ? "✅ Confirmé" : "⏳ En attente",
            transactionHash.isEmpty() ? "N/A" : transactionHash
        );
    }

    @Override
    public String toString() {
        return String.format("[%s] %.4f crypto → Projet %s | %s", 
            id, montant, projetId, confirme ? "Confirmé" : "En attente");
    }
}
