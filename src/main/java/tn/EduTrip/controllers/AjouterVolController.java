package tn.EduTrip.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.EduTrip.entites.Vol;
import tn.EduTrip.services.ServiceVol;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.io.IOException;

public class AjouterVolController {
    @FXML private TextField numVolField;
    @FXML private TextField departField;
    @FXML private TextField arriveeField;
    @FXML private TextField heureDepartField;
    @FXML private TextField prixField;
    @FXML private TextField placesField;
    @FXML private DatePicker dateDepartPicker;

    private final ServiceVol serviceVol = new ServiceVol();

    @FXML
    public void ajouterVol() {
        try {

            String numVol = validerTexte(numVolField, "Numéro de vol");
            String depart = validerTexte(departField, "Aéroport de départ");
            String arrivee = validerTexte(arriveeField, "Aéroport d'arrivée");
            LocalDate dateDepart = validerDate(dateDepartPicker, "Date de départ");
            LocalTime heureDepart = validerHeure(heureDepartField, "Heure de départ");
            double prix = validerDouble(prixField, "Prix");
            int places = validerEntier(placesField, "Places disponibles");


            Vol vol = new Vol(0, places, numVol, depart, arrivee,
                    Timestamp.valueOf(dateDepart.atTime(heureDepart)),
                    Timestamp.valueOf(dateDepart.atTime(heureDepart).plusHours(2)), prix);


            serviceVol.ajouter(vol);
            afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Vol ajouté avec succès !");
            

            naviguerVersAffichageVol();

        } catch (IllegalArgumentException e) {
            afficherAlerte(Alert.AlertType.WARNING, "Données invalides", e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'ajout du vol : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
    }

    private void naviguerVersAffichageVol() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageVol.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) numVolField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte(Alert.AlertType.ERROR, "Erreur de Navigation", 
                "Impossible de charger la page d'affichage des vols : " + e.getMessage());
        }
    }


    private String validerTexte(TextField champ, String nomChamp) {
        if (champ == null || champ.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(nomChamp + " ne peut pas être vide.");
        }
        return champ.getText().trim();
    }


    private LocalDate validerDate(DatePicker picker, String nomChamp) {
        if (picker == null || picker.getValue() == null) {
            throw new IllegalArgumentException(nomChamp + " doit être sélectionnée.");
        }
        return picker.getValue();
    }


    private LocalTime validerHeure(TextField champ, String nomChamp) {
        if (champ == null || champ.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(nomChamp + " ne peut pas être vide.");
        }
        try {
            return LocalTime.parse(champ.getText().trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(nomChamp + " doit être au format HH:mm (ex : 14:30).");
        }
    }

    private double validerDouble(TextField champ, String nomChamp) {
        if (champ == null || champ.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(nomChamp + " ne peut pas être vide.");
        }
        try {
            return Double.parseDouble(champ.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nomChamp + " doit être un nombre valide.");
        }
    }


    private int validerEntier(TextField champ, String nomChamp) {
        if (champ == null || champ.getText().trim().isEmpty()) {
            throw new IllegalArgumentException(nomChamp + " ne peut pas être vide.");
        }
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
}
