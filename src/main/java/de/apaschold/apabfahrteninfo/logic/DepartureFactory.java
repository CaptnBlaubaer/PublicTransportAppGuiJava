package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.model.StopTime;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DepartureFactory {
    //0. constants
    //endregion

    //1. attributes
    //endregion
    
    //2. contructor
    private DepartureFactory(){}
    //endregion
    
    //3. Factory method
    public static StopTime getRepartureFromResultSet(LocalDate date, ResultSet resultSet) throws SQLException {
        String routeNumber = resultSet.getString(1);
        String endStop = resultSet.getString(2);
        String arrivalTimeAsString = resultSet.getString(3);
        String departureTimeAsString = resultSet.getString(4);

        LocalDateTime arrivalDateTime = generateDateTime(date, arrivalTimeAsString);
        LocalDateTime departureDateTime = generateDateTime(date, departureTimeAsString);

        return new StopTime(
                routeNumber,
                endStop,
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
