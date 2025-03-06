package tn.edutrip.tests;

import tn.edutrip.entities.Hebergement;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceHebergement;
import tn.edutrip.services.ServiceReservationHebergement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        // Services initialization
        ServiceHebergement serviceHebergement = new ServiceHebergement();
        ServiceReservationHebergement serviceReservationHebergement = new ServiceReservationHebergement();

        // ***** Affichage des Hébergements et Réservations existants *****
        System.out.println("***** Liste des Hébergements *****");
        for (Hebergement h : serviceHebergement.getAll()) {
            System.out.println(h);
        }

        System.out.println("***** Liste des Réservations *****");
        for (Reservation_hebergement r : serviceReservationHebergement.getAll()) {
            System.out.println(r);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            // Création d'instances Hebergement
            Hebergement h1 = new Hebergement(22, "BALKISS", 1200, "Colocation", "Adresse1", "Disponible", "Description1", "Image1", 12);
            Hebergement h2 = new Hebergement(7, "LINDA", 10, "Type2", "Ariana", "Disponible", "Description2", "Image2", 12);

            // ***** Ajouter, Modifier, Supprimer Hébergement **********
             serviceHebergement.add(h1);
            // serviceHebergement.update(h2);
            //serviceHebergement.remove(8);  // Suppression d'un hébergement avec ID = 8

            // Création d'instances Reservation_hebergement avec des dates valides
            Date dateDebut = dateFormat.parse("20-12-2025");
            Date dateFin = dateFormat.parse("30-12-2025");

            Reservation_hebergement r1 = new Reservation_hebergement(11, 3, dateDebut, dateFin, "Confirmée");
            Reservation_hebergement r2 = new Reservation_hebergement(7, 3, dateDebut, dateFin, "En attente");

            // ***** Ajouter, Modifier, Supprimer Réservation **********
            // serviceReservationHebergement.add(r1);
            // serviceReservationHebergement.update(r2);
            //serviceReservationHebergement.remove(8);  // Suppression d'une réservation avec ID = 8

        } catch (ParseException e) {
            System.err.println("❌ Erreur de format de date : " + e.getMessage());
        }
    }
}
