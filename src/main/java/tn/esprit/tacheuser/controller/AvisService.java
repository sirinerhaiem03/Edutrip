package tn.esprit.tacheuser.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.utils.MySQLConnection;

public class AvisService {
    private Connection conn = MySQLConnection.getInstance().getConnection();

    public AvisService() {}

    // Ajouter un avis
    public void addAvis(Avis avis) {
        String query = "INSERT INTO avis (user_id, commentaire, note, date_creation) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pst = this.conn.prepareStatement(query);
            pst.setInt(1, avis.getUserId());
            pst.setString(2, avis.getCommentaire());
            pst.setInt(3, avis.getNote());
            pst.setString(4, avis.getDateCreation());
            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("✅ Avis ajouté avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'avis : " + e.getMessage());
        }
    }

    // Supprimer un avis
    public void deleteAvis(int avisId) {
        String query = "DELETE FROM avis WHERE id = ?";
        try {
            PreparedStatement pst = this.conn.prepareStatement(query);
            pst.setInt(1, avisId);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Avis supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun avis trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de l'avis : " + e.getMessage());
        }
    }

    // Mettre à jour un avis
    public void updateAvis(Avis avis) {
        String query = "UPDATE avis SET commentaire = ?, note = ? WHERE id = ?";
        try {
            PreparedStatement pst = this.conn.prepareStatement(query);
            pst.setString(1, avis.getCommentaire());
            pst.setInt(2, avis.getNote());
            pst.setInt(3, avis.getId());
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Avis mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun avis trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'avis : " + e.getMessage());
        }
    }

    // Récupérer tous les avis
    public List<Avis> getAllAvis() {
        List<Avis> avisList = new ArrayList<>();
        String query = "SELECT * FROM avis";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Avis avis = new Avis(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("commentaire"),
                        rs.getInt("note"),
                        rs.getString("date_creation")
                );
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des avis : " + e.getMessage());
        }
        return avisList;
    }
}