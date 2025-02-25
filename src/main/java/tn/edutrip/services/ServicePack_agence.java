package tn.edutrip.services;

import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.utils.MyDatabase;
import java.math.BigDecimal;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePack_agence {
    private static Connection connection;

    public ServicePack_agence() {
        connection = MyDatabase.getInstance().getConnection();
    }


    public void add(Pack_agence pack) {

        String checkQuery = "SELECT COUNT(*) FROM agence WHERE id_agence = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, pack.getId_agence());


            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {

                    String req = "INSERT INTO pack_agence (nomPk, descriptionPk, prix, duree, services_inclus, date_ajout, status, id_agence) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
                        preparedStatement.setString(1, pack.getNomPk());
                        preparedStatement.setString(2, pack.getDescriptionPk());
                        preparedStatement.setBigDecimal(3, pack.getPrix());
                        preparedStatement.setInt(4, pack.getDuree());
                        preparedStatement.setString(5, pack.getServices_inclus());
                        preparedStatement.setDate(6, new java.sql.Date(pack.getDate_ajout().getTime()));
                        preparedStatement.setString(7, pack.getStatus().toString());
                        preparedStatement.setInt(8, pack.getId_agence());


                        preparedStatement.executeUpdate();
                        System.out.println("Pack ajouté avec succès.");
                    }
                } else {

                    System.out.println("Erreur : id_agence n'existe pas dans la table agence.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du pack : " + e.getMessage());
        }
    }



    public void update(Pack_agence pack) {
        String query = "UPDATE pack_agence SET nomPk = ?, descriptionPk = ?, prix = ?, duree = ?, services_inclus = ?, date_ajout = ?, status = ?, id_agence = ? WHERE id_pack = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, pack.getNomPk());
            preparedStatement.setString(2, pack.getDescriptionPk());
            preparedStatement.setBigDecimal(3, pack.getPrix());
            preparedStatement.setInt(4, pack.getDuree());
            preparedStatement.setString(5, pack.getServices_inclus());
            preparedStatement.setDate(6, new java.sql.Date(pack.getDate_ajout().getTime()));
            preparedStatement.setString(7, pack.getStatus().toString());
            preparedStatement.setInt(8, pack.getId_agence());
            preparedStatement.setInt(9, pack.getId_pack()); // ID en dernier

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Le pack a été mis à jour avec succès.");
            } else {
                System.out.println("Aucun pack trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du pack : " + e.getMessage());
        }
    }


    public boolean remove(int id_pack) {
        try {
            String query = "DELETE FROM pack_agence WHERE id_pack = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id_pack);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Pack supprimé avec succès !");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du pack : " + e.getMessage());
        }
        return false;
    }


    public void afficherPacks() {
        String query = "SELECT p.* FROM pack_agence p";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idPack = rs.getInt("id_pack");
                String nomPack = rs.getString("nomPk");
                String descriptionPack = rs.getString("descriptionPk");
                BigDecimal prix = rs.getBigDecimal("prix");
                int duree = rs.getInt("duree");
                String servicesInclus = rs.getString("services_inclus");
                Date dateAjout = rs.getDate("date_ajout");
                Pack_agence.Status status = Pack_agence.Status.valueOf(rs.getString("status"));


                Pack_agence pack = new Pack_agence(idPack, nomPack, descriptionPack, prix, duree, servicesInclus, dateAjout, status, rs.getInt("id_agence"));


                System.out.println(pack);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Pack_agence> getPacksByAgence(int id_agence) {
        List<Pack_agence> packs = new ArrayList<>();
        String query = "SELECT * FROM pack_agence WHERE id_agence = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id_agence);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pack_agence pack = new Pack_agence(
                            rs.getInt("id_pack"),
                            rs.getString("nomPk"),
                            rs.getString("descriptionPk"),
                            rs.getBigDecimal("prix"),
                            rs.getInt("duree"),
                            rs.getString("services_inclus"),
                            rs.getDate("date_ajout"),
                            Pack_agence.Status.valueOf(rs.getString("status")),
                            rs.getInt("id_agence")
                    );
                    packs.add(pack);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des packs : " + e.getMessage());
        }
        return packs;
    }
    public List<Agence> getAllAgences() {
        List<Agence> agences = new ArrayList<>();
        String query = "SELECT * FROM agence";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
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
            e.printStackTrace();
        }
        return agences;
    }
    public List<Pack_agence> getAllPacks() {
        List<Pack_agence> packs = new ArrayList<>();
        String query = "SELECT * FROM pack_agence";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {


            while (rs.next()) {
                Pack_agence pack = new Pack_agence(
                        rs.getInt("id_pack"),
                        rs.getString("nomPk"),
                        rs.getString("descriptionPk"),
                        rs.getBigDecimal("prix"),
                        rs.getInt("duree"),
                        rs.getString("services_inclus"),
                        rs.getDate("date_ajout"),
                        Pack_agence.Status.valueOf(rs.getString("status")),
                        rs.getInt("id_agence")
                );
                packs.add(pack);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all packs: " + e.getMessage());
        }

        return packs;
    }

}
