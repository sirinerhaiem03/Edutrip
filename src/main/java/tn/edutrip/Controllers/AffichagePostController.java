package tn.edutrip.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServicePost;
import tn.edutrip.services.ServiceUser;

import java.io.File;
import java.io.IOException;

public class AffichagePostController {

    @FXML
    private ListView<Post> PostListView;

    @FXML
    private Pane ajoutPostPane;

    private String imagePath;

    @FXML
    private TextField IdCategorie;

    @FXML
    private TextField IdContenu;

    @FXML
    private ImageView idImage;

    private ServiceUser serviceUser;
    private ServicePost servicePost;
    private ObservableList<Post> posts;

    public AffichagePostController() {
        servicePost = new ServicePost();
        serviceUser = new ServiceUser();
    }

    @FXML
    public void initialize() {
        posts = FXCollections.observableArrayList(servicePost.getAll());
        PostListView.setItems(posts);

        PostListView.setCellFactory(listView -> new ListCell<Post>() {
            @Override
            protected void updateItem(Post post, boolean empty) {
                super.updateItem(post, empty);
                if (empty || post == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PostItem.fxml"));
                        VBox vbox = loader.load();
                        PostItemController controller = loader.getController();
                        controller.setPostData(post, PostListView);
                        controller.setAffichagePostController(AffichagePostController.this);
                        setGraphic(vbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}