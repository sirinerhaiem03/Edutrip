package tn.EduTrip.controllers;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import tn.EduTrip.entites.Perturbation;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServicePerturbation;
import tn.EduTrip.services.ServiceVol;

import java.sql.SQLException;
import java.util.List;

public class PerturbationController {
    @FXML
    private HBox announcementBanner;
    @FXML
    private Label announcementText;
    @FXML
    private ComboBox<String> volComboBox;
    @FXML
    private ChoiceBox<String> typePerturbationBox;
    @FXML
    private TextArea descriptionField;

    private final ServicePerturbation servicePerturbation = new ServicePerturbation();
    private final ServiceVol serviceVol = new ServiceVol();
    private ObservableList<Vol> volList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadFlights();
        setupPerturbationTypes();
    }

    private void loadFlights() {
        try {
            List<Vol> vols = serviceVol.afficher();
            volList.setAll(vols);

            ObservableList<String> flightOptions = FXCollections.observableArrayList();
            for (Vol vol : vols) {
                String displayText = vol.getNumVol() + " | " + vol.getDepart() + " → " + vol.getArrivee() + " | " +
                        vol.getDateDepart().toLocalDateTime().toString();
                flightOptions.add(displayText);
            }

            volComboBox.setItems(flightOptions);

        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les vols : " + e.getMessage());
        }
    }

    private void setupPerturbationTypes() {
        typePerturbationBox.setItems(FXCollections.observableArrayList("Retard", "Annulation"));
    }

    @FXML
    private void ajouterPerturbation() {
        try {
            String selectedFlight = volComboBox.getValue();
            String type = typePerturbationBox.getValue();
            String description = descriptionField.getText();

            if (selectedFlight == null || type == null || description.isEmpty()) {
                afficherAlerte(Alert.AlertType.WARNING, "Erreur", "Veuillez sélectionner un vol et remplir tous les champs.");
                return;
            }

            // ✅ Extract ID from Selected Flight
            int volId = extractVolId(selectedFlight);
            if (volId == -1) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Vol invalide sélectionné.");
                return;
            }

            Perturbation perturbation = new Perturbation(volId, type, description);
            servicePerturbation.ajouter(perturbation);


            afficherAnnonce("⚠️ " + type + " sur " + selectedFlight + " - " + description);

            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Perturbation déclarée avec succès.");

        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout: " + e.getMessage());
        }
    }

    private int extractVolId(String selectedFlight) {
        for (Vol vol : volList) {
            String displayText = vol.getNumVol() + " | " + vol.getDepart() + " → " + vol.getArrivee() + " | " +
                    vol.getDateDepart().toLocalDateTime().toString();
            if (displayText.equals(selectedFlight)) {
                return vol.getId_Vol();
            }
        }
        return -1; // No match found
    }

    private void afficherAnnonce(String message) {
        Platform.runLater(() -> {
            announcementText.setText(message);
            announcementBanner.setStyle("-fx-opacity: 1;");

            // ✅ Auto-hide after 3 minutes
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), announcementBanner);
            fadeOut.setDelay(Duration.minutes(3));
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.play();
        });
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
