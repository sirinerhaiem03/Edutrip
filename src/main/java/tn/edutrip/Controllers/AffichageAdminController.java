package tn.edutrip.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServicePost;
import tn.edutrip.services.ServiceUser;

import java.io.File;
import java.io.IOException;

public class AffichageAdminController {

    @FXML
    private ListView<Post> PostListView;

    @FXML
    private ImageView chatbotIcon;

    @FXML
    private Pane ajoutPostPane;

    private String imagePath;

    @FXML
    private TextField IdCategorie;

    @FXML
    private TextField IdContenu;

    @FXML
    private ImageView idImage;

    @FXML
    private TextField rechercheTextField;

    private ServiceUser serviceUser;
    private ServicePost servicePost;
    private ObservableList<Post> posts;
    private ObservableList<Post> filteredPosts;
    public AffichageAdminController() {
        servicePost = new ServicePost();
        serviceUser = new ServiceUser();
    }

    @FXML
    public void initialize() {
        posts = FXCollections.observableArrayList(servicePost.getAll());
        PostListView.setItems(posts);
        filteredPosts = FXCollections.observableArrayList(posts);  // Initialiser la liste filtrée avec tous les posts
        PostListView.setItems(filteredPosts);
        chatbotIcon.setOnMouseClicked(event -> openChatPage());
        PostListView.setCellFactory(listView -> new ListCell<Post>() {
            @Override
            protected void updateItem(Post post, boolean empty) {
                super.updateItem(post, empty);
                if (empty || post == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PostItemAdmin.fxml"));
                        VBox vbox = loader.load();
                        AdminPostController controller = loader.getController();
                        controller.setPostData(post, PostListView);
                        controller.setAffichageAdminController(AffichageAdminController.this);
                        setGraphic(vbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void openChatPage() {
        try {
            // Charger le fichier FXML de la page du chatbot
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/chat_interface.fxml"));
            Parent chatPage = loader.load();

            // Créer une nouvelle scène pour la page du chatbot
            Scene chatScene = new Scene(chatPage);

            // Obtenir la fenêtre actuelle (stage)
            Stage currentStage = (Stage) chatbotIcon.getScene().getWindow();

            // Changer la scène de la fenêtre actuelle pour afficher la page du chatbot
            currentStage.setScene(chatScene);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de la page du chatbot.");
        }
    }

    public void updatePostInList(Post updatedPost) {
        int index = -1;

        // Trouver l'index du post mis à jour dans la liste
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId_post() == updatedPost.getId_post()) {
                index = i;
                break;
            }
        }

        // Mettre à jour le post dans la liste
        if (index != -1) {
            posts.set(index, updatedPost);
            PostListView.refresh(); // Rafraîchir la ListView
            System.out.println("Post mis à jour dans la liste : " + updatedPost);
        }
    }

    @FXML
    void AjouterPost(ActionEvent event) {
        String categorie = IdCategorie.getText();
        String contenu = IdContenu.getText();

        if ((contenu == null || contenu.trim().isEmpty()) && (imagePath == null || imagePath.trim().isEmpty())) {
            showAlert("Erreur de saisie", "Vous devez saisir au moins un contenu ou sélectionner une image.", Alert.AlertType.WARNING);
            return;
        }

        Post post = new Post();
        post.setCategorie(categorie);
        post.setContenu(contenu);
        post.setImage(imagePath);
        post.setId_etudiant(1); // Remplacer par l'ID de l'étudiant connecté
        post.setDate_creation(java.time.LocalDate.now().toString());

        servicePost.add(post);
        posts.add(post); // Ajouter le nouveau post à la liste
        showAlert("Succès", "Le post a été ajouté avec succès !", Alert.AlertType.INFORMATION);

        IdCategorie.clear();
        IdContenu.clear();
        idImage.setImage(null);
        imagePath = null;
    }

    @FXML
    void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif"));
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imagePath = file.getAbsolutePath();
            Image image = new Image(file.toURI().toString());
            idImage.setImage(image);
        }
    }
    @FXML
    void filtrerPosts() {
        String rechercheTexte = rechercheTextField.getText().toLowerCase();

        filteredPosts.clear();
        for (Post post : posts) {
            if (post.getCategorie().toLowerCase().contains(rechercheTexte) || post.getContenu().toLowerCase().contains(rechercheTexte)) {
                filteredPosts.add(post);
            }
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
