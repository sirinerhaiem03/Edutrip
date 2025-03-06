package tn.EduTrip.entites;

import java.sql.Timestamp;

public class Perturbation {
    private int id;
    private int idVol;
    private String typePerturbation; // "Retard" or "Annulation"
    private String description;
    private Timestamp dateAnnonce;

    public Perturbation(int id, int idVol, String typePerturbation, String description, Timestamp dateAnnonce) {
        this.id = id;
        this.idVol = idVol;
        this.typePerturbation = typePerturbation;
        this.description = description;
        this.dateAnnonce = dateAnnonce;
    }

    public Perturbation(int idVol, String typePerturbation, String description) {
        this.idVol = idVol;
        this.typePerturbation = typePerturbation;
        this.description = description;
        this.dateAnnonce = new Timestamp(System.currentTimeMillis()); // Auto timestamp
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdVol() { return idVol; }
    public void setIdVol(int idVol) { this.idVol = idVol; }

    public String getTypePerturbation() { return typePerturbation; }
    public void setTypePerturbation(String typePerturbation) { this.typePerturbation = typePerturbation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getDateAnnonce() { return dateAnnonce; }
    public void setDateAnnonce(Timestamp dateAnnonce) { this.dateAnnonce = dateAnnonce; }
}
