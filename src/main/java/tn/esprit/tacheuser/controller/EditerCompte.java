package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.IUserService;
import tn.esprit.tacheuser.services.UserService;
import tn.esprit.tacheuser.utils.UserSession;
import tn.esprit.tacheuser.utils.Utility;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditerCompte implements Initializable {


    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;

    private IUserService userService = new UserService(); ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameField.setText(UserSession.getNom());
                lastNameField.setText(UserSession.getPrenom());
        emailField .setText(UserSession.getMail());
                phoneField.setText(UserSession.getNumTel()+"");
    }
    @FXML
    private void handleRegisterAction(ActionEvent event) {

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();


        // Check for empty fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() ) {
            showAlert(Alert.AlertType.WARNING, "Form Incomplete", "Please fill in all the fields.");
            return;
        }

        // Validate email
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Validate phone
        if (!isValidPhone(phone)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone", "Please enter a valid 10-digit phone number.");
            return;
        }



        User newUser = new User(UserSession.getId(),firstName, lastName, email, phone, UserSession.getMdp(), UserSession.getRole());

        try {
            userService.updateUser(newUser);
            UserSession.setSession(newUser);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte.fxml"));
                Parent root = loader.load();
                Scene currentScene = ((Node) event.getSource()).getScene();
                currentScene.setRoot(root);
            } catch (IOException e) {
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred while registering the user. Please try again.");
            e.printStackTrace();
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^[0-9]{8}$"; // Example: 10-digit phone number
        return phone.matches(phoneRegex);
    }

    private boolean isStrongPassword(String password) {
        // Example: At least 8 characters, one uppercase, one lowercase, one digit, one special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }
    // Method to display alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void goToLogin(ActionEvent event) {
        Utility.navigateTo(event, "/Login.fxml");
    }
}
