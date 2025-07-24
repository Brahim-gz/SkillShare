package model;

import java.io.Serializable;

public abstract class Personne implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse;

    public abstract boolean sAuthentifier(String email, String motDePasse);
    public abstract void modifierProfil();
} 