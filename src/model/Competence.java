package model;

import java.io.Serializable;

public abstract class Competence implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String nom;
    protected String description;
    protected int niveau;
}