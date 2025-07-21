package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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