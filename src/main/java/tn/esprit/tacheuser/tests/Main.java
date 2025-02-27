package tn.esprit.tacheuser.tests;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.AvisService;
import tn.esprit.tacheuser.services.UserService;
import tn.esprit.tacheuser.utils.MySQLConnection;

public class Main {
    public static void main(String[] args) {
        // Connexion à la base de données
        Connection conn = MySQLConnection.getInstance().getConnection();
        if (conn != null) {
            System.out.println("✅ Connexion active à la base de données !");
        } else {
            System.out.println("❌ Connexion échouée !");
            return;
        }

        // Création des services
        UserService userService = new UserService();
        AvisService avisService = new AvisService();

        // ===========================
        // Test de la gestion des utilisateurs
        // ===========================

        // Ajouter un utilisateur
        User newUser = new User("fatma.ben", "Fatma", "Ben", "fatma@example.com", "123456", "user");
        userService.addUser(newUser);
        // Afficher tous les utilisateurs
        System.out.println("\n📋 Liste des utilisateurs :");
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user.getId() + " | " + user.getNom() + " | " + user.getPrenom() + " | " + user.getMail() + " | " + user.getPassword() + " | " + user.getRole() + " | " + user.getTel() + " | " + user.getStatus());
        }

        // Mettre à jour un utilisateur
        User updatedUser = new User(5, "fatma.updated", "Fatma", "Updated", "fatma.updated@example.com", "newpass123", "admin");
        userService.updateUser(updatedUser);

        // Supprimer un utilisateur
        userService.deleteUser(7);

        // ===========================
        // Test de la gestion des avis
        // ===========================

        // Ajouter un avis
        Date dateCreation = new Date(System.currentTimeMillis());
        //Avis newAvis = new Avis(2, "Service impeccable, je recommande !", 5, dateCreation);
      //  avisService.addAvis(newAvis);

        // Afficher tous les avis
        System.out.println("\n📋 Liste des avis :");
      /*  List<Avis> avisList = avisService.getAllAvis();
        for (Avis avis : avisList) {
            System.out.println(avis.getId() + " | " + avis.getUserId() + " | " + avis.getCommentaire() + " | " + avis.getNote() + " | " + avis.getDateCreation());
        }

        // Mettre à jour un avis
       // Avis updatedAvis = new Avis(7, "Expérience formidable, je reviendrai !", 4);
     //   avisService.updateAvis(updatedAvis);

        // Supprimer un avis
       // avisService.deleteAvis(11);*/
    }
}