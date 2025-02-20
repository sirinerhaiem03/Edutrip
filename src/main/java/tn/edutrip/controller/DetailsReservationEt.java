package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceReservationHebergement;

import java.io.IOException;

public class DetailsReservationEt {

    @FXML
    private Label dateStartLabel;

    @FXML
    private Label dateEndLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private Reservation_hebergement reservation;
    private final ServiceReservationHebergement serviceReservationHebergement = new ServiceReservationHebergement();

    public void setReservation(Reservation_hebergement reservation) {
        this.reservation = reservation;
        dateStartLabel.setText("Date de début: " + reservation.getDate_d());
        dateEndLabel.setText("Date de fin: " + reservation.getDate_f());
        statusLabel.setText("Commentaire: " + reservation.getStatus());
    }

    @FXML
    void updateReservation(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReservationEt.fxml")); // Ensure correct path
            Parent root = loader.load();

            // Get the correct controller
            UpdateReservationEt controller = loader.getController();
            controller.setReservation(reservation);

            // Open a new stage for updating
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la Réservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log error for debugging
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de mise à jour !");
        }
    }

    @FXML
    void deleteReservation(ActionEvent event) {
        try {
            serviceReservationHebergement.remove(reservation.getId_reservationh());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation supprimée avec succès !");

            // Close the details window after deletion
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace(); // Log error for debugging
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer la réservation !");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
