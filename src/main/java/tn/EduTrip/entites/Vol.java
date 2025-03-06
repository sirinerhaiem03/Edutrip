package tn.EduTrip.entites;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Vol {
    private int id_vol;

    private String numVol;
    private int places;
    private String depart;
    private String arrivee;
    private Timestamp dateDepart;
    private Timestamp dateArrivee;
    private double prix;

    public Vol(String numVol, String depart, String arrivee, LocalDateTime dateDepart, LocalDateTime dateArrivee, double prix, int places) {}

    public Vol(int id_Vol, String numVol, int places, String depart, String arrivee,
               Timestamp dateDepart, Timestamp dateArrivee, double prix) {
        this.id_vol = id_Vol;

        this.numVol = numVol;
        this.places = places;
        this.depart = depart;
        this.arrivee = arrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.prix = prix;
    }
    public Vol(String numVol, String depart, String arrivee,
               Timestamp dateDepart, Timestamp dateArrivee,
               double prix, int places) {
        this.numVol = numVol;
        this.depart = depart;
        this.arrivee = arrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.prix = prix;
        this.places = places;
    }

    // Getters
    public int getId_Vol() {
        return id_vol;
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
    public void setId_Vol(int id_vol) {
        this.id_vol = id_vol;
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
                "id_Vol=" + id_vol +
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
