package tn.esprit.tacheuser.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.IUserService;
import tn.esprit.tacheuser.services.UserService;
import tn.esprit.tacheuser.utils.Utility;

public class RegisterController {
    @FXML private ComboBox<String> roleComboBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    private IUserService userService;

    public RegisterController() {
        this.userService = new UserService();
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

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Form Incomplete", "Please fill in all the fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (!isValidPhone(phone)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Phone", "Phone number must have exactly 8 digits.");
            return;
        }

        if (!isStrongPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Weak Password", "Password must be at least 8 characters long and include uppercase, lowercase, digit, and special character.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.WARNING, "Password Mismatch", "The passwords do not match.");
            return;
        }

        try {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));  // Hashing sécurisé
            User newUser = new User(firstName, lastName, email, hashedPassword, phone, selectedRole);

            userService.addUser(newUser);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");

            goToLogin(event);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred while registering the user.");
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("^[0-9]{8}$");
    }

    private boolean isStrongPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        return password.matches(passwordRegex);
    }

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
