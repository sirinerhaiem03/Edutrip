package tn.edutrip.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    final String URL = "jdbc:mysql://localhost:3306/edutrip";
    final String USER = "root";
    final String PASSWORD = "";
    private Connection con;
    static MyDatabase instance;

    // Constructeur pour initialiser la connexion
    public MyDatabase() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Méthode pour récupérer l'instance unique de la classe
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    // Méthode pour récupérer la connexion
    public Connection getConnection() {
        return con;
    }
}
