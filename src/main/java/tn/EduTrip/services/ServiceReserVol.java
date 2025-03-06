package tn.EduTrip.services;

import tn.EduTrip.entites.ReserVol;
import tn.EduTrip.utils.MyDatabase;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReserVol implements Iservice<ReserVol> {
    private static Connection con;

    public ServiceReserVol() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(ReserVol Rvol) throws SQLException {
        String req = "INSERT INTO reservationvol (id_etudiant, id_vol ,num_vol, date_reservation, statut, prix, mode_paiement, nom, prenom, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement st = con.prepareStatement(req)) {
            st.setInt(1, Rvol.getIdEtudiant());
            st.setInt(2, Rvol.getId_vol());
            st.setString(2, Rvol.getNumVol()); // <-- String ici
            st.setDate(3, new java.sql.Date(Rvol.getDateReservation().getTime()));
            st.setString(4, Rvol.getStatut());
            st.setDouble(5, Rvol.getPrix());
            st.setString(6, Rvol.getModePaiement());
            st.setString(7, Rvol.getNom());
            st.setString(8, Rvol.getPrenom());
            st.setString(9, Rvol.getEmail());

            st.executeUpdate();
            System.out.println("✅ Réservation ajoutée avec succès !");
        }
    }
    @Override
    public void modifier(ReserVol Rvol) throws SQLException {
        String req = "UPDATE reservationvol SET " +
                "id_etudiant = ?, " +
                "num_vol = ?, " +          // Utilisez num_vol au lieu de id_vol
                "date_reservation = ?, " +
                "statut = ?, " +
                "prix = ?, " +
                "mode_paiement = ?, " +
                "nom = ?, " +
                "prenom = ?, " +
                "email = ? " +
                "WHERE id_reservation = ?"; // Clause WHERE sur l'ID de réservation

        try (PreparedStatement st = con.prepareStatement(req)) {
            st.setInt(1, Rvol.getIdEtudiant());
            st.setString(2, Rvol.getNumVol());   // String pour num_vol
            st.setTimestamp(3, new java.sql.Timestamp(Rvol.getDateReservation().getTime()));
            st.setString(4, Rvol.getStatut());
            st.setDouble(5, Rvol.getPrix());
            st.setString(6, Rvol.getModePaiement());
            st.setString(7, Rvol.getNom());
            st.setString(8, Rvol.getPrenom());
            st.setString(9, Rvol.getEmail());
            st.setInt(10, Rvol.getIdReservation()); // ID de la réservation à modifier

            int rowsUpdated = st.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Réservation modifiée avec succès !");
            } else {
                System.out.println("⚠️ Aucune réservation trouvée avec l'ID : " + Rvol.getIdReservation());
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la modification : " + e.getMessage());
            throw e; // Propagation de l'exception
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
                        rs.getInt("id_vol"),
                        rs.getInt("id_etudiant"),
                        rs.getString("numVol"),
                        rs.getDate("date_reservation"),
                        rs.getString("statut"),
                        rs.getDouble("prix"),
                        rs.getString("mode_paiement"),
                        rs.getString("nom"),         // Récupération du nom
                        rs.getString("prenom"),      // Récupération du prénom
                        rs.getString("email")        // Récupération de l'email
                );
                reservations.add(res);
            }
        }
        return reservations;
    }

    public static void getReservationsWithVols() {
        try {
            String query = "SELECT r.id_reservation, r.id_etudiant, r.date_reservation, r.statut, r.prix, r.mode_paiement, " +
                    "v.id_vol, v.num_vol, v.aeroport_depart, v.aeroport_arrivee, v.date_depart, v.date_arrivee, v.prix_vol, " +
                    "r.nom, r.prenom, r.email " +  // Ajout des champs nom, prenom, email
                    "FROM reservationvol r " +
                    "JOIN vol v ON r.id_vol = v.id_vol";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                System.out.println("📌 Réservation ID: " + rs.getInt("id_reservation"));
                System.out.println("   - Étudiant ID: " + rs.getInt("id_etudiant"));
                System.out.println("   - Date de réservation: " + rs.getTimestamp("date_reservation"));
                System.out.println("   - Statut: " + rs.getString("statut"));
                System.out.println("   - Prix payé: " + rs.getDouble("prix"));
                System.out.println("   - Mode de paiement: " + rs.getString("mode_paiement"));

                System.out.println("   - Nom: " + rs.getString("nom"));      // Affichage du nom
                System.out.println("   - Prénom: " + rs.getString("prenom"));  // Affichage du prénom
                System.out.println("   - Email: " + rs.getString("email"));    // Affichage de l'email

                System.out.println("✈️ Vol associé :");
                System.out.println("   - Vol ID: " + rs.getInt("id_vol"));
                System.out.println("   - Numéro de vol: " + rs.getString("num_vol"));
                System.out.println("   - Départ: " + rs.getString("aeroport_depart") + " → " + rs.getString("aeroport_arrivee"));
                System.out.println("   - Date de départ: " + rs.getTimestamp("date_depart"));
                System.out.println("   - Date d’arrivée: " + rs.getTimestamp("date_arrivee"));
                System.out.println("   - Prix du vol: " + rs.getDouble("prix_vol"));
                System.out.println("--------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des réservations avec vols : " + e.getMessage());
        }
    }
}
