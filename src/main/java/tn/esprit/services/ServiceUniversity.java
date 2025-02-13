package tn.esprit.services;

import tn.esprit.entities.University;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUniversity {

    private Connection connection = MyDatabase.getInstance().getConnection();  // ✅ Correct


    public void ajouter(University university) throws SQLException {
        String query = "INSERT INTO university (nom, ville, email, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, university.getNom());
            stmt.setString(2, university.getVille());
            stmt.setString(3, university.getEmail());
            stmt.setString(4, university.getDescription());
            stmt.executeUpdate();
        }
    }

    public void modifier(University university) throws SQLException {
        String query = "UPDATE university SET nom = ?, ville = ?, email = ?, description = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, university.getNom());
            stmt.setString(2, university.getVille());
            stmt.setString(3, university.getEmail());
            stmt.setString(4, university.getDescription());
            stmt.setInt(5, university.getId());
            int rowsUpdated = stmt.executeUpdate();

        }
    }
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM university WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);  // Associate the ID of the university to delete
            int rowsDeleted = stmt.executeUpdate();  // Execute the delete query
            if (rowsDeleted > 0) {
                System.out.println("University deleted successfully.");
            } else {
                System.out.println("No university found with this ID.");
            }
        }
    }


    public List<University> afficher() throws SQLException {
        List<University> universities = new ArrayList<>();
        String query = "SELECT * FROM university";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                University university = new University(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("ville"),
                        rs.getString("email"),
                        rs.getString("description")
                );
                universities.add(university);
            }
        }
        return universities;
    }
}
