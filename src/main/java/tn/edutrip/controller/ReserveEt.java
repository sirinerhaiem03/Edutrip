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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReserveEt implements Initializable {

    @FXML
    private DatePicker dateStart;

    @FXML
    private DatePicker dateEnd;

    @FXML
    private TextField hebergementName;

    @FXML
    private TextField statusField;

    private final ServiceReservationHebergement serviceReservationHebergement = new ServiceReservationHebergement();
    private Hebergement selectedHebergement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusField.setText("");
    }

    public void setHebergement(Hebergement hebergement) {
        if (hebergement != null) {
            this.selectedHebergement = hebergement;
            hebergementName.setText(hebergement.getNomh());
        }
    }

    @FXML
    void AjouterReservation(ActionEvent event) {
        try {
            if (selectedHebergement == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun hébergement sélectionné !");
                return;
            }

            if (dateStart.getValue() == null || dateEnd.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez sélectionner les dates !");
                return;
            }

            LocalDate today = LocalDate.now();
            LocalDate startLocalDate = dateStart.getValue();
            LocalDate endLocalDate = dateEnd.getValue();

            if (startLocalDate.isBefore(today)) {
                showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de début ne peut pas être avant la date actuelle !");
                return;
            }

            if (!endLocalDate.isAfter(startLocalDate)) {
                showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de fin doit être après la date de début !");
                return;
            }

            String status = statusField.getText().trim();
            if (status.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs !");
                return;
            }

            // Create the reservation object
            Reservation_hebergement reservation = new Reservation_hebergement(
                    0,
                    selectedHebergement.getId_hebergement(),
                    Date.valueOf(startLocalDate),
                    Date.valueOf(endLocalDate),
                    status
            );

            // Add reservation to the database
            serviceReservationHebergement.add(reservation);

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation ajoutée avec succès !");

            // Open details window
            showReservationDetails(reservation);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer des valeurs valides !");
        }
    }

    private void showReservationDetails(Reservation_hebergement reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DetailsReservation.fxml"));
            Parent root = loader.load();

            // Get controller of the details page
            DetailsReservationEt controller = loader.getController();
            controller.setReservation(reservation); // Pass the reservation data

            // Open a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la Réservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
