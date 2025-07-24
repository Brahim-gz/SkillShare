package model;

import java.util.Scanner;
import java.io.Serializable;

public class Service implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String titre;
    private String description;
    private int coutCredits;
    private Etudiant fournisseur;
    private int duree;
    private String statut;

    public Service(int id, String titre, String description, int coutCredits, Etudiant fournisseur, int duree, String statut) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.coutCredits = coutCredits;
        this.fournisseur = fournisseur;
        this.duree = duree;
        this.statut = statut;
    }

    public static Service creerService(Scanner sc, Etudiant fournisseur, int id) {
        System.out.println("--- Proposer un service ---");
        System.out.print("Titre : ");
        String titre = sc.nextLine();
        System.out.print("Description : ");
        String description = sc.nextLine();
        
        int cout = 0;
        boolean coutValide = false;
        do {
            System.out.print("Coût en crédits : ");
            try {
                cout = Integer.parseInt(sc.nextLine());
                if (cout > 0) {
                    coutValide = true;
                } else {
                    System.out.println("Le coût doit être supérieur à 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        } while (!coutValide);
        
        int duree = 0;
        boolean dureeValide = false;
        do {
            System.out.print("Durée estimée (en heures) : ");
            try {
                duree = Integer.parseInt(sc.nextLine());
                if (duree > 0) {
                    dureeValide = true;
                } else {
                    System.out.println("La durée doit être supérieure à 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        } while (!dureeValide);
        
        String statut = "en attente";
        return new Service(id, titre, description, cout, fournisseur, duree, statut);
    }

    public boolean modifierService() {
        if (!statut.equals("en attente")) {
            System.out.println("Impossible de modifier un service qui n'est pas en attente.");
            return false;
        }
        return true;
    }

    public boolean supprimerService() {
        if (!statut.equals("en attente")) {
            System.out.println("Impossible de supprimer un service qui n'est pas en attente.");
            return false;
        }
        return true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getCoutCredits() { return coutCredits; }
    public void setCoutCredits(int coutCredits) { this.coutCredits = coutCredits; }
    public Etudiant getFournisseur() { return fournisseur; }
    public void setFournisseur(Etudiant fournisseur) { this.fournisseur = fournisseur; }
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
} 