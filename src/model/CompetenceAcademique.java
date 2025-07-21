package model;

public class CompetenceAcademique extends Competence {
    private String matiere;
    private double noteObtenue;

    // Getters et setters
    public String getMatiere() { return matiere; }
    public void setMatiere(String matiere) { this.matiere = matiere; }
    public double getNoteObtenue() { return noteObtenue; }
    public void setNoteObtenue(double noteObtenue) { this.noteObtenue = noteObtenue; }
} 