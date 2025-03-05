package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Agence;
import tn.edutrip.services.ServiceAgence;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class AjouterAgencecontroller {

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

    @FXML
    void ajouterAgence(ActionEvent event) {
        try {

            String nom = nomAgField.getText().trim();
            String adresse = adresseAgField.getText().trim();
            String telephoneStr = telephoneAgField.getText().trim();
            String email = emailAgField.getText().trim();
            String description = descriptionAgField.getText().trim();
            String dateStr = dateCreationField.getText().trim();


            if (nom.isEmpty() || adresse.isEmpty() || telephoneStr.isEmpty() || email.isEmpty() || description.isEmpty() || dateStr.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
                return;
            }


            if (!nom.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
                showAlert("Erreur", "Le nom ne doit pas contenir de chiffres !");
                return;
            }


            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]{2,}$")) {
                showAlert("Erreur", "L'adresse email n'est pas valide !");
                return;
            }


            int telephone;
            try {
                telephone = Integer.parseInt(telephoneStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le numéro de téléphone doit être un nombre !");
                return;
            }
            if (telephoneStr.length() != 8) { // Vérifie que le téléphone a exactement 8 chiffres
                showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !");
                return;
            }



            Date dateCreation;
            try {
                LocalDate localDate = LocalDate.parse(dateStr);
                dateCreation = Date.valueOf(localDate);
            } catch (Exception e) {
                showAlert("Erreur", "Format de date incorrect (AAAA-MM-JJ) !");
                return;
            }


            Agence agence = new Agence(nom, adresse, telephone, email, description, dateCreation);


            serviceAgence.add(agence);


            showAlert("Succès", "Agence ajoutée avec succès !");
            clearFields();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListPourAdmin.fxml"));
            Parent root = loader.load();


            Stage stage = new Stage();
            stage.setTitle("Liste des agences");
            stage.setScene(new Scene(root));
            stage.show();


            ((Stage) nomAgField.getScene().getWindow()).close();

        } catch (IOException e) {

            showAlert("Erreur", "Erreur lors du chargement de la fenêtre.");
            e.printStackTrace();
        } catch (Exception e) {

            showAlert("Erreur", "Une erreur est survenue lors de l'ajout !");
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
