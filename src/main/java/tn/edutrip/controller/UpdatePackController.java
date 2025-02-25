package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServicePack_agence;

import java.math.BigDecimal;

public class UpdatePackController {
    @FXML
    private TextField nomPkField;
    @FXML
    private TextField descriptionPkField;
    @FXML
    private TextField prixField;
    @FXML
    private TextField dureeField;
    @FXML
    private TextField servicesInclusField;
    @FXML
    private Button updateButton;

    private Pack_agence currentPack;
    private final ServicePack_agence servicePack = new ServicePack_agence();

    public void loadPack(Pack_agence pack) {
        this.currentPack = pack;
        nomPkField.setText(pack.getNomPk());
        descriptionPkField.setText(pack.getDescriptionPk());
        prixField.setText(pack.getPrix().toString());
        dureeField.setText(String.valueOf(pack.getDuree()));
        servicesInclusField.setText(pack.getServices_inclus());
    }

    @FXML
    private void updatePack() {
        if (currentPack == null) {
            showAlert("Erreur", "Aucun pack sélectionné pour la mise à jour.", Alert.AlertType.ERROR);
            return;
        }

        // Vérification des champs
        if (!validateInputs()) {
            return;
        }

        try {
            currentPack.setNomPk(nomPkField.getText().trim());
            currentPack.setDescriptionPk(descriptionPkField.getText().trim());
            currentPack.setPrix(new BigDecimal(prixField.getText().trim()));
            currentPack.setDuree(Integer.parseInt(dureeField.getText().trim()));
            currentPack.setServices_inclus(servicesInclusField.getText().trim());

            servicePack.update(currentPack);
            showAlert("Succès", "Le pack a été mis à jour avec succès.", Alert.AlertType.INFORMATION);

            // Fermer la fenêtre après mise à jour
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Vérifie si les champs sont valides avant la mise à jour.
     */
    private boolean validateInputs() {
        String nom = nomPkField.getText().trim();
        String description = descriptionPkField.getText().trim();
        String prixText = prixField.getText().trim();
        String dureeText = dureeField.getText().trim();
        String services = servicesInclusField.getText().trim();

        if (nom.isEmpty() || description.isEmpty() || prixText.isEmpty() || dureeText.isEmpty() || services.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification des chiffres dans le nom, description et services
        if (nom.matches(".*\\d.*")) {
            showAlert("Erreur", "Le nom du pack ne doit pas contenir de chiffres.", Alert.AlertType.ERROR);
            return false;
        }

        if (description.matches(".*\\d.*")) {
            showAlert("Erreur", "La description ne doit pas contenir de chiffres.", Alert.AlertType.ERROR);
            return false;
        }

        if (services.matches(".*\\d.*")) {
            showAlert("Erreur", "Les services inclus ne doivent pas contenir de chiffres.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification du prix
        try {
            BigDecimal prix = new BigDecimal(prixText);
            if (prix.compareTo(BigDecimal.ZERO) <= 0) {
                showAlert("Erreur", "Le prix doit être un nombre positif.", Alert.AlertType.ERROR);
                return false;
            }
            // Vérifier que le prix a au maximum 2 décimales
            if (!prixText.matches("\\d+(\\.\\d{1,2})?")) {
                showAlert("Erreur", "Le prix doit être au format correct (ex: 100.50).", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un prix valide.", Alert.AlertType.ERROR);
            return false;
        }

        // Vérification de la durée (nombre entier positif)
        try {
            int duree = Integer.parseInt(dureeText);
            if (duree <= 0) {
                showAlert("Erreur", "La durée doit être un nombre entier positif.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer une durée valide (ex: 5).", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
