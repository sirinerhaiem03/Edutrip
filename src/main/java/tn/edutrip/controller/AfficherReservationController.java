package tn.edutrip.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.edutrip.entities.Reservation_hebergement;
import tn.edutrip.services.ServiceReservationHebergement;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AfficherReservationController {
    @FXML
    private ListView<Reservation_hebergement> listViewReservation;

    private final ServiceReservationHebergement serviceReservation = new ServiceReservationHebergement();
    private ObservableList<Reservation_hebergement> reservationList;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        List<Reservation_hebergement> reservations = serviceReservation.getAll();
        reservationList = FXCollections.observableArrayList(reservations);
        listViewReservation.setItems(reservationList);

        listViewReservation.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation_hebergement reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Labels for reservation details
                    Label dateLabel = new Label("\uD83D\uDCC5 Du " + reservation.getDate_d() + " au " + reservation.getDate_f());
                    dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                    Label statusLabel = new Label("\uD83D\uDD12 Commentaire: " + reservation.getStatus());
                    statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

                    // Set a fixed width for the VBox containing text
                    VBox textLayout = new VBox(5, dateLabel, statusLabel);
                    textLayout.setMinWidth(250); // Adjust width as needed
                    textLayout.setMaxWidth(250);

                    // Buttons for modifying and deleting
                    Button updateButton = new Button("Modifier");
                    updateButton.setStyle("-fx-background-color: #5bc0de; -fx-text-fill: white;");
                    updateButton.setOnAction(event -> handleUpdate(reservation));

                    Button deleteButton = new Button("Supprimer");
                    deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
                    deleteButton.setOnAction(event -> handleDelete(reservation));

                    // Spacer to push buttons to the right
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

                    // HBox with spacing and styling
                    HBox hBox = new HBox(10, textLayout, spacer, updateButton, deleteButton);
                    hBox.setStyle("-fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-radius: 5px;");
                    hBox.setMinWidth(500);  // Ensure uniform width for all cells
                    hBox.setMaxWidth(500);

                    setGraphic(hBox);

                    // Ensure text color remains black even when selected
                    selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                        if (isSelected) {
                            dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                            statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                        }
                    });
                }
            }
        });
    }

    private void handleDelete(Reservation_hebergement reservation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer Réservation");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            serviceReservation.remove(reservation.getId_reservationh());
            reservationList.remove(reservation);
            listViewReservation.refresh();
        }
    }

    private void handleUpdate(Reservation_hebergement reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateReservation.fxml"));
            Parent root = loader.load();

            UpdateReservationController controller = loader.getController();
            controller.setReservation(reservation);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Réservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
