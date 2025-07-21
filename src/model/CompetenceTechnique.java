package model;

import java.util.List;
import java.util.ArrayList;

public class CompetenceTechnique extends Competence {
    private String domaineTechnique;
    private List<String> certifications = new ArrayList<>();

    // Getters et setters
    public String getDomaineTechnique() { return domaineTechnique; }
    public void setDomaineTechnique(String domaineTechnique) { this.domaineTechnique = domaineTechnique; }
    public List<String> getCertifications() { return certifications; }
    public void setCertifications(List<String> certifications) { this.certifications = certifications; }
} 