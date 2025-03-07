package tn.edutrip.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServicePack_agence;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AjouterPacksController {

    @FXML
    private TextField nomPkField;

    @FXML
    private TextField descriptionPkField;

    @FXML
    private TextField prixField;

    @FXML
    private TextField dureeField;

    @FXML
    private TextField serviceField;

    @FXML
    private TextField dateDajoutField;

    @FXML
    private ChoiceBox<String> statusField;

    @FXML
    private ChoiceBox<Agence> agenceChoiceBox;

    private final ServicePack_agence servicePack_agence = new ServicePack_agence();

    @FXML
    public void initialize() {
        // Ajouter les statuts en minuscules pour correspondre à l'énumération
        statusField.getItems().addAll("disponible", "indisponible");
        loadAgences();
    }

    private void loadAgences() {
        List<Agence> agences = servicePack_agence.getAllAgences();
        if (agences != null && !agences.isEmpty()) {
            agenceChoiceBox.getItems().setAll(agences);
        } else {
            System.out.println("Aucune agence trouvée !");
        }
    }

    @FXML
    void ajouterPack(ActionEvent event) {
        try {
            // Vérifier les champs obligatoires
            if (nomPkField.getText().isEmpty() || descriptionPkField.getText().isEmpty() || prixField.getText().isEmpty() ||
                    dureeField.getText().isEmpty() || serviceField.getText().isEmpty() || dateDajoutField.getText().isEmpty() ||
                    statusField.getValue() == null || agenceChoiceBox.getValue() == null) {
                showErrorAlert("Tous les champs doivent être remplis.");
                return;
            }

            // Valider les entrées
            if (!validateInputs()) {
                return;
            }

            // Créer un nouveau pack
            Pack_agence newPack = new Pack_agence();
            newPack.setNomPk(nomPkField.getText());
            newPack.setDescriptionPk(descriptionPkField.getText());
            newPack.setPrix(new BigDecimal(prixField.getText()));
            newPack.setDuree(Integer.parseInt(dureeField.getText()));
            newPack.setServices_inclus(serviceField.getText());
            newPack.setDate_ajout(validateDate(dateDajoutField.getText()));

            // Convertir le statut en valeur d'énumération
            try {
                newPack.setStatus(Pack_agence.Status.valueOf(statusField.getValue())); // Pas de toUpperCase()
            } catch (IllegalArgumentException e) {
                showErrorAlert("Statut invalide. Veuillez choisir un statut valide.");
                return;
            }

            newPack.setId_agence(agenceChoiceBox.getValue().getIdAgence());

            // Ajouter le pack à la base de données
            servicePack_agence.add(newPack);
            showSuccessAlert("Pack ajouté avec succès !");

            // Envoyer un SMS après l'ajout du pack
            smsAgence(event);

            // Rediriger vers la liste des packs
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListPacks.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Liste des packs");
            stage.setScene(new Scene(root));
            stage.show();
            ((Stage) nomPkField.getScene().getWindow()).close();
        } catch (Exception e) {
            showErrorAlert("Erreur lors de l'ajout du pack : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void smsAgence(ActionEvent event) {
        try {
            String twilioAuthToken = System.getenv("TWILIO_AUTH_TOKEN");
            String twilioACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
            final String TWILIO_PHONE_NUMBER = "+12192487639";

            // Initialiser Twilio
            Twilio.init(twilioACCOUNT_SID, twilioAuthToken);

            // Envoyer un SMS
            Message message = Message.creator(
                    new PhoneNumber("+21625096025"), // Numéro de destination
                    new PhoneNumber(TWILIO_PHONE_NUMBER), // Numéro Twilio
                    "Cher étudiant : Nouvelle offre disponible ! Consultez notre site pour plus de détails et Merci."
            ).create();

            System.out.println("SMS envoyé avec SID: " + message.getSid());
        } catch (Exception e) {
            showErrorAlert("Erreur lors de l'envoi du SMS : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        String nom = nomPkField.getText().trim();
        String description = descriptionPkField.getText().trim();
        String services = serviceField.getText().trim();

        if (nom.matches(".*\\d.*")) {
            showErrorAlert("Le nom du pack ne doit pas contenir de chiffres.");
            return false;
        }

        if (description.matches(".*\\d.*")) {
            showErrorAlert("La description ne doit pas contenir de chiffres.");
            return false;
        }

        if (services.matches(".*\\d.*")) {
            showErrorAlert("Les services inclus ne doivent pas contenir de chiffres.");
            return false;
        }

        return true;
    }

    private Date validateDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            showErrorAlert("Format de date invalide. Utilisez le format yyyy-MM-dd.");
            return null;
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}