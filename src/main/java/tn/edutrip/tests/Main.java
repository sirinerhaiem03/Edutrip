package tn.edutrip.tests;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServiceAgence;
import tn.edutrip.services.ServicePack_agence;
import tn.edutrip.utils.MyDatabase;

public class Main {
    public static void main(String[] args) {

        Connection conn = MyDatabase.getInstance().getConnection();


        ServiceAgence serviceAgence = new ServiceAgence();
        ServicePack_agence servicePackAgence = new ServicePack_agence();


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null, date2 = null;

        try {
            date1 = new Date(format.parse("2025-02-10").getTime());
            date2 = new Date(format.parse("2025-02-15").getTime());
        } catch (Exception e) {
            System.out.println(" Erreur lors du parsing des dates : " + e.getMessage());
        }


        Agence g1 = new Agence("Linda7", "slimi", 25096025, "linda@exemple.com", "Description de Linda", date1);
        serviceAgence.add(g1);
        System.out.println(" Agence ajoutée : " + g1);


        Agence g1FromDB = serviceAgence.getByName("Linda7");
        if (g1FromDB == null) {
            System.out.println(" Impossible de récupérer l'agence après insertion.");
            return;
        }


        Pack_agence pack1 = new Pack_agence(
                0, "Pack A", "Description du Pack A",
                new BigDecimal(200.0), 30, "Service A, Service B",
                date1, Pack_agence.Status.disponible,
                g1FromDB.getIdAgence()
        );
        servicePackAgence.add(pack1);
        System.out.println(" Pack ajouté : " + pack1);


        pack1.setPrix(new BigDecimal(250.0));
        servicePackAgence.update(pack1);
        System.out.println(" Pack mis à jour avec succès.");


        // serviceAgence.remove(g1FromDB.getIdAgence());


        afficherAgencesAvecPacks(serviceAgence, servicePackAgence);
    }

    private static void afficherAgencesAvecPacks(ServiceAgence serviceAgence, ServicePack_agence servicePackAgence) {
        List<Agence> agences = serviceAgence.afficher();
        for (Agence agence : agences) {
            System.out.println("\n Agence : " + agence.getNomAg() + " | Adresse : " + agence.getAdresseAg());

            List<Pack_agence> packs = servicePackAgence.getPacksByAgence(agence.getIdAgence());
            System.out.println(" Packs associés :");
            for (Pack_agence pack : packs) {
                System.out.println( pack.getNomPk() + " | Prix : " + pack.getPrix() + " | Statut : " + pack.getStatus());
            }
            System.out.println("------------------------------------------------");
        }
    }
}
