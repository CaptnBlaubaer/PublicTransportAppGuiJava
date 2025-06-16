package de.apaschold.apabfahrteninfo.ui.singlestop;

import de.apaschold.apabfahrteninfo.logic.DepartureHandler;
import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.logic.filehandling.TextFileManager;
import de.apaschold.apabfahrteninfo.model.StopTime;
import de.apaschold.apabfahrteninfo.texts.AppTexts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class SingleStopController implements Initializable {
    //0. constants
    //endregion

    //1. attributes
    private final DepartureHandler departureHandler = new DepartureHandler();
    private List<String> availableStops;
    //endregion

    //2. FXMLs
    @FXML
    private TextField searchBarForStops;

    @FXML
    private ListView<String> stopSuggestions;

    @FXML
    private ListView<StopTime> nextStopTimes;

    @FXML
    private RadioButton searchForDepartures;

    @FXML
    private RadioButton searchForArrivals;

    @FXML
    private DatePicker selectedDate;

    @FXML
    private ComboBox<Integer> selectedHour;

    @FXML
    private ComboBox<Integer> selectedMinute;
    //endregion

    //initialization method
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.availableStops = DbReader.getAllStops();

        List<Integer> hours = IntStream.range(0, 24).boxed().toList();
        this.selectedHour.getItems().setAll(hours);

        List<Integer> minutes = IntStream.range(0, 60).boxed().toList();
        this.selectedMinute.getItems().setAll(minutes);
    }
    //endregion

    //FXML-object methods
    @FXML
    private void showNextStopTimes() {
        if (!this.searchBarForStops.getText().isBlank()) {
            if (this.selectedDate.getValue() == null
                    || this.selectedHour.getValue() == null
                    || this.selectedMinute.getValue() == null) {
                setNow();
            }

            LocalDateTime chosenDateTime = LocalDateTime.of(
                    this.selectedDate.getValue(),
                    LocalTime.of(this.selectedHour.getValue(), this.selectedMinute.getValue())
            );

            /*
             * chosenDateTime shall be similar or equal to now DateTime
             * minusSeconds is necessary as nowDateTime contains Seconds, while chosenDateTime not
             */
            if (LocalDateTime.now().minusSeconds(59).isBefore(chosenDateTime)) {
                String selectedStop = this.stopSuggestions.getSelectionModel().getSelectedItem();
                this.searchBarForStops.setText(selectedStop);

                List<StopTime> stopTimesFromDb = arrivalOrDepartures(selectedStop, chosenDateTime);

                this.nextStopTimes.getItems().setAll(stopTimesFromDb);
                TextFileManager.getInstance().updateRecentlyUsedStopsCsv(selectedStop);
            } else {
                showAlertMessageDateTimeIsDeprecated();
            }

        } else {
            showAlertMessageStopNameIsBlank();
        }
    }

    @FXML
    private void suggestStops() {
        List<String> suggestedStops = new ArrayList<>();
        String stopNameFragment = this.searchBarForStops.getText().toLowerCase();

        for (String stopName : this.availableStops) {
            if (stopName.toLowerCase().contains(stopNameFragment)) {
                suggestedStops.add(stopName);
            }
        }

        this.stopSuggestions.getItems().setAll(suggestedStops);
    }

    @FXML
    private void setNow() {
        this.selectedDate.setValue(LocalDate.now());
        this.selectedHour.setValue(LocalTime.now().getHour());
        this.selectedMinute.setValue(LocalTime.now().getMinute());
    }
    //endregion

    private List<StopTime> arrivalOrDepartures(String stopName, LocalDateTime chosenDateTime) {
        List<StopTime> stopTimes = null;
        if (searchForDepartures.isSelected()){
            stopTimes = this.departureHandler.getAllDepartures(stopName, chosenDateTime);

            this.nextStopTimes.setCellFactory(param -> new DepartureTimeListViewCell());
        } else if (searchForArrivals.isSelected()){
            stopTimes = this.departureHandler.getAllStopTimes(stopName, chosenDateTime);

            this.nextStopTimes.setCellFactory(param -> new ArrivalTimeListViewCell());
        }

        return stopTimes;
    }


    private void showAlertMessageDateTimeIsDeprecated() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(AppTexts.ALERT_DEPRECATED_DATE_TIME_TITLE);
        alert.setContentText(AppTexts.ALERT_DEPRECATED_DATE_TIME_CONTENT);
        alert.show();
    }


    private void showAlertMessageStopNameIsBlank() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(AppTexts.ALERT_EMPTY_STOP_NAME_TITEL);
        alert.setContentText(AppTexts.ALERT_EMPTY_STOP_NAME_CONTENT);
        alert.show();
    }
}