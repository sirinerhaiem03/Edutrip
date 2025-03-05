package tn.edutrip.services;
import tn.edutrip.entities.Post;
import tn.edutrip.utils.MyDatabase;
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePost implements Iservices<Post> {
    Connection con;

    public ServicePost() {
        con = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void add(Post post) {
        try {
            String query = "INSERT INTO post (contenu, date_creation, Id_etudiant, Image, categorie) " +
                    "VALUES ('" + post.getContenu() + "', '" + post.getDate_creation() + "', " +
                    post.getId_etudiant() + ", '" + post.getImage() + "', '" + post.getCategorie() + "')";

            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Erreur ajout : " + e.getMessage());
        }
    }

    @Override
    public void remove(int id) {
        try {
            String query = "DELETE FROM post WHERE id_post = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);  // Set the id_post to the query
            ps.executeUpdate();
            System.out.println("Post supprimé !");
        } catch (SQLException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }
    }

    @Override
    public void update(Post post) {
        try {
            String query = "UPDATE post SET contenu = ?, date_creation = ?, Id_etudiant = ?, " +
                    "Image = ?, categorie = ?, likes = ?, dislikes = ? WHERE id_post = ?";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, post.getContenu());
            ps.setString(2, post.getDate_creation());
            ps.setInt(3, post.getId_etudiant());
            ps.setString(4, post.getImage());
            ps.setString(5, post.getCategorie());
            ps.setInt(6, post.getLikes());    // Ajout du nombre de likes
            ps.setInt(7, post.getDislikes()); // Ajout du nombre de dislikes
            ps.setInt(8, post.getId_post());  // Filtrage par ID du post

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Le post a été mis à jour avec succès !");
            } else {
                System.out.println("Aucun post trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur mise à jour : " + e.getMessage());
        }
    }
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try {
            String query = "SELECT * FROM post";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id_post = rs.getInt("id_post");
                String contenu = rs.getString("contenu");
                String date_creation = rs.getString("date_creation");
                int id_etudiant = rs.getInt("Id_etudiant");
                String image = rs.getString("Image");
                String categorie = rs.getString("categorie");

                // Vérifier si les valeurs sont bien récupérées
                int likes = rs.getInt("likes");
                int dislikes = rs.getInt("dislikes");

                System.out.println("Post récupéré: ID=" + id_post + ", Likes=" + likes + ", Dislikes=" + dislikes);

                Post post = new Post(id_post, contenu, date_creation, id_etudiant, image, categorie, likes, dislikes);
                posts.add(post);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des posts : " + e.getMessage());
        }
        return posts;
    }




    public void getPostsWithComments() {
        try {
            String query = "SELECT p.id_post, p.contenu, p.date_creation, p.id_etudiant, p.image, p.categorie, " +
                    "c.id_commentaire, c.contenu AS commentaire_contenu, c.date_commentaire " +
                    "FROM post p " +
                    "LEFT JOIN commentaire c ON p.id_post = c.id_post";

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("Post ID: " + rs.getInt("id_post"));
                System.out.println("Contenu du Post: " + rs.getString("contenu"));
                System.out.println("Date de création: " + rs.getString("date_creation"));
                System.out.println("Image: " + rs.getString("image"));
                System.out.println("Catégorie: " + rs.getString("categorie"));

                int commentId = rs.getInt("id_commentaire");
                if (commentId != 0) { // Si un commentaire existe
                    System.out.println("  - Commentaire ID: " + commentId);
                    System.out.println("  - Contenu: " + rs.getString("commentaire_contenu"));
                    System.out.println("  - Date: " + rs.getString("date_commentaire"));
                }
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des posts avec commentaires : " + e.getMessage());
        }
    }
    public void updateLikesDislikes(int postId, int likes, int dislikes) {
        String query = "UPDATE post SET likes = ?, dislikes = ? WHERE id_post = ?";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, likes);
            pstmt.setInt(2, dislikes);
            pstmt.setInt(3, postId);

            System.out.println("Mise à jour : PostID = " + postId + ", Likes = " + likes + ", Dislikes = " + dislikes);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Post getPostById(int postId) {
        String query = "SELECT * FROM post WHERE id_post = ?";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Post(
                        rs.getInt("id_post"),
                        rs.getString("contenu"),
                        rs.getString("date_creation"), // Move this before `categorie`
                        rs.getInt("id_etudiant"),
                        rs.getString("image"),
                        rs.getString("categorie"),  // This should come after `image`
                        rs.getInt("likes"),
                        rs.getInt("dislikes")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
