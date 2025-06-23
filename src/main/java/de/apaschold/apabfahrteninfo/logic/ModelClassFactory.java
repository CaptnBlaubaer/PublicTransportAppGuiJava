package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.model.Route;
import de.apaschold.apabfahrteninfo.model.SingleStop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <h2>ModelClassFactory</h2>
 * Factory class to create model classes from ResultSet objects.
 * This class provides static methods to convert database query results into model objects  {@link SingleStop} or {@link Route}.
 */
public class ModelClassFactory {
    //0. constants
    //endregion

    //1. attributes
    //endregion
    
    //2. contructor
    private ModelClassFactory(){}
    //endregion
    
    //3. Factory method
    /**
     * <h2>getStopTimeFromResultSet</h2>
     * Creates a {@link SingleStop} object from the given ResultSet and date.
     *
     * @param date The date for which the stop time is relevant.
     * @param resultSet The ResultSet containing the stop time data.
     * @return A SingleStop object populated with data from the ResultSet.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    public static SingleStop getStopTimeFromResultSet(LocalDate date, ResultSet resultSet) throws SQLException {
        String routeNumber = resultSet.getString(1);
        String direction = resultSet.getString(2);
        String arrivalTimeAsString = resultSet.getString(3);
        String departureTimeAsString = resultSet.getString(4);

        LocalDateTime arrivalDateTime = generateDateTime(date, arrivalTimeAsString);
        LocalDateTime departureDateTime = generateDateTime(date, departureTimeAsString);

        return new SingleStop(
                routeNumber,
                direction,
                arrivalDateTime,
                departureDateTime
        );
    }

    /**
     * <h2>getRouteFromResultSet</h2>
     * Creates a {@link Route} object from the given ResultSet and date.
     *
     * @param date The date for which the route is relevant.
     * @param resultSet The ResultSet containing the route data.
     * @param departureName The name of the departure stop.
     * @param arrivalName The name of the arrival stop.
     * @return A Route object populated with data from the ResultSet.
     * @throws SQLException If an error occurs while accessing the ResultSet.
     */
    public static Route getRouteFromResultSet(LocalDate date, ResultSet resultSet, String departureName, String arrivalName) throws SQLException {
        String routeNumber = resultSet.getString(1);
        String endStop = resultSet.getString(2);
        String arrivalTimeAsString = resultSet.getString(3);
        String departureTimeAsString = resultSet.getString(4);

        LocalDateTime arrivalDateTime = generateDateTime(date, arrivalTimeAsString);
        LocalDateTime departureDateTime = generateDateTime(date, departureTimeAsString);

        return new Route(
                routeNumber,
                endStop,
                departureName,
                arrivalName,
                arrivalDateTime,
                departureDateTime
        );
    }

    /**
     * <h2>generateDateTime</h2>
     * Generates a LocalDateTime object from a LocalDate and a time string.
     *
     * <li>Handles special cases for times starting with "24" or "25".</li>
     * <li>These occur if routes are close to midnight</li>
     *
     * @param date The date to use for the LocalDateTime.
     * @param timeAsString The time string in HH:mm format.
     * @return A LocalDateTime object representing the combined date and time.
     */
    private static LocalDateTime generateDateTime(LocalDate date, String timeAsString) {

        if(timeAsString.startsWith("24")){
            timeAsString = "00" + timeAsString.substring(2);
            date = date.plusDays(1);
        } else if (timeAsString.startsWith("25")){
            timeAsString = "01" + timeAsString.substring(2);
            date = date.plusDays(1);
        }

        LocalTime timeFromString = LocalTime.parse(timeAsString);

        return LocalDateTime.of(date, timeFromString);
    }
}
