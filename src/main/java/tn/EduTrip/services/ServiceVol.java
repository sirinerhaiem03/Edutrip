package tn.EduTrip.services;

import tn.EduTrip.entites.Vol;
import tn.EduTrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceVol implements Iservice<Vol> {
    private final Connection con;

    public ServiceVol() {
        con = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Vol vol) throws SQLException {
        String req = "INSERT INTO vol (places_dispo, num_vol, aeroport_depart, aeroport_arrivee, date_depart, date_arrivee, prix_vol) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, vol.getPlaces());
            ps.setString(2, vol.getNumVol());
            ps.setString(3, vol.getDepart());
            ps.setString(4, vol.getArrivee());
            ps.setTimestamp(5, vol.getDateDepart());
            ps.setTimestamp(6, vol.getDateArrivee());
            ps.setDouble(7, vol.getPrix());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("L'ajout du vol a échoué, aucune ligne insérée.");
            }

            // Get the generated ID and set it to the vol object
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vol.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void modifier(Vol vol) throws SQLException {
        String req = "UPDATE vol SET places_dispo=?, num_vol=?, aeroport_depart=?, aeroport_arrivee=?, " +
                     "date_depart=?, date_arrivee=?, prix_vol=? WHERE id_vol=?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, vol.getPlaces());
            ps.setString(2, vol.getNumVol());
            ps.setString(3, vol.getDepart());
            ps.setString(4, vol.getArrivee());
            ps.setTimestamp(5, vol.getDateDepart());
            ps.setTimestamp(6, vol.getDateArrivee());
            ps.setDouble(7, vol.getPrix());
            ps.setInt(8, vol.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("La modification du vol a échoué, aucune ligne mise à jour.");
            }
        }
    }

    @Override
    public void supprimer(int idVol) throws SQLException {
        String req = "DELETE FROM vol WHERE id_vol = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, idVol);
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("La suppression du vol a échoué, aucun vol trouvé avec l'ID: " + idVol);
            }
        }
    }

    @Override
    public List<Vol> afficher() throws SQLException {
        List<Vol> vols = new ArrayList<>();
        String req = "SELECT * FROM vol ORDER BY date_depart";
        
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            
            while (rs.next()) {
                vols.add(extraireVolDuResultSet(rs));
            }
        }
        return vols;
    }

    public Vol readById(int id) throws SQLException {
        String req = "SELECT * FROM vol WHERE id_vol = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraireVolDuResultSet(rs);
                } else {
                    throw new SQLException("Aucun vol trouvé avec l'ID: " + id);
                }
            }
        }
    }

    public List<Vol> rechercherVols(String depart, String arrivee, Date date) throws SQLException {
        StringBuilder reqBuilder = new StringBuilder("SELECT * FROM vol WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (depart != null && !depart.trim().isEmpty()) {
            reqBuilder.append(" AND LOWER(aeroport_depart) LIKE LOWER(?)");
            params.add("%" + depart.trim() + "%");
        }
        if (arrivee != null && !arrivee.trim().isEmpty()) {
            reqBuilder.append(" AND LOWER(aeroport_arrivee) LIKE LOWER(?)");
            params.add("%" + arrivee.trim() + "%");
        }
        if (date != null) {
            reqBuilder.append(" AND DATE(date_depart) = ?");
            params.add(date);
        }
        
        reqBuilder.append(" ORDER BY date_depart");
        
        List<Vol> vols = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(reqBuilder.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vols.add(extraireVolDuResultSet(rs));
                }
            }
        }
        return vols;
    }

    public boolean mettreAJourPlaces(int idVol, int nombrePlaces) throws SQLException {
        String checkPlaces = "SELECT places_dispo FROM vol WHERE id_vol = ? FOR UPDATE";
        String updatePlaces = "UPDATE vol SET places_dispo = places_dispo - ? WHERE id_vol = ? AND places_dispo >= ?";
        
        boolean success = false;
        con.setAutoCommit(false);
        try {
            // Check available seats
            try (PreparedStatement checkPs = con.prepareStatement(checkPlaces)) {
                checkPs.setInt(1, idVol);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (!rs.next() || rs.getInt("places_dispo") < nombrePlaces) {
                        throw new SQLException("Pas assez de places disponibles");
                    }
                }
            }

            // Update seats
            try (PreparedStatement updatePs = con.prepareStatement(updatePlaces)) {
                updatePs.setInt(1, nombrePlaces);
                updatePs.setInt(2, idVol);
                updatePs.setInt(3, nombrePlaces);
                
                success = updatePs.executeUpdate() > 0;
            }
            
            con.commit();
            return success;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private Vol extraireVolDuResultSet(ResultSet rs) throws SQLException {
        return new Vol(
            rs.getInt("id_vol"),
            rs.getInt("places_dispo"),
            rs.getString("num_vol"),
            rs.getString("aeroport_depart"),
            rs.getString("aeroport_arrivee"),
            rs.getTimestamp("date_depart"),
            rs.getTimestamp("date_arrivee"),
            rs.getDouble("prix_vol")
        );
    }

    // Additional utility methods
    public boolean verifierDisponibilite(int idVol, int nombrePlaces) throws SQLException {
        String req = "SELECT places_dispo FROM vol WHERE id_vol = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, idVol);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("places_dispo") >= nombrePlaces;
            }
        }
    }

    public List<Vol> rechercherVolsDisponibles(String depart, String arrivee, Date date, int nombrePlaces) throws SQLException {
        String req = "SELECT * FROM vol WHERE aeroport_depart LIKE ? AND aeroport_arrivee LIKE ? " +
                     "AND DATE(date_depart) = ? AND places_dispo >= ? ORDER BY date_depart";
        
        List<Vol> vols = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, "%" + depart + "%");
            ps.setString(2, "%" + arrivee + "%");
            ps.setDate(3, date);
            ps.setInt(4, nombrePlaces);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vols.add(extraireVolDuResultSet(rs));
                }
            }
        }
        return vols;
    }
}








