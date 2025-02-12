package tn.edutrip.entities;

import java.util.Date;

public class Commentaire {
    private int id_commentaire;
    private int id_post;
    private int id_etudiant;
    private String contenu;
    private Date date_commentaire ;

    public Commentaire(int id_commentaire, int id_post, int id_etudiant, String contenu, Date date_commentaire) {
        this.id_commentaire = id_commentaire;
        this.id_post = id_post;
        this.id_etudiant = id_etudiant;
        this.contenu = contenu;
        this.date_commentaire = date_commentaire;
    }

    public int getId_commentaire() {
        return id_commentaire;
    }

    public void setId_commentaire(int id_commentaire) {
        this.id_commentaire = id_commentaire;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate_commentaire() {
        return date_commentaire;
    }

    public void setDate_commentaire(Date date_commentaire) {
        this.date_commentaire = date_commentaire;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id_commentaire=" + id_commentaire +
                ", id_post=" + id_post +
                ", id_etudiant=" + id_etudiant +
                ", contenu='" + contenu + '\'' +
                ", date_commentaire=" + date_commentaire +
                '}';
    }
}
