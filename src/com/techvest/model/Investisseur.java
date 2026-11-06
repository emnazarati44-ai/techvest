package com.techvest.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Investisseur extends Utilisateur {
    private List<Investissement> investissements;
    private double totalInvesti;

    public Investisseur(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.investissements = new ArrayList<>();
        this.totalInvesti = 0;
    }

    public Investisseur(String id, String nom, String prenom, String email, String motDePasse, LocalDate dateInscription) {
        super(id, nom, prenom, email, motDePasse, dateInscription);
        this.investissements = new ArrayList<>();
        this.totalInvesti = 0;
    }

    @Override
    public String getType() {
        return "INVESTISSEUR";
    }

    public void ajouterInvestissement(Investissement inv) {
        this.investissements.add(inv);
        this.totalInvesti += inv.getMontant();
    }

    public List<Investissement> getInvestissements() {
        return new ArrayList<>(investissements);
    }

    public int getNombreInvestissements() {
        return investissements.size();
    }

    public double getTotalInvesti() {
        return totalInvesti;
    }

    public void setTotalInvesti(double total) {
        this.totalInvesti = total;
    }

    @Override
    public String toString() {
        return String.format("[INVESTISSEUR] %s %s | %d investissements | Total: %.2f crypto", 
            prenom, nom, investissements.size(), totalInvesti);
    }
}
