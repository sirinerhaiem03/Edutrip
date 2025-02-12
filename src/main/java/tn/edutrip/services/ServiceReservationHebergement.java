package tn.edutrip.services;



import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservationHebergement implements IService<Reservation_hebergement> {
    private Connection connection;

    public ServiceReservationHebergement() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Reservation_hebergement r) {
        String req = "INSERT INTO reservation_hebergement (id_hebergement, status, date_f, date_d) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, r.getId_hebergement());
            ps.setString(2, r.getStatus());
            ps.setDate(3, new java.sql.Date(r.getDate_f().getTime()));
            ps.setDate(4, new java.sql.Date(r.getDate_d().getTime()));
            ps.executeUpdate();
            System.out.println("Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur ajout : " + e.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        String req = "DELETE FROM reservation_hebergement WHERE id_reservationh = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Réservation supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }
    }

    @Override
    public void update(Reservation_hebergement r) {
        String req = "UPDATE reservation_hebergement SET id_hebergement=?, status=?, date_f=?, date_d=? WHERE id_reservationh=?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, r.getId_hebergement());
            ps.setString(2, r.getStatus());
            ps.setDate(3, new java.sql.Date(r.getDate_f().getTime()));
            ps.setDate(4, new java.sql.Date(r.getDate_d().getTime()));
            ps.setInt(5, r.getId_reservationh());
            ps.executeUpdate();
            System.out.println("Réservation modifiée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur modification : " + e.getMessage());
        }
    }

    @Override
    public List<Reservation_hebergement> getAll() {
        List<Reservation_hebergement> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservation_hebergement";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Reservation_hebergement r = new Reservation_hebergement(
                        rs.getInt("id_reservationh"),
                        rs.getInt("id_hebergement"),
                        rs.getDate("date_d"),
                        rs.getDate("date_f"),
                        rs.getString("status")
                );
                reservations.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }

        return reservations;
    }

}
