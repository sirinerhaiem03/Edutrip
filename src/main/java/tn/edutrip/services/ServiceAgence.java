package tn.edutrip.services;

import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAgence implements Iservice<Agence> {
    private Connection connection;

    // Constructeur qui initialise la connexion à la base de données
    public ServiceAgence() {
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void add(Agence agence) {
        String req = "INSERT INTO agence (nomAg, adresseAg, telephoneAg, emailAg, descriptionAg, date_creation) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, agence.getNomAg());
            preparedStatement.setString(2, agence.getAdresseAg());
            preparedStatement.setInt(3, agence.getTelephoneAg());
            preparedStatement.setString(4, agence.getEmailAg());
            preparedStatement.setString(5, agence.getDescriptionAg());
            preparedStatement.setDate(6, new java.sql.Date(agence.getDateCreation().getTime()));

            preparedStatement.executeUpdate();
            System.out.println("Agence ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'agence : " + e.getMessage());
        }
    }

    public void update(Agence agence) {
        String query = "UPDATE agence SET nomAg = ?, adresseAg = ?, telephoneAg = ?, emailAg = ?, descriptionAg = ?, date_creation = ? WHERE id_agence = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, agence.getNomAg());
            preparedStatement.setString(2, agence.getAdresseAg());
            preparedStatement.setInt(3, agence.getTelephoneAg());
            preparedStatement.setString(4, agence.getEmailAg());
            preparedStatement.setString(5, agence.getDescriptionAg());
            preparedStatement.setDate(6, new java.sql.Date(agence.getDateCreation().getTime()));
            preparedStatement.setInt(7, agence.getIdAgence()); // L'ID doit être en dernier

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("L'agence a été mise à jour avec succès.");
            } else {
                System.out.println("Aucune agence trouvée avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }


    @Override
    public void remove(int id_agence){
        try {
            String query = "DELETE FROM agence WHERE id_agence = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id_agence);  // Set the id_agence to the query
            ps.executeUpdate();
            System.out.println("Agence supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public List<Agence> afficher() {
        List<Agence> agences = new ArrayList<>();
        String req = "SELECT * FROM agence";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {

            while (rs.next()) {
                Agence agence = new Agence();
                agence.setIdAgence(rs.getInt("id_agence"));
                agence.setNomAg(rs.getString("nomAg"));
                agence.setAdresseAg(rs.getString("adresseAg"));
                agence.setTelephoneAg(rs.getInt("telephoneAg"));
                agence.setEmailAg(rs.getString("emailAg"));
                agence.setDescriptionAg(rs.getString("descriptionAg"));
                agence.setDateCreation(rs.getDate("date_creation"));
                agences.add(agence);
                agence.setPacks(ServicePack_agence.getPacksByAgence(agence.getIdAgence()));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des agences : " + e.getMessage());
        }
        return agences;
    }

}
