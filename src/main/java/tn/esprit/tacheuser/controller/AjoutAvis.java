package tn.esprit.tacheuser.controller;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.services.AvisService;
import tn.esprit.tacheuser.utils.UserSession;
import tn.esprit.tacheuser.services.EmailService; // ✅ Import du service d'email

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AjoutAvis implements Initializable {

    @FXML
    private MFXTextField image;

    @FXML
    private TextArea textarea;

    @FXML
    private MFXLegacyComboBox<String> Note;

    private final AvisService exp = new AvisService();
    private final EmailService emailService = new EmailService(); // ✅ Instance du service email

    @FXML
    public void Ajoutrec(ActionEvent event) {
        String imageText = image.getText();
        String t = imageText.replace("%20", " ").replace("/", "\\").replace("file:\\", "");

        if (Note.getValue() == null || Note.getValue().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner une note.");
            return;
        }

        if (image.getText() == null || image.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez entrer une image.");
            return;
        }

        if (textarea.getText() == null || textarea.getText().trim().isEmpty() || textarea.getText().length() < 3) {
            showAlert("Erreur", "Veuillez remplir la zone de texte.");
            return;
        }

        try {
            int noteValue = Integer.parseInt(Note.getValue().trim());
            Avis avis = new Avis(UserSession.getId(), textarea.getText(), noteValue, Date.valueOf(LocalDate.now()), t);
            exp.add(avis);

            // ✅ Envoi de l'email de confirmation après l'ajout d'un avis
            String destinataire = UserSession.getMail(); // Utilisez getMail() ici
            String sujet = "Confirmation de votre avis sur EduTrip";
            String contenu = "<h2>Merci d’avoir partagé votre avis !</h2>"
                    + "<p>Votre retour est précieux pour améliorer notre plateforme EduTrip.</p>"
                    + "<p><strong>Note donnée :</strong> " + noteValue + "/5</p>"
                    + "<p><strong>Votre commentaire :</strong> " + textarea.getText() + "</p>"
                    + "<p>À bientôt sur EduTrip !</p>";

            emailService.envoyerEmail(destinataire, sujet, contenu); // ✅ Appel du service email

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La note doit être un nombre valide.");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'interface avis.fxml.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Browse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un fichier");

        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        if (selectedFile != null) {
            String fileUrl = selectedFile.toURI().toString();
            image.setText(fileUrl);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] t2 = {"1", "2", "3", "4", "5"};
        Note.getItems().addAll(t2);
    }
}
