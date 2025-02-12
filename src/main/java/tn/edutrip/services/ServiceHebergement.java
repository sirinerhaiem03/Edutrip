package tn.edutrip.services;

import tn.edutrip.entities.Hebergement;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergement implements IService<Hebergement> {
    private Connection connection;

    public ServiceHebergement() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Hebergement h) {
        String req = "INSERT INTO hebergement (nomh, capaciteh, typeh, adressh, disponibleh, descriptionh, imageh, prixh) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, h.getNomh());
            ps.setInt(2, h.getCapaciteh());
            ps.setString(3, h.getTypeh());
            ps.setString(4, h.getAdressh());
            ps.setString(5, h.getDisponibleh());
            ps.setString(6, h.getDescriptionh());
            ps.setString(7, h.getImageh());
            ps.setFloat(8, h.getPrixh());
            ps.executeUpdate();
            System.out.println("Hébergement ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur ajout : " + e.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        String req = "DELETE FROM hebergement WHERE id_hebergement = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Hébergement supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }
    }

    @Override
    public void update(Hebergement h) {
        String req = "UPDATE hebergement SET nomh=?, capaciteh=?, typeh=?, adressh=?, disponibleh=?, descriptionh=?, imageh=?, prixh=? WHERE id_hebergement=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, h.getNomh());
            ps.setInt(2, h.getCapaciteh());
            ps.setString(3, h.getTypeh());
            ps.setString(4, h.getAdressh());
            ps.setString(5, h.getDisponibleh());
            ps.setString(6, h.getDescriptionh());
            ps.setString(7, h.getImageh());
            ps.setFloat(8, h.getPrixh());
            ps.setInt(9, h.getId_hebergement());
            ps.executeUpdate();
            System.out.println("Hébergement modifié avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur modification : " + e.getMessage());
        }
    }
    @Override
    public List<Hebergement> getAll() {
        List<Hebergement> hebergements = new ArrayList<>();
        String req = "SELECT * FROM hebergement";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id_hebergement"),
                        rs.getString("nomh"),
                        rs.getInt("capaciteh"),
                        rs.getString("typeh"),
                        rs.getString("adressh"),
                        rs.getString("disponibleh"),
                        rs.getString("descriptionh"),
                        rs.getString("imageh"),
                        rs.getFloat("prixh")
                );
                hebergements.add(h);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des hébergements : " + e.getMessage());
        }

        return hebergements;
    }


}