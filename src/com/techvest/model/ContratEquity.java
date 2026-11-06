package com.techvest.model;

import com.techvest.model.enums.StatutContrat;
import com.techvest.model.enums.TypeContrat;
import java.time.LocalDate;

public class ContratEquity extends ContratInvestissement {
    private double pourcentageEquity; // pourcentage du projet
    private String conditionsExit;
    private boolean droitsVote;

    public ContratEquity(String projetId, String termes, double pourcentageEquity, 
                         String conditionsExit, boolean droitsVote) {
        super(projetId, termes, TypeContrat.EQUITY);
        this.pourcentageEquity = pourcentageEquity;
        this.conditionsExit = conditionsExit;
        this.droitsVote = droitsVote;
    }

    public ContratEquity(String id, String projetId, LocalDate dateCreation, StatutContrat statut, 
                          String termes, double pourcentageEquity, String conditionsExit, boolean droitsVote) {
        super(id, projetId, dateCreation, statut, termes, TypeContrat.EQUITY);
        this.pourcentageEquity = pourcentageEquity;
        this.conditionsExit = conditionsExit;
        this.droitsVote = droitsVote;
    }

    @Override
    public double calculerRetourInvestissement(double montantInvesti) {
        // Le retour dépend de la valorisation future du projet
        // Ici on retourne juste le pourcentage d'equity comme indicateur
        return pourcentageEquity;
    }

    public double calculerValeurParts(double valorisationProjet) {
        return valorisationProjet * (pourcentageEquity / 100);
    }

    @Override
    public String getDetailsContrat() {
        return String.format(
            "=== CONTRAT EQUITY ===\n" +
            "ID: %s\n" +
            "Pourcentage du projet: %.2f%%\n" +
            "Droits de vote: %s\n" +
            "Conditions de sortie: %s\n" +
            "Statut: %s\n" +
            "Termes: %s",
            id, pourcentageEquity, droitsVote ? "Oui" : "Non", conditionsExit, statut, termes
        );
    }

    public double getPourcentageEquity() { return pourcentageEquity; }
    public String getConditionsExit() { return conditionsExit; }
    public boolean isDroitsVote() { return droitsVote; }

    public void setPourcentageEquity(double pct) { this.pourcentageEquity = pct; }
    public void setConditionsExit(String conditions) { this.conditionsExit = conditions; }
    public void setDroitsVote(boolean droits) { this.droitsVote = droits; }

    @Override
    public String toString() {
        return String.format("[EQUITY] %.2f%% du projet | Vote: %s", pourcentageEquity, droitsVote ? "Oui" : "Non");
    }
}
