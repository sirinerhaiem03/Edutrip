package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.IUserService;
import tn.esprit.tacheuser.services.UserService;
import tn.esprit.tacheuser.utils.Utility;

import java.io.IOException;

public class RegisterController {
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    private IUserService userService;

    // Initialize the service in the constructor or a method
    public RegisterController() {
        this.userService = new UserService(); // Instantiate with the concrete class
    }

    @FXML
    private void handleRegisterAction(ActionEvent event) {
        String selectedRole = roleComboBox.getValue();

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Check for empty fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.WARNING, "Form Incomplete", "Please fill in all the fields.");
            return;
        }

        // Validate email
        if (!isValidEmail(email)) {
            showAlert(AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        // Validate phone
        if (!isValidPhone(phone)) {
            showAlert(AlertType.WARNING, "Invalid Phone", "Please enter a valid 10-digit phone number.");
            return;
        }

        // Validate password strength
        if (!isStrongPassword(password)) {
            showAlert(AlertType.WARNING, "Weak Password", "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.WARNING, "Password Mismatch", "The passwords do not match.");
            return;
        }


        User newUser = new User(firstName, lastName, email, password,phone,selectedRole);

        try {
            userService.addUser(newUser);
            showAlert(AlertType.INFORMATION, "Registration Successful", "User registered successfully!");
        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Registration Failed", "An error occurred while registering the user. Please try again.");
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
    private void showAlert(AlertType alertType, String title, String message) {
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