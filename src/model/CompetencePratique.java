package model;

public class CompetencePratique extends Competence {
    private int experiencePratique; // en mois ou années
    private String portfolio;

    // Getters et setters
    public int getExperiencePratique() { return experiencePratique; }
    public void setExperiencePratique(int experiencePratique) { this.experiencePratique = experiencePratique; }
    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }
} 