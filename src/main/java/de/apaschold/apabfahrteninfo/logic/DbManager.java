package de.apaschold.apabfahrteninfo.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

public class DbManager {
    //0. constants
    private static final String DB_LOCAL_SERVER_IP_ADDRESS = "localhost/";
    private static final String DB_LOCAL_NAME = "public_transport_mdv";

    private static final String DB_LOCAL_CONNECTION_URL =
            "jdbc:mariadb://" + DB_LOCAL_SERVER_IP_ADDRESS + DB_LOCAL_NAME;

    private static final String DB_LOCAL_USER_NAME = "root";
    private static final String DB_LOCAL_USER_PW = "";
    //endregion

    //1. attributes
    private static DbManager instance;
    //endregion

    //2. constructors
    private DbManager() {
    }
    //endregion

    //3. getInstance Method
    public static DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }
    //endregion

    //4. read and write connection to database
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
