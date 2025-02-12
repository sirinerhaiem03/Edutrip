package tn.EduTrip.entites;
import java.sql.Timestamp;
import java.util.Date;

public class Vol {
    private int id_Vol  ;
    private int placesDispo;
    private String numVol ,aeroportDepart , aeroportArrivee  ;

    private Timestamp  dateDepart , dateArrivee ;
    private double prix;

    public Vol(int id_Vol, int placesDispo, String numVol, String aeroportDepart, String aeroportArrivee,Timestamp dateDepart,Timestamp dateArrivee, double prix) {
        this.id_Vol = id_Vol;

        this.numVol = numVol;
        this.aeroportDepart = aeroportDepart;
        this.aeroportArrivee = aeroportArrivee;
        this.dateDepart = dateDepart;
        this.dateArrivee = dateArrivee;
        this.placesDispo = placesDispo;
        this.prix = prix;
    }

    public int getId_Vol() {
        return id_Vol;
    }

    public int getPlacesDispo() {
        return placesDispo;
    }

    public String getNumVol() {
        return numVol;
    }

    public String getAeroportDepart() {
        return aeroportDepart;
    }

    public String getAeroportArrivee() {
        return aeroportArrivee;
    }


    public Timestamp getDateArrivee() {
        return dateArrivee;
    }

    public Timestamp getDateDepart() {
        return dateDepart;
    }

    public double getPrix() {
        return prix;
    }

    public void setId_Vol(int id_Vol) {
        this.id_Vol = id_Vol;
    }

    public void setPlacesDispo(int placesDispo) {
        this.placesDispo = placesDispo;
    }

    public void setNumVol(String numVol) {
        this.numVol = numVol;
    }

    public void setAeroportDepart(String aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public void setAeroportArrivee(String aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
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
        return "vol{" +
                "id_Vol=" + id_Vol +
                ", placesDispo=" + placesDispo +
                ", numVol='" + numVol + '\'' +
                ", aeroportDepart='" + aeroportDepart + '\'' +
                ", aeroportArrivee='" + aeroportArrivee + '\'' +
                ", dateDepart='" + dateDepart + '\'' +
                ", dateArrivee='" + dateArrivee + '\'' +
                ", prix=" + prix +
                '}';
    }

}
