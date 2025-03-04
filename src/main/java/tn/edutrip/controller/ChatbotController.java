package tn.edutrip.controller;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.edutrip.services.ChatbotService;

public class ChatbotController {

    @FXML
    private TextField userInputField;

    @FXML
    private TextArea chatArea;

    @FXML
    private void sendMessage() {
        String userMessage = userInputField.getText();
        if (!userMessage.isEmpty()) {
            chatArea.appendText("You: " + userMessage + "\n");

            // Get AI response
            String response = ChatbotService.getChatbotResponse(userMessage);
            chatArea.appendText("AI: " + response + "\n");

            userInputField.clear();
        }
    }
}
