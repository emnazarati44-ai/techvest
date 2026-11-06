package com.techvest.model;

import com.techvest.model.enums.StatutContrat;
import com.techvest.model.enums.TypeContrat;
import java.time.LocalDate;
import java.util.UUID;

public abstract class ContratInvestissement {
    protected String id;
    protected String projetId;
    protected LocalDate dateCreation;
    protected StatutContrat statut;
    protected String termes;
    protected TypeContrat typeContrat;

    public ContratInvestissement(String projetId, String termes, TypeContrat typeContrat) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.projetId = projetId;
        this.dateCreation = LocalDate.now();
        this.statut = StatutContrat.BROUILLON;
        this.termes = termes;
        this.typeContrat = typeContrat;
    }

    public ContratInvestissement(String id, String projetId, LocalDate dateCreation, 
                                  StatutContrat statut, String termes, TypeContrat typeContrat) {
        this.id = id;
        this.projetId = projetId;
        this.dateCreation = dateCreation;
        this.statut = statut;
        this.termes = termes;
        this.typeContrat = typeContrat;
    }

    public abstract double calculerRetourInvestissement(double montantInvesti);
    public abstract String getDetailsContrat();

    public void publier() {
        if (this.statut == StatutContrat.BROUILLON) {
            this.statut = StatutContrat.EN_ATTENTE_SIGNATURE;
        }
    }

    public void activer() {
        this.statut = StatutContrat.ACTIF;
    }

    public void terminer() {
        this.statut = StatutContrat.TERMINE;
    }

    public void annuler() {
        this.statut = StatutContrat.ANNULE;
    }

    public String getId() { return id; }
    public String getProjetId() { return projetId; }
    public LocalDate getDateCreation() { return dateCreation; }
    public StatutContrat getStatut() { return statut; }
    public String getTermes() { return termes; }
    public TypeContrat getTypeContrat() { return typeContrat; }

    public void setStatut(StatutContrat statut) { this.statut = statut; }
    public void setTermes(String termes) { this.termes = termes; }
}
