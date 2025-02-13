package tn.esprit.tacheuser.models;

import java.sql.Date;

public class Avis {
    private int id;
    private int userId;
    private String commentaire;
    private int note;
    private String dateCreation;

    // Constructeur pour l'ajout (sans ID, avec date automatique)
    public Avis(int userId, String commentaire, int note, Date dateCreation) {
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
        this.dateCreation = new Date(System.currentTimeMillis()).toString();
    }

    // Constructeur pour la mise à jour (avec ID, sans date)
    public Avis(int id, String commentaire, int note) {
        this.id = id;
        this.commentaire = commentaire;
        this.note = note;
    }

    // Constructeur complet (pour récupérer les avis depuis la BDD)
    public Avis(int id, int userId, String commentaire, int note, String dateCreation) {
        this.id = id;
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public int getNote() { return note; }
    public void setNote(int note) { this.note = note; }

    public String getDateCreation() { return dateCreation; }
    public void setDateCreation(String dateCreation) { this.dateCreation = dateCreation; }
}