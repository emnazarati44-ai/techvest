package com.techvest.model;

import com.techvest.model.enums.NiveauDemande;

public class MetierIT implements Comparable<MetierIT> {
    private String nom;
    private String categorie;
    private String region;
    private double salaireMin;
    private double salaireMax;
    private double salaireMoyen;
    private double tauxCroissance;
    private NiveauDemande demande;

    public MetierIT(String nom, String categorie, String region, double salaireMin, 
                    double salaireMax, double salaireMoyen, double tauxCroissance, NiveauDemande demande) {
        this.nom = nom;
        this.categorie = categorie;
        this.region = region;
        this.salaireMin = salaireMin;
        this.salaireMax = salaireMax;
        this.salaireMoyen = salaireMoyen;
        this.tauxCroissance = tauxCroissance;
        this.demande = demande;
    }

    public double calculerProjection(int annees) {
        return salaireMoyen * Math.pow(1 + tauxCroissance / 100, annees);
    }

    public double getScoreAttrait() {
        // Score combinant salaire, croissance et demande
        double scoreSalaire = salaireMoyen / 1000; // Normaliser
        double scoreCroissance = tauxCroissance * 2;
        double scoreDemande = demande.getNiveau() * 10;
        return scoreSalaire + scoreCroissance + scoreDemande;
    }

    @Override
    public int compareTo(MetierIT other) {
        // Tri par salaire moyen décroissant par défaut
        return Double.compare(other.salaireMoyen, this.salaireMoyen);
    }

    public String getNom() { return nom; }
    public String getCategorie() { return categorie; }
    public String getRegion() { return region; }
    public double getSalaireMin() { return salaireMin; }
    public double getSalaireMax() { return salaireMax; }
    public double getSalaireMoyen() { return salaireMoyen; }
    public double getTauxCroissance() { return tauxCroissance; }
    public NiveauDemande getDemande() { return demande; }

    public String getDetails() {
        return String.format(
            "┌────────────────────────────────────────────────┐\n" +
            "│ %s\n" +
            "├────────────────────────────────────────────────┤\n" +
            "│ Catégorie: %s\n" +
            "│ Région: %s\n" +
            "│ Salaire: %.0f - %.0f (moy: %.0f)\n" +
            "│ Croissance: +%.1f%% par an\n" +
            "│ Demande: %s\n" +
            "│ Projection 5 ans: %.0f\n" +
            "└────────────────────────────────────────────────┘",
            nom, categorie, region, salaireMin, salaireMax, salaireMoyen,
            tauxCroissance, demande, calculerProjection(5)
        );
    }

    @Override
    public String toString() {
        return String.format("%-30s | %s | %.0f€ | +%.1f%% | %s", 
            nom, region, salaireMoyen, tauxCroissance, demande);
    }
}
