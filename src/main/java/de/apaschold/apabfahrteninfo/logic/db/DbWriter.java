package de.apaschold.apabfahrteninfo.logic.db;

import de.apaschold.apabfahrteninfo.logic.filehandling.TextFileManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

/**
 * <h2>DbWriter</h2>
 * DbWriter is responsible for updating the database with data from text files.
 * It reads data from text files and inserts it into the corresponding database tables.
 */
public class DbWriter {
    //0. constants
    private static final String AGENCIES_FILE_NAME = "agency.txt";
    private static final String AGENCIES_DB_TABLE = "agencies";
    private static final String AGENCIES_INSERT_STATEMENT =
            "INSERT INTO " + AGENCIES_DB_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String ROUTES_FILE_NAME = "routes.txt";
    private static final String ROUTES_DB_TABLE = "routes";
    private static final String ROUTES_INSERT_STATEMENT =
            "INSERT INTO " + ROUTES_DB_TABLE + " VALUES (?, ?, ?, ?, ?)";

    private static final String STOPS_FILE_NAME = "stops.txt";
    private static final String STOPS_DB_TABLE = "stops";
    private static final String STOPS_INSERT_STATEMENT =
            "INSERT INTO " + STOPS_DB_TABLE + " VALUES (?, ?, ?, ?)";

    private static final String TRANSFER_FILE_NAME = "transfers.txt";
    private static final String TRANSFERS_DB_TABLE = "transfers";
    private static final String TRANSFERS_INSERT_STATEMENT =
            "INSERT INTO " + TRANSFERS_DB_TABLE + " VALUES (?, ?, ?, ?)";

    private static final String TRIP_FILE_NAME = "trips.txt";
    private static final String TRIPS_DB_TABLE = "trips";
    private static final String TRIPS_INSERT_STATEMENT =
            "INSERT INTO " + TRIPS_DB_TABLE + " VALUES (?, ?, ?, ?, ?, ?)";

    private static final String STOP_TIMES_FILE_NAME = "stop_times.txt";
    private static final String STOP_TIMES_DB_TABLE = "stop_times";
    private static final String STOP_TIMES_INSERT_STATEMENT =
            "INSERT INTO " + STOP_TIMES_DB_TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String CALENDER_DATES_FILE_NAME = "calendar_dates.txt";
    private static final String CALENDAR_DATES_DB_TABLE = "calendar_dates";
    private static final String CALENDAR_DATES_INSERT_STATEMENT =
            "INSERT INTO " + CALENDAR_DATES_DB_TABLE + " VALUES (?, ?, ?)";
    //endregion

    //1. attributes
    //endregion

    //2. constructors
    private DbWriter() {
    }
    //endregion

    //3. writing methods
    public static void updateAllTables(){
        try (Connection connection = DbManager.getInstance().getDatabaseConnection()) {
            if (connection != null) {
                updateAgenciesData(connection);
                updateRoutesData(connection);
                updateStopsData(connection);
                updateTransfersData(connection);
                updateTripsData(connection);
                updateStopTimesData(connection);
                updateCalenderDatesData(connection);
            } else {
                System.err.println("Failed to establish a database connection.");
            }
        } catch (Exception e) {
            System.err.println("Error while updating the database: ");
            e.printStackTrace();
        }
    }

