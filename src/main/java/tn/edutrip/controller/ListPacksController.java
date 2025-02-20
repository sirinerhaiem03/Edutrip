package tn.edutrip.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServicePack_agence;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;

public class ListPacksController {

    @FXML
    private ListView<Pack_agence> listDesPacks;

    private ServicePack_agence servicePack = new ServicePack_agence();

    @FXML
    public void initialize() {
        loadData();
    }


    public void loadData() {
        List<Pack_agence> packList = servicePack.getAllPacks();
        listDesPacks.setItems(FXCollections.observableArrayList(packList));


        listDesPacks.setCellFactory(param -> new ListCell<Pack_agence>() {
            @Override
            protected void updateItem(Pack_agence pack, boolean empty) {
                super.updateItem(pack, empty);
                if (empty || pack == null) {
                    setGraphic(null);
                } else {
                    VBox vboxInfo = new VBox(
                            new Label("Nom: " + pack.getNomPk()),
                            new Label("Description: " + pack.getDescriptionPk()),
                            new Label("Prix: " + pack.getPrix() + "DT"),
                            new Label("Durée: " + pack.getDuree() + " jours"),
                            new Label("Service inclus: " + pack.getServices_inclus()),
                            new Label("Date Dajout: " + pack.getDate_ajout()),
                            new Label("Status: " + pack.getStatus())
                    );

                    Button updateButton = new Button("Mettre à jour");
                    Button deleteButton = new Button("Supprimer");

                    updateButton.setPrefWidth(120);
                    deleteButton.setPrefWidth(120);

                    updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 5px;");
                    deleteButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 5px;");


                    updateButton.setOnAction(event -> updateButton(event, pack));


                    deleteButton.setOnAction(event -> deletePack(event, pack));

                    VBox vboxButtons = new VBox(5, updateButton, deleteButton);
                    vboxButtons.setStyle("-fx-alignment: center;");

                    HBox hboxContent = new HBox(10, vboxInfo, vboxButtons);
                    hboxContent.setStyle("-fx-alignment: center-left; -fx-padding: 10px;");

                    setGraphic(hboxContent);
                }
            }
        });
    }
    @FXML
    private void updateButton(ActionEvent event, Pack_agence pack) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePack.fxml"));
            Parent root = loader.load();

            UpdatePackController updateController = loader.getController();
            updateController.loadPack(pack);

            Stage stage = new Stage();
            stage.setTitle("Mettre à jour le pack");
            stage.setScene(new Scene(root));

            // Recharger la liste après la fermeture de la fenêtre
            stage.setOnHidden(e -> loadData());

            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de mise à jour.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }




    @FXML
    private void deletePack(ActionEvent event, Pack_agence pack) {

        boolean isDeleted = servicePack.remove(pack.getId_pack());
        if (isDeleted) {
            loadData();
            showAlert("Succès", "Le pack a été supprimé avec succès.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Une erreur est survenue lors de la suppression du pack.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
