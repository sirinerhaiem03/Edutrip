package tn.edutrip.services;


import tn.edutrip.entities.User;

import tn.edutrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {
    private Connection conn = MyDatabase.getInstance().getConnection();

    public ServiceUser() {}

    // Ajouter un utilisateur
    public void addUser(User user) {
        String query = "INSERT INTO utilisateur (nom, prenom, mail, tel, password, confirm_password, status, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pst = this.conn.prepareStatement(query);
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getMail());
            pst.setString(4, user.getTel());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getConfirmpassword());
            pst.setString(7, user.getStatus());
            pst.setString(8, user.getRole());
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Utilisateur ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // Supprimer un utilisateur
    public void deleteUser(int userId) {
        String query = "DELETE FROM utilisateur WHERE id = ?";
        try {
            PreparedStatement pst = this.conn.prepareStatement(query);
            pst.setInt(1, userId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Utilisateur supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    // Mettre à jour un utilisateur
    public void updateUser(User user) {
        String query = "UPDATE utilisateur SET nom = ?, prenom = ?, mail = ?, tel = ?, role = ?, status = ? WHERE id = ?";
        try {
            PreparedStatement pst = this.conn.prepareStatement(query);
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getMail());
            pst.setString(4, user.getTel());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getStatus());
            pst.setInt(7, user.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("mail"),
                        rs.getString("password"),
                        rs.getString("tel"),
                        rs.getString("status") != null ? rs.getString("status") : "inactive"
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
        }
        return null; // Return null if user is not found
    }


    // Récupérer tous les utilisateurs
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("mail"),
                        rs.getString("password"),
                        rs.getString("tel"),
                        rs.getString("status") != null ? rs.getString("status") : "inactive" // Si status est null, il devient "inactive"
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }
}