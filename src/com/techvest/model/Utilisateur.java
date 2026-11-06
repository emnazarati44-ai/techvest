package com.techvest.model;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Utilisateur {
    protected String id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse;
    protected LocalDate dateInscription;

    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = LocalDate.now();
    }

    public Utilisateur(String id, String nom, String prenom, String email, String motDePasse, LocalDate dateInscription) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.dateInscription = dateInscription;
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    public abstract String getType();

    public String getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getMotDePasse() { return motDePasse; }
    public LocalDate getDateInscription() { return dateInscription; }
    public String getNomComplet() { return prenom + " " + nom; }

    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    @Override
    public String toString() {
        return String.format("[%s] %s %s (%s)", id, prenom, nom, email);
    }
}
