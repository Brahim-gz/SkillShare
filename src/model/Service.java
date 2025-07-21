package model;

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