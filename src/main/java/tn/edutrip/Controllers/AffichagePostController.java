package tn.edutrip.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.File;

public class AffichagePostController {

    @FXML
    private ListView<Post> PostListView;

    @FXML
    private Pane ajoutPostPane; // Pane pour le formulaire d'ajout de post

    private String imagePath;
    @FXML
    private TextField IdCategorie;

    @FXML
    private TextField IdContenu;

    @FXML
    private ImageView idImage;

    private ServicePost servicePost;
    private ObservableList<Post> posts;

    public AffichagePostController() {
        servicePost = new ServicePost();
        posts = FXCollections.observableArrayList(servicePost.getAll());
    }

    @FXML
    public void initialize() {
        // Charger le formulaire d'ajout de post


        // Affecter la liste observable à la ListView
        PostListView.setItems(posts);

        // Utiliser un cellFactory pour personnaliser chaque élément de la ListView
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
                        setGraphic(vbox);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }



    public void removePostFromList(Post post) {
        posts.remove(post);
    }

    public void updatePostInList(Post updatedPost) {
        int index = -1;

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId_post() == updatedPost.getId_post()) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            posts.set(index, updatedPost);
            PostListView.refresh();
        }
    }

    @FXML
    void AjouterPost(ActionEvent event) {
        // Récupérer les données saisies par l'utilisateur
        String categorie = IdCategorie.getText();
        String contenu = IdContenu.getText();

        // Créer un nouvel objet Post
        Post post = new Post();
        post.setCategorie(categorie);
        post.setContenu(contenu);
        post.setImage(imagePath); // Utiliser le chemin de l'image sélectionnée
        post.setId_etudiant(1); // Remplacez par l'ID de l'étudiant connecté
        post.setDate_creation(java.time.LocalDate.now().toString()); // Date actuelle

        // Ajouter le post via le service
        servicePost.add(post);
        System.out.println("Post ajouté avec succès !");

        // Réinitialiser les champs après l'ajout
        IdCategorie.clear();
        IdContenu.clear();
        idImage.setImage(null); // Réinitialiser l'image
        imagePath = null; // Réinitialiser le chemin de l'image
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
}
