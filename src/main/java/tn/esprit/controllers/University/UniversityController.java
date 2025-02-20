package tn.esprit.controllers.University;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.controllers.Candidature.AjouterCandidatureController;
import tn.esprit.entities.University;
import tn.esprit.services.ServiceUniversity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

    private ServiceUniversity serviceUniversity = new ServiceUniversity();
    @FXML
    public void initialize() {
        // Map columns to object properties
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colVille.setCellValueFactory(new PropertyValueFactory<>("ville"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load data into the TableView
        loadUniversityData();

        // Set up button actions
        btnAjouter.setOnAction(event -> navigateToAjouterForm());
        btnSupprimer.setOnAction(event -> supprimerUniversity());
    }


    private void loadUniversityData() {//Afficher list after ajout delete update ..
        try {
            // Load universities from database and populate TableView
            List<University> universities = serviceUniversity.afficher();
            if (universities.isEmpty()) {
                System.out.println("No universities found.");
            }
            ObservableList<University> observableUniversities = FXCollections.observableArrayList(universities);
            universityTable.setItems(observableUniversities); // Set the items to the TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navigateToAjouterForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/ajouter.fxml"));
            AnchorPane ajouterForm = loader.load();//load the new fxml in ajouterform of type anchorpane
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Candidature/ajouter.fxml"));
                Parent root = loader.load();

                AjouterCandidatureController controller = loader.getController();
                controller.setUniversity(selectedUniversity); // Pass university data

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Selection Required", "Please select a university first.");
        }
    }

    @FXML
    private void handleCancel() {
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
            loadUniversityData(); // Refresh
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
