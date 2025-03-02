package tn.edutrip.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.edutrip.utils.DeepSeekAPI;
import java.io.IOException;
public class ChatController {
    @FXML
    private TextField inputField;
    @FXML
    private TextArea chatArea;

    private DeepSeekAPI openRouterClient;

    public void initialize() {
        openRouterClient = new DeepSeekAPI();
    }

    @FXML
    private void sendMessage() {
        String message = inputField.getText();
        chatArea.appendText("Vous: " + message + "\n");

        try {
            String response = openRouterClient.sendMessage(message);
            chatArea.appendText("Chatbot: " + response + "\n");
        } catch (IOException e) {
            chatArea.appendText("Erreur: " + e.getMessage() + "\n");
        }

        inputField.clear();
    }
    @FXML
    private void goBack() {
        try {
            // Charger le fichier FXML de la page principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichagePost.fxml"));
            Parent mainPage = loader.load();

            // Créer une nouvelle scène pour la page principale
            Scene mainScene = new Scene(mainPage);

            // Obtenir la fenêtre actuelle (stage)
            Stage currentStage = (Stage) inputField.getScene().getWindow();

            // Changer la scène de la fenêtre actuelle pour afficher la page principale
            currentStage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page principale.");
        }
    }
}