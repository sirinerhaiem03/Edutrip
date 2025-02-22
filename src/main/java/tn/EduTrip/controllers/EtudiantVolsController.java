package tn.EduTrip.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class EtudiantVolsController implements Initializable {

    @FXML
    private VBox volContainer;

    private final ServiceVol serviceVol = new ServiceVol();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            chargerVols();
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du chargement des vols: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void chargerVols() {
        try {
            // Clear existing items
            volContainer.getChildren().clear();

            // Get all flights from database
            List<Vol> vols = serviceVol.afficher();

            if (vols.isEmpty()) {
                Label noVolsLabel = new Label("Aucun vol disponible");
                noVolsLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #757575; -fx-padding: 20;");
                volContainer.getChildren().add(noVolsLabel);
                return;
            }

            // Add each flight to the container
            for (Vol vol : vols) {
                volContainer.getChildren().add(creerVolItem(vol));
            }

        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger les vols depuis la base de données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox creerVolItem(Vol vol) {
        HBox cellBox = new HBox(20);
        cellBox.setStyle("-fx-padding: 15; -fx-background-color: white; " +
                "-fx-border-color: #e0e0e0; -fx-border-width: 1; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        // Left side - Flight information
        VBox infoBox = new VBox(10);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Flight number with icon
        Label numVolLabel = new Label("✈ Vol " + vol.getNumVol());
        numVolLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        numVolLabel.setStyle("-fx-text-fill: #1a237e;");

        // Route information
        Label routeLabel = new Label(vol.getDepart() + " → " + vol.getArrivee());
        routeLabel.setFont(Font.font("System", 14));
        routeLabel.setStyle("-fx-text-fill: #424242;");

        // Departure time
        String departureTime = vol.getDateDepart().toLocalDateTime().format(dateFormatter);
        Label departLabel = new Label("Départ: " + departureTime);
        departLabel.setStyle("-fx-text-fill: #616161;");

        // Arrival time
        String arrivalTime = vol.getDateArrivee().toLocalDateTime().format(dateFormatter);
        Label arrivalLabel = new Label("Arrivée: " + arrivalTime);
        arrivalLabel.setStyle("-fx-text-fill: #616161;");

        // Price with currency
        Label prixLabel = new Label(String.format("Prix: %.2f TND", vol.getPrix()));
        prixLabel.setStyle("-fx-text-fill: #2e7d32; -fx-font-weight: bold; -fx-font-size: 14;");

        // Available seats
        Label placesLabel = new Label("Places disponibles: " + vol.getPlaces());
        placesLabel.setStyle("-fx-text-fill: #1976d2; -fx-font-size: 14;");

        infoBox.getChildren().addAll(
                numVolLabel,
                routeLabel,
                departLabel,
                arrivalLabel,
                prixLabel,
                placesLabel
        );

        Button reserverBtn = new Button("Réserver");
        reserverBtn.setStyle("-fx-background-color: #2370c3; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");

        // Add hover effect
        reserverBtn.setOnMouseEntered(e ->
                reserverBtn.setStyle("-fx-background-color: #2370c3; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;"));
        reserverBtn.setOnMouseExited(e ->
                reserverBtn.setStyle("-fx-background-color: #2370c3; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;"));

        // Disable button if no places available
        reserverBtn.setDisable(vol.getPlaces() <= 0);
        reserverBtn.setOnAction(event -> reserverVol(vol));

        cellBox.getChildren().addAll(infoBox, reserverBtn);
        return cellBox;
    }

    private void reserverVol(Vol vol) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationVol.fxml"));
            Parent root = loader.load();

            ReserverVolsController controller = loader.getController();
            controller.setVol(vol);

            Stage stage = new Stage();
            stage.setTitle("Réserver un Vol");
            stage.setScene(new Scene(root));

            // Refresh list when reservation window is closed
            stage.setOnHidden(e -> chargerVols());

            stage.show();
        } catch (IOException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                    "Impossible d'ouvrir la page de réservation : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
