package tn.EduTrip.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import tn.EduTrip.entites.ReserVol;
import tn.EduTrip.services.ServiceReserVol;

import java.sql.SQLException;
import java.util.Date;

public class ReservationVolController {

    // Champs pour les informations du vol
    @FXML
    private TextField volTextField;

    // Champs pour le mode de paiement
    @FXML
    private RadioButton especeRadioButton;
    @FXML
    private RadioButton carteRadioButton;
    @FXML
    private RadioButton paypalRadioButton;

    // Champs pour le type de voyage
    @FXML
    private RadioButton economiqueRadioButton;
    @FXML
    private RadioButton businessRadioButton;

    // Groupes pour les RadioButton
    private ToggleGroup paiementGroup;
    private ToggleGroup voyageGroup;

    // Service pour gérer les réservations
    private ServiceReserVol serviceReserVol;

    @FXML
    public void initialize() {
        // Initialiser les ToggleGroup
        paiementGroup = new ToggleGroup();
        voyageGroup = new ToggleGroup();

        // Associer les RadioButton aux ToggleGroup
        especeRadioButton.setToggleGroup(paiementGroup);
        carteRadioButton.setToggleGroup(paiementGroup);
        paypalRadioButton.setToggleGroup(paiementGroup);

        economiqueRadioButton.setToggleGroup(voyageGroup);
        businessRadioButton.setToggleGroup(voyageGroup);

        // Sélectionner des options par défaut
        especeRadioButton.setSelected(true);
        economiqueRadioButton.setSelected(true);

        // Initialiser le service
        serviceReserVol = new ServiceReserVol();
    }

    @FXML
    private void confirmerReservation() {
        try {
            // Récupérer les informations saisies
            String volInfo = volTextField.getText();
            String modePaiement = ((RadioButton) paiementGroup.getSelectedToggle()).getUserData().toString();
            String typeVoyage = ((RadioButton) voyageGroup.getSelectedToggle()).getUserData().toString();

            // Valider les champs obligatoires
            if (volInfo.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Champ manquant", "Veuillez saisir les informations du vol.");
                return;
            }

            // Créer un objet ReserVol
            ReserVol reservation = new ReserVol(
                    0, // idReservation (auto-généré par la base de données)
                    1, // idEtudiant (à remplacer par l'ID de l'étudiant connecté)
                    1, // id_Vol (à remplacer par l'ID du vol sélectionné)
                    new Date(), // dateReservation (date actuelle)
                    "Confirmée", // statut
                    500.0, // prix (à remplacer par le prix réel)
                    modePaiement // modePaiement
            );

            // Enregistrer la réservation dans la base de données
            serviceReserVol.ajouter(reservation);

            // Afficher une confirmation
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Réservation confirmée",
                    "Votre réservation a été enregistrée avec succès.\n" +
                            "Mode de paiement : " + modePaiement + "\n" +
                            "Type de voyage : " + typeVoyage);

            // Réinitialiser les champs
            annulerReservation();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de base de données",
                    "Une erreur s'est produite lors de l'enregistrement de la réservation : " + e.getMessage());
        }
    }

    @FXML
    private void annulerReservation() {
        volTextField.clear();
        paiementGroup.selectToggle(null);
        voyageGroup.selectToggle(null);
        especeRadioButton.setSelected(true);
        economiqueRadioButton.setSelected(true);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}