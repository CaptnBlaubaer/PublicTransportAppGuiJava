package de.apaschold.apabfahrteninfo.logic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

/**
 * <h2>DbManager</h2>
 * A singleton class that manages the connection to the local database.
 * It provides a method to get a connection to the database.
 */
public class DbManager {
    //0. constants
    private static final String DB_LOCAL_SERVER_IP_ADDRESS = "localhost/";
    private static final String DB_LOCAL_NAME = "fahrplan";

    private static final String DB_LOCAL_CONNECTION_URL =
            "jdbc:mysql://" + DB_LOCAL_SERVER_IP_ADDRESS + DB_LOCAL_NAME;

    private static final String DB_LOCAL_USER_NAME = "java";
    private static final String DB_LOCAL_USER_PW = "password";
    //endregion

    //1. attributes
    private static DbManager instance;
    //endregion

    //2. constructors
    private DbManager() {
    }
    //endregion

    //3. getInstance Method
    public static synchronized DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }
    //endregion

    //4. read and write connection to database
    /**
     * Gets a connection to the local database.
     * First, it attempts to establish a connection and then closes it immediately, to check if the database is reachable.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs
     */
    public Connection getDatabaseConnection() throws SQLException {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(DB_LOCAL_CONNECTION_URL, DB_LOCAL_USER_NAME, DB_LOCAL_USER_PW);
            connection.close();

            connection = DriverManager.getConnection(DB_LOCAL_CONNECTION_URL, DB_LOCAL_USER_NAME, DB_LOCAL_USER_PW);
        } catch (SQLNonTransientConnectionException e) {
            System.err.println("Error connecting to the database: ");
            e.printStackTrace();
        }

        return connection;
    }
    //endregion
}
