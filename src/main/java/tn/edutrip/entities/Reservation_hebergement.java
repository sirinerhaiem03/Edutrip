package tn.edutrip.entities;

import java.util.Date;

public class Reservation_hebergement {
    private int id_reservationh;
    private int id_hebergement;
    private Date date_d;
    private Date date_f;
    private String status;
    private Hebergement hebergement; // ✅ Add Hebergement object

    public Reservation_hebergement(int id_reservationh, int id_hebergement, Date date_d, Date date_f, String status) {
        this.id_reservationh = id_reservationh;
        this.id_hebergement = id_hebergement;
        this.date_d = date_d;
        this.date_f = date_f;
        this.status = status;
    }

    public int getId_reservationh() {
        return id_reservationh;
    }

    public void setId_reservationh(int id_reservationh) {
        this.id_reservationh = id_reservationh;
    }

    public int getId_hebergement() {
        return id_hebergement;
    }

    public void setId_hebergement(int id_hebergement) {
        this.id_hebergement = id_hebergement;
    }

    public Date getDate_d() {
        return date_d;
    }

    public void setDate_d(Date date_d) {
        this.date_d = date_d;
    }

    public Date getDate_f() {
        return date_f;
    }

    public void setDate_f(Date date_f) {
        this.date_f = date_f;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // ✅ Add getter and setter for Hebergement
    public Hebergement getHebergement() {
        return hebergement;
    }

    public void setHebergement(Hebergement hebergement) {
        this.hebergement = hebergement;
    }

    @Override
    public String toString() {
        return "ReservationHebergement{" +
                "id_reservationh=" + id_reservationh +
                ", id_hebergement=" + id_hebergement +
                ", date_d=" + date_d +
                ", date_f=" + date_f +
                ", status='" + status + '\'' +
                ", hebergement=" + (hebergement != null ? hebergement.getNomh() : "N/A") +
                '}';
    }
}
