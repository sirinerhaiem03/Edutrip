package tn.edutrip.tests;

import java.math.BigDecimal;
import java.sql.Connection;
import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServiceAgence;
import tn.edutrip.services.ServicePack_agence;
import tn.edutrip.utils.MyDatabase;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Récupérer la connexion à la base de données
        Connection conn = MyDatabase.getInstance().getConnection();

        // Créer une instance des services
        ServiceAgence serviceAgence = new ServiceAgence();
        ServicePack_agence servicePackAgence = new ServicePack_agence();

        // Format de date pour la conversion
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        // Convertir les chaînes de caractères en objets java.sql.Date
        Date date1 = new Date(0);
        Date date2 = new Date(0);

        try {
            date1 = new Date(format.parse("2025-02-10").getTime());
            date2 = new Date(format.parse("2025-02-15").getTime());
        } catch (Exception ignored) {
        }

        // Créer des instances d'Agence
        Agence g1 = new Agence(21, "Linda8", "Benfoulen", 25096025, "linda@exemple.com", "Description de Linda", date1);
        Agence g2 = new Agence(18, "lindaaaaa8", "ssssssjjjj", 12365478, "maram@exemple.com", "Description de Maram", date2);

        // Ajouter et mettre à jour les agences
        serviceAgence.add(g1);
        serviceAgence.update(g1);

        // Affichage pour confirmer la modification
        System.out.println("L'agence g1 a été modifiée avec succès.");

        // Supprimer l'agence g2
        serviceAgence.remove(18);

        // Créer et ajouter un pack pour l'agence g1
        Pack_agence pack1 = new Pack_agence(
                1, // id_pack
                "Pack A",
                "Description du Pack A",
                new BigDecimal(200.0), // prix
                30, // duree
                "Service A, Service B", // services_inclus
                date1, // date_ajout
                Pack_agence.Status.DISPONIBLE, // status
                g1.getIdAgence() // id_agence
        );
        servicePackAgence.add(pack1);

// Créer et ajouter un autre pack
        Pack_agence pack2 = new Pack_agence(
                2, // id_pack
                "Pack B",
                "Description du Pack B",
                new BigDecimal(300.0), // prix
                60, // duree
                "Service C, Service D", // services_inclus
                date2, // date_ajout
                Pack_agence.Status.INDISPONIBLE, // status
                g1.getIdAgence() // id_agence
        );
        servicePackAgence.add(pack2);


        // Modifier un pack existant
        // Modifier le prix du Pack A
        servicePackAgence.update(pack1);

        // Supprimer un pack (par exemple le Pack B)
        servicePackAgence.remove(pack2.getId_pack());

        // Affichage des agences avec leurs packs
        List<Agence> agences = serviceAgence.afficher();
        for (Agence agence : agences) {
            System.out.println("Agence: " + agence.getNomAg() + ", Adresse: " + agence.getAdresseAg());
            System.out.println("Packs associés:");

            // Afficher les packs associés à l'agence
           /* agence.getPacks().forEach(pack -> {
                System.out.println("  - Pack: " + pack.getNomPk() + ", Prix: " + pack.getPrix());
            });
            System.out.println("------------------------------------------------");*/
        }
    }
}
