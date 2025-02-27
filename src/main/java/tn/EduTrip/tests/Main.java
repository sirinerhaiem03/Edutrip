package tn.EduTrip.tests;

import tn.EduTrip.entites.ReserVol;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceReserVol;
import tn.EduTrip.services.ServiceVol;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;

 public class Main {
     public static void main(String[] args) {


         ServiceVol serviceVol = new ServiceVol();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

         ServiceReserVol serviceRes = new ServiceReserVol();


         try {
             // Parsing des dates
             Timestamp dateDepart = Timestamp.valueOf("2025-02-25 16:00:00");
             Timestamp dateArrivee = Timestamp.valueOf("2025-02-25 18:00:00");

             Timestamp newDateDepart = Timestamp.valueOf("2025-12-31 13:00:00");
             Timestamp newDateArrivee = Timestamp.valueOf("2025-01-01 15:30:00");

             Timestamp newDateDepart1= Timestamp.valueOf("2025-03-03 5:00:00");
             Timestamp newDateArrivee1 = Timestamp.valueOf("2025-03-03 8:00:00");

             Vol vol1 = new Vol(1, "egl102", 12, "TunisCartage", "Paris", dateDepart, dateArrivee, 1500);
             Vol vol2 = new Vol(2, "egl103", 50, "JerbaZarzis", "Lyon", dateDepart, dateArrivee, 1200);
             Vol vol3 = new Vol(8, "egl202", 100, "gihvugvb", "bnbnbn", dateDepart, dateArrivee, 1120);
             Vol vol4 = new Vol(9, "wafa55", 2, "Nice", "Algerie", dateDepart, dateArrivee, 1120);


                 //serviceVol.ajouter(vol1);
                 //serviceVol.ajouter(vol2);
                 //serviceVol.ajouter(vol3);
                 //  serviceVol.ajouter(vol4);


             ReserVol reservation1 = new ReserVol(1, 1, 1, new java.util.Date(), "Confirmée", 1000, "Carte Bancaire");
             ReserVol reservation2 = new ReserVol(2, 2, 2, new java.util.Date(), "Confirmée", 1500, "Carte Bancaire");
             ReserVol reservation3 = new ReserVol(3, 12, 7, new java.util.Date(), "Confirmée", 2200, "Espece");
             ReserVol reservation4 = new ReserVol(4, 9, 4, new java.util.Date(), "Confirmée", 2500, "PayPal");
             ReserVol reservation6 = new ReserVol(6, 11, 8, new java.util.Date(), "Confirmée", 2075, "Carte Bancaire");

             //serviceRes.ajouter(reservation2);
             //serviceRes.ajouter(reservation1);
            // serviceRes.ajouter(reservation3);
            // serviceRes.ajouter(reservation4);
             serviceRes.ajouter(reservation6);


                 //Vol volModifie = new Vol(1, 10, "egl105", "Tunis", "Londres", newDateDepart, newDateArrivee, 1700);

               //serviceVol.modifier(volModifie);

             //ReserVol reservationModifiee = new ReserVol(2, 2, 2, new java.util.Date(), "Annulée", 0, "Aucun");
            // serviceRes.modifier(reservationModifiee);

             // serviceVol.supprimer(6);
              //serviceVol.supprimer(7);
            // serviceRes.supprimer(1);

             //serviceRes.supprimer(1);


             List<Vol> vols = serviceVol.afficher();
             System.out.println("📋 Liste des vols :");
             for (Vol v : vols) {
                 System.out.println(v);
             }
             List<ReserVol> reservations = serviceRes.afficher();
             System.out.println("📋 Liste des réservations :");
             for (ReserVol res : reservations) {
                 System.out.println(res);
             }


             ServiceReserVol.getReservationsWithVols();

             } catch (SQLException e) {
                 System.err.println( e.getMessage());
             }

         }



     }
