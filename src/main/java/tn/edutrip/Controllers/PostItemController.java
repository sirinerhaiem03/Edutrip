package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.concurrent.Task;
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
public class PostItemController {

    @FXML
    private ImageView CommentsIcon;
    @FXML
    private VBox commentsContainer;
    @FXML
    private TextField commentInput; // Champ de texte pour le commentaire
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

    private Post currentPost;
    private ListView<Post> PostListView;
    private AffichagePostController affichagePostController;
    private PostItemController postItemController;
    private ServiceCommentaire ServiceCommentaire;

    private static final Logger logger = Logger.getLogger(PostItemController.class.getName());

    public PostItemController() {
        ServiceCommentaire = new ServiceCommentaire();
    }


    public void setPostItemController(PostItemController postItemController) {
        this.postItemController = postItemController;
    }
    // Référence au contrôleur parent
    public void setPostData(Post post, ListView<Post> postListView) {
        if (post == null) {
            System.out.println("Le post est nul!");
            return;
        }
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
        CommentsIcon.setOnMouseClicked(event -> loadCommentsAsync());
        // Ajouter des événements pour modifier et supprimer
        editIcon.setOnMouseClicked(event -> ouvrirFenetreModification());
        deleteIcon.setOnMouseClicked(event -> supprimerPost());
        // Ajouter un événement pour soumettre un commentaire
        submitCommentButton.setOnAction(event -> submitComment());
    }

    private void submitComment() {
        String commentText = commentInput.getText().trim();
        if (!commentText.isEmpty() && currentPost != null) {
            // Créer un nouveau commentaire
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
            List<Commentaire> commentaires = loadCommentsTask.getValue();
            commentsContainer.getChildren().clear(); // Effacer les commentaires précédents
            for (Commentaire commentaire : commentaires) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/CommentItem.fxml"));
                    VBox commentBox = loader.load();
                    CommentsItemsController controller = loader.getController();
                    controller.setCommentData(commentaire);
                    controller.setPostItemController(PostItemController.this);
                    commentsContainer.getChildren().add(commentBox);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Erreur lors du chargement du commentaire : " + commentaire.getId_commentaire(), e);
                }
            }
        });

        loadCommentsTask.setOnFailed(event -> logger.log(Level.SEVERE, "Erreur lors du chargement des commentaires", loadCommentsTask.getException()));

        new Thread(loadCommentsTask).start();
    }

    private void ouvrirFenetreModification() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdatePost.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Modifier le post");


            UpdatePostController updateController = loader.getController();
            updateController.setAffichagePostController(affichagePostController);
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
    } // Fermeture correcte de la méthode supprimerPost()

        public void setAffichagePostController (AffichagePostController affichagePostController){
            this.affichagePostController = affichagePostController;
        }
    }
