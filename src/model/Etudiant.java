package model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Etudiant extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;
    private String filiere;
    private int niveau;
    private int creditsDisponibles;
    private List<Service> mesServices = new ArrayList<>();
    private List<CompetenceAcademique> competencesAcademiques = new ArrayList<>();
    private List<CompetencePratique> competencesPratiques = new ArrayList<>();
    private List<CompetenceTechnique> competencesTechniques = new ArrayList<>();
    private List<DemandeAide> mesDemandes = new ArrayList<>();

    // Constructeur complet
    public Etudiant(int id, String nom, String prenom, String email, String motDePasse, String filiere, int niveau, int creditsDisponibles) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.filiere = filiere;
        this.niveau = niveau;
        this.creditsDisponibles = creditsDisponibles;
    }

    // Getters et setters
    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }
    public int getNiveau() { return niveau; }
    public void setNiveau(int niveau) { this.niveau = niveau; }
    public int getCreditsDisponibles() { return creditsDisponibles; }
    public void setCreditsDisponibles(int creditsDisponibles) { this.creditsDisponibles = creditsDisponibles; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

} 