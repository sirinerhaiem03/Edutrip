package tn.esprit.controllers.Candidature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.controllers.PaymentController;
import tn.esprit.entities.Candidature;
import tn.esprit.entities.EtatCandidature;
import tn.esprit.entities.University;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.ServiceUniversity;
import tn.esprit.services.UserService;

import java.io.File;
import java.io.IOException;

public class AjouterCandidatureController {

    @FXML private TextField txtUniversity;
    @FXML private Label labelCV, labelLettre, labelDiplome;
    @FXML private Button btnCancel;

    private File fileCV, fileLettre, fileDiplome;
    private University selectedUniversity;
    private ServiceUniversity serviceUniversity = new ServiceUniversity();
    private UserService us = new UserService();

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();

    public void setUniversity(University university) {
        this.selectedUniversity = university;
        txtUniversity.setText(university.getNom());
    }
    User currentUser = us.getUserById(1);
    @FXML
    private void handleUploadCV(ActionEvent event) {
        fileCV = chooseFile();
        if (fileCV != null) {
            labelCV.setText(fileCV.getName());
        }
    }


    
    @FXML
    private void handleUploadLettre(ActionEvent event) {
        fileLettre = chooseFile();
        if (fileLettre != null) {
            labelLettre.setText(fileLettre.getName());
        }
    }

    @FXML
    private void handleUploadDiplome(ActionEvent event) {
        fileDiplome = chooseFile();
        if (fileDiplome != null) {
            labelDiplome.setText(fileDiplome.getName());
        }
    }

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showOpenDialog(null);
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (fileCV == null || fileLettre == null || fileDiplome == null) {
            showAlert("Erreur", "Veuillez sélectionner tous les fichiers requis.");
            return;
        }

         // Replace with dynamic session user if needed
        if (currentUser == null) {
            showAlert("Erreur", "Vous devez être connecté.");
            return;
        }

        try {
            // Check if the university already exists in the database
            University existingUniversity = serviceUniversity.getByName(selectedUniversity.getNom());
            if (existingUniversity == null) {
                // If it doesn't exist, insert it into the database
                serviceUniversity.ajouter(selectedUniversity);//If it does not exist, it is added to the database.
                // Fetch the last inserted university to get its ID
                existingUniversity = serviceUniversity.getLastInsertedUniversity();//Retrieves the last inserted university to ensure it has an ID.

            }

            // Prepare Candidature with the existing university
            Candidature candidature = new Candidature(
                    fileCV.getAbsolutePath(),
                    fileLettre.getAbsolutePath(),
                    fileDiplome.getAbsolutePath(),
                    EtatCandidature.EN_ATTENTE,
                    currentUser,
                    existingUniversity // Use the existing university with a valid ID
            );

            System.out.println(candidature);
            System.out.println(existingUniversity);

            // Open Payment Page (pass candidature and payment details)
            openPaymentPage(currentUser, candidature, 50.0f);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout !");
        }
    }

    private void openPaymentPage(User user, Candidature candidature, float amount) {
        try {
            // Load the Payment.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaymentView.fxml"));
            Parent paymentView = loader.load();

            // Get the current stage (candidature window)
            Stage currentStage = (Stage) btnCancel.getScene().getWindow();

            // Get the current window dimensions
            double currentWidth = currentStage.getWidth();
            double currentHeight = currentStage.getHeight();

            // Create a new scene with the payment view
            Scene paymentScene = new Scene(paymentView, currentWidth, currentHeight);

            // Set the new scene in the current stage
            currentStage.setScene(paymentScene);

            // Pass payment details to the PaymentController
            PaymentController paymentController = loader.getController();
            paymentController.setPaymentDetails(user.getNom(), user.getMail(), amount, candidature);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page de paiement.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/university.fxml"));
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}