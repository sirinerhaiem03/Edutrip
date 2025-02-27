package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.IUserService;
import tn.esprit.tacheuser.services.UserService;
import tn.esprit.tacheuser.utils.UserSession;
import tn.esprit.tacheuser.utils.Utility;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private IUserService userService;

    // Initialize the service in the constructor or a method
    public LoginController() {
        this.userService = new UserService(); // Instantiate with the concrete class
    }
    @FXML
    private void handleLoginAction(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Vérification si les champs sont vides
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Login Failed", "Please enter both email and password.");
            return;
        }

        // Vérifier les identifiants de l'utilisateur
        User user = userService.login(email, password);

        if (user != null) {
            UserSession.setSession(user); // Initialiser la session de l'utilisateur
            showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getNom() + "!");

            switch (user.getRole()) {
                case "Admin":
                    Utility.navigateTo(event, "/adminPage.fxml");
                    break;
                case "Étudiant":
                    Utility.navigateTo(event, "/avis.fxml");
                    break;
                case "responsable agence":
                    Utility.navigateTo(event, "/ResponsableAgence.fxml");
                    break;
                default:
                    showAlert(AlertType.WARNING, "Access Denied", "Your role is not recognized.");
                    break;
            }
        } else {
            showAlert(AlertType.ERROR, "Login Failed", "Invalid email or password. Please check your credentials.");
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}