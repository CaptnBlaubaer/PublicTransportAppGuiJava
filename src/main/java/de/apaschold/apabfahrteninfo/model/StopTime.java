package de.apaschold.apabfahrteninfo.model;

import java.time.LocalDateTime;

public record StopTime(String routeNumber,
                       String direction,
                       LocalDateTime arrivalDateTime,
                       LocalDateTime departureDateTime) implements Comparable<StopTime> {
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
    public int compareTo(StopTime otherDeparture){
        int comparator = 0;

        if (departureDateTime.isBefore(otherDeparture.departureDateTime())){
            comparator = -1;
        } else if (departureDateTime.isAfter(otherDeparture.departureDateTime())) {
            comparator = 1;
        }

        return comparator;
    }
}
