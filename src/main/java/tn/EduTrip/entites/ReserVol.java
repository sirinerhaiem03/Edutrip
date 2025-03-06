package tn.EduTrip.entites;

import java.util.Date;

public class ReserVol {
    private int idReservation;
    private int idEtudiant;
    private int id_vol;;
    private String numVol;
    private Date dateReservation;
    private String statut;
    private double prix;
    private String modePaiement;
    private String nom;
    private String prenom;
    private String email;

    public ReserVol(int i, int i1, int i2, Date date, String confirmée, double v, String modePaiement) {
    }
    public ReserVol(int idReservation, int idEtudiant, int id_vol, String numVol, Date dateReservation, String statut,
                    double prix, String modePaiement, String nom, String prenom, String email) {
        this.idReservation = idReservation;
        this.idEtudiant = idEtudiant;
        this.id_vol = id_vol;
        this.numVol = numVol;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.prix = prix;
        this.modePaiement = modePaiement;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;

    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId_vol() {
        return id_vol;
    }

    public void setId_vol(int id_vol) {
        this.id_vol = id_vol;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Les autres getters et setters restent inchangés
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public String getNumVol() { return numVol; }

    public void setNumVol(String numVol) { this.numVol = numVol; }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    @Override
    public String toString() {
        return "ReservationVol{" +
                "idReservation=" + idReservation +
                ", idEtudiant=" + idEtudiant +
                ", numVol=" + numVol +
                ", dateReservation=" + dateReservation +
                ", statut='" + statut + '\'' +
                ", prix=" + prix +
                ", modePaiement='" + modePaiement + '\'' +
                ", nom='" + nom + '\'' + // Affichage du nom
                ", prenom='" + prenom + '\'' + // Affichage du prénom
                ", email='" + email + '\'' + // Affichage de l'email
                '}';
    }
}
