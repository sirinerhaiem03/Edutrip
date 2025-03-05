package tn.esprit.tacheuser.models;

import java.sql.Date;

public class Avis {
    private int id;
    private int userId;
    private String commentaire;
    private int note;
    private Date dateCreation;
    private String photo;

    // Constructeurs
    public Avis() {}

    public Avis(int userId, String commentaire, int note, Date dateCreation, String photo) {
        this.userId = userId;
        this.commentaire = commentaire;
        this.note = note;
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

    public Avis(int id, String commentaire, int note, String photo) {
        this.id = id;
        this.commentaire = commentaire;
        this.note = note;
        this.photo = photo;
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

    public Date getDateCreation() { return dateCreation; }
    public void setDateCreation(Date dateCreation) { this.dateCreation = dateCreation; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    @Override
    public String toString() {
        return "Avis{" +
                "id=" + id +
                ", userId=" + userId +
                ", commentaire='" + commentaire + '\'' +
                ", note=" + note +
                ", dateCreation=" + dateCreation +
                ", photo='" + photo + '\'' +
                '}';
    }
}
