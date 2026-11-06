package com.techvest.model.enums;

public enum StatutProjet {
    BROUILLON("En cours de rédaction"),
    EN_FINANCEMENT("Ouvert aux investissements"),
    FINANCE("Objectif atteint"),
    CLOTURE("Projet terminé"),
    ECHEC("Financement échoué");

    private String description;

    StatutProjet(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
