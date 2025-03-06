package tn.EduTrip.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;


import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ModifierVolController implements Initializable {
    @FXML private TextField numVolField;
    @FXML private TextField departField;
    @FXML private TextField arriveeField;
    @FXML private TextField heureDepartField;
    @FXML private TextField prixField;
    @FXML private TextField placesField;
    @FXML private DatePicker dateDepartPicker;

    // New fields for arrival date and time
    @FXML private DatePicker dateArriveePicker;
    @FXML private TextField heureArriveeField;

    private Vol volAModifier;
    private final ServiceVol serviceVol = new ServiceVol();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setVol(Vol vol) {
        this.volAModifier = vol;
        if (this.volAModifier != null) {
            remplirChamps();
        }
    }

    private void remplirChamps() {
        try {
            // Set flight number
            numVolField.setText(volAModifier.getNumVol());

            // Set airports
            departField.setText(volAModifier.getDepart());
            arriveeField.setText(volAModifier.getArrivee());

            prixField.setText(String.valueOf(volAModifier.getPrix()));

            placesField.setText(String.valueOf(volAModifier.getPlaces()));

            LocalDateTime departDateTime = volAModifier.getDateDepart().toLocalDateTime();
            dateDepartPicker.setValue(departDateTime.toLocalDate());
            heureDepartField.setText(departDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));

            // Populate new arrival fields
            LocalDateTime arriveeDateTime = volAModifier.getDateArrivee().toLocalDateTime();
            dateArriveePicker.setValue(arriveeDateTime.toLocalDate());
            heureArriveeField.setText(arriveeDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors du remplissage des champs : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void modifierVol() {
        try {
            // Validation et récupération des valeurs
            String numVol = validerTexte(numVolField, "Numéro de vol");
            String depart = validerTexte(departField, "Aéroport de départ");
            String arrivee = validerTexte(arriveeField, "Aéroport d'arrivée");
            LocalDate dateDepart = validerDate(dateDepartPicker, "Date de départ");
            LocalTime heureDepart = validerHeure(heureDepartField, "Heure de départ");
            LocalDate dateArrivee = validerDate(dateArriveePicker, "Date d'arrivée");
            LocalTime heureArrivee = validerHeure(heureArriveeField, "Heure d'arrivée");
            double prix = validerDouble(prixField, "Prix");
            int places = validerEntier(placesField, "Places disponibles");

            // Create modified flight object
            Vol volModifie = new Vol(
                    volAModifier.getId_Vol(),
                    numVol, places,
                    depart,
                    arrivee,
                    Timestamp.valueOf(dateDepart.atTime(heureDepart)),
                    Timestamp.valueOf(dateArrivee.atTime(heureArrivee)),
                    prix
            );

            // Update the flight
            serviceVol.modifier(volModifie);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Vol modifié avec succès !");
            fermerFenetre();

        } catch (IllegalArgumentException e) {
            afficherAlerte(Alert.AlertType.WARNING, "Données invalides", e.getMessage());
        } catch (SQLException e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL",
                    "Erreur lors de la modification du vol : " + e.getMessage());
        } catch (Exception e) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur",
                    "Une erreur inattendue s'est produite : " + e.getMessage());
        }
    }

    private String validerTexte(TextField champ, String nomChamp) {
        if (champ.getText() == null || champ.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(nomChamp + " ne peut pas être vide.");
        }
        return champ.getText().trim();
    }

    private LocalDate validerDate(DatePicker picker, String nomChamp) {
        if (picker.getValue() == null) {
            throw new IllegalArgumentException(nomChamp + " doit être sélectionnée.");
        }
        return picker.getValue();
    }

    private LocalTime validerHeure(TextField champ, String nomChamp) {
        try {
            String heure = champ.getText().trim();
            return LocalTime.parse(heure);
        } catch (Exception e) {
            throw new IllegalArgumentException(nomChamp + " doit être au format HH:mm (ex: 14:30)");
        }
    }

    private double validerDouble(TextField champ, String nomChamp) {
        try {
            return Double.parseDouble(champ.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nomChamp + " doit être un nombre valide.");
        }
    }

    private int validerEntier(TextField champ, String nomChamp) {
        try {
            return Integer.parseInt(champ.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nomChamp + " doit être un nombre entier valide.");
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String contenu) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    @FXML
    private void annuler() {
        fermerFenetre();
    }

    private void fermerFenetre() {
        Stage stage = (Stage) numVolField.getScene().getWindow();
        stage.close();
    }
}
