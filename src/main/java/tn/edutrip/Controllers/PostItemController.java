package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.edutrip.entities.Post;
import tn.edutrip.services.ServicePost;

import java.io.IOException;

public class PostItemController {

    @FXML
    private Text IdAuthor;
    @FXML
    private Text IdDate;
    @FXML
    private Text IdContenu;
    @FXML
    private ImageView IdPostImage;
    @FXML
    private Text IdCategorie;
    @FXML
    private ImageView editIcon;
    @FXML
    private ImageView deleteIcon;

    private Post currentPost;
    private ListView<Post> PostListView;
    private AffichagePostController affichagePostController;

    public void setPostData(Post post, ListView<Post> postListView) {
        currentPost = post;
        this.PostListView = postListView;

        // Afficher les données du post
        IdAuthor.setText("Auteur ID: " + post.getId_etudiant());
        IdDate.setText(post.getDate_creation());
        IdContenu.setText(post.getContenu());
        IdCategorie.setText("Catégorie: " + post.getCategorie());

        // Charger l'image du post si elle existe
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            try {
                Image img = new Image(getClass().getResource("/images/" + post.getImage()).toExternalForm());
                IdPostImage.setImage(img);
            } catch (Exception e) {
                System.out.println("Image introuvable : " + post.getImage());
            }
        }

        // Ajouter des événements pour modifier et supprimer
        editIcon.setOnMouseClicked(event -> ouvrirFenetreModification());
        deleteIcon.setOnMouseClicked(event -> supprimerPost());
    }

    private void ouvrirFenetreModification() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePost.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier le post");

            UpdatePostController updateController = loader.getController();
            updateController.setPost(currentPost, affichagePostController);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerPost() {
        if (currentPost != null) {
            ServicePost servicePost = new ServicePost();
            servicePost.remove(currentPost.getId_post());

            if (PostListView != null) {
                PostListView.getItems().remove(currentPost);
            }

            System.out.println("Post supprimé !");
        }
    }

    public void setAffichagePostController(AffichagePostController affichagePostController) {
        this.affichagePostController = affichagePostController;
    }
}