package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.edutrip.entities.Agence;
import tn.edutrip.services.ServiceAgence;

import java.sql.Date;
import java.time.LocalDate;

public class UpdateAgenceController {

    @FXML
    private TextField nomAgField;

    @FXML
    private TextField adresseAgField;

    @FXML
    private TextField telephoneAgField;

    @FXML
    private TextField emailAgField;

    @FXML
    private TextField descriptionAgField;

    @FXML
    private TextField dateCreationField;

    private final ServiceAgence serviceAgence = new ServiceAgence();
    private Agence agenceToUpdate;


    public void loadAgence(String nom, String adresse) {

        this.agenceToUpdate = serviceAgence.getAgenceByNomEtAdresse(nom, adresse);

        if (agenceToUpdate != null) {
            nomAgField.setText(agenceToUpdate.getNomAg());
            adresseAgField.setText(agenceToUpdate.getAdresseAg());
            telephoneAgField.setText(String.valueOf(agenceToUpdate.getTelephoneAg()));
            emailAgField.setText(agenceToUpdate.getEmailAg());
            descriptionAgField.setText(agenceToUpdate.getDescriptionAg());
            dateCreationField.setText(agenceToUpdate.getDateCreation().toString());
        } else {
            showAlert("Erreur", "Agence non trouvée avec ce nom et cette adresse.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void updateAgence(ActionEvent event) {
        if (agenceToUpdate == null) {
            showAlert("Erreur", "Aucune agence à mettre à jour.", Alert.AlertType.ERROR);
            return;
        }

        String nom = nomAgField.getText().trim();
        String adresse = adresseAgField.getText().trim();
        String telephoneStr = telephoneAgField.getText().trim();
        String email = emailAgField.getText().trim();
        String description = descriptionAgField.getText().trim();
        String dateStr = dateCreationField.getText().trim();

        // Validation des champs
        if (nom.isEmpty() || adresse.isEmpty() || telephoneStr.isEmpty() || email.isEmpty() || description.isEmpty() || dateStr.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs !", Alert.AlertType.ERROR);
            return;
        }

        if (!nom.matches("^[a-zA-ZÀ-ÖØ-öø-ÿ\\s]+$")) {
            showAlert("Erreur", "Le nom de l'agence ne doit contenir que des lettres et des espaces.", Alert.AlertType.ERROR);
            return;
        }

        int telephone;
        try {
            telephone = Integer.parseInt(telephoneStr);
            if (telephone < 10000000 || telephone > 99999999) {
                showAlert("Erreur", "Le numéro de téléphone doit être un nombre valide à 8 chiffres.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le numéro de téléphone doit être un nombre !", Alert.AlertType.ERROR);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$")) {
            showAlert("Erreur", "L'adresse email n'est pas valide !", Alert.AlertType.ERROR);
            return;
        }

        Date dateCreation;
        try {
            LocalDate localDate = LocalDate.parse(dateStr);
            dateCreation = Date.valueOf(localDate);
        } catch (Exception e) {
            showAlert("Erreur", "Format de date incorrect (AAAA-MM-JJ) !", Alert.AlertType.ERROR);
            return;
        }


        agenceToUpdate.setNomAg(nom);
        agenceToUpdate.setAdresseAg(adresse);
        agenceToUpdate.setTelephoneAg(telephone);
        agenceToUpdate.setEmailAg(email);
        agenceToUpdate.setDescriptionAg(description);
        agenceToUpdate.setDateCreation(dateCreation);

        boolean updateSuccessful = serviceAgence.update(agenceToUpdate);

        if (updateSuccessful) {
            showAlert("Succès", "Agence mise à jour avec succès !", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomAgField.clear();
        adresseAgField.clear();
        telephoneAgField.clear();
        emailAgField.clear();
        descriptionAgField.clear();
        dateCreationField.clear();
    }

}