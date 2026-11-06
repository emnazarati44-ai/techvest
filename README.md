# TechVest — Plateforme d'investissement dans les projets tech

> *Java OOP console application: IT job-market analysis + tech project crowdfunding with JDBC/SQLite persistence.*

Application Java en console qui combine deux modules :

1. **Analyse du marché IT** — lecture d'un jeu de données de 49 métiers (salaires min / max / moyen, croissance, niveau de demande) par région (France, Canada, USA…), avec classements et filtres.
2. **Crowdfunding tech** — les porteurs de projet publient un projet (après vérification d'identité KYC), choisissent un type de contrat, et les investisseurs enregistrent leurs investissements.

## Aperçu

```
═══ TOP 10 SALAIRES ═══

MÉTIER                         | RÉGION | SALAIRE | CROIS.  | DEMANDE
--------------------------------------------------------------------------
Ingénieur IA                   | USA    | 160000€ | +19.8% | TRES_ELEVE
Machine Learning Engineer      | USA    | 145000€ | +17.8% | TRES_ELEVE
Data Scientist                 | USA    | 130000€ | +14.5% | TRES_ELEVE
Ingénieur Cybersécurité        | USA    | 125000€ | +11.5% | TRES_ELEVE
DevOps Engineer                | USA    | 115000€ | +12.8% | TRES_ELEVE
```

## Conception orientée objet

| Concept | Mise en œuvre |
|---|---|
| Héritage & classes abstraites | `Utilisateur` → `Investisseur`, `PorteurProjet` · `ContratInvestissement` → `ContratRevenuFixe`, `ContratEquity` |
| Polymorphisme | Calcul et affichage propres à chaque type de contrat |
| Interfaces | `MetierIT implements Comparable` (tri par salaire / croissance) |
| Énumérations | `StatutKYC`, `StatutProjet`, `StatutContrat`, `CategorieProjet`, `TypeContrat`, `NiveauDemande` |
| Exceptions métier | `KYCNonValideException`, `ProjetNonModifiableException`, `EmailDejaUtiliseException`, `ContratDejaSigneException`… |
| Collections & génériques | `List`, `Map`, regroupements et classements dans `AnalyseurMarcheService` |
| Fichiers | Lecture du CSV `resources/salaires_it.csv` |
| Persistance JDBC | Pattern DAO (`UtilisateurDAO`, `ProjetDAO`, `ContratDAO`, `InvestissementDAO`, `DocumentKYCDAO`) sur SQLite |

## Architecture

```
src/com/techvest/
├── model/        # Entités métier et énumérations
├── exception/    # Exceptions personnalisées
├── dao/          # Accès aux données JDBC (pattern DAO) + connexion SQLite
├── service/      # AnalyseurMarcheService : statistiques du marché IT
├── ui/           # ConsoleUI : menus et parcours utilisateur
└── Main.java
resources/salaires_it.csv   # données du marché IT
```

Base de données SQLite créée automatiquement au premier lancement (tables `utilisateurs`, `projets`, `contrats`, `investissements`, `documents_kyc`).

## Lancer le projet

Prérequis : Java JDK 17+.

```bash
./run.sh
```

Le script télécharge le driver SQLite JDBC si nécessaire, compile les sources et lance l'application.

Sous Windows (PowerShell), après avoir placé `sqlite-jdbc.jar` dans `lib/` :

```powershell
javac -encoding UTF-8 -d bin -cp "lib/*" (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "bin;lib/*;resources" com.techvest.Main
```

## Parcours utilisateur

- **Porteur de projet** : inscription → vérification KYC → création du projet → choix du contrat (revenu fixe ou equity) → mise en financement
- **Investisseur** : inscription → consultation des projets ouverts → enregistrement d'un investissement

---

Projet académique — Programmation orientée objet en Java, TED University, 2025–2026 · **Emna Zarati** · [LinkedIn](https://www.linkedin.com/in/emna-zarati-30955937b)

*Projet réalisé en binôme avec Luca Raciti ([GitHub](https://github.com/RacitiLuca)).*
