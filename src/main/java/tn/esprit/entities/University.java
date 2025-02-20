package tn.esprit.entities;

public class University {

    private int idUniversity;
    private String nom;
    private String ville;
    private String email;
    private String description;

    public University() {
    }

    public University(int idUniversity, String nom, String ville, String email, String description) {
        this.idUniversity = idUniversity;
        this.nom = nom;
        this.ville = ville;
        this.email = email;
        this.description = description;
    }



    public University(String nom, String ville, String email, String description) {
        this.nom = nom;
        this.ville = ville;
        this.email = email;
        this.description = description;
    }

    public int getIdUniversity() {
        return idUniversity;
    }

    public void setIdUniversity(int idUniversity) {
        this.idUniversity = idUniversity;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "University{" +
                "idUniversity=" + idUniversity +
                ", nom='" + nom + '\'' +
                ", ville='" + ville + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
