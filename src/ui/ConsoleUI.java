package ui;

import model.Etudiant;
import model.DemandeAide;
import model.Competence;
import model.CompetenceAcademique;
import model.CompetencePratique;
import model.CompetenceTechnique;
import model.Service;
import model.Transaction;
import model.Administrateur;
import service.HistoriqueTransactions;
import service.DataManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


public class ConsoleUI {
    private static List<Etudiant> etudiants;
    private static List<Administrateur> administrateurs;
    private static int nextId = 1;
    private static List<DemandeAide> demandesAide;
    private static int nextDemandeId = 1;
    private static List<Service> services;
    private static int nextServiceId = 1;
    private static List<Transaction> transactions;
    private static int nextTransactionId = 1;
    private static List<HistoriqueTransactions> historiqueTransactions;

    public static void lancer() {
        // Charger les données au démarrage
        chargerDonnees();

        Scanner sc = new Scanner(System.in);
        int choix = -1;
        while (choix != 5) {
            System.out.println("===== SKILLSHARE CAMPUS =====");
            System.out.println("1. Connexion étudiant");
            System.out.println("2. Connexion administrateur");
            System.out.println("3. Inscription étudiant");
            System.out.println("4. Inscription administrateur");
            System.out.println("5. Quitter");
            System.out.print("> ");
            try {
                choix = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }
            switch (choix) {
                case 1:
                    Etudiant etu = connexion(sc);
                    if (etu != null) {
                        menuEtudiant(sc, etu);
                    }
                    break;
                case 2:
                    Administrateur admin = connexionAdmin(sc);
                    if (admin != null) {
                        menuAdmin(sc, admin);
                    }
                    break;
                case 3:
                    inscription(sc);
                    // Sauvegarder après l'inscription
                    sauvegarderDonnees();
                    break;
                case 4:
                    inscriptionAdmin(sc);
                    // Sauvegarder après l'inscription
                    sauvegarderDonnees();
                    break;
                case 5:
                    // Sauvegarder les données avant de quitter
                    sauvegarderDonnees();
                    System.out.println("Données sauvegardées. Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
            System.out.println();
        }
        sc.close();
    }

    private static void chargerDonnees() {
        Object[] donnees = DataManager.chargerDonnees();
        etudiants = (List<Etudiant>) donnees[0];
        administrateurs = (List<Administrateur>) donnees[1];
        demandesAide = (List<DemandeAide>) donnees[2];
        services = (List<Service>) donnees[3];
        transactions = (List<Transaction>) donnees[4];
        historiqueTransactions = (List<HistoriqueTransactions>) donnees[5];

        // Ajouter un administrateur par défaut s'il n'y en a pas
        if (administrateurs.isEmpty()) {
            Administrateur admin = new Administrateur(1, "admin", "admin", "admin@gmail.com", "admin");
            administrateurs.add(admin);
            System.out.println("Un compte administrateur par défaut a été créé.");
            System.out.println("Email : admin@gmail.com");
            System.out.println("Mot de passe : admin");
        }

        // Mettre à jour les IDs
        if (!etudiants.isEmpty()) {
            nextId = etudiants.stream().mapToInt(Etudiant::getId).max().getAsInt() + 1;
        }
        if (!demandesAide.isEmpty()) {
            nextDemandeId = demandesAide.stream().mapToInt(DemandeAide::getId).max().getAsInt() + 1;
        }
        if (!services.isEmpty()) {
            nextServiceId = services.stream().mapToInt(Service::getId).max().getAsInt() + 1;
        }
        if (!transactions.isEmpty()) {
            nextTransactionId = transactions.stream().mapToInt(Transaction::getId).max().getAsInt() + 1;
        }
    }

    private static void sauvegarderDonnees() {
        DataManager.sauvegarderDonnees(etudiants, administrateurs, demandesAide,
                services, transactions, historiqueTransactions);
    }

    private static Etudiant connexion(Scanner sc) {
        System.out.println("--- Connexion ---");
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = sc.nextLine();
        for (Etudiant e : etudiants) {
            if (e.sAuthentifier(email, mdp)) {
                System.out.println("Connexion réussie");
                return e;
            }
        }
        System.out.println("Identifiants incorrects ou utilisateur non trouvé.");
        return null;
    }

    private static Administrateur connexionAdmin(Scanner sc) {
        System.out.println("--- Connexion Administrateur ---");
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = sc.nextLine();
        for (Administrateur admin : administrateurs) {
            if (admin.sAuthentifier(email, mdp)) {
                System.out.println("Connexion réussie. Bienvenue, " + admin.getPrenom() + " !");
                return admin;
            }
        }
        System.out.println("Identifiants incorrects ou administrateur non trouvé.");
        return null;
    }

    private static void inscription(Scanner sc) {
        System.out.println("--- Inscription ---");
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("Prénom : ");
        String prenom = sc.nextLine();

        // Validation de l'email
        String email;
        boolean emailValide = false;
        do {
            System.out.print("Email : ");
            email = sc.nextLine();
            // Vérifier si l'email contient @ et .
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                emailValide = true;
            } else {
                System.out.println("Format d'email invalide. Veuillez réessayer.");
            }
        } while (!emailValide);

        System.out.print("Mot de passe : ");
        String mdp = sc.nextLine();
        System.out.print("Filière : ");
        String filiere = sc.nextLine();

        // Validation du niveau (doit être un entier)
        int niveau = 0;
        boolean niveauValide = false;
        do {
            System.out.print("Niveau : ");
            try {
                niveau = Integer.parseInt(sc.nextLine());
                niveauValide = true;
            } catch (NumberFormatException e) {
                System.out.println("Le niveau doit être un nombre entier. Veuillez réessayer.");
            }
        } while (!niveauValide);

        int credits = 50;
        Etudiant e = new Etudiant(nextId++, nom, prenom, email, mdp, filiere, niveau, credits);
        etudiants.add(e);
        System.out.println("Inscription réussie. Bienvenue, " + prenom + " !");
    }

    private static void inscriptionAdmin(Scanner sc) {
        System.out.println("--- Inscription Administrateur ---");
        System.out.print("Nom : ");
        String nom = sc.nextLine().trim();
        System.out.print("Prénom : ");
        String prenom = sc.nextLine().trim();

        // Validation de l'email
        String email;
        boolean emailValide = false;
        do {
            System.out.print("Email : ");
            email = sc.nextLine().trim();
            // Vérifier si l'email contient @ et .
            if (email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                emailValide = true;
            } else {
                System.out.println("Format d'email invalide. Veuillez réessayer.");
            }
        } while (!emailValide);

        System.out.print("Mot de passe : ");
        String mdp = sc.nextLine().trim();

        // Créer le nouvel administrateur
        Administrateur admin = new Administrateur(nextId++, nom, prenom, email, mdp);
        administrateurs.add(admin);
        System.out.println("Inscription réussie. Bienvenue, " + prenom + " !");
    }

    private static void menuEtudiant(Scanner sc, Etudiant etu) {
        int choix = -1;
        while (choix != 10) {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("Bienvenue " + etu.getPrenom() + "! Vos crédits: " + etu.getCreditsDisponibles() + "\n");
            System.out.println("1. Consulter les demandes d'aide disponibles");
            System.out.println("2. Voir mes demandes d'aide");
            System.out.println("3. Créer une demande d'aide");
            System.out.println("4. Proposer un service");
            System.out.println("5. Voir mes services");
            System.out.println("6. Voir mes transactions");
            System.out.println("7. Rechercher une compétence");
            System.out.println("8. Gérer mes compétences");
            System.out.println("9. Gérer mon profil");
            System.out.println("10. Déconnexion");
            System.out.print("> ");

            try {
                choix = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }
            switch (choix) {
                case 1:
                    consulterDemandesAide(sc);
                    break;
                case 2:
                    voirMesDemandes(sc, etu);
                    break;
                case 3:
                    creerDemandeAide(sc, etu);
                    break;
                case 4:
                    proposerService(sc, etu);
                    break;
                case 5:
                    voirMesServices(sc, etu);
                    break;
                case 6:
                    voirTransactions(sc, etu);
                    break;
                case 7:
                    rechercherCompetence(sc);
                    break;
                case 8:
                    gererCompetences(sc, etu);
                    break;
                case 9:
                    etu.modifierProfil();
                    break;
                case 10:
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void voirMesDemandes(Scanner sc, Etudiant etu) {
        System.out.println("\n--- Mes demandes d'aide ---");
        if (etu.getMesDemandes().isEmpty()) {
            System.out.println("Vous n'avez pas encore créé de demande d'aide.");
            return;
        }

        for (DemandeAide d : etu.getMesDemandes()) {
            System.out.println("\nDemande #" + d.getId());
            System.out.println("Titre : " + d.getTitre());
            System.out.println("Description : " + d.getDescription());
            System.out.println("Crédits offerts : " + d.getCreditsOfferts());
            System.out.println("Date limite : " + d.getDateLimite());
            System.out.println("Compétences requises : ");
            for (Competence comp : d.getCompetencesRequises()) {
                if (comp instanceof CompetenceAcademique) {
                    CompetenceAcademique compAcad = (CompetenceAcademique) comp;
                    System.out.println("  - Académique : " + compAcad.getMatiere() + " (Note : "
                            + compAcad.getNoteObtenue() + "/20)");
                } else if (comp instanceof CompetenceTechnique) {
                    CompetenceTechnique compTech = (CompetenceTechnique) comp;
                    System.out.println("  - Technique : " + compTech.getDomaineTechnique() +
                            " (Certifications : " + compTech.getCertifications().size() + ")");
                } else if (comp instanceof CompetencePratique) {
                    CompetencePratique compPrat = (CompetencePratique) comp;
                    System.out.println("  - Pratique : " + compPrat.getPortfolio() +
                            " (Expérience : " + compPrat.getExperiencePratique() + " mois)");
                }
            }
            System.out.println("Statut : " + d.getStatut());

            // Afficher les réponses si la demande est en cours
            if (d.getStatut().equals("En cours")) {
                System.out.println("\nRéponses reçues :");
                for (Transaction t : transactions) {
                    if (t.getService().getTitre().equals(d.getTitre())) {
                        System.out.println("- " + t.getFournisseur().getPrenom() + " " +
                                t.getFournisseur().getNom() + " a accepté votre demande");
                    }
                }
            }
        }

        System.out.println("\nOptions :");
        System.out.println("1. Modifier une demande");
        System.out.println("2. Supprimer une demande");
        System.out.println("3. Retour au menu principal");
        System.out.print("Votre choix : ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        switch (choix) {
            case 1:
                System.out.print("Entrez l'ID de la demande à modifier : ");
                int idModif = 0;
                try {
                    idModif = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                DemandeAide demandeAModifier = etu.getDemande(idModif);
                if (demandeAModifier != null) {
                    if (demandeAModifier.modifierDemande(sc)) {
                        System.out.println("Demande modifiée avec succès !");
                    }
                } else {
                    System.out.println("Demande non trouvée.");
                }
                break;

            case 2:
                System.out.print("Entrez l'ID de la demande à supprimer : ");
                int idSuppr = 0;
                try {
                    idSuppr = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                DemandeAide demandeASupprimer = etu.getDemande(idSuppr);
                if (demandeASupprimer != null) {
                    if (demandeASupprimer.getStatut().equals("en attente")) {
                        // Supprimer la demande de la liste de l'étudiant
                        etu.supprimerDemande(idSuppr);
                        // Supprimer la demande de la liste globale
                        demandesAide.remove(demandeASupprimer);
                        System.out.println("Demande supprimée avec succès !");
                    } else {
                        System.out.println("Impossible de supprimer cette demande car elle n'est pas en attente.");
                    }
                } else {
                    System.out.println("Demande non trouvée.");
                }
                break;
        }

        System.out.println("\nAppuyez sur Entrée pour continuer...");
        sc.nextLine();
    }

    private static void consulterDemandesAide(Scanner sc) {
        System.out.println("--- Liste des demandes d'aide disponibles ---");
        if (demandesAide.isEmpty()) {
            System.out.println("Aucune demande d'aide disponible.");
            return;
        }

        // Filtrer pour n'afficher que les demandes ouvertes
        List<DemandeAide> demandesOuvertes = demandesAide.stream()
                .filter(d -> d.getStatut().equals("en attente"))
                .collect(Collectors.toList());

        if (demandesOuvertes.isEmpty()) {
            System.out.println("Aucune demande d'aide en attente disponible.");
            return;
        }

        for (int i = 0; i < demandesOuvertes.size(); i++) {
            DemandeAide d = demandesOuvertes.get(i);
            String demandeur = d.getDemandeur() != null ? d.getDemandeur().getPrenom() + " " + d.getDemandeur().getNom()
                    : "?";
            System.out.println((i + 1) + ". " + d.getTitre() + " - " + d.getDescription() +
                    " (Crédits offerts : " + d.getCreditsOfferts() + ", Demandeur : " + demandeur + ")");
        }

        System.out.print("Sélectionnez une demande pour voir les détails (0 pour revenir) : ");
        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        if (choix > 0 && choix <= demandesOuvertes.size()) {
            DemandeAide d = demandesOuvertes.get(choix - 1);
            System.out.println("\nDétails de la demande :");
            System.out.println("Titre : " + d.getTitre());
            System.out.println("Description : " + d.getDescription());
            System.out.println("Crédits offerts : " + d.getCreditsOfferts());
            System.out.println("Date limite : " + d.getDateLimite());
            System.out.println("Compétences requises : ");
            for (Competence comp : d.getCompetencesRequises()) {
                if (comp instanceof CompetenceAcademique) {
                    CompetenceAcademique compAcad = (CompetenceAcademique) comp;
                    System.out.println("  - Académique : " + compAcad.getMatiere() + " (Note : "
                            + compAcad.getNoteObtenue() + "/20)");
                } else if (comp instanceof CompetenceTechnique) {
                    CompetenceTechnique compTech = (CompetenceTechnique) comp;
                    System.out.println("  - Technique : " + compTech.getDomaineTechnique() +
                            " (Certifications : " + compTech.getCertifications().size() + ")");
                } else if (comp instanceof CompetencePratique) {
                    CompetencePratique compPrat = (CompetencePratique) comp;
                    System.out.println("  - Pratique : " + compPrat.getPortfolio() +
                            " (Expérience : " + compPrat.getExperiencePratique() + " mois)");
                }
            }
            System.out.println("Statut : " + d.getStatut());
            String demandeur = d.getDemandeur() != null ? d.getDemandeur().getPrenom() + " " + d.getDemandeur().getNom()
                    : "?";
            System.out.println("Demandeur : " + demandeur);

            System.out.println("\nVoulez-vous répondre à cette demande ? (o/n)");
            String reponse = sc.nextLine().toLowerCase();
            if (reponse.equals("o")) {
                System.out.println("Entrez votre email pour confirmer votre identité : ");
                String email = sc.nextLine();
                Etudiant fournisseur = null;
                for (Etudiant e : etudiants) {
                    if (e.getEmail().equals(email)) {
                        fournisseur = e;
                        break;
                    }
                }

                if (fournisseur != null) {
                    // Créer un service temporaire pour la transaction
                    Service service = new Service(nextServiceId++, d.getTitre(), d.getDescription(),
                            d.getCreditsOfferts(), fournisseur, 0, "En cours");
                    services.add(service);

                    // Créer la transaction
                    Transaction transaction = creerTransaction(sc, d.getDemandeur(), service, fournisseur);
                    // Enregistrer la transaction dans l'historique
                    HistoriqueTransactions historique = new HistoriqueTransactions();
                    historique.enregistrer(transaction);
                    historiqueTransactions.add(historique);

                    // Mettre à jour les crédits
                    d.getDemandeur()
                            .setCreditsDisponibles(d.getDemandeur().getCreditsDisponibles() - d.getCreditsOfferts());
                    fournisseur.setCreditsDisponibles(fournisseur.getCreditsDisponibles() + d.getCreditsOfferts());

                    // Mettre à jour le statut de la demande
                    d.setStatut("En cours");

                    System.out.println("Transaction créée avec succès !");
                } else {
                    System.out.println("Email non trouvé. Veuillez vous connecter d'abord.");
                }
            }
        }
    }

    private static void creerDemandeAide(Scanner sc, Etudiant etu) {
        DemandeAide demande = etu.demanderAide(sc, nextDemandeId++);
        demandesAide.add(demande);
        System.out.println("Demande d'aide créée avec succès !");
    }

    private static void proposerService(Scanner sc, Etudiant fournisseur) {
        Service service = fournisseur.proposerService(sc, nextServiceId++);
        services.add(service);

        System.out.println("Service proposé avec succès !");

        // Afficher le catalogue personnel mis à jour
        System.out.println("\nVotre catalogue de services :");
        for (Service s : fournisseur.getMesServices()) {
            System.out.println("- " + s.getTitre() + " (" + s.getCoutCredits() + " crédits) - " + s.getStatut());
        }
    }

    private static void voirTransactions(Scanner sc, Etudiant etu) {
        System.out.println("--- Mes transactions ---");
        boolean aDesTransactions = false;

        // Afficher les transactions en cours
        System.out.println("\nTransactions en cours :");
        for (Transaction t : transactions) {
            if (t.getDemandeur().getId() == etu.getId() || t.getFournisseur().getId() == etu.getId()) {
                aDesTransactions = true;
                System.out.println("\nTransaction #" + t.getId());
                System.out.println("Date : " + t.getDate());
                System.out.println("Service : " + t.getService().getTitre());
                System.out.println("Crédits échangés : " + t.getCreditsEchanges());
                System.out.println("Statut : " + t.getStatut());

                if (t.getDemandeur().getId() == etu.getId()) {
                    System.out.println("Type : Vous êtes le demandeur de ce service");
                    System.out.println(
                            "Fournisseur : " + t.getFournisseur().getPrenom() + " " + t.getFournisseur().getNom());
                    System.out.println("Service demandé : " + t.getService().getTitre());
                } else {
                    System.out.println("Type : Vous êtes le fournisseur de ce service");
                    System.out.println("Demandeur : " + t.getDemandeur().getPrenom() + " " + t.getDemandeur().getNom());
                    System.out.println("Service fourni : " + t.getService().getTitre());
                }
            }
        }

        if (!aDesTransactions) {
            System.out.println("Vous n'avez aucune transaction.");
        }

        System.out.println("\nAppuyez sur Entrée pour revenir au menu principal...");
        sc.nextLine();
    }

    private static Transaction creerTransaction(Scanner sc, Etudiant demandeur, Service service, Etudiant fournisseur) {
        // Créer la transaction
        Transaction transaction = new Transaction(nextTransactionId++, new Date(),
                service, demandeur, fournisseur, service.getCoutCredits(), "En cours");
        transactions.add(transaction);

        // Mettre à jour les crédits
        demandeur.setCreditsDisponibles(demandeur.getCreditsDisponibles() - service.getCoutCredits());
        fournisseur.setCreditsDisponibles(fournisseur.getCreditsDisponibles() + service.getCoutCredits());

        // Mettre à jour le statut du service
        service.setStatut("En cours");

        System.out.println("Transaction créée avec succès !");
        return transaction;
    }

    private static void rechercherCompetence(Scanner sc) {
        System.out.println("--- Recherche de compétences ---");
        System.out.println("1. Rechercher par type de compétence");
        System.out.println("2. Rechercher par niveau");
        System.out.println("3. Rechercher par filière");
        System.out.print("Votre choix : ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        // Demander le nombre de crédits à offrir
        System.out.print("\nCombien de crédits souhaitez-vous offrir ? ");
        int creditsOfferts = 0;
        try {
            creditsOfferts = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        // Demander l'email du demandeur
        System.out.print("\nEntrez votre email pour confirmer votre identité : ");
        String email = sc.nextLine();
        Etudiant demandeur = null;
        for (Etudiant e : etudiants) {
            if (e.getEmail().equals(email)) {
                demandeur = e;
                break;
            }
        }

        if (demandeur == null) {
            System.out.println("Email non trouvé. Veuillez vous connecter d'abord.");
            return;
        }

        switch (choix) {
            case 1:
                System.out.println("\nTypes de compétences disponibles :");
                System.out.println("1. Académique (cours, examens)");
                System.out.println("2. Technique (programmation, logiciels)");
                System.out.println("3. Pratique (projets, travaux pratiques)");
                System.out.print("Choisissez un type : ");
                int type = 0;
                try {
                    type = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                if (type < 1 || type > 3) {
                    System.out.println("Type de compétence invalide. Veuillez choisir entre 1 et 3.");
                    break;
                }

                System.out.println("\nÉtudiants disponibles avec ce type de compétence :");
                boolean foundStudents = false;
                for (Etudiant e : etudiants) {
                    boolean aDesCompetences = false;

                    if (type == 1 && !e.getCompetencesAcademiques().isEmpty()) {
                        System.out.println("- " + e.getPrenom() + " " + e.getNom() +
                                " (Niveau " + e.getNiveau() + ", " + e.getFiliere() + ")");
                        System.out.println("  Compétences académiques :");
                        for (CompetenceAcademique comp : e.getCompetencesAcademiques()) {
                            System.out.println("  * " + comp.getMatiere() +
                                    " (Note : " + comp.getNoteObtenue() + "/20)");
                        }
                        aDesCompetences = true;
                        foundStudents = true;
                    } else if (type == 2 && !e.getCompetencesTechniques().isEmpty()) {
                        System.out.println("- " + e.getPrenom() + " " + e.getNom() +
                                " (Niveau " + e.getNiveau() + ", " + e.getFiliere() + ")");
                        System.out.println("  Compétences techniques :");
                        for (CompetenceTechnique comp : e.getCompetencesTechniques()) {
                            System.out.println("  * " + comp.getDomaineTechnique() +
                                    " (Certifications : " + comp.getCertifications().size() + ")");
                        }
                        aDesCompetences = true;
                        foundStudents = true;
                    } else if (type == 3 && !e.getCompetencesPratiques().isEmpty()) {
                        System.out.println("- " + e.getPrenom() + " " + e.getNom() +
                                " (Niveau " + e.getNiveau() + ", " + e.getFiliere() + ")");
                        System.out.println("  Compétences pratiques :");
                        for (CompetencePratique comp : e.getCompetencesPratiques()) {
                            System.out.println("  * " + comp.getPortfolio() +
                                    " (Expérience : " + comp.getExperiencePratique() + " mois)");
                        }
                        aDesCompetences = true;
                        foundStudents = true;
                    }

                    if (aDesCompetences) {
                        System.out.println("  Services proposés :");
                        for (Service s : e.getMesServices()) {
                            if (s.getStatut().equals("en attente")) {
                                System.out.println("  * " + s.getTitre() +
                                        " (" + s.getCoutCredits() + " crédits)");
                                if (s.getCoutCredits() <= creditsOfferts) {
                                    System.out.println("    Votre offre est suffisante");
                                } else {
                                    System.out.println("    Votre offre est insuffisante (manque " +
                                            (s.getCoutCredits() - creditsOfferts) + " crédits)");
                                }
                            }
                        }
                    }
                }
                if (!foundStudents) {
                    System.out.println("Aucun étudiant trouvé avec ce type de compétence.");
                }
                break;

            case 2:
                System.out.print("\nEntrez le niveau minimum recherché : ");
                int niveauMin = 0;
                try {
                    niveauMin = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                if (niveauMin <= 0) {
                    System.out.println("Niveau invalide. Le niveau doit être un nombre positif.");
                    break;
                }

                System.out.println("\nÉtudiants de niveau " + niveauMin + " ou supérieur :");
                boolean foundStudentsNiveau = false;
                for (Etudiant e : etudiants) {
                    if (e.getNiveau() >= niveauMin) {
                        foundStudentsNiveau = true;
                        System.out.println("- " + e.getPrenom() + " " + e.getNom() +
                                " (Niveau " + e.getNiveau() + ", " + e.getFiliere() + ")");

                        if (!e.getCompetencesAcademiques().isEmpty()) {
                            System.out.println("  Compétences académiques :");
                            for (CompetenceAcademique comp : e.getCompetencesAcademiques()) {
                                System.out.println("  * " + comp.getMatiere() +
                                        " (Note : " + comp.getNoteObtenue() + "/20)");
                            }
                        }

                        if (!e.getCompetencesTechniques().isEmpty()) {
                            System.out.println("  Compétences techniques :");
                            for (CompetenceTechnique comp : e.getCompetencesTechniques()) {
                                System.out.println("  * " + comp.getDomaineTechnique() +
                                        " (Certifications : " + comp.getCertifications().size() + ")");
                            }
                        }

                        if (!e.getCompetencesPratiques().isEmpty()) {
                            System.out.println("  Compétences pratiques :");
                            for (CompetencePratique comp : e.getCompetencesPratiques()) {
                                System.out.println("  * " + comp.getPortfolio() +
                                        " (Expérience : " + comp.getExperiencePratique() + " mois)");
                            }
                        }

                        System.out.println("  Services proposés :");
                        for (Service s : e.getMesServices()) {
                            System.out.println("  * " + s.getTitre() +
                                    " (" + s.getCoutCredits() + " crédits)");
                            if (s.getCoutCredits() <= creditsOfferts) {
                                System.out.println("    Votre offre est suffisante");
                            } else {
                                System.out.println("    Votre offre est insuffisante (manque " +
                                        (s.getCoutCredits() - creditsOfferts) + " crédits)");
                            }
                        }
                    }
                }
                if (!foundStudentsNiveau) {
                    System.out.println("Aucun étudiant trouvé avec ce niveau ou supérieur.");
                }
                break;

            case 3:
                System.out.print("\nEntrez la filière recherchée : ");
                String filiere = sc.nextLine().toLowerCase();

                System.out.println("\nÉtudiants de la filière " + filiere + " :");
                boolean foundStudentsFiliere = false;
                for (Etudiant e : etudiants) {
                    if (e.getFiliere().toLowerCase().contains(filiere)) {
                        foundStudentsFiliere = true;
                        System.out.println("- " + e.getPrenom() + " " + e.getNom() +
                                " (Niveau " + e.getNiveau() + ")");

                        if (!e.getCompetencesAcademiques().isEmpty()) {
                            System.out.println("  Compétences académiques :");
                            for (CompetenceAcademique comp : e.getCompetencesAcademiques()) {
                                System.out.println("  * " + comp.getMatiere() +
                                        " (Note : " + comp.getNoteObtenue() + "/20)");
                            }
                        }

                        if (!e.getCompetencesTechniques().isEmpty()) {
                            System.out.println("  Compétences techniques :");
                            for (CompetenceTechnique comp : e.getCompetencesTechniques()) {
                                System.out.println("  * " + comp.getDomaineTechnique() +
                                        " (Certifications : " + comp.getCertifications().size() + ")");
                            }
                        }

                        if (!e.getCompetencesPratiques().isEmpty()) {
                            System.out.println("  Compétences pratiques :");
                            for (CompetencePratique comp : e.getCompetencesPratiques()) {
                                System.out.println("  * " + comp.getPortfolio() +
                                        " (Expérience : " + comp.getExperiencePratique() + " mois)");
                            }
                        }

                        System.out.println("  Services proposés :");
                        for (Service s : e.getMesServices()) {
                            System.out.println("  * " + s.getTitre() +
                                    " (" + s.getCoutCredits() + " crédits)");
                            if (s.getCoutCredits() <= creditsOfferts) {
                                System.out.println("    Votre offre est suffisante");
                            } else {
                                System.out.println("    Votre offre est insuffisante (manque " +
                                        (s.getCoutCredits() - creditsOfferts) + " crédits)");
                            }
                        }
                    }
                }
                if (!foundStudentsFiliere) {
                    System.out.println("Aucun étudiant trouvé dans cette filière.");
                }
                break;

            default:
                System.out.println("Choix invalide.");
        }

        System.out.println("\nAppuyez sur Entrée pour revenir au menu principal...");
        sc.nextLine();
    }

    private static void gererCompetences(Scanner sc, Etudiant etu) {
        System.out.println("\n--- Gestion des compétences ---");
        System.out.println("1. Ajouter une compétence académique");
        System.out.println("2. Ajouter une compétence technique");
        System.out.println("3. Ajouter une compétence pratique");
        System.out.println("4. Voir mes compétences");
        System.out.println("5. Retour au menu principal");
        System.out.print("Votre choix : ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        switch (choix) {
            case 1:
                System.out.print("Matière : ");
                String matiere = sc.nextLine();
                System.out.print("Note obtenue (sur 20) : ");
                double note = 0;
                try {
                    note = Double.parseDouble(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                CompetenceAcademique compAcad = new CompetenceAcademique();
                compAcad.setMatiere(matiere);
                compAcad.setNoteObtenue(note);
                etu.ajouterCompetenceAcademique(compAcad);
                System.out.println("Compétence académique ajoutée avec succès !");
                break;

            case 2:
                System.out.print("Domaine technique : ");
                String domaine = sc.nextLine();
                System.out.print("Nombre de certifications : ");
                int nbCertifs = 0;
                try {
                    nbCertifs = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                CompetenceTechnique compTech = new CompetenceTechnique();
                compTech.setDomaineTechnique(domaine);
                for (int i = 0; i < nbCertifs; i++) {
                    System.out.print("Nom de la certification " + (i + 1) + " : ");
                    compTech.ajouterCertification(sc.nextLine());
                }
                etu.ajouterCompetenceTechnique(compTech);
                System.out.println("Compétence technique ajoutée avec succès !");
                break;

            case 3:
                System.out.print("Description du portfolio : ");
                String portfolio = sc.nextLine();
                System.out.print("Expérience pratique (en mois) : ");
                int experience = 0;
                try {
                    experience = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                CompetencePratique compPrat = new CompetencePratique();
                compPrat.setPortfolio(portfolio);
                compPrat.setExperiencePratique(experience);
                etu.ajouterCompetencePratique(compPrat);
                System.out.println("Compétence pratique ajoutée avec succès !");
                break;

            case 4:
                System.out.println("\nVos compétences :");

                if (!etu.getCompetencesAcademiques().isEmpty()) {
                    System.out.println("\nCompétences académiques :");
                    for (CompetenceAcademique comp : etu.getCompetencesAcademiques()) {
                        System.out.println("- " + comp.getMatiere() + " (Note : " + comp.getNoteObtenue() + "/20)");
                    }
                }

                if (!etu.getCompetencesTechniques().isEmpty()) {
                    System.out.println("\nCompétences techniques :");
                    for (CompetenceTechnique comp : etu.getCompetencesTechniques()) {
                        System.out.println("- " + comp.getDomaineTechnique() +
                                " (Certifications : " + comp.getCertifications().size() + ")");
                    }
                }

                if (!etu.getCompetencesPratiques().isEmpty()) {
                    System.out.println("\nCompétences pratiques :");
                    for (CompetencePratique comp : etu.getCompetencesPratiques()) {
                        System.out.println("- " + comp.getPortfolio() +
                                " (Expérience : " + comp.getExperiencePratique() + " mois)");
                    }
                }

                if (etu.getCompetencesAcademiques().isEmpty() &&
                        etu.getCompetencesTechniques().isEmpty() &&
                        etu.getCompetencesPratiques().isEmpty()) {
                    System.out.println("Vous n'avez pas encore ajouté de compétences.");
                }
                break;

            case 5:
                return;

            default:
                System.out.println("Choix invalide.");
        }

        System.out.println("\nAppuyez sur Entrée pour continuer...");
        sc.nextLine();
    }

    private static void voirMesServices(Scanner sc, Etudiant etu) {
        System.out.println("\n--- Mes services ---");
        if (etu.getMesServices().isEmpty()) {
            System.out.println("Vous n'avez pas encore proposé de service.");
            return;
        }

        for (Service s : etu.getMesServices()) {
            System.out.println("\nService #" + s.getId());
            System.out.println("Titre : " + s.getTitre());
            System.out.println("Description : " + s.getDescription());
            System.out.println("Coût en crédits : " + s.getCoutCredits());
            System.out.println("Durée estimée : " + s.getDuree() + " heures");
            System.out.println("Statut : " + s.getStatut());
        }

        System.out.println("\nOptions :");
        System.out.println("1. Modifier un service");
        System.out.println("2. Supprimer un service");
        System.out.println("3. Retour au menu principal");
        System.out.print("Votre choix : ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        switch (choix) {
            case 1:
                System.out.print("Entrez l'ID du service à modifier : ");
                int idModif = 0;
                try {
                    idModif = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                Service serviceAModifier = etu.getService(idModif);
                if (serviceAModifier != null) {
                    if (serviceAModifier.modifierService()) {
                        System.out.println("Nouveau titre (laisser vide pour ne pas changer) : ");
                        String nvTitre = sc.nextLine();
                        if (!nvTitre.isEmpty())
                            serviceAModifier.setTitre(nvTitre);

                        System.out.println("Nouvelle description (laisser vide pour ne pas changer) : ");
                        String nvDesc = sc.nextLine();
                        if (!nvDesc.isEmpty())
                            serviceAModifier.setDescription(nvDesc);

                        System.out.println("Nouveau coût en crédits (0 pour ne pas changer) : ");
                        int nvCout = 0;
                        try {
                            nvCout = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                        }
                        if (nvCout > 0)
                            serviceAModifier.setCoutCredits(nvCout);

                        System.out.println("Nouvelle durée estimée (0 pour ne pas changer) : ");
                        int nvDuree = 0;
                        try {
                            nvDuree = Integer.parseInt(sc.nextLine());
                        } catch (NumberFormatException e) {
                        }
                        if (nvDuree > 0)
                            serviceAModifier.setDuree(nvDuree);

                        System.out.println("Service modifié avec succès !");
                    }
                } else {
                    System.out.println("Service non trouvé.");
                }
                break;

            case 2:
                System.out.print("Entrez l'ID du service à supprimer : ");
                int idSuppr = 0;
                try {
                    idSuppr = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                Service serviceASupprimer = etu.getService(idSuppr);
                if (serviceASupprimer != null) {
                    if (serviceASupprimer.supprimerService()) {
                        etu.supprimerService(idSuppr);
                        services.remove(serviceASupprimer);
                        System.out.println("Service supprimé avec succès !");
                    }
                } else {
                    System.out.println("Service non trouvé.");
                }
                break;
        }

        System.out.println("\nAppuyez sur Entrée pour continuer...");
        sc.nextLine();
    }

    private static void menuAdmin(Scanner sc, Administrateur admin) {
        int choix = -1;
        while (choix != 5) {
            System.out.println("\n==== MENU ADMINISTRATEUR ====");
            System.out.println("1. Gérer les utilisateurs");
            System.out.println("2. Voir toutes les transactions");
            System.out.println("3. Voir les statistiques");
            System.out.println("4. Créer un nouvel administrateur");
            System.out.println("5. Déconnexion");
            System.out.print("> ");
            try {
                choix = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                choix = -1;
            }
            switch (choix) {
                case 1:
                    gererUtilisateurs(sc);
                    break;
                case 2:
                    voirTransactions(sc);
                    break;
                case 3:
                    voirStatistiques(sc);
                    break;
                case 4:
                    creerAdmin(sc);
                    break;
                case 5:
                    System.out.println("Déconnexion...");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    private static void gererUtilisateurs(Scanner sc) {
        System.out.println("\n--- Gestion des utilisateurs ---");
        System.out.println("1. Voir tous les utilisateurs");
        System.out.println("2. Supprimer un utilisateur");
        System.out.println("3. Voir les statistiques des utilisateurs");
        System.out.println("4. Retour au menu administrateur");
        System.out.print("> ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        switch (choix) {
            case 1:
                System.out.println("\nListe des utilisateurs :");
                for (Etudiant e : etudiants) {
                    System.out.println("\nID : " + e.getId());
                    System.out.println("Nom : " + e.getNom());
                    System.out.println("Prénom : " + e.getPrenom());
                    System.out.println("Email : " + e.getEmail());
                    System.out.println("Filière : " + e.getFiliere());
                    System.out.println("Niveau : " + e.getNiveau());
                    System.out.println("Crédits : " + e.getCreditsDisponibles());
                }
                break;

            case 2:
                supprimerUtilisateur(sc);
                break;
            case 3:
                voirStatistiques(sc);
                break;
            case 4:
                return;
        }
    }

    private static void supprimerUtilisateur(Scanner sc) {
        System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
        int idSupprimer = 0;
        try {
            idSupprimer = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        Etudiant etuASupprimer = null;
        for (Etudiant e : etudiants) {
            if (e.getId() == idSupprimer) {
                etuASupprimer = e;
                break;
            }
        }

        if (etuASupprimer != null) {
            // Vérifier si l'utilisateur a des transactions en cours
            boolean aTransactionsEnCours = false;
            for (Transaction t : transactions) {
                if ((t.getDemandeur().getId() == etuASupprimer.getId() ||
                        t.getFournisseur().getId() == etuASupprimer.getId()) &&
                        t.getStatut().equals("En cours")) {
                    aTransactionsEnCours = true;
                    break;
                }
            }

            if (aTransactionsEnCours) {
                System.out.println("Impossible de supprimer l'utilisateur car il a des transactions en cours.");
                return;
            }

            // Supprimer les demandes d'aide de l'utilisateur
            List<DemandeAide> demandesASupprimer = new ArrayList<>();
            for (DemandeAide d : demandesAide) {
                if (d.getDemandeur().getId() == etuASupprimer.getId()) {
                    demandesASupprimer.add(d);
                }
            }
            demandesAide.removeAll(demandesASupprimer);

            // Supprimer les services de l'utilisateur
            List<Service> servicesASupprimer = new ArrayList<>();
            for (Service s : services) {
                if (s.getFournisseur().getId() == etuASupprimer.getId()) {
                    servicesASupprimer.add(s);
                }
            }
            services.removeAll(servicesASupprimer);

            // Supprimer les transactions de l'utilisateur
            List<Transaction> transactionsASupprimer = new ArrayList<>();
            for (Transaction t : transactions) {
                if (t.getDemandeur().getId() == etuASupprimer.getId() ||
                        t.getFournisseur().getId() == etuASupprimer.getId()) {
                    transactionsASupprimer.add(t);
                }
            }
            transactions.removeAll(transactionsASupprimer);

            // Supprimer l'utilisateur
            etudiants.remove(etuASupprimer);
            System.out.println("Utilisateur et toutes ses données associées ont été supprimés avec succès.");
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    private static void voirTransactions(Scanner sc) {
        System.out.println("\n--- Toutes les transactions ---");
        System.out.println("1. Voir toutes les transactions");
        System.out.println("2. Filtrer par statut");
        System.out.println("3. Filtrer par date");
        System.out.println("4. Retour au menu administrateur");
        System.out.print("> ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        switch (choix) {
            case 1:
                for (Transaction t : transactions) {
                    afficherTransaction(t);
                }
                break;

            case 2:
                System.out.println("\nStatuts disponibles :");
                System.out.println("1. En cours");
                System.out.println("2. en attente");
                System.out.println("3. terminée");
                System.out.print("Choisissez un statut : ");

                int statut = 0;
                try {
                    statut = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                }

                String statutRecherche = "";
                switch (statut) {
                    case 1:
                        statutRecherche = "En cours";
                        break;
                    case 2:
                        statutRecherche = "en attente";
                        break;
                    case 3:
                        statutRecherche = "terminée";
                        break;
                }

                for (Transaction t : transactions) {
                    if (t.getStatut().equals(statutRecherche)) {
                        afficherTransaction(t);
                    }
                }
                break;

            case 3:
                System.out.print("Date de début (JJ/MM/AAAA) : ");
                String dateDebut = sc.nextLine();
                System.out.print("Date de fin (JJ/MM/AAAA) : ");
                String dateFin = sc.nextLine();

                // Convertir les dates et filtrer les transactions
                // À implémenter avec la gestion des dates
                break;
        }
    }

    private static void afficherTransaction(Transaction t) {
        System.out.println("\nTransaction #" + t.getId());
        System.out.println("Date : " + t.getDate());
        System.out.println("Service : " + t.getService().getTitre());
        System.out.println("Crédits échangés : " + t.getCreditsEchanges());
        System.out.println("Statut : " + t.getStatut());
        System.out.println("Demandeur : " + t.getDemandeur().getPrenom() + " " + t.getDemandeur().getNom());
        System.out.println("Fournisseur : " + t.getFournisseur().getPrenom() + " " + t.getFournisseur().getNom());
    }

    private static void voirStatistiques(Scanner sc) {
        System.out.println("\n--- Statistiques ---");
        System.out.println("1. Statistiques générales");
        System.out.println("2. Top fournisseurs");
        System.out.println("3. Top demandeurs");
        System.out.println("4. Retour au menu administrateur");
        System.out.print("> ");

        int choix = 0;
        try {
            choix = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        switch (choix) {
            case 1:
                System.out.println("\nStatistiques générales :");
                System.out.println("Total des transactions : " + transactions.size());
                int enCours = 0, enAttente = 0, terminees = 0;
                for (Transaction t : transactions) {
                    switch (t.getStatut()) {
                        case "En cours":
                            enCours++;
                            break;
                        case "en attente":
                            enAttente++;
                            break;
                        case "terminée":
                            terminees++;
                            break;
                    }
                }
                System.out.println("Transactions en cours : " + enCours);
                System.out.println("Transactions en attente : " + enAttente);
                System.out.println("Transactions terminées : " + terminees);
                break;

            case 2:
                System.out.println("\nTop fournisseurs :");
                Map<Etudiant, Integer> topFournisseurs = new HashMap<>();
                for (Transaction t : transactions) {
                    Etudiant fournisseur = t.getFournisseur();
                    topFournisseurs.put(fournisseur, topFournisseurs.getOrDefault(fournisseur, 0) + 1);
                }
                topFournisseurs.entrySet().stream()
                        .sorted(Map.Entry.<Etudiant, Integer>comparingByValue().reversed())
                        .limit(5)
                        .forEach(entry -> System.out.println(entry.getKey().getPrenom() + " " +
                                entry.getKey().getNom() + " : " + entry.getValue() + " services"));
                break;

            case 3:
                System.out.println("\nTop demandeurs :");
                Map<Etudiant, Integer> topDemandeurs = new HashMap<>();
                for (Transaction t : transactions) {
                    Etudiant demandeur = t.getDemandeur();
                    topDemandeurs.put(demandeur, topDemandeurs.getOrDefault(demandeur, 0) + 1);
                }
                topDemandeurs.entrySet().stream()
                        .sorted(Map.Entry.<Etudiant, Integer>comparingByValue().reversed())
                        .limit(5)
                        .forEach(entry -> System.out.println(entry.getKey().getPrenom() + " " +
                                entry.getKey().getNom() + " : " + entry.getValue() + " demandes"));
                break;
        }
    }

    private static void creerAdmin(Scanner sc) {
        System.out.println("\n--- Création d'un administrateur ---");
        System.out.print("Nom : ");
        String nom = sc.nextLine();
        System.out.print("Prénom : ");
        String prenom = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = sc.nextLine();

        Administrateur admin = new Administrateur(nextId++, nom, prenom, email, mdp);
        administrateurs.add(admin);
        System.out.println("Administrateur créé avec succès !");
    }
}