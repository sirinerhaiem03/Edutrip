package tn.EduTrip.services;

import tn.EduTrip.entites.Perturbation;
import tn.EduTrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePerturbation {
    private final Connection con;

    public ServicePerturbation() {
        con = MyDatabase.getInstance().getConnection();
    }

    // ✅ Add a new perturbation
    public void ajouter(Perturbation perturbation) throws SQLException {
        String req = "INSERT INTO perturbation (id_vol, type_perturbation, description, date_annonce) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, perturbation.getIdVol());
            ps.setString(2, perturbation.getTypePerturbation());
            ps.setString(3, perturbation.getDescription());
            ps.setTimestamp(4, perturbation.getDateAnnonce());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("L'ajout de la perturbation a échoué.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    perturbation.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // ✅ Get all perturbations
    public List<Perturbation> afficher() throws SQLException {
        List<Perturbation> perturbations = new ArrayList<>();
        String req = "SELECT * FROM perturbation ORDER BY date_annonce DESC";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                perturbations.add(new Perturbation(
                        rs.getInt("id_perturbation"),
                        rs.getInt("id_vol"),
                        rs.getString("type_perturbation"),
                        rs.getString("description"),
                        rs.getTimestamp("date_annonce")
                ));
            }
        }
        return perturbations;
    }
}
