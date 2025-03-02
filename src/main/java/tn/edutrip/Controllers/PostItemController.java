package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
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
import tn.edutrip.services.ServiceCommentaire;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tn.edutrip.entities.Commentaire;
import tn.edutrip.utils.OpenAIModerationAPI;

public class PostItemController {

    @FXML
    private ImageView CommentsIcon;
    @FXML
    private VBox commentsContainer;
    @FXML
    private TextField commentInput; // Text field for the comment
    @FXML
    private Button submitCommentButton;
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
    @FXML
    private ImageView likeIcon;
    @FXML
    private ImageView dislikeIcon;
    @FXML
    private Text likeCountText;  // Afficher le nombre de likes
    @FXML
    private Text dislikeCountText;

    private Post currentPost;
    private ServicePost servicePost;
    private ListView<Post> PostListView;
    private AffichagePostController affichagePostController;
    private ServiceCommentaire ServiceCommentaire;

    private static final Logger logger = Logger.getLogger(PostItemController.class.getName());

    public PostItemController() {
        ServiceCommentaire = new ServiceCommentaire();
        servicePost = new ServicePost();
    }

    public void setPostData(Post post, ListView<Post> postListView) {
        if (post == null) {
            System.out.println("The post is null!");
            return;
        }
        currentPost = post;
        this.PostListView = postListView;

        // Display post data
        IdAuthor.setText("Author ID: " + post.getId_etudiant());
        IdDate.setText(post.getDate_creation());
        IdContenu.setText(post.getContenu());
        IdCategorie.setText("Category: " + post.getCategorie());
        likeCountText.setText(String.valueOf(post.getLikes()));
        dislikeCountText.setText(String.valueOf(post.getDislikes()));
        // Load post image if it exists
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            try {
                Image img = new Image(getClass().getResource("/images/" + post.getImage()).toExternalForm());
                IdPostImage.setImage(img);
            } catch (Exception e) {
                System.out.println("Image not found: " + post.getImage());
            }
        }
        CommentsIcon.setOnMouseClicked(event -> loadCommentsAsync());
        // Add events for edit and delete
        editIcon.setOnMouseClicked(event -> openEditWindow());
        deleteIcon.setOnMouseClicked(event -> deletePost());
        // Add event to submit a comment
        submitCommentButton.setOnAction(event -> submitComment());
    }
    @FXML
    private void likePost() {
        if (currentPost != null) {
            // Incrémentez le nombre de likes
            currentPost.setLikes(currentPost.getLikes() + 1);

            // Mettez à jour l'interface
            likeCountText.setText(String.valueOf(currentPost.getLikes()));

            // Sauvegarder la modification dans la base de données
            servicePost.update(currentPost);
        }
    }
    @FXML
    private void dislikePost() {
        if (currentPost != null) {
            // Incrémentez le nombre de dislikes
            currentPost.setDislikes(currentPost.getDislikes() + 1);

            // Mettez à jour l'interface
            dislikeCountText.setText(String.valueOf(currentPost.getDislikes()));

            // Sauvegarder la modification dans la base de données
            servicePost.update(currentPost);
        }
    }

    private void showAlert(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void submitComment() {
        String commentText = commentInput.getText().trim();

        // Vérifier si le commentaire est vide
        if (commentText.isEmpty()) {
            showAlert("Erreur de saisie", "Le commentaire ne peut pas être vide.", Alert.AlertType.WARNING);
            return;
        }



        // Si le commentaire est approprié, l'ajouter à la base de données
        if (currentPost != null) {
            Commentaire newComment = new Commentaire();
            newComment.setId_post(currentPost.getId_post());
            newComment.setId_etudiant(1); // Remplacez par l'ID de l'utilisateur actuel
            newComment.setContenu(commentText);
            newComment.setDate_commentaire(new Date());

            // Ajouter le commentaire à la base de données
            ServiceCommentaire.add(newComment);

            // Effacer le champ de texte
            commentInput.clear();

            // Recharger les commentaires
            loadCommentsAsync();
        }
    }



    public void loadCommentsAsync() {
        Task<List<Commentaire>> loadCommentsTask = new Task<>() {
            @Override
            protected List<Commentaire> call() throws Exception {
                return ServiceCommentaire.getCommentsByPost(currentPost.getId_post());
            }
        };

        loadCommentsTask.setOnSucceeded(event -> {
            List<Commentaire> comments = loadCommentsTask.getValue();
            commentsContainer.getChildren().clear(); // Clear previous comments
            for (Commentaire comment : comments) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommentItem.fxml"));
                    VBox commentBox = loader.load();
                    CommentsItemsController controller = loader.getController();
                    controller.setCommentData(comment);
                    controller.setPostItemController(PostItemController.this);
                    commentsContainer.getChildren().add(commentBox);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error loading comment: " + comment.getId_commentaire(), e);
                }
            }
        });

        loadCommentsTask.setOnFailed(event -> logger.log(Level.SEVERE, "Error loading comments", loadCommentsTask.getException()));

        new Thread(loadCommentsTask).start();
    }

    private void openEditWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePost.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Post");

            UpdatePostController updateController = loader.getController();
            updateController.setAffichagePostController(affichagePostController);
            updateController.setPost(currentPost, affichagePostController);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePost() {
        if (currentPost != null) {
            ServicePost servicePost = new ServicePost();
            servicePost.remove(currentPost.getId_post());

            if (PostListView != null) {
                PostListView.getItems().remove(currentPost);
            }

            System.out.println("Post deleted!");
        }
    }

    public void setAffichagePostController(AffichagePostController affichagePostController) {
        this.affichagePostController = affichagePostController;
    }
}
