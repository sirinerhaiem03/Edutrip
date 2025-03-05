package tn.esprit.controllers.University;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.University;
import tn.esprit.services.ServiceUniversity;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

public class AjouterUniversityController {

    @FXML
    private ComboBox<String> villeComboBox;
    @FXML
    private ComboBox<String> nomComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;

    private ServiceUniversity serviceUniversity = new ServiceUniversity();
    private University selectedUniversity = null; // Holds the university being edited (if any)

    @FXML
    public void initialize() {
        // Populate villeComboBox with governorates
        List<String> governorates = List.of("Paris", "Berlin", "Madrid", "Rome", "Amsterdam", "Lisbon", "Vienna", "Brussels",
                "Stockholm", "Prague", "Copenhagen", "Budapest", "New York", "Los Angeles", "Chicago", "Toronto", "Miami", "San Francisco",
                "Houston", "Boston", "Vancouver", "Mexico City", "Buenos Aires", "São Paulo");
        villeComboBox.setItems(FXCollections.observableArrayList(governorates));//make the items in an observable list puis put them in the combo box

        // Populate nomComboBox with universities
        List<String> universities = List.of(
                "Sorbonne Université à Paris", "Humboldt-Universität à Berlin", "Universidad Complutense à Madrid",
                "Sapienza Università à Rome", "Universiteit van Amsterdam à Amsterdam", "Universidade de Lisboa à Lisbon",
                "Universität Wien à Vienna", "Université libre de Bruxelles à Brussels", "Stockholm University à Stockholm",
                "Charles University à Prague", "University of Copenhagen à Copenhagen", "Eötvös Loránd University à Budapest",
                "Columbia University à New York", "UCLA à Los Angeles", "University of Chicago à Chicago", "University of Toronto à Toronto",
                "University of Miami à Miami", "UCSF à San Francisco", "Rice University à Houston",
                "Harvard University à Boston", "University of British Columbia à Vancouver",
                "UNAM à Mexico City", "UBA à Buenos Aires", "USP à São Paulo"
        );
        nomComboBox.setItems(FXCollections.observableArrayList(universities));
    }

    /**
     * This method is called when editing an existing university.
     * It pre-fills the form fields with the selected university's details.
     */
    public void setUniversityToEdit(University university) {
        this.selectedUniversity = university;

        if (university != null) {
            nomComboBox.setValue(university.getNom());
            villeComboBox.setValue(university.getVille());
            emailField.setText(university.getEmail());
            descriptionField.setText(university.getDescription());

            btnSave.setText("Update"); // Change button text to indicate update
        }
    }

    @FXML
    private void handleSave() {
        String nom = nomComboBox.getValue();
        String ville = villeComboBox.getValue();
        String email = emailField.getText();
        String description = descriptionField.getText();

        if (nom == null || ville == null || email.isEmpty() || description.isEmpty()) {
            showAlert("Validation Error", "All fields must be filled.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Validation Error", "Invalid email format. Email must contain '@' and end with '.tn'.");
            return;
        }

        if (selectedUniversity == null) {
            // Adding new university
            University university = new University(nom, ville, email, description);
            try {
                serviceUniversity.ajouter(university);
                showAlert("Success", "University added successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to add university.");
                e.printStackTrace();
            }
        } else {
            // Updating existing university
            selectedUniversity.setNom(nom);
            selectedUniversity.setVille(ville);
            selectedUniversity.setEmail(email);
            selectedUniversity.setDescription(description);

            try {
                serviceUniversity.update(selectedUniversity);
                showAlert("Success", "University updated successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to update university.");
                e.printStackTrace();
            }
        }

        navigateToList(); // Redirect to the list page after saving
    }

    @FXML
    private void handleCancel() {
        navigateToList();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.tn$";
        return Pattern.matches(emailRegex, email);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/university.fxml"));
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
