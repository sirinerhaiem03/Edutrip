package tn.edutrip.services;

import tn.edutrip.entities.Agence;
import tn.edutrip.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAgence implements Iservice<Agence> {
    private final Connection connection;


    public ServiceAgence() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Agence agence) {
        String req = "INSERT INTO agence (nomAg, adresseAg, telephoneAg, emailAg, descriptionAg, date_creation) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
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

    @Override
    public boolean update(Agence agence) {
        // Mise à jour de l'agence en utilisant l'ID
        String query = "UPDATE agence SET nomAg = ?, adresseAg = ?, telephoneAg = ?, emailAg = ?, descriptionAg = ?, date_creation = ? WHERE id_agence = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, agence.getNomAg());
            preparedStatement.setString(2, agence.getAdresseAg());
            preparedStatement.setInt(3, agence.getTelephoneAg());
            preparedStatement.setString(4, agence.getEmailAg());
            preparedStatement.setString(5, agence.getDescriptionAg());
            preparedStatement.setDate(6, new java.sql.Date(agence.getDateCreation().getTime()));
            preparedStatement.setInt(7, agence.getIdAgence());  // Utilisation de l'ID pour la mise à jour

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Retourne true si l'agence a été mise à jour
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'agence : " + e.getMessage());
            return false;
        }
    }

    @Override
    public void remove(int id_agence) {
        try {
            // Suppression des packs associés à l'agence
            String queryPack = "DELETE FROM pack_agence WHERE id_agence = ?";
            try (PreparedStatement psPack = connection.prepareStatement(queryPack)) {
                psPack.setInt(1, id_agence);
                psPack.executeUpdate();
            }

            // Suppression de l'agence
            String queryAgence = "DELETE FROM agence WHERE id_agence = ?";
            try (PreparedStatement psAgence = connection.prepareStatement(queryAgence)) {
                psAgence.setInt(1, id_agence);
                psAgence.executeUpdate();
            }

            System.out.println("Agence supprimée avec succès !");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'agence ou des packs : " + e.getMessage());
        }
    }

    @Override
    public List<Agence> afficher() {
        List<Agence> agences = new ArrayList<>();
        String req = "SELECT * FROM agence";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(req)) {

            // Parcours des résultats et ajout dans la liste
            while (rs.next()) {
                Agence agence = new Agence(
                        rs.getInt("id_agence"),
                        rs.getString("nomAg"),
                        rs.getString("adresseAg"),
                        rs.getInt("telephoneAg"),
                        rs.getString("emailAg"),
                        rs.getString("descriptionAg"),
                        rs.getDate("date_creation")
                );
                agences.add(agence);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des agences : " + e.getMessage());
        }
        return agences;
    }


    public Agence getAgenceByNomEtAdresse(String nom, String adresse) {
        Agence agence = null;
        String query = "SELECT * FROM agence WHERE nomAg = ? AND adresseAg = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, nom);
            ps.setString(2, adresse);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                agence = new Agence(
                        rs.getInt("id_agence"),
                        rs.getString("nomAg"),
                        rs.getString("adresseAg"),
                        rs.getInt("telephoneAg"),
                        rs.getString("emailAg"),
                        rs.getString("descriptionAg"),
                        rs.getDate("date_creation")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'agence : " + e.getMessage());
        }

        return agence;
    }


    public Agence getByName(String nomAg) {
        Agence agence = null;
        String req = "SELECT * FROM agence WHERE nomAg = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, nomAg);
            ResultSet rs = ps.executeQuery();


            if (rs.next()) {
                agence = new Agence(
                        rs.getInt("id_agence"),
                        rs.getString("nomAg"),
                        rs.getString("adresseAg"),
                        rs.getInt("telephoneAg"),
                        rs.getString("emailAg"),
                        rs.getString("descriptionAg"),
                        rs.getDate("date_creation")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'agence : " + e.getMessage());
        }

        return agence;
    }



}
