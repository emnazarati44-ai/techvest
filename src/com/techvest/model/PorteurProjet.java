package com.techvest.model;

import com.techvest.model.enums.StatutKYC;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PorteurProjet extends Utilisateur {
    private List<ProjetTech> projets;
    private DocumentKYC documentKYC;
    private StatutKYC statutKYC;
    private String adresseCrypto;
    private String contactExterne;

    public PorteurProjet(String nom, String prenom, String email, String motDePasse) {
        super(nom, prenom, email, motDePasse);
        this.projets = new ArrayList<>();
        this.statutKYC = StatutKYC.NON_SOUMIS;
        this.adresseCrypto = "";
        this.contactExterne = "";
    }

    public PorteurProjet(String id, String nom, String prenom, String email, String motDePasse, 
                         LocalDate dateInscription, StatutKYC statutKYC, String adresseCrypto, String contactExterne) {
        super(id, nom, prenom, email, motDePasse, dateInscription);
        this.projets = new ArrayList<>();
        this.statutKYC = statutKYC;
        this.adresseCrypto = adresseCrypto != null ? adresseCrypto : "";
        this.contactExterne = contactExterne != null ? contactExterne : "";
    }

    @Override
    public String getType() {
        return "PORTEUR_PROJET";
    }

    public void soumettreKYC(DocumentKYC document) {
        this.documentKYC = document;
        this.statutKYC = StatutKYC.EN_ATTENTE;
    }

    public void validerKYC() {
        if (this.documentKYC != null) {
            this.documentKYC.valider();
            this.statutKYC = StatutKYC.VERIFIE;
        }
    }

    public void rejeterKYC() {
        if (this.documentKYC != null) {
            this.documentKYC.rejeter();
            this.statutKYC = StatutKYC.REJETE;
        }
    }

    public boolean isKYCValide() {
        return this.statutKYC == StatutKYC.VERIFIE;
    }

    public void ajouterProjet(ProjetTech projet) {
        this.projets.add(projet);
    }

    public List<ProjetTech> getProjets() { return new ArrayList<>(projets); }
    public DocumentKYC getDocumentKYC() { return documentKYC; }
    public StatutKYC getStatutKYC() { return statutKYC; }
    public String getAdresseCrypto() { return adresseCrypto; }
    public String getContactExterne() { return contactExterne; }

    public void setDocumentKYC(DocumentKYC doc) { this.documentKYC = doc; }
    public void setStatutKYC(StatutKYC statut) { this.statutKYC = statut; }
    public void setAdresseCrypto(String adresse) { this.adresseCrypto = adresse; }
    public void setContactExterne(String contact) { this.contactExterne = contact; }

    @Override
    public String toString() {
        return String.format("[PORTEUR] %s %s | KYC: %s | %d projets", 
            prenom, nom, statutKYC, projets.size());
    }
}
