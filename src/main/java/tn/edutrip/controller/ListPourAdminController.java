package tn.edutrip.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import tn.edutrip.entities.Agence;
import tn.edutrip.entities.Pack_agence;
import tn.edutrip.services.ServiceAgence;
import tn.edutrip.services.ServicePack_agence;

import java.io.IOException;
import java.util.List;

public class ListPourAdminController {

    @FXML
    private ListView<Agence> listDesAgences;

    @FXML
    private TextField nomrecherche;

    @FXML
    private Button smsAgenceButton;

    @FXML
    private ComboBox<String> comboTri;

    private ServiceAgence serviceAgence = new ServiceAgence();
    private ServicePack_agence servicePack = new ServicePack_agence();

    @FXML
    public void initialize() {
        loadData();
        nomrecherche.textProperty().addListener((observable, oldValue, newValue) -> rechercherAgences());
        comboTri.getItems().addAll("Nom", "Adresse", "Date de création");
    }

    private void loadData() {
        List<Agence> agenceList = serviceAgence.afficher();
        listDesAgences.setItems(FXCollections.observableArrayList(agenceList));

        listDesAgences.setCellFactory(param -> new ListCell<Agence>() {
            @Override
            protected void updateItem(Agence agence, boolean empty) {
                super.updateItem(agence, empty);
                if (empty || agence == null) {
                    setGraphic(null);
                } else {
                    // Image de l'agence
                    ImageView imageView = new ImageView(new Image(getClass().getResource("/image/compte.png").toExternalForm()));
                    imageView.setFitHeight(50);
                    imageView.setFitWidth(50);

                    // Informations de l'agence
                    VBox vboxInfo = new VBox(
                            new Label("Nom: " + agence.getNomAg()),
                            new Label("Adresse: " + agence.getAdresseAg()),
                            new Label("Téléphone: " + agence.getTelephoneAg()),
                            new Label("Email: " + agence.getEmailAg()),
                            new Label("Description: " + agence.getDescriptionAg()),
                            new Label("Date de création: " + agence.getDateCreation())
                    );

                    // Liste des packs disponibles
                    VBox vboxPacks = new VBox();
                    List<Pack_agence> packs = servicePack.getPacksByAgence(agence.getIdAgence());
                    if (!packs.isEmpty()) {
                        Label packTitle = new Label("Packs disponibles:");
                        packTitle.setStyle("-fx-font-weight: bold;");
                        vboxPacks.getChildren().add(packTitle);
                        for (Pack_agence pack : packs) {
                            VBox packDetails = new VBox(
                                    new Label("• " + pack.getNomPk() + " - " + pack.getPrix() + "DT"),
                                    new Label("  Description: " + pack.getDescriptionPk()),
                                    new Label("  Durée: " + pack.getDuree() + " jours"),
                                    new Label("  Services inclus: " + pack.getServices_inclus()),
                                    new Label("  Statut: " + pack.getStatus()),
                                    new Label("  Date d'ajout: " + pack.getDate_ajout())
                            );
                            vboxPacks.getChildren().add(packDetails);
                        }
                    } else {
                        vboxPacks.getChildren().add(new Label("Aucun pack disponible."));
                    }

                    // Création des boutons
                    Button updateButton = new Button("Mettre à jour");
                    Button deleteButton = new Button("Supprimer");
                    Button packsButton = new Button("Packs");

                    // Actions des boutons
                    updateButton.setOnAction(event -> openUpdateAgenceWindow(agence));
                    deleteButton.setOnAction(event -> {
                        serviceAgence.remove(agence.getIdAgence());
                        listDesAgences.getItems().remove(agence);
                    });
                    packsButton.setOnAction(event -> openAjouterPacksWindow(event));

                    // VBox pour aligner les boutons verticalement
                    VBox vboxButtons = new VBox(10, updateButton, deleteButton, packsButton);
                    vboxButtons.setAlignment(Pos.CENTER);  // Alignement des boutons au centre

                    // HBox pour aligner tout horizontalement
                    HBox hboxContent = new HBox(10, imageView, vboxInfo, vboxPacks, vboxButtons);
                    hboxContent.setAlignment(Pos.CENTER_LEFT);  // Alignement général à gauche

                    // Garder les boutons à la même position
                    HBox.setHgrow(vboxInfo, Priority.ALWAYS);  // Permet à vboxInfo de s'étendre

                    setGraphic(hboxContent);  // Afficher le contenu
                }
            }
        });
    }

    @FXML
    private void rechercherAgences() {
        String searchText = nomrecherche.getText().toLowerCase().trim();
        List<Agence> agencesFiltrées = serviceAgence.afficher().stream()
                .filter(agence -> agence.getNomAg().toLowerCase().contains(searchText))
                .toList();
        listDesAgences.setItems(FXCollections.observableArrayList(agencesFiltrées));
    }

    @FXML
    public void trierAgences(ActionEvent actionEvent) {
        String critere = comboTri.getValue();  // Critère sélectionné
        List<Agence> agencesList = serviceAgence.afficher();

        switch (critere) {
            case "Nom":
                agencesList = agencesList.stream()
                        .sorted((a1, a2) -> a1.getNomAg().compareToIgnoreCase(a2.getNomAg()))
                        .toList();
                break;
            case "Adresse":
                agencesList = agencesList.stream()
                        .sorted((a1, a2) -> a1.getAdresseAg().compareToIgnoreCase(a2.getAdresseAg()))
                        .toList();
                break;
            case "Date de création":
                agencesList = agencesList.stream()
                        .sorted((a1, a2) -> a1.getDateCreation().compareTo(a2.getDateCreation()))
                        .toList();
                break;
            default:
                break;
        }

        listDesAgences.setItems(FXCollections.observableArrayList(agencesList));
    }

    private void openUpdateAgenceWindow(Agence agence) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateAgence.fxml"));
            Parent root = loader.load();
            UpdateAgenceController updateController = loader.getController();
            updateController.loadAgence(agence.getNomAg(), agence.getAdresseAg());
            Stage stage = new Stage();
            stage.setTitle("Mettre à jour l'agence");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> loadData());
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de mise à jour.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openAjouterPacksWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterPacks.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Pack");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la page Ajouter Packs.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void StatAgence(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StatAgence.fxml"));
            Parent root = loader.load();
            StatAgenceController statController = loader.getController();
            List<Agence> agences = serviceAgence.afficher();
            statController.generateStatistiques(agences);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Statistiques des Agences");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la page des statistiques.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void smsAgence(ActionEvent event) {
        String twilioAuthToken = System.getenv("TWILIO_AUTH_TOKEN");
        String twilioACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
        final String TWILIO_PHONE_NUMBER = "+12192487639";

        Twilio.init(twilioACCOUNT_SID, twilioAuthToken);

        Message message = Message.creator(
                new PhoneNumber("+21625096025"),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                "Cher étudiant : Nouvelle offre disponible ! Consultez notre site pour plus de détails et Merci." // Message
        ).create();

        System.out.println("SMS envoyé avec SID: " + message.getSid());
    }
}
