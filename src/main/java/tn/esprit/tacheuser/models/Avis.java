package tn.esprit.tacheuser.models;

import java.sql.Date;

public class Avis {
    private int id;

    public Avis() {
    }

    private int userId;
    private String commentaire;
    private int note;
    private java.sql.Date dateCreation;
    private String photo; // new attribute

    // Constructor for adding Avis (without ID, dateCreation is auto-set)
    public Avis(int userId, String commentaire, int note, java.sql.Date dateCreation, String photo) {
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
        // Automatically set the creation date using current system time
        this.dateCreation = dateCreation;
        this.photo = photo;
    }

    public Avis(int id, int userId, String commentaire, int note, Date dateCreation, String photo) {
        this.id = id;
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
        this.dateCreation = dateCreation;
        this.photo = photo;
    }

    // Constructor for updating Avis (with ID, without dateCreation)
    public Avis(int id, String commentaire, int note, String photo) {
        this.id = id;
        this.commentaire = commentaire;
        this.note = note;
        this.photo = photo;
    }

    // Complete constructor (for retrieving avis from the DB)


    // Getters and Setters
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

    public java.sql.Date getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(java.sql.Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    // Getter and Setter for photo
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
