
package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.edutrip.entities.Post;
import tn.edutrip.entities.User;
import tn.edutrip.services.ServicePost;
import tn.edutrip.services.ServiceCommentaire;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tn.edutrip.entities.Commentaire;
import tn.edutrip.services.ServiceUser;
import tn.edutrip.utils.CommentAnalysis;
import tn.edutrip.utils.OpenAIModerationAPI;
public class AdminPostController {

    @FXML
    private ImageView CommentsIcon;
    @FXML
    private VBox commentsContainer;

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


    private Post currentPost;
    private ServicePost servicePost;
    private ListView<Post> PostListView;
    private AffichagePostController affichagePostController;
    private ServiceCommentaire ServiceCommentaire;
    private AffichageAdminController affichageAdminController;
    private static final Logger logger = Logger.getLogger(PostItemController.class.getName());

    public AdminPostController() {
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

        // Get the User corresponding to the post's id_etudiant
        User author = getUserById(post.getId_etudiant());


        IdAuthor.setText(author != null ? author.getNom() + " " + author.getPrenom() : "Unknown Author");
        IdDate.setText(post.getDate_creation());
        IdContenu.setText(post.getContenu());
        IdCategorie.setText("Category: " + post.getCategorie());

        // Display post data
        if (author != null) {
            IdAuthor.setText(author.getNom() + " " + author.getPrenom()); // Set the name and surname
        } else {
            IdAuthor.setText("Unknown Author");
        }



        // Load post image if it exists
        // Si l'image est dans le dossier resources/images
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            try {
                // Utilise le chemin relatif
                String imagePath = "/images/" + post.getImage(); // Dans le classpath
                Image img = new Image(getClass().getResource(imagePath).toExternalForm());
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



    }
    private User getUserById(int id_etudiant) {
        // You can replace this with your actual logic to fetch a User by id from the database
        ServiceUser userService = new ServiceUser(); // Assuming you have a service class for users
        return userService.getUserById(id_etudiant); // Implement the logic to get the user from the DB
    }

    @FXML
    private void likePost() {
        if (currentPost != null) {
            // Incrémentez le nombre de likes
            currentPost.setLikes(currentPost.getLikes() + 1);




            // Sauvegarder la modification dans la base de données
            servicePost.update(currentPost);
        }
    }
    @FXML
    private void dislikePost() {
        if (currentPost != null) {
            // Incrémentez le nombre de dislikes
            currentPost.setDislikes(currentPost.getDislikes() + 1);



            // Sauvegarder la modification dans la base de données
            servicePost.update(currentPost);
        }
    }



    @FXML

    private void showAlert(String title, String message, javafx.scene.control.Alert.AlertType alertType) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                    controller.setAdminPostController(AdminPostController.this);
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment supprimer ce post ?");

            // Vérifier la réponse de l'utilisateur
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    servicePost.remove(currentPost.getId_post());

                    if (PostListView != null) {
                        PostListView.getItems().remove(currentPost);
                    }

                    System.out.println("Post supprimé !");
                }
            });
        }
    }

    public void setAffichageAdminController(AffichageAdminController affichageAdminController) {
        this.affichageAdminController = affichageAdminController;
    }
}


