package tn.edutrip.entities;



public class Hebergement {
    private int id_hebergement;
    private String nomh;
    private String typeh;
    private String adressh;
    private int capaciteh;
    private float prixh;
    private String disponibleh;
    private String descriptionh;
    private String imageh;

    public Hebergement(int id_hebergement, String nomh, int capaciteh, String typeh, String adressh,
                       String disponibleh, String descriptionh, String imageh, float prixh) {
        this.id_hebergement = id_hebergement;
        this.nomh = nomh;
        this.capaciteh = capaciteh;
        this.typeh = typeh;
        this.adressh = adressh;
        this.disponibleh = disponibleh;
        this.descriptionh = descriptionh;
        this.imageh = imageh;
        this.prixh = prixh;
    }


    public int getId_hebergement() {
        return id_hebergement;
    }

    public void setId_hebergement(int id_hebergement) {
        this.id_hebergement = id_hebergement;
    }

    public String getNomh() {
        return nomh;
    }

    public void setNomh(String nomh) {
        this.nomh = nomh;
    }

    public String getTypeh() {
        return typeh;
    }

    public void setTypeh(String typeh) {
        this.typeh = typeh;
    }

    public String getAdressh() {
        return adressh;
    }

    public void setAdressh(String adressh) {
        this.adressh = adressh;
    }

    public int getCapaciteh() {
        return capaciteh;
    }

    public void setCapaciteh(int capaciteh) {
        this.capaciteh = capaciteh;
    }

    public float getPrixh() {
        return prixh;
    }

    public void setPrixh(float prixh) {
        this.prixh = prixh;
    }

    public String getDisponibleh() {
        return disponibleh;
    }

    public void setDisponibleh(String disponibleh) {
        this.disponibleh = disponibleh;
    }

    public String getDescriptionh() {
        return descriptionh;
    }

    public void setDescriptionh(String descriptionh) {
        this.descriptionh = descriptionh;
    }

    public String getImageh() {
        return imageh;
    }

    public void setImageh(String imageh) {
        this.imageh = imageh;
    }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id_hebergement=" + id_hebergement +
                ", nomh='" + nomh + '\'' +
                ", typeh='" + typeh + '\'' +
                ", adressh='" + adressh + '\'' +
                ", capaciteh=" + capaciteh +
                ", prixh=" + prixh +
                ", disponibleh='" + disponibleh + '\'' +
                ", descriptionh='" + descriptionh + '\'' +
                ", imageh='" + imageh + '\'' +
                '}';
    }
}
