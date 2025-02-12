package tn.EduTrip.services;

import tn.EduTrip.entites.ReserVol;
import tn.EduTrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReserVol implements Iservice<ReserVol> {
    private Connection con;

    public ServiceReserVol() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(ReserVol Rvol) throws SQLException {
        String req = "INSERT INTO reservationvol (id_etudiant, id_vol, date_reservation, statut, prix, mode_paiement) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(req)) {
            st.setInt(1, Rvol.getIdEtudiant());
            st.setInt(2, Rvol.getId_Vol());
            st.setDate(3, new java.sql.Date(Rvol.getDateReservation().getTime()));
            st.setString(4, Rvol.getStatut());
            st.setDouble(5, Rvol.getPrix());
            st.setString(6, Rvol.getModePaiement());

            st.executeUpdate();
            System.out.println("✅ Réservation ajoutée avec succès !");
        }
    }

    @Override
    public void modifier(ReserVol Rvol) throws SQLException {
        String req = "UPDATE reservationvol SET id_etudiant=?, id_vol=?, date_reservation=?, statut=?, prix=?, mode_paiement=? " +
                "WHERE id_reservation=?";
        try (PreparedStatement st = con.prepareStatement(req)) {
            st.setInt(1, Rvol.getIdEtudiant());
            st.setInt(2, Rvol.getId_Vol());
            st.setDate(3, new java.sql.Date(Rvol.getDateReservation().getTime()));
            st.setString(4, Rvol.getStatut());
            st.setDouble(5, Rvol.getPrix());
            st.setString(6, Rvol.getModePaiement());
            st.setInt(7, Rvol.getIdReservation());

            st.executeUpdate();
            System.out.println("✅ Réservation modifiée avec succès !");
        }
    }

    @Override
    public void supprimer(int idReservation) throws SQLException {
        String req = "DELETE FROM reservationvol WHERE id_reservation=?";

        try (PreparedStatement st = con.prepareStatement(req)) {
            st.setInt(1, idReservation);
            st.executeUpdate();
            System.out.println("🗑️ Réservation supprimée avec succès !");
        }
    }


    @Override
    public List<ReserVol> afficher() throws SQLException {
        List<ReserVol> reservations = new ArrayList<>();
        String req = "SELECT * FROM reservationvol";

        try (PreparedStatement st = con.prepareStatement(req);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                ReserVol res = new ReserVol(
                        rs.getInt("id_reservation"),
                        rs.getInt("id_etudiant"),
                        rs.getInt("id_vol"),
                        rs.getDate("date_reservation"),
                        rs.getString("statut"),
                        rs.getDouble("prix"),
                        rs.getString("mode_paiement")
                );
                reservations.add(res);
            }
        }
        return reservations;
    }
}
