package nl.hu.dp;

import java.sql.*;

import static java.lang.String.format;

public class Main {

    public static void main(String[] args) {
        System.out.println("Test");
    }
    private static Connection connection = null;

    private static Connection getConnection() throws SQLException {
        if (connection == null) {

            String databaseName = "ovchip";
            String username = "Bas";
            String password = "MelissaVoets13";

        String url = format(
                "jdbc:postgresql://localhost/%s?user=%s&password=%s",
                databaseName, username, password
        );

        connection = DriverManager.getConnection(url);
    }
        return connection;
    }
    private static void closeConnection() throws
            SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
    private static void testConnection() throws SQLException {
        getConnection();
        String query = "SELECT * FROM reiziger;";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet set = statement.executeQuery();
        while (set != null && set.next()) {
            System.out.println(set.getString("achternaam"));
        }
        closeConnection();
    }
}