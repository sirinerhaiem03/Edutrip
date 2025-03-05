package tn.esprit.entities;

public class Candidature {
    private int id;
    private String cv;
    private String lettre_motivation;
    private String diplome;
    private EtatCandidature etat;
    private User user; // Association with User
    private University university; // Association with University

    public Candidature() {
    }

    public Candidature(int id, String cv, String lettre_motivation, String diplome, EtatCandidature etat, User user, University university) {
        this.id = id;
        this.cv = cv;
        this.lettre_motivation = lettre_motivation;
        this.diplome = diplome;
        this.etat = etat;
        this.user = user;
        this.university = university;
    }

    public Candidature(String cv, String lettre_motivation, String diplome, EtatCandidature etat, User user, University university) {
        this.cv = cv;
        this.lettre_motivation = lettre_motivation;
        this.diplome = diplome;
        this.etat = etat;
        this.user = user;
        this.university = university;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "id=" + id +
                ", cv='" + cv + '\'' +
                ", lettre_motivation='" + lettre_motivation + '\'' +
                ", diplome='" + diplome + '\'' +
                ", etat=" + etat +
                ", user=" + (user != null ? user.getNom() + " " + user.getPrenom() : "null") +
                ", university=" + (university != null ? university.getNom() : "null") +
                '}';
    }
}
