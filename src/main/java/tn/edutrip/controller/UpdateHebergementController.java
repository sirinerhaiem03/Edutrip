package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.edutrip.entities.Hebergement;
import tn.edutrip.services.ServiceHebergement;

public class UpdateHebergementController {

    @FXML private TextField txtNom;
    @FXML private TextField txtCapacite;
    @FXML private TextField txtType;
    @FXML private TextField txtAdresse;
    @FXML private TextField txtDisponible;
    @FXML private TextField txtDescription;
    @FXML private TextField txtPrix;

    private Hebergement selectedHebergement;
    private ServiceHebergement serviceHebergement = new ServiceHebergement();

    // Reference to the parent controller
    private AfficherHebergementController parentController;

    // Setter for parentController
    public void setParentController(AfficherHebergementController parentController) {
        this.parentController = parentController;
    }

    public void setHebergement(Hebergement hebergement) {
        this.selectedHebergement = hebergement;
        txtNom.setText(hebergement.getNomh());
        txtCapacite.setText(String.valueOf(hebergement.getCapaciteh()));
        txtType.setText(hebergement.getTypeh());
        txtAdresse.setText(hebergement.getAdressh());
        txtDisponible.setText(hebergement.getDisponibleh());
        txtDescription.setText(hebergement.getDescriptionh());
        txtPrix.setText(String.valueOf(hebergement.getPrixh()));
    }

    @FXML
    private void updateHebergement() {
        if (selectedHebergement == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun hébergement sélectionné !");
            return;
        }

        try {
            // **Check for empty fields**
            String nom = txtNom.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String type = txtType.getText().trim();
            String disponible = txtDisponible.getText().trim();
            String description = txtDescription.getText().trim();
            String capaciteStr = txtCapacite.getText().trim();
            String prixStr = txtPrix.getText().trim();

            if (nom.isEmpty() || adresse.isEmpty() || type.isEmpty() || disponible.isEmpty() ||
                    description.isEmpty() || capaciteStr.isEmpty() || prixStr.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            // **Validate capacity (1 - 1000)**
            int capacite = Integer.parseInt(capaciteStr);
            if (capacite < 1 || capacite > 1000) {
                showAlert(Alert.AlertType.ERROR, "Capacité invalide", "La capacité doit être entre 1 et 1000 !");
                return;
            }

            // **Validate price (1 - 9000)**
            float prix = Float.parseFloat(prixStr);
            if (prix < 1 || prix > 9000) {
                showAlert(Alert.AlertType.ERROR, "Prix invalide", "Le prix doit être entre 1 et 9000 !");
                return;
            }

            // **Validate disponibilité**
            if (!disponible.equals("Disponible") && !disponible.equals("Non disponible") && !disponible.equals("Réservée")) {
                showAlert(Alert.AlertType.ERROR, "Disponibilité invalide", "Veuillez choisir 'Disponible', 'Non disponible' ou 'Réservée' !");
                return;
            }

            // **Update the object**
            selectedHebergement.setNomh(nom);
            selectedHebergement.setCapaciteh(capacite);
            selectedHebergement.setTypeh(type);
            selectedHebergement.setAdressh(adresse);
            selectedHebergement.setDisponibleh(disponible);
            selectedHebergement.setDescriptionh(description);
            selectedHebergement.setPrixh(prix);

            // **Save updates**
            serviceHebergement.update(selectedHebergement);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Hébergement mis à jour avec succès !");

            // Refresh the main list via the parent controller
            if (parentController != null) {
                System.out.println("Refreshing list...");
                parentController.refreshHebergementList();
            } else {
                System.out.println("Parent controller is NULL!");
            }

            // Close the update window
            txtNom.getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", "Veuillez entrer des valeurs numériques valides pour la capacité et le prix !");
        }
    }


    // **Method to show alerts**
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }


}
