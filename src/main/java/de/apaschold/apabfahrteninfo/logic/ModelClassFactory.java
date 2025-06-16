package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.model.Route;
import de.apaschold.apabfahrteninfo.model.StopTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ModelClassFactory {
    //0. constants
    //endregion

    //1. attributes
    //endregion
    
    //2. contructor
    private ModelClassFactory(){}
    //endregion
    
    //3. Factory method
    public static StopTime getStopTimeFromResultSet(LocalDate date, ResultSet resultSet) throws SQLException {
        String routeNumber = resultSet.getString(1);
        String direction = resultSet.getString(2);
        String arrivalTimeAsString = resultSet.getString(3);
        String departureTimeAsString = resultSet.getString(4);

        LocalDateTime arrivalDateTime = generateDateTime(date, arrivalTimeAsString);
        LocalDateTime departureDateTime = generateDateTime(date, departureTimeAsString);

        return new StopTime(
                routeNumber,
                direction,
                arrivalDateTime,
                departureDateTime
        );
    }

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
