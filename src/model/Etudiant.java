package model;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;
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

    // Getters et setters pour les services
    public List<Service> getMesServices() { return mesServices; }

    public void ajouterService(Service service) { 
        mesServices.add(service);
    }
    public void supprimerService(int serviceId) {
        mesServices.removeIf(s -> s.getId() == serviceId);
    }
    public Service getService(int serviceId) {
        for (Service s : mesServices) {
            if (s.getId() == serviceId) return s;
        }
        return null;
    }

    // Getters et setters pour les compétences
    public List<CompetenceAcademique> getCompetencesAcademiques() { return competencesAcademiques; }
    public void ajouterCompetenceAcademique(CompetenceAcademique comp) { 
        competencesAcademiques.add(comp);
    }
    
    public List<CompetencePratique> getCompetencesPratiques() { return competencesPratiques; }
    public void ajouterCompetencePratique(CompetencePratique comp) { 
        competencesPratiques.add(comp);
    }
    
    public List<CompetenceTechnique> getCompetencesTechniques() { return competencesTechniques; }
    public void ajouterCompetenceTechnique(CompetenceTechnique comp) { 
        competencesTechniques.add(comp);
    }

    // Getters et setters pour les demandes d'aide
    public List<DemandeAide> getMesDemandes() { return mesDemandes; }
    public void ajouterDemande(DemandeAide demande) {
        mesDemandes.add(demande);
    }
    public void supprimerDemande(int demandeId) {
        mesDemandes.removeIf(d -> d.getId() == demandeId);
    }
    public DemandeAide getDemande(int demandeId) {
        for (DemandeAide d : mesDemandes) {
            if (d.getId() == demandeId) return d;
        }
        return null;
    }

    
    public DemandeAide demanderAide(Scanner sc, int id) {
        DemandeAide demande = DemandeAide.creerDemandeAide(sc, this, id);
        mesDemandes.add(demande);
        return demande;
    }
    
    @Override
    public boolean sAuthentifier(String email, String motDePasse) {
        return this.email.equals(email) && this.motDePasse.equals(motDePasse);
    }
    @Override
    public void modifierProfil() {
        Scanner sc = new Scanner(System.in);
        System.out.println("--- Modifier le profil étudiant ---");
        System.out.println("1. Nom actuel : " + nom);
        System.out.print("Nouveau nom (laisser vide pour ne pas changer) : ");
        String nvNom = sc.nextLine();
        if (!nvNom.isEmpty()) this.nom = nvNom;

        System.out.println("2. Prénom actuel : " + prenom);
        System.out.print("Nouveau prénom (laisser vide pour ne pas changer) : ");
        String nvPrenom = sc.nextLine();
        if (!nvPrenom.isEmpty()) this.prenom = nvPrenom;

        System.out.println("3. Email actuel : " + email);
        System.out.print("Nouvel email (laisser vide pour ne pas changer) : ");
        String nvEmail = sc.nextLine();
        if (!nvEmail.isEmpty()) this.email = nvEmail;

        System.out.print("Nouveau mot de passe (laisser vide pour ne pas changer) : ");
        String nvMdp = sc.nextLine();
        if (!nvMdp.isEmpty()) this.motDePasse = nvMdp;

        System.out.println("4. Filière actuelle : " + filiere);
        System.out.print("Nouvelle filière (laisser vide pour ne pas changer) : ");
        String nvFiliere = sc.nextLine();
        if (!nvFiliere.isEmpty()) this.filiere = nvFiliere;

        System.out.println("5. Niveau actuel : " + niveau);
        System.out.print("Nouveau niveau (laisser vide pour ne pas changer) : ");
        String nvNiveau = sc.nextLine();
        if (!nvNiveau.isEmpty()) {
            try { this.niveau = Integer.parseInt(nvNiveau); } catch (NumberFormatException e) {}
        }

        System.out.println("Profil étudiant mis à jour !");
    }

    public Service proposerService(Scanner sc, int id) {
        Service service = Service.creerService(sc, this, id);
        mesServices.add(service);
        return service;
    }
} 