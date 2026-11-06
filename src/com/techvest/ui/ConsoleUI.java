package com.techvest.ui;

import com.techvest.dao.*;
import com.techvest.model.*;
import com.techvest.model.enums.*;
import com.techvest.service.AnalyseurMarcheService;
import com.techvest.exception.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private Scanner scanner;
    private Utilisateur utilisateurConnecte;
    private UtilisateurDAO utilisateurDAO;
    private ProjetDAO projetDAO;
    private ContratDAO contratDAO;
    private InvestissementDAO investissementDAO;
    private DocumentKYCDAO documentKYCDAO;
    private AnalyseurMarcheService analyseurMarche;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.utilisateurDAO = new UtilisateurDAO();
        this.projetDAO = new ProjetDAO();
        this.contratDAO = new ContratDAO();
        this.investissementDAO = new InvestissementDAO();
        this.documentKYCDAO = new DocumentKYCDAO();
        this.analyseurMarche = new AnalyseurMarcheService();
    }

    public void demarrer() {
        afficherBanniere();
        
        try {
            analyseurMarche.chargerDonneesCSV("resources/salaires_it.csv");
        } catch (Exception e) {
            System.out.println("⚠️ Données marché non chargées: " + e.getMessage());
        }

        boolean continuer = true;
        while (continuer) {
            if (utilisateurConnecte == null) {
                continuer = menuPrincipal();
            } else if (utilisateurConnecte instanceof Investisseur) {
                menuInvestisseur();
            } else if (utilisateurConnecte instanceof PorteurProjet) {
                menuPorteur();
            }
        }
        
        System.out.println("\n👋 Merci d'avoir utilisé TechVest. À bientôt!");
        scanner.close();
    }

    private void afficherBanniere() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                ║");
        System.out.println("║   ████████╗███████╗ ██████╗██╗  ██╗██╗   ██╗███████╗███████╗   ║");
        System.out.println("║   ╚══██╔══╝██╔════╝██╔════╝██║  ██║██║   ██║██╔════╝╚══██╔══╝  ║");
        System.out.println("║      ██║   █████╗  ██║     ███████║██║   ██║█████╗     ██║     ║");
        System.out.println("║      ██║   ██╔══╝  ██║     ██╔══██║╚██╗ ██╔╝██╔══╝     ██║     ║");
        System.out.println("║      ██║   ███████╗╚██████╗██║  ██║ ╚████╔╝ ███████╗   ██║     ║");
        System.out.println("║      ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝  ╚═══╝  ╚══════╝   ╚═╝     ║");
        System.out.println("║                                                                ║");
        System.out.println("║        Plateforme d'Investissement dans les Projets Tech       ║");
        System.out.println("║                                                                ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private boolean menuPrincipal() {
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│         MENU PRINCIPAL              │");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. 🔐 Se connecter                  │");
        System.out.println("│ 2. 📝 S'inscrire                    │");
        System.out.println("│ 3. 📊 Voir analyse marché IT        │");
        System.out.println("│ 4. 📋 Voir projets en financement   │");
        System.out.println("│ 0. 🚪 Quitter                       │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Votre choix: ");

        int choix = lireEntier();
        switch (choix) {
            case 1: seConnecter(); break;
            case 2: sInscrire(); break;
            case 3: afficherAnalyseMarche(); break;
            case 4: afficherProjetsPublics(); break;
            case 0: return false;
            default: System.out.println("❌ Choix invalide");
        }
        return true;
    }

    private void menuInvestisseur() {
        Investisseur inv = (Investisseur) utilisateurConnecte;
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│      MENU INVESTISSEUR              │");
        System.out.println("│  Bonjour " + inv.getPrenom() + "!");
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. 🔍 Parcourir les projets         │");
        System.out.println("│ 2. 💰 Investir dans un projet       │");
        System.out.println("│ 3. 📜 Mon historique                │");
        System.out.println("│ 4. 📊 Analyse marché IT             │");
        System.out.println("│ 5. 👤 Mon profil                    │");
        System.out.println("│ 0. 🚪 Déconnexion                   │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Votre choix: ");

        int choix = lireEntier();
        switch (choix) {
            case 1: parcourirProjets(); break;
            case 2: investirDansProjet(); break;
            case 3: afficherHistoriqueInvestisseur(); break;
            case 4: afficherAnalyseMarche(); break;
            case 5: afficherProfil(); break;
            case 0: seDeconnecter(); break;
            default: System.out.println("❌ Choix invalide");
        }
    }

    private void menuPorteur() {
        PorteurProjet pp = (PorteurProjet) utilisateurConnecte;
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│     MENU PORTEUR DE PROJET          │");
        System.out.println("│  Bonjour " + pp.getPrenom() + "! KYC: " + pp.getStatutKYC());
        System.out.println("├─────────────────────────────────────┤");
        System.out.println("│ 1. ➕ Créer un projet               │");
        System.out.println("│ 2. 📋 Mes projets                   │");
        System.out.println("│ 3. 📄 Gérer KYC                     │");
        System.out.println("│ 4. 📊 Analyse marché IT             │");
        System.out.println("│ 5. 👤 Mon profil                    │");
        System.out.println("│ 0. 🚪 Déconnexion                   │");
        System.out.println("└─────────────────────────────────────┘");
        System.out.print("Votre choix: ");

        int choix = lireEntier();
        switch (choix) {
            case 1: creerProjet(); break;
            case 2: afficherMesProjets(); break;
            case 3: gererKYC(); break;
            case 4: afficherAnalyseMarche(); break;
            case 5: afficherProfil(); break;
            case 0: seDeconnecter(); break;
            default: System.out.println("❌ Choix invalide");
        }
    }

    private void seConnecter() {
        System.out.println("\n═══ CONNEXION ═══");
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Mot de passe: ");
        String mdp = scanner.nextLine();

        try {
            Utilisateur user = utilisateurDAO.trouverParEmail(email);
            if (user != null && user.verifierMotDePasse(mdp)) {
                utilisateurConnecte = user;
                System.out.println("✅ Connexion réussie! Bienvenue " + user.getPrenom());
            } else {
                System.out.println("❌ Email ou mot de passe incorrect");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void sInscrire() {
        System.out.println("\n═══ INSCRIPTION ═══");
        System.out.println("Type de compte:");
        System.out.println("1. Investisseur");
        System.out.println("2. Porteur de projet");
        System.out.print("Choix: ");
        int type = lireEntier();

        System.out.print("Nom: ");
        String nom = scanner.nextLine().trim();
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Mot de passe: ");
        String mdp = scanner.nextLine();

        try {
            if (utilisateurDAO.emailExiste(email)) {
                System.out.println("❌ Cet email est déjà utilisé");
                return;
            }

            Utilisateur newUser;
            if (type == 1) {
                newUser = new Investisseur(nom, prenom, email, mdp);
            } else {
                newUser = new PorteurProjet(nom, prenom, email, mdp);
            }

            utilisateurDAO.sauvegarder(newUser);
            System.out.println("✅ Inscription réussie! Vous pouvez maintenant vous connecter.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void seDeconnecter() {
        utilisateurConnecte = null;
        System.out.println("✅ Déconnexion réussie");
    }

    private void creerProjet() {
        PorteurProjet pp = (PorteurProjet) utilisateurConnecte;
        
        if (pp.getStatutKYC() != StatutKYC.VERIFIE) {
            System.out.println("⚠️ Vous devez d'abord compléter votre KYC pour créer un projet.");
            return;
        }

        System.out.println("\n═══ CRÉER UN PROJET ═══");
        
        System.out.print("Titre du projet: ");
        String titre = scanner.nextLine().trim();
        
        System.out.println("Description (terminez par une ligne vide):");
        StringBuilder desc = new StringBuilder();
        String ligne;
        while (!(ligne = scanner.nextLine()).isEmpty()) {
            desc.append(ligne).append("\n");
        }

        System.out.println("\nCatégories disponibles:");
        CategorieProjet[] categories = CategorieProjet.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i].getDescription());
        }
        System.out.print("Choix: ");
        int catIndex = lireEntier() - 1;
        CategorieProjet categorie = categories[Math.max(0, Math.min(catIndex, categories.length - 1))];

        System.out.print("Montant demandé (en crypto): ");
        double montant = lireDouble();

        System.out.print("Date limite (jj/mm/aaaa): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate dateLimite;
        try {
            dateLimite = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            dateLimite = LocalDate.now().plusMonths(3);
            System.out.println("Date invalide, définie à " + dateLimite);
        }

        System.out.print("Votre adresse crypto (pour recevoir les fonds): ");
        String adresseCrypto = scanner.nextLine().trim();

        System.out.print("Contact externe (Telegram/Discord/Email): ");
        String contact = scanner.nextLine().trim();

        try {
            ProjetTech projet = new ProjetTech(titre, desc.toString().trim(), categorie, 
                                                montant, dateLimite, pp.getId(), adresseCrypto, contact);
            projetDAO.sauvegarder(projet);
            
            System.out.println("\n✅ Projet créé avec succès!");
            System.out.println("ID du projet: " + projet.getId());
            System.out.println("\n⚠️ N'oubliez pas de créer un contrat et de soumettre votre projet pour qu'il soit visible.");
            
            System.out.print("\nVoulez-vous créer un contrat maintenant? (o/n): ");
            if (scanner.nextLine().trim().toLowerCase().startsWith("o")) {
                creerContratPourProjet(projet);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void creerContratPourProjet(ProjetTech projet) {
        System.out.println("\n═══ CRÉATION DU CONTRAT ═══");
        System.out.println("Type de contrat:");
        System.out.println("1. Revenu Fixe (rendement garanti)");
        System.out.println("2. Equity (parts du projet)");
        System.out.print("Choix: ");
        int typeContrat = lireEntier();

        System.out.print("Termes du contrat (conditions générales): ");
        String termes = scanner.nextLine().trim();

        try {
            ContratInvestissement contrat;
            if (typeContrat == 1) {
                System.out.print("Durée de l'investissement (en mois): ");
                int duree = lireEntier();
                System.out.print("Taux de rendement annuel (%): ");
                double taux = lireDouble();
                contrat = new ContratRevenuFixe(projet.getId(), termes, duree, taux);
            } else {
                System.out.print("Pourcentage d'equity offert (%): ");
                double pct = lireDouble();
                System.out.print("Conditions de sortie: ");
                String conditions = scanner.nextLine().trim();
                System.out.print("Droits de vote inclus? (o/n): ");
                boolean vote = scanner.nextLine().trim().toLowerCase().startsWith("o");
                contrat = new ContratEquity(projet.getId(), termes, pct, conditions, vote);
            }

            contratDAO.sauvegarder(contrat);
            projet.setContratId(contrat.getId());
            projetDAO.mettreAJourContrat(projet.getId(), contrat.getId());

            System.out.println("✅ Contrat créé!");
            System.out.println(contrat.getDetailsContrat());

            System.out.print("\nSoumettre le projet pour financement? (o/n): ");
            if (scanner.nextLine().trim().toLowerCase().startsWith("o")) {
                projet.soumettre();
                projetDAO.mettreAJourStatut(projet.getId(), StatutProjet.EN_FINANCEMENT);
                contrat.publier();
                contratDAO.mettreAJourStatut(contrat.getId(), StatutContrat.EN_ATTENTE_SIGNATURE);
                System.out.println("✅ Projet soumis! Il est maintenant visible aux investisseurs.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void afficherMesProjets() {
        try {
            List<ProjetTech> projets = projetDAO.listerParPorteur(utilisateurConnecte.getId());
            if (projets.isEmpty()) {
                System.out.println("\n📭 Vous n'avez pas encore de projet.");
                return;
            }

            System.out.println("\n═══ MES PROJETS ═══");
            for (int i = 0; i < projets.size(); i++) {
                System.out.println("\n" + (i + 1) + ". " + projets.get(i).getResume());
            }

            System.out.print("\nVoir les détails d'un projet? (numéro ou 0): ");
            int choix = lireEntier();
            if (choix > 0 && choix <= projets.size()) {
                ProjetTech p = projets.get(choix - 1);
                System.out.println(p.getDetailsComplets());
                
                if (p.getContratId() != null) {
                    ContratInvestissement contrat = contratDAO.trouverParId(p.getContratId());
                    if (contrat != null) {
                        System.out.println("\n" + contrat.getDetailsContrat());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void gererKYC() {
        PorteurProjet pp = (PorteurProjet) utilisateurConnecte;
        
        System.out.println("\n═══ GESTION KYC ═══");
        System.out.println("Statut actuel: " + pp.getStatutKYC());

        if (pp.getStatutKYC() == StatutKYC.VERIFIE) {
            System.out.println("✅ Votre KYC est déjà validé!");
            return;
        }

        if (pp.getStatutKYC() == StatutKYC.EN_ATTENTE) {
            System.out.println("⏳ Votre KYC est en cours de vérification...");
            // Simulation: auto-validation après soumission
            pp.validerKYC();
            try {
                utilisateurDAO.mettreAJourKYC(pp.getId(), StatutKYC.VERIFIE);
                System.out.println("✅ KYC validé avec succès!");
            } catch (SQLException e) {
                System.out.println("❌ Erreur: " + e.getMessage());
            }
            return;
        }

        System.out.println("\nSoumettre vos documents KYC:");
        System.out.print("Type de document (CIN/Passeport): ");
        String type = scanner.nextLine().trim();
        System.out.print("Numéro du document: ");
        String numero = scanner.nextLine().trim();
        System.out.print("Adresse complète: ");
        String adresse = scanner.nextLine().trim();

        try {
            DocumentKYC doc = new DocumentKYC(type, numero, adresse);
            documentKYCDAO.sauvegarder(doc, pp.getId());
            pp.soumettreKYC(doc);
            utilisateurDAO.mettreAJourKYC(pp.getId(), StatutKYC.EN_ATTENTE);
            
            System.out.println("✅ Documents soumis! Statut: EN_ATTENTE");
            System.out.println("💡 Revenez dans ce menu pour voir si votre KYC a été validé.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void parcourirProjets() {
        try {
            List<ProjetTech> projets = projetDAO.listerOuvertsAuxInvestissements();
            if (projets.isEmpty()) {
                System.out.println("\n📭 Aucun projet ouvert aux investissements pour le moment.");
                return;
            }

            System.out.println("\n═══ PROJETS DISPONIBLES ═══");
            System.out.println("Filtrer par:");
            System.out.println("1. Tous les projets");
            System.out.println("2. Par catégorie");
            System.out.print("Choix: ");
            int filtre = lireEntier();

            if (filtre == 2) {
                System.out.println("\nCatégories:");
                CategorieProjet[] cats = CategorieProjet.values();
                for (int i = 0; i < cats.length; i++) {
                    System.out.println((i + 1) + ". " + cats[i].getDescription());
                }
                System.out.print("Choix: ");
                int catIndex = lireEntier() - 1;
                if (catIndex >= 0 && catIndex < cats.length) {
                    projets = projetDAO.listerParCategorie(cats[catIndex]);
                }
            }

            for (int i = 0; i < projets.size(); i++) {
                System.out.println("\n" + (i + 1) + ". " + projets.get(i).getResume());
            }

            System.out.print("\nVoir les détails d'un projet? (numéro ou 0): ");
            int choix = lireEntier();
            if (choix > 0 && choix <= projets.size()) {
                afficherDetailsProjet(projets.get(choix - 1));
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void afficherDetailsProjet(ProjetTech projet) {
        System.out.println(projet.getDetailsComplets());
        
        try {
            if (projet.getContratId() != null) {
                ContratInvestissement contrat = contratDAO.trouverParId(projet.getContratId());
                if (contrat != null) {
                    System.out.println("\n" + contrat.getDetailsContrat());
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Impossible de charger le contrat");
        }
    }

    private void investirDansProjet() {
        System.out.print("\nEntrez l'ID du projet: ");
        String projetId = scanner.nextLine().trim();

        try {
            ProjetTech projet = projetDAO.trouverParId(projetId);
            if (projet == null) {
                System.out.println("❌ Projet non trouvé");
                return;
            }

            if (!projet.isOuvertAuxInvestissements()) {
                System.out.println("❌ Ce projet n'est pas ouvert aux investissements");
                return;
            }

            afficherDetailsProjet(projet);

            System.out.println("\n═══ INVESTIR ═══");
            System.out.println("📍 Pour investir:");
            System.out.println("1. Contactez le porteur: " + projet.getContactExterne());
            System.out.println("2. Discutez des termes du contrat");
            System.out.println("3. Envoyez les fonds à: " + projet.getAdresseCrypto());
            System.out.println("4. Revenez ici pour enregistrer votre investissement");

            System.out.print("\nAvez-vous déjà envoyé les fonds? (o/n): ");
            if (!scanner.nextLine().trim().toLowerCase().startsWith("o")) {
                System.out.println("💡 Contactez d'abord le porteur et envoyez les fonds.");
                return;
            }

            System.out.print("Montant investi (en crypto): ");
            double montant = lireDouble();

            System.out.print("Hash de la transaction: ");
            String hash = scanner.nextLine().trim();

            // Créer l'investissement
            Investissement inv = new Investissement(utilisateurConnecte.getId(), 
                                                     projet.getId(), 
                                                     projet.getContratId(), 
                                                     montant);
            inv.confirmerTransaction(hash);
            investissementDAO.sauvegarder(inv);

            // Mettre à jour le projet
            double nouveauTotal = projet.getMontantCollecte() + montant;
            projetDAO.mettreAJourMontantCollecte(projet.getId(), nouveauTotal);

            // Mettre à jour l'investisseur
            Investisseur investisseur = (Investisseur) utilisateurConnecte;
            double totalInv = investissementDAO.getTotalParInvestisseur(investisseur.getId());
            utilisateurDAO.mettreAJourTotalInvesti(investisseur.getId(), totalInv);

            System.out.println("\n✅ Investissement enregistré avec succès!");
            System.out.println(inv.getDetails());

        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void afficherHistoriqueInvestisseur() {
        try {
            List<Investissement> invs = investissementDAO.listerParInvestisseur(utilisateurConnecte.getId());
            
            if (invs.isEmpty()) {
                System.out.println("\n📭 Vous n'avez pas encore d'investissement.");
                return;
            }

            System.out.println("\n═══ MON HISTORIQUE D'INVESTISSEMENTS ═══");
            double total = 0;
            for (Investissement inv : invs) {
                System.out.println(inv.getDetails());
                total += inv.getMontant();
            }
            System.out.println("\n💰 Total investi: " + total + " crypto");

        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private void afficherProfil() {
        System.out.println("\n═══ MON PROFIL ═══");
        System.out.println("ID: " + utilisateurConnecte.getId());
        System.out.println("Nom: " + utilisateurConnecte.getNomComplet());
        System.out.println("Email: " + utilisateurConnecte.getEmail());
        System.out.println("Type: " + utilisateurConnecte.getType());
        System.out.println("Inscrit le: " + utilisateurConnecte.getDateInscription());

        if (utilisateurConnecte instanceof PorteurProjet) {
            PorteurProjet pp = (PorteurProjet) utilisateurConnecte;
            System.out.println("Statut KYC: " + pp.getStatutKYC());
            System.out.println("Contact: " + pp.getContactExterne());
            System.out.println("Adresse Crypto: " + pp.getAdresseCrypto());
        } else if (utilisateurConnecte instanceof Investisseur) {
            Investisseur inv = (Investisseur) utilisateurConnecte;
            System.out.println("Total investi: " + inv.getTotalInvesti() + " crypto");
        }
    }

    private void afficherAnalyseMarche() {
        System.out.println("\n═══ ANALYSE DU MARCHÉ IT ═══");
        System.out.println("1. Rapport complet");
        System.out.println("2. Top 10 salaires");
        System.out.println("3. Top 10 croissance");
        System.out.println("4. Filtrer par région");
        System.out.println("5. Filtrer par catégorie");
        System.out.print("Choix: ");

        int choix = lireEntier();
        switch (choix) {
            case 1:
                System.out.println(analyseurMarche.genererRapport());
                break;
            case 2:
                System.out.println("\n═══ TOP 10 SALAIRES ═══");
                afficherListeMetiers(analyseurMarche.getTop10Salaires());
                break;
            case 3:
                System.out.println("\n═══ TOP 10 CROISSANCE ═══");
                afficherListeMetiers(analyseurMarche.getTop10Croissance());
                break;
            case 4:
                System.out.println("Régions: " + analyseurMarche.getRegionsDisponibles());
                System.out.print("Région: ");
                String region = scanner.nextLine().trim();
                afficherListeMetiers(analyseurMarche.filtrerParRegion(region));
                break;
            case 5:
                System.out.println("Catégories: " + analyseurMarche.getCategoriesDisponibles());
                System.out.print("Catégorie: ");
                String cat = scanner.nextLine().trim();
                afficherListeMetiers(analyseurMarche.filtrerParCategorie(cat));
                break;
        }
    }

    private void afficherListeMetiers(List<MetierIT> metiers) {
        if (metiers.isEmpty()) {
            System.out.println("Aucun résultat.");
            return;
        }
        System.out.println("\n" + String.format("%-32s | %-10s | %10s | %8s | %s", 
            "MÉTIER", "RÉGION", "SALAIRE", "CROIS.", "DEMANDE"));
        System.out.println("-".repeat(80));
        for (MetierIT m : metiers) {
            System.out.println(m.toString());
        }
    }

    private void afficherProjetsPublics() {
        try {
            List<ProjetTech> projets = projetDAO.listerOuvertsAuxInvestissements();
            if (projets.isEmpty()) {
                System.out.println("\n📭 Aucun projet disponible.");
                return;
            }
            System.out.println("\n═══ PROJETS EN FINANCEMENT ═══");
            for (ProjetTech p : projets) {
                System.out.println(p.getResume());
            }
            System.out.println("\n💡 Connectez-vous pour investir!");
        } catch (SQLException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private int lireEntier() {
        try {
            String input = scanner.nextLine().trim();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double lireDouble() {
        try {
            String input = scanner.nextLine().trim();
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
