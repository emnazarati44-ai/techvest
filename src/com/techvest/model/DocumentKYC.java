package com.techvest.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class DocumentKYC {
    private String id;
    private String typeDocument;
    private String numeroIdentite;
    private String adresse;
    private LocalDateTime dateSoumission;
    private boolean estValide;

    public DocumentKYC(String typeDocument, String numeroIdentite, String adresse) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.typeDocument = typeDocument;
        this.numeroIdentite = numeroIdentite;
        this.adresse = adresse;
        this.dateSoumission = LocalDateTime.now();
        this.estValide = false;
    }

    public DocumentKYC(String id, String typeDocument, String numeroIdentite, String adresse, 
                       LocalDateTime dateSoumission, boolean estValide) {
        this.id = id;
        this.typeDocument = typeDocument;
        this.numeroIdentite = numeroIdentite;
        this.adresse = adresse;
        this.dateSoumission = dateSoumission;
        this.estValide = estValide;
    }

    public void valider() {
        this.estValide = true;
    }

    public void rejeter() {
        this.estValide = false;
    }

    public String getId() { return id; }
    public String getTypeDocument() { return typeDocument; }
    public String getNumeroIdentite() { return numeroIdentite; }
    public String getAdresse() { return adresse; }
    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public boolean isEstValide() { return estValide; }

    @Override
    public String toString() {
        return String.format("KYC [%s] - %s | Valide: %s", typeDocument, numeroIdentite, estValide ? "Oui" : "Non");
    }
}