    /**
     * Reads the agency data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing agency data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateAgenciesData(Connection connection){
        clearTable(connection,AGENCIES_DB_TABLE);

        List<String> agenciesData = TextFileManager.getInstance().readTextFile(AGENCIES_FILE_NAME);
        System.out.println("Agencies data size: " + agenciesData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < agenciesData.size(); index++){
            String[] agencyDataParts = agenciesData.get(index).split(",");

            try(PreparedStatement preparedStatement = connection.prepareStatement(AGENCIES_INSERT_STATEMENT)) {
                preparedStatement.setString(1, agencyDataParts[0]); // agencyId
                preparedStatement.setString(2, agencyDataParts[1]); // name
                preparedStatement.setString(3, agencyDataParts[2]); // shortName
                preparedStatement.setString(4, agencyDataParts[3]); // url
                preparedStatement.setString(5, agencyDataParts[4]); // email
                preparedStatement.setString(6, agencyDataParts[5]); // phone
                preparedStatement.setString(7, agencyDataParts[6]); // address

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for agency: " + Arrays.toString(agencyDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process agencies data.");
        System.out.println("Total rows affected in " + AGENCIES_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Reads the routes data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing route data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateRoutesData(Connection connection) {
        clearTable(connection, ROUTES_DB_TABLE);

        List<String> routesData = TextFileManager.getInstance().readTextFile(ROUTES_FILE_NAME);
        System.out.println("Routes data size: " + routesData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < routesData.size(); index++) {
            String[] routeDataParts = routesData.get(index).split(",");

            try (PreparedStatement preparedStatement = connection.prepareStatement(ROUTES_INSERT_STATEMENT)) {
                preparedStatement.setString(1, routeDataParts[0]); // routeId
                preparedStatement.setString(2, routeDataParts[1]); // agencyId
                preparedStatement.setString(3, routeDataParts[2]); // shortName
                preparedStatement.setString(4, routeDataParts[3]); // longName
                preparedStatement.setString(5, routeDataParts[4]); // type

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for route: " + Arrays.toString(routeDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process routes data.");
        System.out.println("Total rows affected in " + ROUTES_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Reads the stops data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing stop data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateStopsData(Connection connection) {
        clearTable(connection, STOPS_DB_TABLE);

        List<String> stopsData = TextFileManager.getInstance().readTextFile(STOPS_FILE_NAME);
        System.out.println("Stops data size: " + stopsData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < stopsData.size(); index++) {
            String[] stopDataParts = stopsData.get(index).split(",");

            try (PreparedStatement preparedStatement = connection.prepareStatement(STOPS_INSERT_STATEMENT)) {
                preparedStatement.setString(1, stopDataParts[0]);// stopId

                /*
                 * Some of the stops contain a comma in their name,
                 * e.g. "Leipzig, Hauptbahnhof" or "Leipzig, Markkleeberg"
                 * Then stopDataParts.length == 5 and "stop_name" is the second part and third index
                 */
                if (stopDataParts.length == 4) {
                    preparedStatement.setString(2, stopDataParts[1]); // name
                    preparedStatement.setString(3, stopDataParts[2]); // latitude
                    preparedStatement.setString(4, stopDataParts[3]); // longitude
                } else {
                    preparedStatement.setString(2,
                            stopDataParts[1].replace("\"", "") + "," + stopDataParts[2].replace("\"", "") ); // name
                    preparedStatement.setString(3, stopDataParts[3]); // latitude
                    preparedStatement.setString(4, stopDataParts[4]); // longitude
                }

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for stop: " + Arrays.toString(stopDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process stops data.");
        System.out.println("Total rows affected in " + STOPS_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Reads the transfer data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing stop data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateTransfersData(Connection connection) {
        clearTable(connection, TRANSFERS_DB_TABLE);

        List<String> transfersData = TextFileManager.getInstance().readTextFile(TRANSFER_FILE_NAME);
        System.out.println("Transfers data size: " + transfersData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < transfersData.size(); index++) {
            String[] transferDataParts = transfersData.get(index).split(",");

            if( transferDataParts.length < 4) {
                /*some entries show transfer between a single stop and itself
                 * e.g. served by tram AND bus, tranfer time is 0 seconds
                 * these will not be inserted into the database
                 */
                continue;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(TRANSFERS_INSERT_STATEMENT)) {
                preparedStatement.setString(1, transferDataParts[0]); // fromStopId
                preparedStatement.setString(2, transferDataParts[1]); // toStopId
                preparedStatement.setString(3, transferDataParts[2]); // fromRouteId
                preparedStatement.setString(4, transferDataParts[3]); // toRouteId

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for transfer: " + Arrays.toString(transferDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process transfers data.");
        System.out.println("Total rows affected in " + TRANSFERS_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Reads the trips data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing stop data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateTripsData(Connection connection) {
        clearTable(connection, TRIPS_DB_TABLE);

        List<String> tripsData = TextFileManager.getInstance().readTextFile(TRIP_FILE_NAME);
        System.out.println("Trips data size: " + tripsData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < tripsData.size(); index++) {
            String[] tripDataParts = tripsData.get(index).split(",");

            try (PreparedStatement preparedStatement = connection.prepareStatement(TRIPS_INSERT_STATEMENT)) {
                preparedStatement.setString(1, tripDataParts[0]); // routeId
                preparedStatement.setString(2, tripDataParts[1]); // serviceId
                preparedStatement.setString(3, tripDataParts[2]); // tripId
                /*
                 * Some of the stops contain a comma in their name,
                 * e.g. "Leipzig, Hauptbahnhof" or "Leipzig, Markkleeberg"
                 * Then stopDataParts.length == 5 and "stop_name" is the second part and third index
                 */
                if (tripDataParts.length == 6) {
                    preparedStatement.setString(4, tripDataParts[3]); // tripHeadsign
                    preparedStatement.setString(5, tripDataParts[4]); // shortName
                    preparedStatement.setString(6, tripDataParts[5]); // directionId
                } else {
                    preparedStatement.setString(4,
                            tripDataParts[3].replace("\"", "") + "," + tripDataParts[4].replace("\"", "") ); // tripHeadsign
                    preparedStatement.setString(5, tripDataParts[5]); // shortName
                    preparedStatement.setString(6, tripDataParts[6]); // directionId
                }

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for trip: " + Arrays.toString(tripDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process trips data.");
        System.out.println("Total rows affected in " + TRIPS_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Reads the stoptimes data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing stop data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateStopTimesData(Connection connection) {
        clearTable(connection, STOP_TIMES_DB_TABLE);

        List<String> stopTimesData = TextFileManager.getInstance().readTextFile(STOP_TIMES_FILE_NAME);
        System.out.println("Stop times data size: " + stopTimesData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < stopTimesData.size(); index++) {
            String[] stopTimeDataParts = stopTimesData.get(index).split(",");

            try (PreparedStatement preparedStatement = connection.prepareStatement(STOP_TIMES_INSERT_STATEMENT)) {
                preparedStatement.setString(1, stopTimeDataParts[0]); // tripId
                preparedStatement.setString(2, stopTimeDataParts[1]); // arrivalDateTime
                preparedStatement.setString(3, stopTimeDataParts[2]); // departureDateTime
                preparedStatement.setString(4, stopTimeDataParts[3]); // stopId
                preparedStatement.setString(5, stopTimeDataParts[4]); // stopSequence
                preparedStatement.setString(6, stopTimeDataParts[5]); // pickupType
                preparedStatement.setString(7, stopTimeDataParts[6]); // dropOffType

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for stop time: " + Arrays.toString(stopTimeDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process stop times data.");
        System.out.println("Total rows affected in " + STOP_TIMES_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Reads the stops data from a text file and inserts it into the database.
     * The text file should be formatted with each line containing stop data separated by commas.
     * The first line is expected to be a header and will be skipped.
     *
     * @param connection the database connection to use for inserting data
     */
    private static void updateCalenderDatesData(Connection connection) {
        clearTable(connection, CALENDAR_DATES_DB_TABLE);

        List<String> calendarDatesData = TextFileManager.getInstance().readTextFile(CALENDER_DATES_FILE_NAME);
        System.out.println("Calendar dates data size: " + calendarDatesData.size());

        int rowsAffected = 0;
        LocalTime startTime = LocalTime.now();

        for (int index = 1; index < calendarDatesData.size(); index++) {
            String[] calendarDateDataParts = calendarDatesData.get(index).split(",");

            try (PreparedStatement preparedStatement = connection.prepareStatement(CALENDAR_DATES_INSERT_STATEMENT)) {
                preparedStatement.setString(1, calendarDateDataParts[0]); // serviceId
                preparedStatement.setString(2, calendarDateDataParts[1]); // date
                preparedStatement.setString(3, calendarDateDataParts[2]); // exceptionType

                rowsAffected += preparedStatement.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error inserting data for calendar date: " + Arrays.toString(calendarDateDataParts));
                e.printStackTrace();
            }
        }

        LocalTime endTime = LocalTime.now();
        long duration = Duration.between(startTime, endTime).toMillis();

        System.out.println(duration + " milliseconds to process calendar dates data.");
        System.out.println("Total rows affected in " + CALENDAR_DATES_DB_TABLE + ": " + rowsAffected);
    }

    /**
     * Clears the database table by deleting all rows.
     *
     * @param connection the database connection to use for clearing the table
     */
    private static void clearTable(Connection connection, String tableName) {
        String clearStatement = "DELETE FROM " + tableName;
        try (PreparedStatement preparedStatement = connection.prepareStatement(clearStatement )) {
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Cleared " + rowsAffected + " rows from " + tableName);
        } catch (Exception e) {
            System.err.println("Error clearing the database table: " + tableName);
            e.printStackTrace();
        }
    }

}

