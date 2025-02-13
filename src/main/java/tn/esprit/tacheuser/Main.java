package tn.esprit.tacheuser;

import java.sql.Connection;
import java.util.Iterator;
import tn.esprit.tacheuser.controller.UserService;
import tn.esprit.tacheuser.controller.AvisService;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.utils.MySQLConnection;

public class Main {

    public Main() {
    }

    public static void main(String[] args) {
        // Connexion à la base de données
        Connection conn = MySQLConnection.getInstance().getConnection();
        if (conn != null) {
            System.out.println("✅ Connexion active !");
        } else {
            System.out.println("❌ Connexion échouée !");
        }

        // Création des services pour utilisateurs et avis
        UserService userService = new UserService();
        AvisService avisService = new AvisService();

        // Ajouter un utilisateur
        User user = new User("foulen", "ben foulen", "foulen@mail.com", "123456789", "123", "123555");
        userService.addUser(user);

        // Ajouter un avis
        Avis avis = new Avis(user.getId(), "Très bon service !", 5);
        avisService.addAvis(avis);

        // Supprimer un utilisateur
        int userIdToDelete = 1;
        userService.deleteUser(userIdToDelete);

        // Supprimer un avis
        int avisIdToDelete = 1;
        avisService.deleteAvis(avisIdToDelete);

        // Mettre à jour un utilisateur
        User updatedUser = new User(1, "f", "NouveauPrenom", "newmail@example.com", "123456789", "responsable");
        userService.updateUser(updatedUser);

        // Mettre à jour un avis
      //  Avis updatedAvis = new Avis(1, user.getId(), "Excellent service, je recommande !", 5);
      //  avisService.updateAvis(updatedAvis);

        // Afficher la liste des utilisateurs
        System.out.println("\ud83d\udccb Liste des utilisateurs :");
        Iterator<User> userIterator = userService.getAllUsers().iterator();
        while (userIterator.hasNext()) {
            User u = userIterator.next();
            System.out.println(u.getId() + " | " + u.getNom() + " | " + u.getPrenom() + " | " + u.getMail() + " | " + u.getPassword() + " | " + u.getTel() + " | " + u.getRole() + " | " + u.getStatus());
        }

        // Afficher la liste des avis
        System.out.println("\ud83d\udccb Liste des avis :");
        Iterator<Avis> avisIterator = avisService.getAllAvis().iterator();
        while (avisIterator.hasNext()) {
            Avis a = avisIterator.next();
            System.out.println(a.getId() + " | " + a.getUserId() + " | " + a.getCommentaire() + " | " + a.getNote() + " | " + a.getDateCreation());
        }
    }
}
