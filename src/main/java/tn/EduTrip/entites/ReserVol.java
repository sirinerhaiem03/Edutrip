package tn.EduTrip.entites;

import java.util.Date;

public class ReserVol {
    private int idReservation, idEtudiant, id_Vol;
    private Date dateReservation;
    private String statut;
    private double prix;
    private String modePaiement;
    private String nom;    // Nouveau champ
    private String prenom; // Nouveau champ
    private String email;  // Nouveau champ

    // Constructeur mis à jour pour inclure les nouveaux champs
    public ReserVol(int idReservation, int idEtudiant, int id_Vol, Date dateReservation, String statut,
                    double prix, String modePaiement, String nom, String prenom, String email) {
        this.idReservation = idReservation;
        this.idEtudiant = idEtudiant;
        this.id_Vol = id_Vol;
        this.dateReservation = dateReservation;
        this.statut = statut;
        this.prix = prix;
        this.modePaiement = modePaiement;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Getters et setters pour les nouveaux champs
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public int getId_Vol() {
        return id_Vol;
    }

    public void setId_Vol(int id_Vol) {
        this.id_Vol = id_Vol;
    }

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
                ", id_Vol=" + id_Vol +
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
