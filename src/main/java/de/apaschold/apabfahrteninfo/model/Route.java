package de.apaschold.apabfahrteninfo.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public record Route(String routeNumber,
                    String direction,
                    String departureStop,
                    String arrivalStop,
                    LocalDateTime arrivalDateTime,
                    LocalDateTime departureDateTime) implements Comparable<Route> {
    //0. attirbute
    //endregion

    //1. propertyGetters for TableView
    public StringProperty routeNumberProperty() {
        return new SimpleStringProperty(routeNumber);
    }

    public StringProperty directionProperty() {
        return new SimpleStringProperty(direction);
    }

    public StringProperty departureTimeProperty() {
        return new SimpleStringProperty(departureDateTime.toLocalTime().toString());
    }

    public StringProperty arrivalTimeProperty() {
        return new SimpleStringProperty(arrivalDateTime.toLocalTime().toString());
    }
    //endregion

    //2. helper methods
    @Override
    public String toString() {
        return routeNumber + ", " + direction + ":\n" +
                "from: " + departureStop + " at " + departureDateTime.toLocalTime() +
                " to: " + arrivalStop + " at " + arrivalDateTime.toLocalTime();
    }

    @Override
    public int compareTo(Route otherRoute) {
        int comparator = 0;

        if (arrivalDateTime.isBefore(otherRoute.arrivalDateTime())) {
            comparator = -1;
        } else if (arrivalDateTime.isAfter(otherRoute.arrivalDateTime())) {
            comparator = 1;
        }

        return comparator;
    }
    //endregion
}

