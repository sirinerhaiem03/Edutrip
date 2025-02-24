package tn.esprit.services;

import tn.esprit.entities.Candidature;
import tn.esprit.entities.EtatCandidature;
import tn.esprit.entities.University;
import tn.esprit.entities.User;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidature implements IService<Candidature>{

    private Connection connection = MyDatabase.getInstance().getConnection();// get the singleton instance of the MyDatabase class

    public void ajouter(Candidature candidature) throws SQLException {
        String query = "INSERT INTO candidature (cv, lettre_motivation, diplome, etat, idUser, idUniversity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, candidature.getCv());
            stmt.setString(2, candidature.getLettre_motivation());
            stmt.setString(3, candidature.getDiplome());
            stmt.setString(4, candidature.getEtat().name());
            stmt.setInt(5, candidature.getUser().getId()); // Using User object
            stmt.setInt(6, candidature.getUniversity().getIdUniversity()); // Using University object
            stmt.executeUpdate();
        }
    }

    public void modifier(Candidature candidature) throws SQLException {
        String query = "UPDATE candidature SET cv = ?, lettre_motivation = ?, diplome = ?, etat = ?, idUser = ?, idUniversity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, candidature.getCv());
            stmt.setString(2, candidature.getLettre_motivation());
            stmt.setString(3, candidature.getDiplome());
            stmt.setString(4, candidature.getEtat().name());
            stmt.setInt(5, candidature.getUser().getId()); // Using User object
            stmt.setInt(6, candidature.getUniversity().getIdUniversity()); // Using University object
            stmt.setInt(7, candidature.getId());
            stmt.executeUpdate();
        }
    }

    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM candidature WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Candidature> afficher() throws SQLException {
        List<Candidature> candidatures = new ArrayList<>();
        UserService userService = new UserService(); // Initialize UserService

        String query = "SELECT c.*, u.nom AS university_name, u.ville, u.email, u.description, " +
                "usr.id AS user_id FROM candidature c " +
                "JOIN university u ON c.idUniversity = u.idUniversity " +
                "JOIN utilisateur usr ON c.idUser = usr.id";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                University university = new University(
                        rs.getInt("idUniversity"),
                        rs.getString("university_name"),
                        rs.getString("ville"),
                        rs.getString("email"),
                        rs.getString("description")
                );

                // Get user by ID from UserService
                User user = userService.getUserById(rs.getInt("user_id"));

                Candidature candidature = new Candidature(
                        rs.getInt("id"),
                        rs.getString("cv"),
                        rs.getString("lettre_motivation"),
                        rs.getString("diplome"),
                        EtatCandidature.valueOf(rs.getString("etat")),
                        user,
                        university
                );

                candidatures.add(candidature);
            }
        }
        return candidatures;
    }

}
