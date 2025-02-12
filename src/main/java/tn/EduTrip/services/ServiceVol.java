package tn.EduTrip.services;

import tn.EduTrip.entites.Vol;
import tn.EduTrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceVol implements Iservice<Vol> {
    Connection con;

    public ServiceVol() {
        con = MyDatabase.getInstance().getConnection();

    }

    @Override
    public void ajouter(Vol vol) throws SQLException {
        String req = "INSERT INTO vol (num_vol, aeroport_depart, aeroport_arrivee, date_depart, date_arrivee, prix_vol, places_dispo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = con.prepareStatement(req) ;
            st.setString(1, vol.getNumVol());
            st.setString(2, vol.getAeroportDepart());
            st.setString(3, vol.getAeroportArrivee());
            st.setTimestamp(4, vol.getDateDepart());
            st.setTimestamp(5, vol.getDateArrivee());
            st.setDouble(6, vol.getPrix());
            st.setInt(7, vol.getPlacesDispo());
            st.executeUpdate();
            System.out.println(" Vol ajouté avec succès !");

        }


    @Override
    public void modifier(Vol vol) throws SQLException {

        String req = "UPDATE vol SET num_vol = ?, aeroport_depart = ?, aeroport_arrivee = ?, " +
                "date_depart = ?, date_arrivee = ?, prix_vol = ?, places_dispo = ? WHERE id_vol = ?";

                PreparedStatement st = con.prepareStatement(req);
            st.setString(1, vol.getNumVol());
            st.setString(2, vol.getAeroportDepart());
            st.setString(3, vol.getAeroportArrivee());
            st.setTimestamp(4, vol.getDateDepart());
            st.setTimestamp(5, vol.getDateArrivee());
            st.setDouble(6, vol.getPrix());
            st.setInt(7, vol.getPlacesDispo());
            st.setInt(8, vol.getId_Vol());

            int rowsUpdated = st.executeUpdate();
            System.out.println("Vol mis à jour avec succès !");
        }


    @Override
    public void supprimer(int id_vol) throws SQLException {

        String req = "DELETE FROM vol WHERE id_vol = ?";
        try (PreparedStatement st = con.prepareStatement(req)) {
            st.setInt(1, id_vol);

            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Vol supprimé avec succès !");
            } else {
                System.out.println("Aucun vol trouvé avec cet ID.");
            }
        }

    }

    @Override
    public List<Vol> afficher() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String req = "SELECT * FROM vol";
        Statement st= con.createStatement();
        ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Vol vol = new Vol(
                        rs.getInt("id_vol"),
                        rs.getInt("places_dispo"),
                        rs.getString("num_vol"),
                        rs.getString("aeroport_depart"),
                        rs.getString("aeroport_arrivee"),
                        rs.getTimestamp("date_depart"),
                        rs.getTimestamp("date_arrivee"),
                        rs.getDouble("prix_vol")
                );
                vols.add(vol);
            }

        return vols;
    }
}