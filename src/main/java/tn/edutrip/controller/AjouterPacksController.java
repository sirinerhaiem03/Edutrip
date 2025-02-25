package tn.edutrip.controller;

import tn.edutrip.entities.Pack_agence;
import tn.edutrip.entities.Agence;
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
import java.util.List;

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

    @FXML
    private ChoiceBox<Agence> agenceChoiceBox;

    private final ServicePack_agence servicePack_agence = new ServicePack_agence();

    @FXML
    public void initialize() {
        if (statusField != null) {
            statusField.getItems().addAll("disponible", "indisponible");
        }

        loadAgences();

        agenceChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Sélectionné : " + newValue.getIdAgence() + " - " + newValue.getNomAg());
            }
        });
    }

    private void loadAgences() {
        List<Agence> agences = servicePack_agence.getAllAgences();
        if (agences != null && !agences.isEmpty()) {
            agenceChoiceBox.getItems().setAll(agences);
        } else {
            System.out.println("Aucune agence trouvée !");
        }
    }

    @FXML
    void ajouterPack(ActionEvent event) {
        // Vérification des champs
        if (nomPkField.getText().isEmpty() || descriptionPkField.getText().isEmpty() || prixField.getText().isEmpty() ||
                dureeField.getText().isEmpty() || serviceField.getText().isEmpty() || dateDajoutField.getText().isEmpty() ||
                statusField.getValue() == null || agenceChoiceBox.getValue() == null) {
            showErrorAlert("Tous les champs doivent être remplis.");
            return;
        }

        // Vérification des valeurs dans nom, description et services
        if (!validateInputs()) {
            return;
        }

        try {
            String nomPk = nomPkField.getText();
            String descriptionPk = descriptionPkField.getText();

            // Vérification du prix positif
            BigDecimal prix = new BigDecimal(prixField.getText());
            if (prix.compareTo(BigDecimal.ZERO) <= 0) {
                showErrorAlert("Le prix doit être un nombre positif.");
                return;
            }

            // Vérification de la durée positive
            int duree = Integer.parseInt(dureeField.getText());
            if (duree <= 0) {
                showErrorAlert("La durée doit être un nombre positif.");
                return;
            }

            String services = serviceField.getText();
            Date dateAjout = validateDate(dateDajoutField.getText());

            if (dateAjout == null) {
                showErrorAlert("Le format de la date est incorrect. Utilisez le format yyyy-MM-dd.");
                return;
            }

            String status = statusField.getValue().trim().toLowerCase();
            Pack_agence.Status statusEnum = Pack_agence.Status.valueOf(status);

            Agence selectedAgence = agenceChoiceBox.getValue();
            int idAgence = selectedAgence.getIdAgence();

            Pack_agence newPack = new Pack_agence();
            newPack.setNomPk(nomPk);
            newPack.setDescriptionPk(descriptionPk);
            newPack.setPrix(prix);
            newPack.setDuree(duree);
            newPack.setServices_inclus(services);
            newPack.setDate_ajout(dateAjout);
            newPack.setStatus(statusEnum);
            newPack.setId_agence(idAgence);

            servicePack_agence.add(newPack);
            showSuccessAlert("Pack ajouté avec succès!");
            clearFields();

            // Charger la liste des packs
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

    /**
     * Valide si les champs ne contiennent pas de chiffres (nom, description, services).
     */
    private boolean validateInputs() {
        String nom = nomPkField.getText().trim();
        String description = descriptionPkField.getText().trim();
        String services = serviceField.getText().trim();

        // Vérifier si ces champs contiennent des chiffres
        if (nom.matches(".*\\d.*")) {
            showErrorAlert("Le nom du pack ne doit pas contenir de chiffres.");
            return false;
        }

        if (description.matches(".*\\d.*")) {
            showErrorAlert("La description ne doit pas contenir de chiffres.");
            return false;
        }

        if (services.matches(".*\\d.*")) {
            showErrorAlert("Les services inclus ne doivent pas contenir de chiffres.");
            return false;
        }

        return true;
    }

    /**
     * Valide et convertit la date d'ajout.
     */
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
        agenceChoiceBox.setValue(null);
    }
}
