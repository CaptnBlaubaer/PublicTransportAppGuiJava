package de.apaschold.apabfahrteninfo.logic.db;

import de.apaschold.apabfahrteninfo.logic.ModelClassFactory;
import de.apaschold.apabfahrteninfo.model.Route;
import de.apaschold.apabfahrteninfo.model.SingleStop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>DbReader</h2>
 * This class is responsible for reading data from the database.
 * Provides the SQL statements to retrieve information about:
 * <li>available stop names </li>
 * <li>information for a SingleStop </li>
 * <li>direct routes between two stops </li>
 */
public class DbReader {
    //0. constants
    private static final String SQL_REQUEST_ARRIVAL_AND_DEPARTURE_FOR_DATE_AND_STOP =
            "SELECT routes.route_short_name, trips.trip_headsign, stop_times.arrival_time, stop_times.departure_time " +
                    "FROM stop_times " +
                    "JOIN stops ON stop_times.stop_id = stops.stop_id " +
                    "JOIN trips ON stop_times.trip_id = trips.trip_id " +
                    "JOIN routes ON trips.route_id = routes.route_id " +
                    "JOIN calendar_dates ON trips.service_id = calendar_dates.service_id " +
                    "WHERE stops.stop_name = '%s' AND calendar_dates.date = '%s'";

    private static final String SQL_REQUEST_FOR_STOP_NAMES = "SELECT stop_name FROM stops";

    public static final String SQL_REQUEST_FOR_ROUTE_BETWEEN_TWO_STOPS =
            "SELECT routes.route_short_name, trips.trip_headsign, arrival.arrival_time, departure.departure_time " +
                    "FROM trips " +
                    // checks that both stop_times belong to the same trip
                    "JOIN stop_times departure ON departure.trip_id = trips.trip_id " +
                    "JOIN stop_times arrival ON arrival.trip_id = departure.trip_id " +
                    //gets the stop names for departure and arrival
                    "JOIN stops departureStop ON departure.stop_id = departureStop.stop_id " +
                    "JOIN stops arrivalStop ON arrival.stop_id = arrivalStop.stop_id " +
                    //gets the route number belonging to the trip
                    "JOIN routes ON routes.route_id = trips.route_id " +
                    "JOIN calendar_dates ON trips.service_id = calendar_dates.service_id " +
                    "WHERE departureStop.stop_name = '%s' " +
                    "AND arrivalStop.stop_name = '%s' " +
                    "AND calendar_dates.date = '%s' " +
                    // ensures that the departure stop comes before the arrival stop in the trip
                    "AND departure.stop_sequence < arrival.stop_sequence ";


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    //endregion

    //1.attributes
    //endregion

    //2.constructor
    private DbReader() {
    }
    //endregion

    /**
     * <h2>getDeparturesForDateAndStop</h2>
     * <li>Retrieves a list of departures for a given stop and date.</li>
     * <li>Obtains the necessary informations from a sql-based database</li>
     * <li>Creates {@link SingleStop} Objects through factory method</li>
     *
     * @param stopName
     * @param chosenDateTime
     * @return sorted list of {@link SingleStop} objects sorted by departure time
     */
    public static List<SingleStop> getArrivalsAndDeparturesForDateAndStop(String stopName, LocalDateTime chosenDateTime) {
        List<SingleStop> departures = new ArrayList<>();

        LocalDate chosenDate = chosenDateTime.toLocalDate();

        String statement = String.format(
                SQL_REQUEST_ARRIVAL_AND_DEPARTURE_FOR_DATE_AND_STOP,
                stopName, //stop_name
                chosenDate.format(DATE_FORMATTER) // date
        );

        try (Connection connection = DbManager.getInstance().getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                SingleStop generatedDeparture = ModelClassFactory.getStopTimeFromResultSet(chosenDate, resultSet);
                departures.add(generatedDeparture);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departures.stream()
                .sorted()
                .toList();
    }

    /**
     * <h2>getAllStops</h2>
     * <li>Retrieves a list of all available stop names from the database.</li>
     *
     * @return alphabetically sorted list of stop names
     */
    public static List<String> getAllStops() {
        List<String> stops = new ArrayList<>();

        String statement = SQL_REQUEST_FOR_STOP_NAMES;

        try (Connection connection = DbManager.getInstance().getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                stops.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stops.stream()
                .sorted()
                .toList();
    }

    /**
     * <h2>getDirectRouteForDate</h2>
     * <li>Retrieves a list of direct routes between two stops for a given date.</li>
     * <li>Obtains the necessary informations from a sql-based database</li>
     * <li>Creates {@link Route} Objects through factory method</li>
     *
     * @param departureStop
     * @param arrivalStop
     * @param chosenDateTime
     * @return sorted list of {@link Route} objects sorted by departure time
     */
    public static List<Route> getDirectRouteForDate(String departureStop, String arrivalStop, LocalDateTime chosenDateTime) {
        List<Route> routes = new ArrayList<>();

        LocalDate chosenDate = chosenDateTime.toLocalDate();

        String statement = String.format(
                SQL_REQUEST_FOR_ROUTE_BETWEEN_TWO_STOPS,
                departureStop,
                arrivalStop,
                chosenDate.format(DATE_FORMATTER)); // date);

        try (Connection connection = DbManager.getInstance().getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {

                    Route generatedRoute = ModelClassFactory.getRouteFromResultSet(
                            chosenDate,
                            resultSet,
                            departureStop,
                            arrivalStop
                    );
                    routes.add(generatedRoute);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return routes.stream()
                .sorted()
                .toList();
    }
    //endregion


}
