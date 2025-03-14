package tn.EduTrip.entites;

import java.sql.Timestamp;

public class Vol {
    private int id;
    private int places;
    private String numVol;
    private String depart;
    private String arrivee;
    private Timestamp dateDepart;
    private Timestamp dateArrivee;
    private double prix;

    public Vol(int id, int places, String numVol, String depart, String arrivee, 
              Timestamp dateDepart, Timestamp dateArrivee, double prix) {
        this.id = id;
        this.places = places;
        this.numVol = numVol;
        this.depart = depart;
        this.arrivee = arrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.prix = prix;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getPlaces() {
        return places;
    }

    public String getNumVol() {
        return numVol;
    }

    public String getDepart() {
        return depart;
    }

    public String getArrivee() {
        return arrivee;
    }

    public Timestamp getDateDepart() {
        return dateDepart;
    }

    public Timestamp getDateArrivee() {
        return dateArrivee;
    }

    public double getPrix() {
        return prix;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public void setNumVol(String numVol) {
        this.numVol = numVol;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public void setArrivee(String arrivee) {
        this.arrivee = arrivee;
    }

    public void setDateDepart(Timestamp dateDepart) {
        this.dateDepart = dateDepart;
    }

    public void setDateArrivee(Timestamp dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Vol{" +
                "id=" + id +
                ", places=" + places +
                ", numVol='" + numVol + '\'' +
                ", depart='" + depart + '\'' +
                ", arrivee='" + arrivee + '\'' +
                ", dateDepart=" + dateDepart +
                ", dateArrivee=" + dateArrivee +
                ", prix=" + prix +
                '}';
    }
}
