package tn.edutrip.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyDatabase {private static final String URL = "jdbc:mysql://localhost:3306/edutrip";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static MyDatabase instance; // ACCEDEE SANS CREE UNE AUTRE INSTANCE
    private Connection con;

    private MyDatabase() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
    }

//crée une seule instance de MyDatabase et la conserve.
    public static MyDatabase getInstance() {
        if (instance == null) {//masaretch creation lel cnx
            instance = new MyDatabase();
        }
        return instance;
    }
// retourne cette connexion unique, évitant d'en créer plusieurs.
    public Connection getConnection() {
        return con;
    }
}





