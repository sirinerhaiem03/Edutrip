package tn.esprit.tacheuser.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import tn.esprit.tacheuser.models.Avis;
import tn.esprit.tacheuser.models.User;
import tn.esprit.tacheuser.services.IAvisService;
import tn.esprit.tacheuser.services.AvisService;
import tn.esprit.tacheuser.services.UserService;
import tn.esprit.tacheuser.utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AvisController implements Initializable {
    private Stage primaryStage;

    @FXML
    private MFXButton Ajoutp;

    @FXML
    private MFXTextField recherche;

    @FXML
    private MFXButton Delete;

    @FXML
    private MFXButton Editer;

    private Map<String, Integer> map = new HashMap<>();


    @FXML
    private ListView<Avis> List;
    AvisService MRC=new AvisService();
    UserService MRC1=new UserService();

    @Override

    public void initialize(URL url, ResourceBundle resourceBundle)  {
        int size = 125;

        List.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Avis avis, boolean empty) {
                super.updateItem(avis, empty);
                if (empty || avis == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create NEW nodes for each cell
                    GridPane container = new GridPane();
                    TextFlow textFlow = new TextFlow();

                    // Style definitions
                    String labelStyle = "-fx-fill: #000000; -fx-font-size: 27;";
                    String nameStyle = "-fx-fill: #000000; -fx-font-size: 30;";
                    String name1Style = "-fx-fill: #000000; -fx-font-size: 24;";
                    String dataStyle = "-fx-fill: #000000; -fx-font-size: 20;";

                    User u=MRC1.searchuser(avis.getUserId());
                    Text NomData = new Text(u.getNom() + " : \n");
                    NomData.setStyle(nameStyle);
                    Text AvisData = new Text(avis.getCommentaire() + "\n");
                    AvisData.setStyle(name1Style);

                    Text NoteLabel = new Text("Note: ");
                    NoteLabel.setStyle(labelStyle);
                    Text NoteData = new Text(avis.getNote() + "\n");
                    NoteData.setStyle(dataStyle);



                    // Configure image
                    ImageView imageView = new ImageView();
                    try {
                        Image image = new Image(new File(avis.getPhoto()).toURI().toString());
                        imageView.setImage(image);

                        // Resize the image to 200x200 pixels
                        imageView.setFitWidth(200);  // Set the width to 200 pixels
                        imageView.setFitHeight(200); // Set the height to 200 pixels
                        imageView.setPreserveRatio(false); // Ensure the image is stretched to fit the exact dimensions
                    } catch (Exception e) {
                        // Handle missing image
                        System.err.println("Error loading image: " + e.getMessage());
                    }

                    // Set wrapping widths
                    NomData.setWrappingWidth(200);
                    AvisData.setWrappingWidth(200);
                    NoteLabel.setWrappingWidth(200);
                    NoteData.setWrappingWidth(200);


                    // Add elements to text flow (NO DUPLICATES)
                    textFlow.getChildren().addAll(
                            NomData,
                            AvisData,
                            NoteLabel, NoteData
                    );

                    // Configure grid layout
                    ColumnConstraints imageCol = new ColumnConstraints(200);
                    ColumnConstraints textCol = new ColumnConstraints(450);
                    container.getColumnConstraints().addAll(imageCol, textCol);
                    container.add(imageView, 0, 0);
                    container.add(textFlow, 1, 0);
                    container.setHgap(30);

                    setGraphic(container);
                }
            }
        });
        List.getItems().addAll(MRC.fetch());




    }



    public void deleteSelected(ActionEvent event) {

        Avis SP = List.getSelectionModel().getSelectedItem();

        if (SP != null) {
            int id = SP.getId();
            MRC.delete(id);
            List.getItems().remove(SP);
        }}

    public void Editer(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditAvis.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == EditAvis.class) {
                    EditAvis editionController = new EditAvis();
                    Avis selected = List.getSelectionModel().getSelectedItem();
                    int id = selected.getId();
                    System.out.println(id);
                    editionController.setPassedId(id);
                    return editionController;
                } else {
                    return new EditAvis();
                }
            });

            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);

            EditAvis EditAvisController = loader.getController();
            EditAvisController.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void ajoutpass(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AvisAjout.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handleCellClick(javafx.scene.input.MouseEvent mouseEvent) {
        if(!UserSession.getRole().equals("Admin")){
        if(List.getSelectionModel().getSelectedItem().getUserId()!= UserSession.getId())
        {
            Editer.setDisable(true);
            Delete.setDisable(true);
        }
        else{
            Editer.setDisable(false);
            Delete.setDisable(false);
        }}
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

    public void post(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/avis.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }

    public void compte(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Compte.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) mouseEvent.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
        }
    }
}
