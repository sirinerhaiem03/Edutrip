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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.controllers.Candidature.AjouterCandidatureController;
import tn.esprit.entities.University;
import tn.esprit.services.ServiceUniversity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UniversityController {

    @FXML
    private TableView<University> universityTable;
    @FXML
    private TableColumn<University, String> colNom;
    @FXML
    private TableColumn<University, String> colVille;
    @FXML
    private TableColumn<University, String> colEmail;
    @FXML
    private TableColumn<University, String> colDescription;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnSupprimer;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnSort;

    private ServiceUniversity serviceUniversity = new ServiceUniversity();
    private ObservableList<University> allUniversities;

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadUniversityData();

        // Adding listener to automatically update the table when user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());

        btnAjouter.setOnAction(event -> navigateToAjouterForm());
        btnSupprimer.setOnAction(event -> supprimerUniversity());
    }

    private void loadUniversityData() {
        try {
            List<University> universities = serviceUniversity.afficher();
            allUniversities = FXCollections.observableArrayList(universities);
            universityTable.setItems(allUniversities);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            universityTable.setItems(allUniversities);  // Reset to show all universities
        } else {
            List<University> filteredList = allUniversities.stream()
                    .filter(u -> u.getNom().toLowerCase().contains(searchText) ||
                            u.getVille().toLowerCase().contains(searchText) ||
                            u.getEmail().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            universityTable.setItems(FXCollections.observableArrayList(filteredList));  // Update the table
        }
    }

    @FXML
    private void handleSort() {
        List<University> sortedList = allUniversities.stream()
                .sorted((u1, u2) -> u1.getNom().compareToIgnoreCase(u2.getNom()))
                .collect(Collectors.toList());
        universityTable.setItems(FXCollections.observableArrayList(sortedList));
    }

    private void navigateToAjouterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/ajouter.fxml"));
            AnchorPane ajouterForm = loader.load();
            universityTable.getScene().setRoot(ajouterForm);
        } catch (IOException e) {
            e.printStackTrace();
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
                controller.setUniversityToEdit(selectedUniversity);

                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Selection Error", "Il faut sélectionner une université à modifier.");
        }
    }

    @FXML
    private void handlePostuler(ActionEvent event) {
        University selectedUniversity = universityTable.getSelectionModel().getSelectedItem();

        if (selectedUniversity != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/ajouter.fxml"));
                Parent root = loader.load();

                AjouterCandidatureController controller = loader.getController();
                controller.setUniversity(selectedUniversity);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Selection Required", "Il faut sélectionner une université.");
        }
    }

    @FXML
    private void handleCancel() {
        navigateToList();
    }

    private void supprimerUniversity() {
        University selectedUniversity = universityTable.getSelectionModel().getSelectedItem();
        if (selectedUniversity == null) {
            showAlert("Selection Required", "Il faut sélectionner une université.");
            return;
        }

        try {
            serviceUniversity.supprimer(selectedUniversity.getIdUniversity());
            loadUniversityData();  // Refresh data after deletion
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
