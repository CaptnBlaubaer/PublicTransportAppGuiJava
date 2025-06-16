package de.apaschold.apabfahrteninfo.logic;

import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.model.StopTime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StopTimeHandler {
    //0. constants
    //endregion

    //1. attributes
    //endregion

    //2. constructor
    public StopTimeHandler(){
    }
    //endregion

    //4. operating methods
    public List<StopTime> getAllStopTimes(String stopName, LocalDateTime chosenDateTime){
        List<StopTime> allStopTimes = DbReader.getDeparturesForDateAndStop(stopName, chosenDateTime);

        allStopTimes = departuresInTheNextHour(allStopTimes, chosenDateTime);

        return allStopTimes;
    }

    public List<StopTime> getAllDepartures(String stopName, LocalDateTime chosenDateTime){
        List<StopTime> allStopTimes = DbReader.getDeparturesForDateAndStop(stopName, chosenDateTime);
        List<StopTime> allDepartures = new ArrayList<>();

        //Liste enthält auch Endhalte, diese haben aber keine Abfahrt sondern nur Ankunft
        for(StopTime stopTime : allStopTimes){
            if(!stopTime.direction().equals(stopName)){
                allDepartures.add(stopTime);
            }
        }

        allDepartures = departuresInTheNextHour(allDepartures, chosenDateTime);

        return allDepartures;
    }

    private List<StopTime> departuresInTheNextHour(List<StopTime> departures, LocalDateTime chosenDateTime) {


        LocalDateTime oneHourLater = chosenDateTime.plusHours(1);

        return departures.stream()
                .filter(stopTime -> stopTime.departureDateTime().isAfter(chosenDateTime)
                        && stopTime.departureDateTime().isBefore(oneHourLater))
                .toList();
    }
}
