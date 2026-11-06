package com.techvest.model.enums;

public enum TypeContrat {
    REVENU_FIXE("Contrat à Revenu Fixe"),
    EQUITY("Contrat Equity (Parts du projet)");

    private String description;

    TypeContrat(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
