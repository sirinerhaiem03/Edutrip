package tn.EduTrip.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    final String URL = "jdbc:mysql://localhost:3306/edutrip";
    final String USER = "root";
    final String PASSWORD = "";

    Connection con ;
    static MyDatabase instance;

    private MyDatabase(){
        try {
            con = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static   MyDatabase getInstance(){
        if (instance==null){
            instance= new MyDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return con;
    }
}
