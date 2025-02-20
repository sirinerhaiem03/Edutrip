package tn.esprit.entities;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String mail;
    private String tel;
    private String password;
    private String confirmpassword;
    private String status = "inactive";
    private String role = "USER";

    // Constructeurs
    public User() {}

    public User(String nom, String prenom, String mail, String tel, String password, String confirmpassword, String status, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.tel = tel;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.status = (status != null) ? status : "inactive";  // Assurez-vous que le statut ne soit jamais null
        this.role = (role != null) ? role : "USER";  // Assurez-vous que le rôle ne soit jamais null
    }

    public User(int id, String nom, String prenom, String mail, String tel, String status) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.tel = tel;
        this.status = (status != null) ? status : "inactive";  // Assurez-vous que le statut ne soit jamais null
        this.role = "USER";  // Par défaut "USER"
    }

    public User(int id, String nom, String prenom, String mail, String password, String tel, String status) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.password = password;
        this.tel = tel;
        this.status = (status != null) ? status : "inactive";  // Assurez-vous que le statut ne soit jamais null
        this.role = "USER";  // Par défaut "USER"
    }

    public User(String nom, String prenom, String mail, String tel, String password, String confirmpassword) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.tel = tel;
        this.password = password;
        this.confirmpassword = confirmpassword;
        this.role = "USER"; // Par défaut "USER"
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmpassword() { return confirmpassword; }
    public void setConfirmpassword(String confirmpassword) { this.confirmpassword = confirmpassword; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
