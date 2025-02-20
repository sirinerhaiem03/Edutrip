package tn.edutrip.controller;

import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServicePack_agence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AjouterPacksController {

    @FXML
    private TextField nomPkField;

    @FXML
    private TextField descriptionPkField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField dureeField;

    @FXML
    private TextField serviceField;

    @FXML
    private TextField dateDajoutField;

    @FXML
    private ChoiceBox<String> statusField;

    private final ServicePack_agence servicePack_agence = new ServicePack_agence();

    @FXML
    public void initialize() {

        if (statusField != null) {
            statusField.getItems().addAll("disponible", "indisponible");
        }
    }

    @FXML
    void ajouterPack(ActionEvent event) {

        if (nomPkField.getText().isEmpty() || descriptionPkField.getText().isEmpty() || prixField.getText().isEmpty() ||
                dureeField.getText().isEmpty() || serviceField.getText().isEmpty() || dateDajoutField.getText().isEmpty() ||
                statusField.getValue() == null) {
            showErrorAlert("Tous les champs doivent être remplis.");
            return;
        }

        try {

            String nomPk = nomPkField.getText();
            String descriptionPk = descriptionPkField.getText();


            BigDecimal prix;
            try {
                prix = new BigDecimal(prixField.getText());
            } catch (NumberFormatException e) {
                showErrorAlert("Le prix doit être un nombre valide.");
                return;
            }


            int duree;
            try {
                duree = Integer.parseInt(dureeField.getText());
            } catch (NumberFormatException e) {
                showErrorAlert("La durée doit être un nombre entier.");
                return;
            }

            String services = serviceField.getText();


            Date dateAjout = validateDate(dateDajoutField.getText());
            if (dateAjout == null) {
                showErrorAlert("Le format de la date est incorrect. Utilisez le format yyyy-MM-dd.");
                return;
            }

            String status = statusField.getValue().trim().toLowerCase();  // Statut en minuscule


            Pack_agence.Status statusEnum;
            try {
                statusEnum = Pack_agence.Status.valueOf(status);  // Convertir le statut en Enum
            } catch (IllegalArgumentException e) {
                showErrorAlert("Erreur : Statut invalide.");
                return;
            }


            Pack_agence newPack = new Pack_agence();
            newPack.setNomPk(nomPk);
            newPack.setDescriptionPk(descriptionPk);
            newPack.setPrix(prix);
            newPack.setDuree(duree);
            newPack.setServices_inclus(services);
            newPack.setDate_ajout(dateAjout);
            newPack.setStatus(statusEnum);


            servicePack_agence.add(newPack);


            showSuccessAlert("Pack ajouté avec succès!");


            clearFields();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListPacks.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Liste des packs");
            stage.setScene(new Scene(root));
            stage.show();


            ((Stage) nomPkField.getScene().getWindow()).close();

        } catch (Exception e) {
            showErrorAlert("Erreur lors de l'ajout du pack : " + e.getMessage());
        }
    }


    private Date validateDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void clearFields() {
        nomPkField.clear();
        descriptionPkField.clear();
        prixField.clear();
        dureeField.clear();
        serviceField.clear();
        dateDajoutField.clear();
        statusField.setValue(null);
    }
}
