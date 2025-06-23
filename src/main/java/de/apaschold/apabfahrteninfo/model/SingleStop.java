package de.apaschold.apabfahrteninfo.model;

import java.time.LocalDateTime;

/**
 * <h2>SingleStop record class</h2>
 * Represents a single stop with its route number, direction, and arrival/departure times.
 * Implements Comparable to allow sorting based on departure time.
 */
public record SingleStop(String routeNumber,
                         String direction,
                         LocalDateTime arrivalDateTime,
                         LocalDateTime departureDateTime) implements Comparable<SingleStop> {
    //0. constants
    private static final String STRING_REPRESENTATION = "%s, %s: %s";
    //endregion

    //1. helper methods
    public String arrivalAsString(){
        return String.format(STRING_REPRESENTATION,
                routeNumber,
                direction,
                arrivalDateTime.toLocalTime()
        );
    }

    public String departureAsString(){
        return String.format(STRING_REPRESENTATION,
                routeNumber,
                direction,
                departureDateTime.toLocalTime()
        );
    }

    @Override
    public int compareTo(SingleStop otherDeparture){
        int comparator = 0;

        if (departureDateTime.isBefore(otherDeparture.departureDateTime())){
            comparator = -1;
        } else if (departureDateTime.isAfter(otherDeparture.departureDateTime())) {
            comparator = 1;
        }

        return comparator;
    }
}
