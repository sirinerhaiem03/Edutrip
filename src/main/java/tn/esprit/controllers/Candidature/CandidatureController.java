package tn.esprit.controllers.Candidature;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.entities.Candidature;
import tn.esprit.entities.University;
import tn.esprit.entities.User;
import tn.esprit.services.ServiceCandidature;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CandidatureController {

    @FXML
    private TableView<Candidature> candidatureTable;
    @FXML
    private TableColumn<Candidature, String> colUser;
    @FXML
    private TableColumn<Candidature, String> colUniversity;
    @FXML
    private TableColumn<Candidature, String> colCv;
    @FXML
    private TableColumn<Candidature, String> colLettre;
    @FXML
    private TableColumn<Candidature, String> colDiplome;
    @FXML
    private TableColumn<Candidature, String> colEtat;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnClose;

    private final ServiceCandidature serviceCandidature = new ServiceCandidature();//interact with service

    @FXML
    public void initialize() {
        try {
            afficherCandidatures();//call
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficherCandidatures() throws SQLException {
        List<Candidature> candidatures = serviceCandidature.afficher();//retrieve data
        ObservableList<Candidature> candidatureObservableList = FXCollections.observableArrayList(candidatures);

        // Définition des cellules des colonnes
        colUser.setCellValueFactory(cellData -> {
            User user = cellData.getValue().getUser();
            return new javafx.beans.property.SimpleStringProperty(user.getPrenom() + " " + user.getNom());
        });

        colUniversity.setCellValueFactory(cellData -> {
            University university = cellData.getValue().getUniversity();//Gets the university object from the Candidature.

            return new javafx.beans.property.SimpleStringProperty(university.getNom());
        });

        // Afficher uniquement le nom du fichier pour CV, Lettre et Diplôme
        colCv.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(getFileName(cellData.getValue().getCv())));//xtracting the file name from a file path
        colLettre.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(getFileName(cellData.getValue().getLettre_motivation())));
        colDiplome.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(getFileName(cellData.getValue().getDiplome())));

        colEtat.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEtat().name()));

        candidatureTable.setItems(candidatureObservableList);
    }

    // le nom du fichier depuis un chemin
    private String getFileName(String filePath) {
        if (filePath == null || filePath.isEmpty()) return "";
        return new File(filePath).getName();
    }

    @FXML
    private void handleDelete() {
        Candidature selectedCandidature = candidatureTable.getSelectionModel().getSelectedItem();

        if (selectedCandidature == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Veuillez sélectionner une candidature à supprimer.", ButtonType.OK);
            alert.show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cette candidature ?");
        confirmationAlert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();//result chthot feha confirm
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                serviceCandidature.supprimer(selectedCandidature.getId());
                afficherCandidatures(); // refresh
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la suppression !", ButtonType.OK);
                alert.show();
            }
        }
    }

    @FXML
    private void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/University/university.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) candidatureTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Impossible de retourner à la liste des universités.", ButtonType.OK);
            alert.show();
        }
    }
}
