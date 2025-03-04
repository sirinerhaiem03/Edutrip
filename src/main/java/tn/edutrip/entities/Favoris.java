package tn.edutrip.entities;

public class Favoris {
    private int id_favoris;
    private int id_etudiant;
    private int id_post;

    public Favoris(int id_etudiant, int id_post) {
        this.id_etudiant = id_etudiant;
        this.id_post = id_post;
    }

    public int getIdEtudiant() { return id_etudiant; }
    public int getIdPost() { return id_post; }
}
