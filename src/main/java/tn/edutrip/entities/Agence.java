package tn.edutrip.entities;

import java.util.Date;
import java.util.List;

public class Agence {
    private int id_agence;
    private String nomAg;
    private String adresseAg;
    private int telephoneAg;
    private String emailAg;
    private String descriptionAg;
    private Date date_creation;
    private List<Pack_agence> packs;


    public Agence() {}


    public Agence(String nomAg, String adresseAg, int telephoneAg, String emailAg, String descriptionAg, Date dateCreation) {
        this.nomAg = nomAg;
        this.adresseAg = adresseAg;
        this.telephoneAg = telephoneAg;
        this.emailAg = emailAg;
        this.descriptionAg = descriptionAg;
        this.date_creation = dateCreation;
    }


    public Agence(int idAgence, String nomAg, String adresseAg, int telephoneAg, String emailAg, String descriptionAg, Date dateCreation) {
        this.id_agence = idAgence;
        this.nomAg = nomAg;
        this.adresseAg = adresseAg;
        this.telephoneAg = telephoneAg;
        this.emailAg = emailAg;
        this.descriptionAg = descriptionAg;
        this.date_creation = dateCreation;
    }


    public int getIdAgence() {
        return id_agence;
    }

    public void setIdAgence(int idAgence) {
        this.id_agence = idAgence;
    }

    public String getNomAg() {
        return nomAg;
    }

    public void setNomAg(String nomAg) {
        this.nomAg = nomAg;
    }

    public String getAdresseAg() {
        return adresseAg;
    }

    public void setAdresseAg(String adresseAg) {
        this.adresseAg = adresseAg;
    }

    public int getTelephoneAg() {
        return telephoneAg;
    }

    public void setTelephoneAg(int telephoneAg) {
        this.telephoneAg = telephoneAg;
    }

    public String getEmailAg() {
        return emailAg;
    }

    public void setEmailAg(String emailAg) {
        this.emailAg = emailAg;
    }

    public String getDescriptionAg() {
        return descriptionAg;
    }

    public void setDescriptionAg(String descriptionAg) {
        this.descriptionAg = descriptionAg;
    }

    public Date getDateCreation() {
        return date_creation;
    }

    public void setDateCreation(Date dateCreation) {
        this.date_creation = dateCreation;
    }

    public List<Pack_agence> getPacks() {
        return packs;
    }

    public void setPacks(List<Pack_agence> packs) {
        this.packs = packs;
    }


    @Override
    public String toString() {
        return "Agence{" +
                "idAgence=" + id_agence +
                ", nomAg='" + nomAg + '\'' +
                ", adresseAg='" + adresseAg + '\'' +
                ", telephoneAg=" + telephoneAg +
                ", emailAg='" + emailAg + '\'' +
                ", descriptionAg='" + descriptionAg + '\'' +
                ", dateCreation=" + date_creation +
                ", packs=" + (packs != null ? packs.size() + " packs associés" : "Aucun pack") +
                '}';
    }
}
