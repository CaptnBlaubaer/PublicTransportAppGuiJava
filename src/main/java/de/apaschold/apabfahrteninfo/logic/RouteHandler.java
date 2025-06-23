package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.model.Route;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>RouteHandler</h2>
 * This class handles the logic for finding routes between two stops.
 * It retrieves routes from the database and filters them based on the specified criteria.
 */
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

    /**
     * <h2>findRoutes</h2>
     * This method retrieves routes between two stops based on the specified departure andd arrival name, and date-time.
     *
     * <li>Retrieves {@link List} of {@link Route}-Objects from database</li>
     * <li>Filters the routes based on the specified date-time and whether the search is for departure or arrival time</li>
     * @param departureName
     * @param arrivalName
     * @param chosenDateTime
     * @param isDepartureSearch
     * @return
     */
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

    /**
     * <h2>routesByArrivalInTheNextHour</h2>
     * Filters the routes to find those where arrival time within the next hour from the specified date-time.
     *
     * @param foundRoutes
     * @param chosenDateTime
     * @return List of {@link Route} objects that arrive within the next hour
     */
    private static List<Route> routesByArrivalInTheNextHour(List<Route> foundRoutes, LocalDateTime chosenDateTime) {
        LocalDateTime oneHourLater = chosenDateTime.plusHours(1);

        return foundRoutes.stream()
                .filter(route -> route.arrivalDateTime().isAfter(chosenDateTime)
                        && route.arrivalDateTime().isBefore(oneHourLater))
                .toList();
    }

    /**
     * <h2>routesByDepartureInTheNextHour</h2>
     * Filters the routes to find those where departure time within the next hour from the specified date-time.
     *
     * @param foundRoutes
     * @param chosenDateTime
     * @return List of {@link Route} objects that depart within the next hour
     */
    private static List<Route> routesByDepartureInTheNextHour(List<Route> foundRoutes, LocalDateTime chosenDateTime) {
        LocalDateTime oneHourLater = chosenDateTime.plusHours(1);

        return foundRoutes.stream()
                .filter(route -> route.departureDateTime().isAfter(chosenDateTime)
                        && route.departureDateTime().isBefore(oneHourLater))
                .toList();
    }
    //endregion
}
