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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.services.AvisService;
import tn.esprit.tacheuser.services.EmailService;
import tn.esprit.tacheuser.utils.UserSession;

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

    @FXML
    private ListView<Avis> listView; // Liste pour afficher les avis

    private final AvisService exp = new AvisService();
    private final EmailService emailService = new EmailService();

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

            // Envoi d'email
            String destinataire = UserSession.getMail();
            String sujet = "Confirmation de votre avis sur EduTrip";
            String contenu = "<h2>Merci d’avoir partagé votre avis !</h2>"
                    + "<p><strong>Note donnée :</strong> " + noteValue + "/5</p>"
                    + "<p><strong>Votre commentaire :</strong> " + textarea.getText() + "</p>";

            emailService.envoyerEmail(destinataire, sujet, contenu);

            refreshListView(); // Mise à jour de la liste
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La note doit être un nombre valide.");
        }
    }

    @FXML
    private void modifierAvis(ActionEvent event) {
        Avis selectedAvis = listView.getSelectionModel().getSelectedItem();
        if (selectedAvis != null) {
            selectedAvis.setCommentaire(textarea.getText());
            selectedAvis.setNote(Integer.parseInt(Note.getValue().trim()));
            selectedAvis.setPhoto(image.getText());

          //  exp.update(selectedAvis);
            refreshListView();
        } else {
            showAlert("Erreur", "Veuillez sélectionner un avis à modifier.");
        }
    }

    @FXML
    private void supprimerAvis(ActionEvent event) {
        Avis selectedAvis = listView.getSelectionModel().getSelectedItem();
        if (selectedAvis != null) {
            exp.delete(selectedAvis.getId());
            refreshListView();
        } else {
            showAlert("Erreur", "Veuillez sélectionner un avis à supprimer.");
        }
    }

    @FXML
    private void sortByRating(ActionEvent event) {
      //  listView.getItems().setAll(exp.getAllSortedByRating());
    }

    private void refreshListView() {
        listView.getItems().setAll(exp.getAll());
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
        Note.getItems().addAll("1", "2", "3", "4", "5");
        refreshListView();

        listView.setOnMouseClicked(event -> {
            Avis selectedAvis = listView.getSelectionModel().getSelectedItem();
            if (selectedAvis != null) {
                textarea.setText(selectedAvis.getCommentaire());
                Note.setValue(String.valueOf(selectedAvis.getNote()));
                image.setText(selectedAvis.getPhoto());
            }
        });
    }
}
