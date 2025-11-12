package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/todo";
    private static final String USER = "postgres";
    private static final String PASS = "123";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
