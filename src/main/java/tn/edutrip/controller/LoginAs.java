package tn.edutrip.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginAs {

    @FXML
    private void handleAdminLogin(ActionEvent event) {
        openNewWindow("/AjouterHebergement.fxml", "Ajouter Hébergement");
    }

    @FXML
    private void handleEtudiantLogin(ActionEvent event) {
        openNewWindow("/AfficherHebergementEt.fxml", "Afficher Hébergement");
    }

    @FXML
    private void handleChatbot(ActionEvent event) {
        openNewWindow("/chatbot.fxml", "AI Chatbot");
    }

    private void openNewWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
