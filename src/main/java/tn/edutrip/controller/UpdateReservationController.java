package tn.edutrip.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceReservationHebergement;

import java.sql.Date;

public class UpdateReservationController {

    @FXML private DatePicker dateD;
    @FXML private DatePicker dateF;
    @FXML private TextField txtStatus;

    private Reservation_hebergement selectedReservation;
    private ServiceReservationHebergement serviceReservation = new ServiceReservationHebergement();

    public void setReservation(Reservation_hebergement reservation) {
        this.selectedReservation = reservation;

        // Convert java.util.Date or java.sql.Date to LocalDate safely
        if (reservation.getDate_d() != null) {
            dateD.setValue(new Date(reservation.getDate_d().getTime()).toLocalDate());
        }
        if (reservation.getDate_f() != null) {
            dateF.setValue(new Date(reservation.getDate_f().getTime()).toLocalDate());
        }

        txtStatus.setText(reservation.getStatus());
    }

    @FXML
    private void updateReservation() {
        if (selectedReservation == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune réservation sélectionnée !");
            return;
        }

        // Ensure both dates are selected
        if (dateD.getValue() == null || dateF.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez sélectionner les dates !");
            return;
        }

        // Convert LocalDate to SQL Date
        Date startDate = Date.valueOf(dateD.getValue());
        Date endDate = Date.valueOf(dateF.getValue());

        // Validate that endDate is after startDate
        if (!endDate.after(startDate)) {
            showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de fin doit être après la date de début !");
            return;
        }

        // Ensure status is not empty
        String status = txtStatus.getText().trim();
        if (status.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir le statut !");
            return;
        }

        // Update reservation details
        selectedReservation.setDate_d(startDate);
        selectedReservation.setDate_f(endDate);
        selectedReservation.setStatus(status);

        // Save changes
        serviceReservation.update(selectedReservation);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation mise à jour avec succès !");

        // Close the window
        ((Stage) txtStatus.getScene().getWindow()).close();
    }

    // Method to display alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }

}
