package model;

import service.HistoriqueTransactions;
import java.io.Serializable;
import java.util.Scanner;
import java.util.List;

public class Administrateur extends Personne implements Serializable {
    private static final long serialVersionUID = 1L;

    public Administrateur(int id, String nom, String prenom, String email, String motDePasse) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    // Supervision des transactions
    public void superviserTransactions(HistoriqueTransactions historique) {
        if (historique == null) {
            System.out.println("Aucun historique de transactions disponible.");
            return;
        }

        System.out.println("\n=== Supervision des transactions ===");
        System.out.println("Nombre total de transactions : " + historique.getTransactions().size());

        // Afficher les 5 dernières transactions
        System.out.println("\nDernières transactions :");
        List<Transaction> transactions = historique.getTransactions();
        int startIndex = Math.max(0, transactions.size() - 5);
        for (int i = startIndex; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            System.out.printf("Transaction #%d - %s -> %s\n",
                    t.getId(),
                    t.getDemandeur().getNom(),
                    t.getFournisseur().getNom());
        }
    }

    // Résolution de litige
    public void resoudreLitige(int serviceId, String decision) {
        System.out.println("\n=== Résolution de litige ===");
        System.out.println("Service concerné : " + serviceId);
        System.out.println("Décision : " + decision);
        System.out.println("Date de résolution : " + new java.util.Date());
    }

    @Override
    public boolean sAuthentifier(String email, String motDePasse) {
        return this.email.equals(email) && this.motDePasse.equals(motDePasse);
    }

    @Override
    public void modifierProfil() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Modification du profil administrateur ===");

        System.out.println("1. Nom actuel : " + nom);
        System.out.print("Nouveau nom (laisser vide pour ne pas changer) : ");
        String nvNom = sc.nextLine().trim();
        if (!nvNom.isEmpty())
            this.nom = nvNom;

        System.out.println("\n2. Prénom actuel : " + prenom);
        System.out.print("Nouveau prénom (laisser vide pour ne pas changer) : ");
        String nvPrenom = sc.nextLine().trim();
        if (!nvPrenom.isEmpty())
            this.prenom = nvPrenom;

        System.out.println("\n3. Email actuel : " + email);
        System.out.print("Nouvel email (laisser vide pour ne pas changer) : ");
        String nvEmail = sc.nextLine().trim();
        if (!nvEmail.isEmpty())
            this.email = nvEmail;

        System.out.print("\nNouveau mot de passe (laisser vide pour ne pas changer) : ");
        String nvMdp = sc.nextLine().trim();
        if (!nvMdp.isEmpty())
            this.motDePasse = nvMdp;

        System.out.println("\nProfil administrateur mis à jour avec succès !");
    }
}