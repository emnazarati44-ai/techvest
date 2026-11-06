package com.techvest.model;

import com.techvest.model.enums.CategorieProjet;
import com.techvest.model.enums.StatutProjet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProjetTech {
    private String id;
    private String titre;
    private String description;
    private CategorieProjet categorie;
    private double montantDemande;
    private double montantCollecte;
    private LocalDate dateCreation;
    private LocalDate dateLimite;
    private StatutProjet statut;
    private String porteurId;
    private String adresseCrypto;
    private String contactExterne;
    private String contratId;
    private List<Investissement> investissements;

    public ProjetTech(String titre, String description, CategorieProjet categorie, 
                      double montantDemande, LocalDate dateLimite, String porteurId,
                      String adresseCrypto, String contactExterne) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.montantDemande = montantDemande;
        this.montantCollecte = 0;
        this.dateCreation = LocalDate.now();
        this.dateLimite = dateLimite;
        this.statut = StatutProjet.BROUILLON;
        this.porteurId = porteurId;
        this.adresseCrypto = adresseCrypto;
        this.contactExterne = contactExterne;
        this.investissements = new ArrayList<>();
    }

    public ProjetTech(String id, String titre, String description, CategorieProjet categorie,
                      double montantDemande, double montantCollecte, LocalDate dateCreation,
                      LocalDate dateLimite, StatutProjet statut, String porteurId,
                      String adresseCrypto, String contactExterne, String contratId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.montantDemande = montantDemande;
        this.montantCollecte = montantCollecte;
        this.dateCreation = dateCreation;
        this.dateLimite = dateLimite;
        this.statut = statut;
        this.porteurId = porteurId;
        this.adresseCrypto = adresseCrypto;
        this.contactExterne = contactExterne;
        this.contratId = contratId;
        this.investissements = new ArrayList<>();
    }

    public void soumettre() {
        if (this.statut == StatutProjet.BROUILLON && this.contratId != null) {
            this.statut = StatutProjet.EN_FINANCEMENT;
        }
    }

    public void ajouterInvestissement(Investissement inv) {
        this.investissements.add(inv);
        this.montantCollecte += inv.getMontant();
        if (this.montantCollecte >= this.montantDemande) {
            this.statut = StatutProjet.FINANCE;
        }
    }

    public void cloturer() {
        if (this.montantCollecte >= this.montantDemande) {
            this.statut = StatutProjet.FINANCE;
        } else {
            this.statut = StatutProjet.ECHEC;
        }
    }

    public double getPourcentageFinance() {
        if (montantDemande == 0) return 0;
        return (montantCollecte / montantDemande) * 100;
    }

    public boolean isOuvertAuxInvestissements() {
        return this.statut == StatutProjet.EN_FINANCEMENT && 
               LocalDate.now().isBefore(dateLimite) &&
               this.montantCollecte < this.montantDemande;
    }

    // Getters
    public String getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public CategorieProjet getCategorie() { return categorie; }
    public double getMontantDemande() { return montantDemande; }
    public double getMontantCollecte() { return montantCollecte; }
    public LocalDate getDateCreation() { return dateCreation; }
    public LocalDate getDateLimite() { return dateLimite; }
    public StatutProjet getStatut() { return statut; }
    public String getPorteurId() { return porteurId; }
    public String getAdresseCrypto() { return adresseCrypto; }
    public String getContactExterne() { return contactExterne; }
    public String getContratId() { return contratId; }
    public List<Investissement> getInvestissements() { return new ArrayList<>(investissements); }

    // Setters
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String desc) { this.description = desc; }
    public void setCategorie(CategorieProjet cat) { this.categorie = cat; }
    public void setMontantDemande(double montant) { this.montantDemande = montant; }
    public void setMontantCollecte(double montant) { this.montantCollecte = montant; }
    public void setDateLimite(LocalDate date) { this.dateLimite = date; }
    public void setStatut(StatutProjet statut) { this.statut = statut; }
    public void setAdresseCrypto(String adresse) { this.adresseCrypto = adresse; }
    public void setContactExterne(String contact) { this.contactExterne = contact; }
    public void setContratId(String contratId) { this.contratId = contratId; }

    public String getResume() {
        return String.format(
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "📋 %s [%s]\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "Catégorie: %s\n" +
            "Statut: %s\n" +
            "Financement: %.2f / %.2f crypto (%.1f%%)\n" +
            "Date limite: %s\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
            titre, id, categorie.getDescription(), statut.getDescription(),
            montantCollecte, montantDemande, getPourcentageFinance(), dateLimite
        );
    }

    public String getDetailsComplets() {
        return String.format(
            "╔══════════════════════════════════════════════════════════════╗\n" +
            "║ PROJET: %s\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║ ID: %s\n" +
            "║ Catégorie: %s\n" +
            "║ Statut: %s\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║ DESCRIPTION:\n" +
            "║ %s\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║ FINANCEMENT:\n" +
            "║ Objectif: %.2f crypto\n" +
            "║ Collecté: %.2f crypto (%.1f%%)\n" +
            "║ Date limite: %s\n" +
            "╠══════════════════════════════════════════════════════════════╣\n" +
            "║ CONTACT:\n" +
            "║ 💬 %s\n" +
            "║ 💰 Adresse Crypto: %s\n" +
            "╚══════════════════════════════════════════════════════════════╝",
            titre, id, categorie.getDescription(), statut.getDescription(),
            description, montantDemande, montantCollecte, getPourcentageFinance(),
            dateLimite, contactExterne, adresseCrypto
        );
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%.1f%% financé)", id, titre, statut, getPourcentageFinance());
    }
}
