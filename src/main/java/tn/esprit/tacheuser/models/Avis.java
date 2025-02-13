package tn.esprit.tacheuser.models;

import java.sql.Date;

public class Avis {
    private int id;
    private int userId;
    private String commentaire;
    private int note;
    private String dateCreation;

    // Constructeur avec 4 paramètres (ajout)
    public Avis(int userId, String commentaire, int note) {
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
        this.dateCreation = new Date(System.currentTimeMillis()).toString(); // Date actuelle
    }

    // Constructeur avec 5 paramètres (mise à jour)
    public Avis(int id, int userId, String commentaire, int note, String dateCreation) {
        this.id = id;
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }
}
