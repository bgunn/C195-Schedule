package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost:3306/";
    private static final String database = "client_schedule";
    private static final String user = "sqlUser";
    private static final String password = "Passw0rd!";
    private static final String jdbcURL = protocol + vendor + location + database + "?connectionTimeZone=SERVER";
    private static Connection connection;

    /**
     * Opens a connection to the configured MySQL database.
     * @return Connection
     */
    public static Connection openConnection() {
        try {
            connection = DriverManager.getConnection(jdbcURL, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Method to close the connection to the MySQL database.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter method for the database connection
     *
     * @return Connection
     */
    public static Connection getConnection() {
        return connection;
    }
}
