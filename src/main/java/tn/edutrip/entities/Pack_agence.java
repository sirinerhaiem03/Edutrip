package tn.edutrip.entities;

import java.math.BigDecimal;
import java.sql.Date; // Use java.sql.Date directly

public class Pack_agence {
    private int id_pack;
    private String nomPk;
    private String descriptionPk;
    private BigDecimal prix;
    private int duree;
    private String services_inclus;
    private Date date_ajout; // Changed to java.sql.Date directly
    private Status status;
    private int id_agence;

    public enum Status {
        disponible, indisponible;
    }

    public Pack_agence() {}

    public Pack_agence(int id_pack, String nomPk, String descriptionPk, BigDecimal prix, int duree,
                       String services_inclus, Date date_ajout, Status status, int id_agence) {
        this.id_pack = id_pack;
        this.nomPk = nomPk;
        this.descriptionPk = descriptionPk;
        this.prix = prix;
        this.duree = duree;
        this.services_inclus = services_inclus;
        this.date_ajout = date_ajout;
        this.status = status;
        this.id_agence = id_agence;
    }

    public int getId_pack() {
        return id_pack;
    }

    public void setId_pack(int id_pack) {
        this.id_pack = id_pack;
    }

    public String getNomPk() {
        return nomPk;
    }

    public void setNomPk(String nomPk) {
        this.nomPk = nomPk;
    }

    public String getDescriptionPk() {
        return descriptionPk;
    }

    public void setDescriptionPk(String descriptionPk) {
        this.descriptionPk = descriptionPk;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getServices_inclus() {
        return services_inclus;
    }

    public void setServices_inclus(String services_inclus) {
        this.services_inclus = services_inclus;
    }

    public Date getDate_ajout() {
        return date_ajout; // No need for conversion, now it's java.sql.Date
    }

    public void setDate_ajout(Date date_ajout) {
        this.date_ajout = date_ajout;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId_agence() {
        return id_agence;
    }

    public void setId_agence(int id_agence) {
        this.id_agence = id_agence;
    }

    @Override
    public String toString() {
        return "Pack_agence{" +
                "id_pack=" + id_pack +
                ", nomPk='" + nomPk + '\'' +
                ", descriptionPk='" + descriptionPk + '\'' +
                ", prix=" + prix +
                ", duree=" + duree +
                ", services_inclus='" + services_inclus + '\'' +
                ", date_ajout=" + date_ajout +
                ", status=" + status +
                ", id_agence=" + id_agence +
                '}';
    }
}
