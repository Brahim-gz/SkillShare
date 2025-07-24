package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class DemandeAide implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String titre;
    private String description;
    private int creditsOfferts;
    private Date dateLimite;
    private List<Competence> competencesRequises;
    private String statut;
    private Etudiant demandeur;

    public DemandeAide(int id, String titre, String description, int creditsOfferts, Date dateLimite,
            List<Competence> competencesRequises, String statut, Etudiant demandeur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.creditsOfferts = creditsOfferts;
        this.dateLimite = dateLimite;
        this.competencesRequises = competencesRequises;
        this.statut = statut;
        this.demandeur = demandeur;
    }

    public static DemandeAide creerDemandeAide(Scanner sc, Etudiant demandeur, int id) {
        System.out.println("--- Créer une demande d'aide ---");
        System.out.print("Titre : ");
        String titre = sc.nextLine();
        System.out.print("Description : ");
        String description = sc.nextLine();
        System.out.print("Crédits offerts (valeur suggérée : 10) : ");
        int credits = 10;
        try {
            credits = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }

        // Saisie de la date limite
        Date dateLimite = null;
        boolean dateValide = false;
        while (!dateValide) {
            try {
                System.out.println("\nDate limite de la demande :");
                System.out.print("Jour (1-31) : ");
                int jour = Integer.parseInt(sc.nextLine());
                System.out.print("Mois (1-12) : ");
                int mois = Integer.parseInt(sc.nextLine()) - 1; // Les mois commencent à 0 en Java
                System.out.print("Année : ");
                int annee = Integer.parseInt(sc.nextLine());

                dateLimite = new Date(annee - 1900, mois, jour); // L'année commence à 1900 en Java
                if (dateLimite.after(new Date())) {
                    dateValide = true;
                } else {
                    System.out.println("La date doit être dans le futur. Veuillez réessayer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Format de date invalide. Veuillez réessayer.");
            }
        }

        // Saisie des compétences requises
        List<Competence> competences = new ArrayList<>();
        System.out.println("\nCompétences requises :");
        System.out.println("1. Compétence académique");
        System.out.println("2. Compétence technique");
        System.out.println("3. Compétence pratique");
        System.out.println("4. Terminer la saisie");

        boolean saisieTerminee = false;
        while (!saisieTerminee) {
            System.out.print("\nChoisissez le type de compétence (1-4) : ");
            int choix = 0;
            try {
                choix = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
            }

            switch (choix) {
                case 1:
                    CompetenceAcademique compAcad = new CompetenceAcademique();
                    System.out.print("Matière : ");
                    compAcad.setMatiere(sc.nextLine());
                    System.out.print("Note minimale requise (sur 20) : ");
                    try {
                        compAcad.setNoteObtenue(Double.parseDouble(sc.nextLine()));
                    } catch (NumberFormatException e) {
                    }
                    competences.add(compAcad);
                    break;

                case 2:
                    CompetenceTechnique compTech = new CompetenceTechnique();
                    System.out.print("Domaine technique : ");
                    compTech.setDomaineTechnique(sc.nextLine());
                    System.out.print("Nombre de certifications requises : ");
                    int nbCertifs = 0;
                    try {
                        nbCertifs = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                    }
                    for (int i = 0; i < nbCertifs; i++) {
                        System.out.print("Nom de la certification " + (i + 1) + " : ");
                        compTech.ajouterCertification(sc.nextLine());
                    }
                    competences.add(compTech);
                    break;

                case 3:
                    CompetencePratique compPrat = new CompetencePratique();
                    System.out.print("Description du portfolio requis : ");
                    compPrat.setPortfolio(sc.nextLine());
                    System.out.print("Expérience minimale requise (en mois) : ");
                    try {
                        compPrat.setExperiencePratique(Integer.parseInt(sc.nextLine()));
                    } catch (NumberFormatException e) {
                    }
                    competences.add(compPrat);
                    break;

                case 4:
                    saisieTerminee = true;
                    break;

                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }

        String statut = "en attente";
        return new DemandeAide(id, titre, description, credits, dateLimite, competences, statut, demandeur);
    }

    public boolean modifierDemande(Scanner sc) {
        if (!statut.equals("en attente")) {
            System.out.println("Impossible de modifier une demande qui n'est pas en attente.");
            return false;
        }

        System.out.println("\nModification de la demande #" + id);
        System.out.println("Titre actuel : " + titre);
        System.out.print("Nouveau titre (laisser vide pour ne pas changer) : ");
        String nvTitre = sc.nextLine();
        if (!nvTitre.isEmpty())
            this.titre = nvTitre;

        System.out.println("Description actuelle : " + description);
        System.out.print("Nouvelle description (laisser vide pour ne pas changer) : ");
        String nvDesc = sc.nextLine();
        if (!nvDesc.isEmpty())
            this.description = nvDesc;

        System.out.println("Crédits offerts actuels : " + creditsOfferts);
        System.out.print("Nouveaux crédits offerts (0 pour ne pas changer) : ");
        int nvCredits = 0;
        try {
            nvCredits = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
        }
        if (nvCredits > 0)
            this.creditsOfferts = nvCredits;

        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreditsOfferts() {
        return creditsOfferts;
    }

    public void setCreditsOfferts(int creditsOfferts) {
        this.creditsOfferts = creditsOfferts;
    }

    public Date getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    public List<Competence> getCompetencesRequises() {
        return competencesRequises;
    }

    public void setCompetencesRequises(List<Competence> competencesRequises) {
        this.competencesRequises = competencesRequises;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Etudiant getDemandeur() {
        return demandeur;
    }

    public void setDemandeur(Etudiant demandeur) {
        this.demandeur = demandeur;
    }
}