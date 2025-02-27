package tn.esprit.tacheuser.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.utils.MySQLConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService {
    private final Connection cnx=MySQLConnection.getInstance().getConnection();

    // Method to add a new Avis
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
            System.out.println("Avis Added Successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to fetch all Avis records
    public ObservableList<Avis> fetch() {
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

    // Method to search Avis by ID (using a LIKE query)
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

    // Method to delete an Avis by ID
    //    ~\Desktop\JAVA\Edutrip-gestion_utilisateur\Edutrip-gestion_utilisateur\out/
    public void delete(int id) {
        try {
            String req = "DELETE FROM avis WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Avis Deleted Successfully!");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method to update an Avis (by ID)
    public void edit(int id, String commentaire, int note, String photo) {
        try {
            String req = "UPDATE avis SET commentaire = ?, note = ?, photo = ? WHERE id = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, commentaire);
            ps.setInt(2, note);
            ps.setString(3, photo);
            ps.setInt(4, id);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Avis Updated Successfully!");
            } else {
                System.out.println("Failed to update Avis. No matching record found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}