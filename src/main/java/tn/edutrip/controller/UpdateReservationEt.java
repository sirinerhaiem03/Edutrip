package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceReservationHebergement;

import java.sql.Date;
import java.time.LocalDate;

public class UpdateReservationEt {

    @FXML private DatePicker dateStartPicker;
    @FXML private DatePicker dateEndPicker;
    @FXML private Button saveButton;

    private Reservation_hebergement reservation;
    private final ServiceReservationHebergement serviceReservationHebergement = new ServiceReservationHebergement();

    public void setReservation(Reservation_hebergement reservation) {
        this.reservation = reservation;

        if (reservation.getDate_d() != null) {
            dateStartPicker.setValue(new Date(reservation.getDate_d().getTime()).toLocalDate());
        }
        if (reservation.getDate_f() != null) {
            dateEndPicker.setValue(new Date(reservation.getDate_f().getTime()).toLocalDate());
        }
    }

    @FXML
    void saveUpdate(ActionEvent event) {
        try {
            LocalDate startLocalDate = dateStartPicker.getValue();
            LocalDate endLocalDate = dateEndPicker.getValue();

            if (startLocalDate == null || endLocalDate == null) {
                showAlert(Alert.AlertType.WARNING, "Champs requis", "Veuillez sélectionner les dates !");
                return;
            }

            // ✅ Validation: End date must be after start date
            if (!endLocalDate.isAfter(startLocalDate)) {
                showAlert(Alert.AlertType.ERROR, "Date invalide", "La date de fin doit être après la date de début !");
                return;
            }

            // ✅ Convert LocalDate to SQL Date
            Date newStartDate = Date.valueOf(startLocalDate);
            Date newEndDate = Date.valueOf(endLocalDate);

            if (newStartDate.equals(reservation.getDate_d()) && newEndDate.equals(reservation.getDate_f())) {
                showAlert(Alert.AlertType.INFORMATION, "Aucune modification", "Les dates sont identiques à l'ancienne réservation.");
                return;
            }

            // ✅ Update reservation dates
            reservation.setDate_d(newStartDate);
            reservation.setDate_f(newEndDate);

            boolean success = serviceReservationHebergement.update(reservation);

            if (success) {
                Reservation_hebergement updatedReservation = serviceReservationHebergement.getById(reservation.getId_reservationh());

                if (updatedReservation != null) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Réservation mise à jour avec succès !\n\n" +
                                    "Nouvelle date début: " + updatedReservation.getDate_d() + "\n" +
                                    "Nouvelle date fin: " + updatedReservation.getDate_f());
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "⚠️ La réservation n'a pas été trouvée après la mise à jour !");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "⚠️ La mise à jour a échoué !");
            }

            // ✅ Close the update window
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour la réservation !");
        }
    }

    @FXML
    void cancelUpdate(ActionEvent event) {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
