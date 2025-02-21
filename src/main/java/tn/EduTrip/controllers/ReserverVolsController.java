package tn.EduTrip.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReserverVolsController implements Initializable {
    @FXML
    private Label numVolLabel;
    @FXML
    private Label departLabel;
    @FXML
    private Label arriveeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label prixLabel;
    @FXML
    private Label placesLabel;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private Spinner<Integer> nombrePlacesSpinner;

    private Vol vol;
    private final ServiceVol serviceVol = new ServiceVol();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nombrePlacesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10));
    }

    public void setVol(Vol vol) {
        this.vol = vol;
        if (vol != null) {
            afficherDetailsVol();

            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, vol.getPlaces());
            nombrePlacesSpinner.setValueFactory(valueFactory);
        }
    }

    private void afficherDetailsVol() {
        numVolLabel.setText("Vol " + vol.getNumVol());
        departLabel.setText(vol.getDepart());
        arriveeLabel.setText(vol.getArrivee());
        dateLabel.setText(vol.getDateDepart().toLocalDateTime().format(dateFormatter));
        prixLabel.setText(String.format("%.2f TND", vol.getPrix()));
        placesLabel.setText(String.valueOf(vol.getPlaces()));
    }

    @FXML
    private void confirmerReservation() {
        if (!validerChamps()) {
            return;
        }

        try {

            int nombrePlaces = nombrePlacesSpinner.getValue();


            if (nombrePlaces > vol.getPlaces()) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                        "Il n'y a pas assez de places disponibles.");
                return;
            }

            vol.setPlaces(vol.getPlaces() - nombrePlaces);
            serviceVol.modifier(vol);


            afficherAlerte(Alert.AlertType.INFORMATION, "Succès",
                    "Réservation effectuée avec succès !\n" +
                            "Nom: " + nomField.getText() + "\n" +
                            "Prénom: " + prenomField.getText() + "\n" +
                            "Email: " + emailField.getText() + "\n" +
                            "Nombre de places: " + nombrePlaces);


            fermerFenetre();

        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la réservation : " + e.getMessage());
        }
    }

    private boolean validerChamps() {
        if (nomField.getText().trim().isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Validation", "Le nom est requis");
            return false;
        }
        if (prenomField.getText().trim().isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Validation", "Le prénom est requis");
            return false;
        }
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            afficherAlerte(Alert.AlertType.WARNING, "Validation", "Email invalide");
            return false;
        }
        return true;
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}