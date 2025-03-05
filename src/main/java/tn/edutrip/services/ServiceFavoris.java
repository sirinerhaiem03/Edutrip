package tn.edutrip.services;



import tn.edutrip.entities.Favoris;
import tn.edutrip.utils.MyDatabase;

import java.sql.*;
import java.util.List;

public class ServiceFavoris implements Iservices<Favoris>  {
    Connection con;

    public ServiceFavoris() {
        con = MyDatabase.getInstance().getConnection();
    }


    public void add(Favoris favoris) {
        String query = "INSERT INTO favoris (id_etudiant, id_post) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, favoris.getIdEtudiant());
            stmt.setInt(2, favoris.getIdPost());
            stmt.executeUpdate();
            System.out.println("Post ajouté aux favoris !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout aux favoris : " + e.getMessage());
        }
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void update(Favoris favoris) {

    }

    @Override
    public List<Favoris> getAll() {
        return List.of();
    }

    public void remove(int id_etudiant, int id_post) {
        String query = "DELETE FROM favoris WHERE id_etudiant = ? AND id_post = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id_etudiant);
            stmt.setInt(2, id_post);
            stmt.executeUpdate();
            System.out.println("Post retiré des favoris !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression des favoris : " + e.getMessage());
        }
    }

    public boolean estDejaFavoris(int userId, int postId) {
        String query = "SELECT COUNT(*) FROM favoris WHERE id_etudiant = ? AND id_post = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setInt(2, postId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean estFavoris(int id_etudiant, int id_post) {
        String query = "SELECT COUNT(*) FROM favoris WHERE id_etudiant = ? AND id_post = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id_etudiant);
            stmt.setInt(2, id_post);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification des favoris : " + e.getMessage());
            return false;
        }

}}
