package com.techvest.model.enums;

public enum CategorieProjet {
    IA("Intelligence Artificielle"),
    BLOCKCHAIN("Blockchain & Web3"),
    SAAS("Software as a Service"),
    MOBILE("Application Mobile"),
    IOT("Internet des Objets"),
    CYBERSECURITE("Cybersécurité"),
    GAMING("Jeux Vidéo"),
    FINTECH("Finance & Tech"),
    AUTRE("Autre");

    private String description;

    CategorieProjet(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
