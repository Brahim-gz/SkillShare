package model;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Date date;
    private Service service;
    private Etudiant demandeur;
    private Etudiant fournisseur;
    private int creditsEchanges;
    private String statut;

    public Transaction(int id, Date date, Service service, Etudiant demandeur, Etudiant fournisseur, int creditsEchanges, String statut) {
        this.id = id;
        this.date = date;
        this.service = service;
        this.demandeur = demandeur;
        this.fournisseur = fournisseur;
        this.creditsEchanges = creditsEchanges;
        this.statut = statut;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }
    public Etudiant getDemandeur() { return demandeur; }
    public void setDemandeur(Etudiant demandeur) { this.demandeur = demandeur; }
    public Etudiant getFournisseur() { return fournisseur; }
    public void setFournisseur(Etudiant fournisseur) { this.fournisseur = fournisseur; }
    public int getCreditsEchanges() { return creditsEchanges; }
    public void setCreditsEchanges(int creditsEchanges) { this.creditsEchanges = creditsEchanges; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
} 