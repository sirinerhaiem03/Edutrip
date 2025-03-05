package tn.esprit.controllers.University;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.controllers.Candidature.AjouterCandidatureController;
import tn.esprit.entities.University;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceUniversity;
import tn.esprit.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UniversityController {

    public TextField countryField;
    @FXML private TableView<University> universityTable;
    @FXML private TableColumn<University, String> colNom;
    @FXML private TableColumn<University, String> colVille;
    @FXML private TableColumn<University, String> colEmail;
    @FXML private TableColumn<University, String> colDescription;
    @FXML private Button btnAjouter;
    @FXML private Button btnEdit;
    @FXML private Button btnSupprimer;
    @FXML private Button btnPostuler;
    @FXML private Button btnConsulter;
    @FXML private ComboBox<String> villeFilterComboBox;

    private boolean isApiData = false; // Flag to track if data is from API
    private ServiceUniversity serviceUniversity = new ServiceUniversity();
    private UserService userService = new UserService();
    private User currentUser; // Store the current user

    @FXML
    public void initialize() {
        currentUser = userService.getUserById(1); // Example: Fetch user with ID 1

        // Set button visibility based on user role
        setButtonVisibility(currentUser.getRole());
        System.out.println("Rooole" +currentUser.getRole()+currentUser.getMail());
        // Map columns to object properties
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load data into the TableView
        loadUniversityData();
        loadVilleFilter();
        btnSupprimer.setOnAction(event -> supprimerUniversity());
    }
    private void loadVilleFilter() {
        try {
            List<String> villes = serviceUniversity.getDistinctVilles();
            ObservableList<String> observableVilles = FXCollections.observableArrayList(villes);
            villeFilterComboBox.setItems(observableVilles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset() {
        // Clear the ComboBox selection
        villeFilterComboBox.getSelectionModel().clearSelection();
        villeFilterComboBox.setPromptText("Filter by Ville");
        // Reload the original data into the TableView
        loadUniversityData();
    }

    @FXML
    private void handleVilleFilter() {
        String selectedVille = villeFilterComboBox.getSelectionModel().getSelectedItem();
        if (selectedVille != null && !selectedVille.isEmpty()) {
            try {
                List<University> universities = serviceUniversity.afficher();
                ObservableList<University> filteredUniversities = FXCollections.observableArrayList();
                for (University university : universities) {
                    if (university.getVille().equals(selectedVille)) {
                        filteredUniversities.add(university);
                    }
                }
                universityTable.setItems(filteredUniversities);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            loadUniversityData();
        }
    }
    private void setButtonVisibility(String role) {
        if ("ADMIN".equals(role)) {
            // Show Admin buttons (Ajouter, Edit, Delete)
            // Hide User buttons (Consulter, Postuler)
            btnConsulter.setVisible(false);
            btnPostuler.setVisible(false);
        } else if ("USER".equals(role)) {
            // Show User buttons (Consulter, Postuler)
            btnConsulter.setVisible(true);
            btnPostuler.setVisible(true);
            // Hide Admin buttons (Ajouter, Edit, Delete)
            btnAjouter.setVisible(false);
            btnEdit.setVisible(false);
            btnSupprimer.setVisible(false);
        }
    }

    private void loadUniversityData() {
        try {
            // Load universities from database and populate TableView
            List<University> universities = serviceUniversity.afficher();
            if (universities.isEmpty()) {
                System.out.println("No universities found.");
            }
            ObservableList<University> observableUniversities = FXCollections.observableArrayList(universities);
            universityTable.setItems(observableUniversities);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouter() {
        if (isApiData) {
            // Logic for adding API-searched universities
            University selectedUniversity = universityTable.getSelectionModel().getSelectedItem();
            if (selectedUniversity == null) {
                showAlert("Selection Required", "Please select a university first.");
                return;
            }

            try {
                // Check if the university already exists in the database
                University existingUniversity = serviceUniversity.getByName(selectedUniversity.getNom());
                if (existingUniversity == null) {
                    // If it doesn't exist, add it to the database
                    serviceUniversity.ajouter(selectedUniversity);
                    showAlert("Success", "University added to the database.");
                    // Refresh the table to show the newly added university
                    loadUniversityData();
                } else {
                    showAlert("Info", "University already exists in the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add university to the database.");
            }
        } else {
            // Logic for navigating to the "Ajouter University" form
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/ajouter.fxml"));
                AnchorPane ajouterForm = loader.load();
                universityTable.getScene().setRoot(ajouterForm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEdit() {
        University selectedUniversity = universityTable.getSelectionModel().getSelectedItem();
        if (selectedUniversity != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/ajouter.fxml"));
                Stage stage = (Stage) universityTable.getScene().getWindow();
                Scene scene = new Scene(loader.load());

                AjouterUniversityController controller = loader.getController();
                controller.setUniversityToEdit(selectedUniversity); // Pass the university for editing

                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Selection Error", "Please select a university to edit.");
        }
    }

    @FXML
    private void handlePostuler(ActionEvent event) {
        University selectedUniversity = universityTable.getSelectionModel().getSelectedItem();

        if (selectedUniversity != null) {
            try {
                if (serviceUniversity.getByName(selectedUniversity.getNom()) == null) {
                    serviceUniversity.ajouter(selectedUniversity);
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/ajouter.fxml"));
                Parent root = loader.load();

                AjouterCandidatureController controller = loader.getController();
                controller.setUniversity(selectedUniversity); // Pass university data

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert("Selection Required", "Please select a university first.");
        }
    }
    @FXML
    private void handleSearchOnKeyPress(KeyEvent event) {
        // Get the text from the search field
        String country = countryField.getText().trim();

        // If the search field is empty, repopulate the cards with all clubs
        if (country.isEmpty()) {
            loadUniversityData(); // Repopulate the cards with all clubs
            isApiData = false;
            return;
        }

        if (!country.isEmpty()) {
            try {
                isApiData = true;
                // Fetch universities from the API
                List<University> universities = serviceUniversity.fetchUniversities(country);
                // Clear the list view and add the fetched universities
                ObservableList<University> observableUniversities = FXCollections.observableArrayList(universities);
                universityTable.setItems(observableUniversities);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to fetch universities. Please try again.");
            }
        }
    }

    @FXML
    private void handleConsult() {
        navigateToList();
    }

    private void supprimerUniversity() {
        // Check if a university is selected
        University selectedUniversity = universityTable.getSelectionModel().getSelectedItem();
        if (selectedUniversity == null) {
            showAlert("Selection Required", "Please select a university first.");
            return;
        }

        try {
            serviceUniversity.supprimer(selectedUniversity.getIdUniversity());
            loadUniversityData(); // Refresh TableView after deletion
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navigateToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/Candidature.fxml"));
            Stage stage = (Stage) btnAjouter.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
