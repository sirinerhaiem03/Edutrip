package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServicePack_agence;

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

        try {
            currentPack.setNomPk(nomPkField.getText());
            currentPack.setDescriptionPk(descriptionPkField.getText());
            currentPack.setPrix(new java.math.BigDecimal(prixField.getText()));
            currentPack.setDuree(Integer.parseInt(dureeField.getText()));
            currentPack.setServices_inclus(servicesInclusField.getText());

            servicePack.update(currentPack);

            showAlert("Succès", "Le pack a été mis à jour avec succès.", Alert.AlertType.INFORMATION);


            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer des valeurs valides.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
