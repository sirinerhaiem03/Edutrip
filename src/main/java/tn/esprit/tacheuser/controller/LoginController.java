package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
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

    public LoginController() {
        this.userService = new UserService();
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
    @FXML
    private void handleLoginAction(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Login Failed", "Please enter both email and password.");
            return;
        }

        try {
            User user = userService.getUserByEmail(email);

            if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                UserSession.setSession(user);
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getNom() + "!");

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
                        showAlert(Alert.AlertType.WARNING, "Access Denied", "Your role is not recognized.");
                        break;
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password. Please check your credentials.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "An error occurred while logging in.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
