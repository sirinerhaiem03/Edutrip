package tn.esprit.tacheuser.services;

import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.utils.MySQLConnection;
import tn.esprit.tacheuser.utils.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {
    private final Connection conn;

    public UserService() {
        this.conn = MySQLConnection.getInstance().getConnection();
    }

    @Override
    public void addUser(User user) {
        String query = "INSERT INTO user (nom, prenom, mail, tel, password, confirm_password, status, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getMail());
            pst.setString(4, user.getTel());
            pst.setString(5, user.getPassword());
            pst.setString(6, user.getPassword());
            pst.setString(7, user.getStatus());
            pst.setString(8, user.getRole());

            int rowsInserted = pst.executeUpdate();
            System.out.println(rowsInserted > 0 ? "✅ Utilisateur ajouté avec succès !" : "⚠️ Échec de l'ajout de l'utilisateur.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(String email, String password) {
        String query = "SELECT * FROM user WHERE mail = ? AND password = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            rs.getString("tel"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User searchuser(int ID) {
        String query = "SELECT * FROM user WHERE id = ? ";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, ID);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            rs.getString("tel"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteUser(int userId) {
        String query = "DELETE FROM user WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            int rowsDeleted = pst.executeUpdate();
            System.out.println(rowsDeleted > 0 ? "✅ Utilisateur supprimé avec succès !" : "⚠️ Aucun utilisateur trouvé avec cet ID !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        String query = "UPDATE user SET nom = ?, prenom = ?, mail = ?, tel = ?, role = ?, status = ? WHERE id = ?";
        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, user.getNom());
            pst.setString(2, user.getPrenom());
            pst.setString(3, user.getMail());
            pst.setString(4, user.getTel());
            pst.setString(5, user.getRole());
            pst.setString(6, user.getStatus());
            pst.setInt(7, user.getId());
            UserSession.setSession(user);
            int rowsUpdated = pst.executeUpdate();
            System.out.println(rowsUpdated > 0 ? "✅ Utilisateur mis à jour avec succès !" : "⚠️ Aucun utilisateur trouvé avec cet ID !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("mail"),
                        rs.getString("password"),
                        rs.getString("tel"),
                        rs.getString("status") != null ? rs.getString("status") : "inactive"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
