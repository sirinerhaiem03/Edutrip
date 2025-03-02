package tn.edutrip.entities;

public class Post {
    private int id_post;
    private String contenu;
    private String date_creation;
    private int id_etudiant;
    private String image;
    private String categorie;
    private int likes;
    private int dislikes;

    public Post() {
    }
    public Post(int id_post, String contenu, String date_creation, int id_etudiant, String image, String categorie, int likes, int dislikes) {
        this.id_post = id_post;
        this.contenu = contenu;
        this.date_creation = date_creation;
        this.id_etudiant = id_etudiant;
        this.image = image;
        this.categorie = categorie;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public int getId_etudiant() {
        return id_etudiant;
    }

    public void setId_etudiant(int id_etudiant) {
        this.id_etudiant = id_etudiant;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id_post=" + id_post +
                ", contenu='" + contenu + '\'' +
                ", date_creation='" + date_creation + '\'' +
                ", id_etudiant=" + id_etudiant +
                ", image='" + image + '\'' +
                ", categorie='" + categorie + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                '}';
    }
}
