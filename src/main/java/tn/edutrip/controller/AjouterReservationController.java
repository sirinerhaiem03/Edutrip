package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Hebergement;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceReservationHebergement;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AjouterReservationController implements Initializable {

    @FXML
    private DatePicker dateStart;

    @FXML
    private DatePicker dateEnd;

    @FXML
    private TextField hebergementName;

    @FXML
    private TextField statusField; // TextField for status

    private final ServiceReservationHebergement serviceReservationHebergement = new ServiceReservationHebergement();

    // **1. Declare selectedHebergement as a field inside the class**
    private Hebergement selectedHebergement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusField.setText(""); // Set default status
    }

    // **2. Define the setHebergement() method**
    public void setHebergement(Hebergement hebergement) {
        if (hebergement != null) {
            this.selectedHebergement = hebergement;  // Store the selected Hebergement
            hebergementName.setText(hebergement.getNomh()); // Display the name
        }
    }

    @FXML
    void AjouterReservation(ActionEvent event) {
        try {
            // **Ensure a valid Hebergement is selected**
            if (selectedHebergement == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun hébergement sélectionné !");
                return;
            }

            // **Ensure both dates are selected**
            if (dateStart.getValue() == null || dateEnd.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez sélectionner les dates !");
                return;
            }

            Date startDate = Date.valueOf(dateStart.getValue());
            Date endDate = Date.valueOf(dateEnd.getValue());
            String status = statusField.getText().trim();

            // **Ensure endDate is after startDate**
            if (!endDate.after(startDate)) {
                showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de fin doit être après la date de début !");
                return;
            }

            // **Ensure status is not empty**
            if (status.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            // **Create and add reservation**
            Reservation_hebergement reservation = new Reservation_hebergement(
                    0,  // Auto-generated ID
                    selectedHebergement.getId_hebergement(),  // Correct ID from selected Hebergement
                    startDate,
                    endDate,
                    status
            );

            serviceReservationHebergement.add(reservation);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs valides !");
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

    @FXML
    void AfficherReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherReservation.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Réservations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
