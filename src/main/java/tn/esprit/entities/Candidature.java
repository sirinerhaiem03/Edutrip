package tn.esprit.entities;

public class Candidature {
    private int id;
    private String cv;
    private String lettre_motivation;
    private String diplome;
    private EtatCandidature etat;

    public Candidature() {
    }

    public Candidature(int id, String cv, String lettre_motivation, String diplome, EtatCandidature etat) {
        this.id = id;
        this.cv = cv;
        this.lettre_motivation = lettre_motivation;
        this.diplome = diplome;
        this.etat = etat;
    }

    public Candidature(String cv, String lettre_motivation, String diplome, EtatCandidature etat) {
        this.cv = cv;
        this.lettre_motivation = lettre_motivation;
        this.diplome = diplome;
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getLettre_motivation() {
        return lettre_motivation;
    }

    public void setLettre_motivation(String lettre_motivation) {
        this.lettre_motivation = lettre_motivation;
    }

    public String getDiplome() {
        return diplome;
    }

    public void setDiplome(String diplome) {
        this.diplome = diplome;
    }

    public EtatCandidature getEtat() {
        return etat;
    }

    public void setEtat(EtatCandidature etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", cv='" + cv + '\'' +
                ", lettre_motivation='" + lettre_motivation + '\'' +
                ", diplome='" + diplome + '\'' +
                ", etat=" + etat +
                '}';
    }
}