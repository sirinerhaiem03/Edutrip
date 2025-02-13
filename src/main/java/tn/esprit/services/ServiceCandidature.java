package tn.esprit.services;

import tn.esprit.entities.Candidature;
import tn.esprit.utils.MyDatabase;
import tn.esprit.entities.EtatCandidature;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidature {

    private Connection connection = MyDatabase.getInstance().getConnection();  // ✅ Correct


    public void ajouter(Candidature candidature) throws SQLException {
        String query = "INSERT INTO candidature (cv, lettre_motivation, diplome, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, candidature.getCv());
            stmt.setString(2, candidature.getLettre_motivation());
            stmt.setString(3, candidature.getDiplome());
            stmt.setString(4, candidature.getEtat().name());
            stmt.executeUpdate();
        }
    }

    public void modifier(Candidature candidature) throws SQLException {
        String query = "UPDATE candidature SET cv = ?, lettre_motivation = ?, diplome = ?, etat = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, candidature.getCv());
            stmt.setString(2, candidature.getLettre_motivation());
            stmt.setString(3, candidature.getDiplome());
            stmt.setString(4, candidature.getEtat().name());
            stmt.setInt(5, candidature.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Candidature mise à jour avec succès.");
            } else {
                System.out.println("Aucune candidature trouvée avec cet ID.");
            }
        }
    }
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM candidature WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);  // Associe l'ID de la candidature à supprimer
            int rowsDeleted = stmt.executeUpdate();  // Exécute la suppression
            if (rowsDeleted > 0) {
                System.out.println("Candidature supprimée avec succès.");
            } else {
                System.out.println("Aucune candidature trouvée avec cet ID.");
            }
        }
    }


    public List<Candidature> afficher() throws SQLException {
        List<Candidature> candidatures = new ArrayList<>();
        String query = "SELECT * FROM candidature";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Candidature candidature = new Candidature(
                        rs.getInt("id"),
                        rs.getString("cv"),
                        rs.getString("lettre_motivation"),
                        rs.getString("diplome"),
                        EtatCandidature.valueOf(rs.getString("etat"))
                );
                candidatures.add(candidature);
            }
        }
        return candidatures;
    }
}
