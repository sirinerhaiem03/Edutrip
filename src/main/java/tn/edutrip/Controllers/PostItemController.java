
package tn.edutrip.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.edutrip.entities.Favoris;
import tn.edutrip.entities.Post;
import tn.edutrip.entities.User;
import tn.edutrip.services.ServiceFavoris;
import tn.edutrip.services.ServicePost;
import tn.edutrip.services.ServiceCommentaire;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import tn.edutrip.entities.Commentaire;
import tn.edutrip.services.ServiceUser;

import tn.edutrip.utils.BadWordsAPI;
import tn.edutrip.utils.CommentAnalysis;

import static tn.edutrip.utils.MyMemoryTranslateAPI.translateText;

public class PostItemController {

    private Post post;
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
    private Button translateButton;
    @FXML
    private Text likeCountText;  // Afficher le nombre de likes
    @FXML
    private Text dislikeCountText;
    @FXML
    private ImageView favorisIcon;
    @FXML
    private ComboBox<String> languageSelector;
    @FXML
    private ListView<Post> favorisListView;
    @FXML
    public void initialize() {
        // Ajouter des langues au sélecteur
        languageSelector.getItems().addAll("fr", "es", "de", "it", "ar"); // Exemples de langues
    }


    private Post currentPost;
    private ServicePost servicePost;
    private ListView<Post> PostListView;
    private AffichagePostController affichagePostController;
    private ServiceCommentaire ServiceCommentaire;
    private ServiceFavoris serviceFavoris = new ServiceFavoris();
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

        // Get the User corresponding to the post's id_etudiant
        User author = getUserById(post.getId_etudiant());

        // Debug logs
        System.out.println("Post id_etudiant: " + post.getId_etudiant());
        if (author != null) {
            System.out.println("Author found: " + author.getNom() + " " + author.getPrenom());
        } else {
            System.out.println("Author is null for id_etudiant: " + post.getId_etudiant());
        }

        // Set author name
        IdAuthor.setText(author != null ? author.getNom() + " " + author.getPrenom() : "Auteur inconnu");

        // Set other post data
        IdDate.setText(post.getDate_creation());
        IdContenu.setText(post.getContenu());
        IdCategorie.setText("Category: " + post.getCategorie());
        likeCountText.setText(String.valueOf(post.getLikes()));
        dislikeCountText.setText(String.valueOf(post.getDislikes()));

        // Load post image if it exists
        if (post.getImage() != null && !post.getImage().isEmpty()) {
            try {
                String imagePath = "/images/" + post.getImage(); // Chemin relatif
                Image img = new Image(getClass().getResource(imagePath).toExternalForm());
                IdPostImage.setImage(img);
            } catch (Exception e) {
                System.out.println("Image not found: " + post.getImage());
            }
        }

        // Set favorite icon
        int idEtudiant = 1; // Remplacez par l'ID de l'utilisateur actuel
        int idPost = post.getId_post();
        if (serviceFavoris.estFavoris(idEtudiant, idPost)) {
            loadImage(favorisIcon, "/images/star (1).png"); // Icône remplie
        } else {
            loadImage(favorisIcon, "/images/star.png"); // Icône vide
        }

        // Set event handlers
        CommentsIcon.setOnMouseClicked(event -> loadCommentsAsync());
        editIcon.setOnMouseClicked(event -> openEditWindow());
        deleteIcon.setOnMouseClicked(event -> deletePost());
        submitCommentButton.setOnAction(event -> submitComment());
        favorisIcon.setOnMouseClicked(event -> toggleFavoris());
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
            servicePost.updateLikesDislikes(currentPost.getId_post(), currentPost.getLikes(), currentPost.getDislikes());

            // Mettre à jour l'affichage
            likeCountText.setText(String.valueOf(currentPost.getLikes()));
        }
    }

    @FXML
    private void dislikePost() {
        if (currentPost != null) {
            // Incrémentez le nombre de dislikes
            currentPost.setDislikes(currentPost.getDislikes() + 1);

            // Sauvegarder la modification dans la base de données
            servicePost.updateLikesDislikes(currentPost.getId_post(), currentPost.getLikes(), currentPost.getDislikes());

            // Mettre à jour l'affichage
            dislikeCountText.setText(String.valueOf(currentPost.getDislikes()));
        }
    }




    @FXML
    private void submitComment() {
        String commentText = commentInput.getText().trim();

        // Vérifier si le commentaire est vide
        if (commentText.isEmpty()) {
            showAlert("Erreur de saisie", "Le commentaire ne peut pas être vide.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean hasBadWords = BadWordsAPI.containsBadWords(commentText);

            if (hasBadWords) {
                showAlert("Modération", "Votre commentaire contient des mots inappropriés. Veuillez le modifier.", Alert.AlertType.WARNING);
                return;
            }
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la vérification du commentaire.", Alert.AlertType.ERROR);
            return;
        }

        // Si le commentaire est approprié, l'ajouter à la base de données
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


    public void setAffichagePostController(AffichagePostController affichagePostController) {
        this.affichagePostController = affichagePostController;
    }


    @FXML
    private void translatePost() {
        String originalText = IdContenu.getText(); // Récupérer le texte du post
        String targetLanguage = languageSelector.getValue(); // Récupérer la langue cible

        if (originalText == null || originalText.isEmpty()) {
            showAlert("Erreur", "Aucun texte à traduire.", Alert.AlertType.WARNING);
            return;
        }

        if (targetLanguage == null || targetLanguage.isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner une langue cible.", Alert.AlertType.WARNING);
            return;
        }

        // Traduire le texte
        String translatedText = translateText(originalText, "en", targetLanguage);

        if (translatedText != null) {
            IdContenu.setText(translatedText); // Afficher le texte traduit
        } else {
            showAlert("Erreur", "La traduction a échoué.", Alert.AlertType.ERROR);
        }
}
    @FXML
    private void toggleFavoris() {
        if (currentPost == null) {
            return; // Si le post est null, ne rien faire
        }

        int idEtudiant = 1; // Remplacez par l'ID de l'utilisateur actuel (pour le moment, on utilise 1)
        int idPost = currentPost.getId_post();

        // Vérifier si le post est déjà dans les favoris
        if (serviceFavoris.estFavoris(idEtudiant, idPost)) {
            // Retirer des favoris
            serviceFavoris.remove(idEtudiant, idPost);
            System.out.println("Post retiré des favoris !");
            loadImage(favorisIcon, "/images/star.png"); // Charger l'icône vide
        } else {
            // Ajouter aux favoris
            Favoris favoris = new Favoris(idEtudiant, idPost);
            serviceFavoris.add(favoris);
            System.out.println("Post ajouté aux favoris !");
            loadImage(favorisIcon, "/images/star (1).png"); // Charger l'icône remplie
        }
        refreshFavorisList();
    }

    private void loadImage(ImageView imageView, String imagePath) {
        try {
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl == null) {
                System.err.println("L'image n'a pas été trouvée : " + imagePath);
            } else {
                Image image = new Image(imageUrl.toExternalForm());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void refreshFavorisList() {
        int idEtudiant = 1; // Remplacez par l'ID de l'utilisateur actuel
        List<Integer> favorisIds = serviceFavoris.getFavorisByEtudiant(idEtudiant);
        favorisListView.getItems().clear();

        for (int idPost : favorisIds) {
            Post post = servicePost.getPostById(idPost);
            if (post != null) {
                favorisListView.getItems().add(post);
            }
        }
    }

    }


