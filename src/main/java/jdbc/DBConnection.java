package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/intellistock";
    private static final String USER = "root";          
    private static final String PASSWORD = "Devashri236*";  

    private static Connection connection;

    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {

                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();

        }

        return connection;
    }
}