package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.model.SingleStop;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>SingleStopHandler class</h2>
 * <li>Handles the logic for retrieving single stop informations from the database for a chosen stop and date-time.</li>
 */
public class SingleStopHandler {
    //0. constants
    //endregion

    //1. attributes
    //endregion

    //2. constructor
    private SingleStopHandler(){
    }
    //endregion

    //4. operating methods
    /**
     * <h2>getAllArrivalTimes</h2>
     * Retrieves all {@link SingleStop} for a given stop name and date-time in the next hour.
     * <li>Gets all {@link List} with {@link SingleStop} from Database for chosen date</li>
     * <li>Filters the list to include only those within the next hour</li>
     *
     * @param stopName the name of the stop
     * @param chosenDateTime the date and time for which to retrieve stop times
     * @return a list of SingleStop objects containing all stop times
     */
    public static List<SingleStop> getAllArrivalTimes(String stopName, LocalDateTime chosenDateTime){
        List<SingleStop> allSingleStops = DbReader.getArrivalsAndDeparturesForDateAndStop(stopName, chosenDateTime);

        return singleStopsInTheNextHour(allSingleStops, chosenDateTime);
    }

    /**
     * <h2>getAllDepartureTimes</h2>
     * Retrieves all departure times for a given stop name and date-time in the next hour.
     * <li>Gets all {@link List} with {@link SingleStop} from Database for chosen date</li>
     * <li>Removes all stops, where direction name is similar to stop name (endstops don't have a departure time)</li>
     * <li>Filters the list to include only those within the next hour</li>
     *
     * @param stopName the name of the stop
     * @param chosenDateTime the date and time for which to retrieve stop times
     * @return a list of SingleStop objects containing all departure times
     */
    public static List<SingleStop> getAllDepartureTimes(String stopName, LocalDateTime chosenDateTime){
        List<SingleStop> allStopTimes = DbReader.getArrivalsAndDeparturesForDateAndStop(stopName, chosenDateTime);
        List<SingleStop> allDepartures = new ArrayList<>();

        //Liste enthält auch Endhalte, diese haben aber keine Abfahrt sondern nur Ankunft
        for(SingleStop stopTime : allStopTimes){
            if(!stopTime.direction().equals(stopName)){
                allDepartures.add(stopTime);
            }
        }

        return singleStopsInTheNextHour(allDepartures, chosenDateTime);
    }


    /**
     * <h2>singleStopsInTheNextHour</h2>
     * Filters a list of SingleStop objects to include only those with departure times within the next hour.
     *
     * @param departures the list of SingleStop objects to filter
     * @param chosenDateTime the date and time from which to calculate the next hour
     * @return a list of SingleStop objects that have departure times within the next hour
     */
    private static List<SingleStop> singleStopsInTheNextHour(List<SingleStop> departures, LocalDateTime chosenDateTime) {
        LocalDateTime oneHourLater = chosenDateTime.plusHours(1);

        return departures.stream()
                .filter(stopTime -> stopTime.departureDateTime().isAfter(chosenDateTime)
                        && stopTime.departureDateTime().isBefore(oneHourLater))
                .toList();
    }
}
