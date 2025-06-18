package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.model.Route;

import java.time.LocalDateTime;
import java.util.List;

public class RouteHandler {
    //0. constants
    //endregion

    //1. attributes
    //endregion

    //2. constructor
    private RouteHandler() {
        // Constructor logic if needed
    }
    //endregion

    //3. operating methods
    public static List<Route> findRoutes(String departureName, String arrivalName, LocalDateTime chosenDateTime, boolean isDepartureSearch) {

        List<Route> foundRoutes = DbReader.getDirectRouteForDate(departureName, arrivalName, chosenDateTime);

        List<Route> routesInNextHour;

        if (isDepartureSearch){
            routesInNextHour = routesByDepartureInTheNextHour(foundRoutes, chosenDateTime);
        } else {
            routesInNextHour = routesByArrivalInTheNextHour(foundRoutes, chosenDateTime);
        }

        return routesInNextHour;
    }

    private static List<Route> routesByArrivalInTheNextHour(List<Route> foundRoutes, LocalDateTime chosenDateTime) {
        LocalDateTime oneHourLater = chosenDateTime.plusHours(1);

        return foundRoutes.stream()
                .filter(route -> route.arrivalDateTime().isAfter(chosenDateTime)
                        && route.arrivalDateTime().isBefore(oneHourLater))
                .toList();
    }

    private static List<Route> routesByDepartureInTheNextHour(List<Route> foundRoutes, LocalDateTime chosenDateTime) {
        LocalDateTime oneHourLater = chosenDateTime.plusHours(1);

        return foundRoutes.stream()
                .filter(route -> route.departureDateTime().isAfter(chosenDateTime)
                        && route.departureDateTime().isBefore(oneHourLater))
                .toList();
    }
    //endregion
}
