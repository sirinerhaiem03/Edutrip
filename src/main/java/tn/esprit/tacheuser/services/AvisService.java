package tn.esprit.tacheuser.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService {
    private final Connection cnx = MySQLConnection.getInstance().getConnection();

    // Méthode pour ajouter un Avis
    public void add(Avis a) {
        try {
            String req = "INSERT INTO avis(user_id, commentaire, note, date_creation, photo) VALUES (?,?,?,?,?)";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, a.getUserId());
            ps.setString(2, a.getCommentaire());
            ps.setInt(3, a.getNote());
            ps.setDate(4, a.getDateCreation());
            ps.setString(5, a.getPhoto());
            ps.executeUpdate();
            System.out.println("Avis ajouté avec succès !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Méthode pour récupérer tous les Avis
    public ObservableList<Avis> getAll() {
        ObservableList<Avis> avisList = FXCollections.observableArrayList();
        try {
            String req = "SELECT * FROM avis";
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Avis a = new Avis(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("commentaire"),
                        rs.getInt("note"),
                        rs.getDate("date_creation"),
                        rs.getString("photo")
                );
                avisList.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return avisList;
    }

    // Méthode pour rechercher un Avis par ID
    public List<Avis> rechercheAvis(int id) {
        List<Avis> avisList = new ArrayList<>();
        try {
            String req = "SELECT * FROM avis WHERE id LIKE CONCAT(?, '%')";
            PreparedStatement st = cnx.prepareStatement(req);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Avis a = new Avis(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("commentaire"),
                        rs.getInt("note"),
                        rs.getDate("date_creation"),
                        rs.getString("photo")
                );
                avisList.add(a);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return avisList;
    }

    // Méthode pour supprimer un Avis
    public void delete(int id) {
        try {
            String req = "DELETE FROM avis WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Avis supprimé avec succès !");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Méthode renommée en 'edit' pour correspondre à l'appel dans le contrôleur
    public void edit(Avis a) {  // Correspond à la méthode 'update' initiale
        try {
            String req = "UPDATE avis SET commentaire = ?, note = ?, photo = ? WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, a.getCommentaire());
            ps.setInt(2, a.getNote());
            ps.setString(3, a.getPhoto());
            ps.setInt(4, a.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Avis mis à jour avec succès !");
            } else {
                System.out.println("Échec de la mise à jour de l'avis. Aucun enregistrement correspondant trouvé.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
