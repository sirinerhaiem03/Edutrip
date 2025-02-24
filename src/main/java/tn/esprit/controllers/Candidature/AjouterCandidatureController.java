package tn.esprit.controllers.Candidature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.entities.Candidature;
import tn.esprit.entities.EtatCandidature;
import tn.esprit.entities.University;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceCandidature;
import tn.esprit.services.UserService;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AjouterCandidatureController {

    @FXML
    private TextField txtUniversity;
    @FXML
    private Label labelCV, labelLettre, labelDiplome;// the names
    private File fileCV, fileLettre, fileDiplome;// stocke les file paths.

    private University selectedUniversity;
    private UserService us = new UserService();
    private final ServiceCandidature serviceCandidature = new ServiceCandidature();

    public void setUniversity(University university) {// txtUniversity field filled
        this.selectedUniversity = university;
        txtUniversity.setText(university.getNom());
    }

    @FXML
    private void handleUploadCV(ActionEvent event) {
        fileCV = chooseFile();//file object contains choose file
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
        FileChooser fileChooser = new FileChooser();//outil JavaFX p
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        return fileChooser.showOpenDialog(null);
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (fileCV == null || fileLettre == null || fileDiplome == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez sélectionner tous les fichiers requis.", ButtonType.OK);
            alert.show();
            return;
        }

        User currentUser = us.getUserById(1);//store
        if (currentUser == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Vous devez être connecté.", ButtonType.OK);
            alert.show();
            return;
        }

        try {
            Candidature candidature = new Candidature( fileCV.getAbsolutePath(), fileLettre.getAbsolutePath(),
                    fileDiplome.getAbsolutePath(), EtatCandidature.EN_ATTENTE, currentUser, selectedUniversity);

            serviceCandidature.ajouter(candidature);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Candidature ajoutée avec succès!", ButtonType.OK);
            alert.showAndWait();

            // Naviguer vers la vue des candidatures
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/Candidature.fxml"));
            Parent root = loader.load();//This line loads the new UI from the Candidature.fxml file and stores it in root car on ajoute une personne donc il va se changer le file fxml
            Stage stage = (Stage) txtUniversity.getScene().getWindow();//Retrieves the current window
            stage.setScene(new Scene(root));//Replaces the current scene with list UI.

            stage.show();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'ajout!", ButtonType.OK);
            alert.show();
        }
    }


    @FXML
    private void handleCancel() {
        //navigateToList();
    }


}
