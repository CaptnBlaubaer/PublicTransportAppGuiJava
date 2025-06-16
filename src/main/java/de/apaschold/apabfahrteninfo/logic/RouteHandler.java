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
    public RouteHandler() {
        // Constructor logic if needed
    }
    //endregion

    //3. operating methods
    public void findRoutes() {
        String departureName = "Halle (Saale), Landesmuseum für Vorgeschichte";
        String arrivalName = "Halle (Saale), Hauptbahnhof (Tram/Bus)";
        LocalDateTime now = LocalDateTime.parse("2025-06-17T08:00:00");

        List<Route> foundRoutes = DbReader.getDirectRouteForDate(departureName, arrivalName, now);

        List<Route> foundRoutesInNextHour = routesInTheNextHour(foundRoutes, now);

        foundRoutesInNextHour.forEach(System.out::println);
    }

    private List<Route> routesInTheNextHour(List<Route> foundRoutes, LocalDateTime now) {
        LocalDateTime oneHourLater = now.plusHours(1);

        return foundRoutes.stream()
                .filter(route -> route.departureDateTime().isAfter(now)
                        && route.departureDateTime().isBefore(oneHourLater))
                .toList();
    }
    //endregion
}
