package com.techvest.model.enums;

public enum NiveauDemande {
    FAIBLE(1),
    MOYEN(2),
    ELEVE(3),
    TRES_ELEVE(4);

    private int niveau;

    NiveauDemande(int niveau) {
        this.niveau = niveau;
    }

    public int getNiveau() {
        return niveau;
    }

    public static NiveauDemande fromString(String s) {
        switch (s.toUpperCase()) {
            case "FAIBLE": return FAIBLE;
            case "MOYEN": return MOYEN;
            case "ELEVE": return ELEVE;
            case "TRES_ELEVE": return TRES_ELEVE;
            default: return MOYEN;
        }
    }
}
