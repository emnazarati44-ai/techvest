package com.techvest.service;

import com.techvest.model.MetierIT;
import com.techvest.model.enums.NiveauDemande;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AnalyseurMarcheService {
    private List<MetierIT> metiersIT;
    private Map<String, List<MetierIT>> donneesParRegion;
    private Map<String, List<MetierIT>> donneesParCategorie;

    public AnalyseurMarcheService() {
        this.metiersIT = new ArrayList<>();
        this.donneesParRegion = new HashMap<>();
        this.donneesParCategorie = new HashMap<>();
    }

    public void chargerDonneesCSV(String cheminFichier) throws IOException {
        metiersIT.clear();
        donneesParRegion.clear();
        donneesParCategorie.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            boolean premiereLigne = true;
            
            while ((ligne = br.readLine()) != null) {
                if (premiereLigne) {
                    premiereLigne = false;
                    continue;
                }
                
                String[] valeurs = ligne.split(",");
                if (valeurs.length >= 8) {
                    MetierIT metier = new MetierIT(
                        valeurs[0].trim(),
                        valeurs[1].trim(),
                        valeurs[2].trim(),
                        Double.parseDouble(valeurs[3].trim()),
                        Double.parseDouble(valeurs[4].trim()),
                        Double.parseDouble(valeurs[5].trim()),
                        Double.parseDouble(valeurs[6].trim()),
                        NiveauDemande.fromString(valeurs[7].trim())
                    );
                    
                    metiersIT.add(metier);
                    donneesParRegion.computeIfAbsent(metier.getRegion(), k -> new ArrayList<>()).add(metier);
                    donneesParCategorie.computeIfAbsent(metier.getCategorie(), k -> new ArrayList<>()).add(metier);
                }
            }
        }
        System.out.println("✅ " + metiersIT.size() + " métiers IT chargés");
    }

    public List<MetierIT> getTousLesMetiers() {
        return new ArrayList<>(metiersIT);
    }

    public List<MetierIT> filtrerParRegion(String region) {
        return donneesParRegion.getOrDefault(region, new ArrayList<>());
    }

    public List<MetierIT> filtrerParCategorie(String categorie) {
        return donneesParCategorie.getOrDefault(categorie, new ArrayList<>());
    }

    public List<MetierIT> getTop10Salaires() {
        List<MetierIT> copie = new ArrayList<>(metiersIT);
        Collections.sort(copie);
        return copie.subList(0, Math.min(10, copie.size()));
    }

    public List<MetierIT> getTop10Croissance() {
        List<MetierIT> copie = new ArrayList<>(metiersIT);
        copie.sort((m1, m2) -> Double.compare(m2.getTauxCroissance(), m1.getTauxCroissance()));
        return copie.subList(0, Math.min(10, copie.size()));
    }

    public List<MetierIT> getTop10Demande() {
        List<MetierIT> copie = new ArrayList<>(metiersIT);
        copie.sort((m1, m2) -> Integer.compare(m2.getDemande().getNiveau(), m1.getDemande().getNiveau()));
        return copie.subList(0, Math.min(10, copie.size()));
    }

    public Set<String> getRegionsDisponibles() {
        return donneesParRegion.keySet();
    }

    public Set<String> getCategoriesDisponibles() {
        return donneesParCategorie.keySet();
    }

    public double getSalaireMoyenGlobal() {
        if (metiersIT.isEmpty()) return 0;
        double total = 0;
        for (MetierIT m : metiersIT) {
            total += m.getSalaireMoyen();
        }
        return total / metiersIT.size();
    }

    public double getCroissanceMoyenne() {
        if (metiersIT.isEmpty()) return 0;
        double total = 0;
        for (MetierIT m : metiersIT) {
            total += m.getTauxCroissance();
        }
        return total / metiersIT.size();
    }

    public String genererRapport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔══════════════════════════════════════════════════════════════╗\n");
        sb.append("║           RAPPORT D'ANALYSE DU MARCHÉ IT                     ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Nombre de métiers analysés: %d\n", metiersIT.size()));
        sb.append(String.format("║ Régions: %s\n", String.join(", ", getRegionsDisponibles())));
        sb.append(String.format("║ Salaire moyen global: %.0f€\n", getSalaireMoyenGlobal()));
        sb.append(String.format("║ Croissance moyenne: %.1f%%\n", getCroissanceMoyenne()));
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append("║ TOP 5 - MEILLEURS SALAIRES                                   ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        
        List<MetierIT> topSalaires = getTop10Salaires();
        for (int i = 0; i < Math.min(5, topSalaires.size()); i++) {
            MetierIT m = topSalaires.get(i);
            sb.append(String.format("║ %d. %-22s %-8s %8.0f€\n", i+1, m.getNom(), m.getRegion(), m.getSalaireMoyen()));
        }
        
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        sb.append("║ TOP 5 - PLUS FORTE CROISSANCE                                ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════╣\n");
        
        List<MetierIT> topCroissance = getTop10Croissance();
        for (int i = 0; i < Math.min(5, topCroissance.size()); i++) {
            MetierIT m = topCroissance.get(i);
            sb.append(String.format("║ %d. %-22s %-8s +%.1f%%/an\n", i+1, m.getNom(), m.getRegion(), m.getTauxCroissance()));
        }
        
        sb.append("╚══════════════════════════════════════════════════════════════╝\n");
        return sb.toString();
    }
}
