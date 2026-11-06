package com.techvest.model;

import com.techvest.model.enums.StatutContrat;
import com.techvest.model.enums.TypeContrat;
import java.time.LocalDate;

public class ContratRevenuFixe extends ContratInvestissement {
    private int dureeInvestissement; // en mois
    private double tauxRendement; // en pourcentage annuel

    public ContratRevenuFixe(String projetId, String termes, int dureeInvestissement, double tauxRendement) {
        super(projetId, termes, TypeContrat.REVENU_FIXE);
        this.dureeInvestissement = dureeInvestissement;
        this.tauxRendement = tauxRendement;
    }

    public ContratRevenuFixe(String id, String projetId, LocalDate dateCreation, StatutContrat statut, 
                              String termes, int dureeInvestissement, double tauxRendement) {
        super(id, projetId, dateCreation, statut, termes, TypeContrat.REVENU_FIXE);
        this.dureeInvestissement = dureeInvestissement;
        this.tauxRendement = tauxRendement;
    }

    @Override
    public double calculerRetourInvestissement(double montantInvesti) {
        double tauxMensuel = tauxRendement / 100 / 12;
        return montantInvesti * (1 + tauxMensuel * dureeInvestissement);
    }

    @Override
    public String getDetailsContrat() {
        return String.format(
            "=== CONTRAT REVENU FIXE ===\n" +
            "ID: %s\n" +
            "Durée: %d mois\n" +
            "Taux de rendement: %.2f%% annuel\n" +
            "Statut: %s\n" +
            "Termes: %s\n" +
            "Exemple: 1000 crypto → %.2f crypto après %d mois",
            id, dureeInvestissement, tauxRendement, statut, termes,
            calculerRetourInvestissement(1000), dureeInvestissement
        );
    }

    public int getDureeInvestissement() { return dureeInvestissement; }
    public double getTauxRendement() { return tauxRendement; }

    public void setDureeInvestissement(int duree) { this.dureeInvestissement = duree; }
    public void setTauxRendement(double taux) { this.tauxRendement = taux; }

    @Override
    public String toString() {
        return String.format("[REVENU FIXE] Durée: %d mois | Taux: %.2f%%", dureeInvestissement, tauxRendement);
    }
}
