package tn.esprit.tacheuser.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.legacy.MFXLegacyComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.services.AvisService;
import tn.esprit.tacheuser.utils.UserSession;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EditAvis implements Initializable {





    private int ID ;

    @FXML
    private MFXTextField image;

    @FXML
    private TextArea textarea;

    @FXML
    private MFXLegacyComboBox<String> Note;

    private final AvisService exp = new AvisService();

    private Map<String, Integer> eventMap = new HashMap<>();
    private Stage primaryStage;

    public void setPassedId(int ID) {
        this.ID = ID;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {


        Avis e1=new Avis();
        for (Avis e: exp.rechercheAvis(ID)) {
            e1 = e;
        }
        String[] t2 = {"1", "2", "3", "4", "5"};
        Note.getItems().addAll(t2);

        textarea.setText(e1.getCommentaire());
        image.setText(e1.getPhoto());
        Note.setValue(e1.getNote()+"");

    }
    public void setPrimaryStage(Stage primaryStage) {

        this.primaryStage = primaryStage;
    }
    public void Browse(javafx.event.ActionEvent event) {
        Stage primaryStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String fileUrl = selectedFile.toURI().toString();
            image.setText(fileUrl);
        }
    }
    public void EditRec(javafx.event.ActionEvent actionEvent) {
        if (Note.getValue() == null || Note.getValue().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner une note.");
            return ;
        }

        // Vérification du champ image
        if (image.getText() == null || image.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez entrer une image.");
            return ;
        }

        // Vérification du champ textarea
        if (textarea.getText() == null || textarea.getText().trim().isEmpty()||textarea.getText().length()<3) {
            showAlert("Erreur", "Veuillez remplir la zone de texte.");
            return ;
        }
        String imageText = image.getText();
        String t = imageText.replace("%20", " ");
        t = t.replace("/", "\\").replace("file:\\", "");
        int noteValue = Integer.parseInt(Note.getValue().trim());
        exp.edit(ID, textarea.getText(), noteValue, t);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene currentScene = ((Node) actionEvent.getSource()).getScene();
        currentScene.setRoot(root);}
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    public void logout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }

    public void compte(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }

    public void post(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }
}
