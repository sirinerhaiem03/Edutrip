package tn.edutrip.services;

import tn.edutrip.entities.Commentaire;
import tn.edutrip.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCommentaire implements Iservices<Commentaire> {
    Connection con;

    public ServiceCommentaire() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Commentaire commentaire) {
        String query = "INSERT INTO commentaire (id_post, id_etudiant, contenu, date_commentaire) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, commentaire.getId_post());
            ps.setInt(2, commentaire.getId_etudiant());
            ps.setString(3, commentaire.getContenu());
            ps.setDate(4, new java.sql.Date(commentaire.getDate_commentaire().getTime()));

            ps.executeUpdate();
            System.out.println("Commentaire ajouté !");
        } catch (SQLException e) {
            System.err.println("Erreur ajout : " + e.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM commentaire WHERE id_commentaire = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);  // Set the id_commentaire to the query
            ps.executeUpdate();
            System.out.println("Commentaire supprimé !");
        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }
    }

    @Override
    public void update(Commentaire commentaire) {
        String query = "UPDATE commentaire SET contenu = ?, date_commentaire = ? " +
                "WHERE id_commentaire = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, commentaire.getContenu());
            ps.setDate(2, new java.sql.Date(commentaire.getDate_commentaire().getTime()));
            ps.setInt(3, commentaire.getId_commentaire());

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Le commentaire a été mis à jour avec succès !");
            } else {
                System.out.println("Aucun commentaire trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur mise à jour : " + e.getMessage());
        }
    }

    @Override
    public List<Commentaire> getAll() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Parcourir les résultats et ajouter chaque commentaire à la liste
            while (rs.next()) {
                Commentaire commentaire = new Commentaire(
                        rs.getInt("id_commentaire"),
                        rs.getInt("id_post"),
                        rs.getInt("id_etudiant"),
                        rs.getString("contenu"),
                        rs.getDate("date_commentaire")
                );
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commentaires : " + e.getMessage());
        }
        return commentaires;
    }
}
