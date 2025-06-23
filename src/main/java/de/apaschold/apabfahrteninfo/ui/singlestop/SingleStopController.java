package de.apaschold.apabfahrteninfo.ui.singlestop;

import de.apaschold.apabfahrteninfo.logic.SingleStopHandler;
import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.logic.filehandling.TextFileManager;
import de.apaschold.apabfahrteninfo.model.SingleStop;
import de.apaschold.apabfahrteninfo.texts.AppTexts;
import de.apaschold.apabfahrteninfo.ui.GuiController;
import de.apaschold.apabfahrteninfo.ui.ListCellShowTwoDigits;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;
/** * <h2>SingleStopController class</h2>
 * <li>Controller for the view that displays next stop times for a single stop.</li>
 */
public class SingleStopController implements Initializable {
    //0. constants
    //endregion

    //1. attributes
    private List<String> availableStops;
    //endregion

    //2. FXMLs
    @FXML
    private TextField searchBarForStops;

    @FXML
    private ListView<String> stopSuggestions;

    @FXML
    private ListView<SingleStop> nextStopTimes;

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

    //3. initialization method
    /**
     * <h2>initialize method</h2>
     * <li>Loading list of available stops from database with {@link DbReader}</li>
     * <li>Sets the hour and minute pickers ({@link ComboBox}-objects)</li>
     * <li>Checks if in the entrance view a recently used stop was selected (stored in the {@link GuiController})</li>
     * <ul>
     *     <li>If yes, place it in the searchbar and calls search method</li>
     * </ul>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.availableStops = DbReader.getAllStops();

        setUpHourPickerAndMinutePicker();

        String recentlyUsedStopSelected = GuiController.getInstance().getRecentlyUsedStop();
        if (recentlyUsedStopSelected != null) {
            this.searchBarForStops.setText(recentlyUsedStopSelected);
            showNextStopTimes();
        }
    }
    //endregion

    //4. FXML-object methods
    /**
     * <h2>showNextStopTimes method</h2>
     * <li>Called either by the {@link ListView} containing the suggested stops onClick or
     * by the initialize method if recently used stop was selected</li>
     * <li>Retrieves the chosen date and time from the UI components.</li>
     * <li>Checks if the search bar is not empty and if the chosen date and time is valid.</li>
     * <li>Calls {@link SingleStopHandler} to get the next stop times for the selected stop.</li>
     * <li>Updates the {@link ListView} with the retrieved stop times.</li>
     */
    @FXML
    private void showNextStopTimes() {
        if (!this.searchBarForStops.getText().isBlank()) {
            LocalDateTime chosenDateTime = getChosenDateTime();

            /*
             * chosenDateTime shall be similar or equal to now DateTime
             * minusSeconds is necessary as nowDateTime contains Seconds, while chosenDateTime not
             */
            if (LocalDateTime.now().minusSeconds(59).isBefore(chosenDateTime)) {
                String selectedStop = this.stopSuggestions.getSelectionModel().getSelectedItem();

                if (selectedStop != null) {
                    this.searchBarForStops.setText(selectedStop);
                } else {
                    selectedStop = this.searchBarForStops.getText();
                }

                List<SingleStop> stopTimesFromDb = arrivalOrDepartures(selectedStop, chosenDateTime);

                this.nextStopTimes.getItems().setAll(stopTimesFromDb);
                TextFileManager.getInstance().updateRecentlyUsedStopsCsv(selectedStop);
            } else {
                showAlertMessageDateTimeIsDeprecated();
            }

        } else {
            showAlertMessageStopNameIsBlank();
        }
    }

    /**
     * <h2>showStopSuggestions method</h2>
     * <li>Bound to the search bar "onKeyTyped"</li>
     * <li>Filters the list of available stops based on the text entered in the search bar.</li>
     * <li>Updates the stop suggestions {@link ListView} with the filtered results.</li>
     */
    @FXML
    private void showStopSuggestions() {
        String stopNameFragment = this.searchBarForStops.getText().toLowerCase();

        List<String> suggestedStops = this.availableStops.stream()
                .filter(stop -> stop.toLowerCase().contains(stopNameFragment))
                .toList();

        this.stopSuggestions.getItems().setAll(suggestedStops);
    }

    /**
     * <h2>setNow method</h2>
     * <li>Sets the  {@link DatePicker} and the time picker {@link ComboBox} to the current date and time.</li>
     */
    @FXML
    private void setNow() {
        this.selectedDate.setValue(LocalDate.now());
        this.selectedHour.setValue(LocalTime.now().getHour());
        this.selectedMinute.setValue(LocalTime.now().getMinute());
    }

    @FXML
    private void goToDirectRouteSearch(){
        GuiController.getInstance().openDirectRouteSearch();
    }
    //endregion

    //5. helper methods
    /**
     * <h2>setUpHourPickerAndMinutePicker method</h2>
     * <li>Sets up the hour and minute pickers with values from 0 to 23 for hours and 0 to 59 for minutes.</li>
     * <li>Uses a custom {@link ListCellShowTwoDigits} to display the values in two-digit format.</li>
     */
    private void setUpHourPickerAndMinutePicker() {
        List<Integer> hours = IntStream.range(0, 24).boxed().toList();
        this.selectedHour.getItems().setAll(hours);
        this.selectedHour.setCellFactory(_ -> new ListCellShowTwoDigits());
        this.selectedHour.setButtonCell(new ListCellShowTwoDigits());

        List<Integer> minutes = IntStream.range(0, 60).boxed().toList();
        this.selectedMinute.getItems().setAll(minutes);
        this.selectedMinute.setCellFactory(_ -> new ListCellShowTwoDigits());
        this.selectedMinute.setButtonCell(new ListCellShowTwoDigits());
    }

    /**
     * <h2>getChosenDateTime method</h2>
     * <li>Retrieves the selected date, hour, and minute from the UI components.</li>
     * <li>If any of these values are null, it sets the current date and time.</li>
     * @return {@link LocalDateTime} object representing the chosen date and time.
     */
    private LocalDateTime getChosenDateTime() {
        if (this.selectedDate.getValue() == null
                || this.selectedHour.getValue() == null
                || this.selectedMinute.getValue() == null) {
            setNow();
        }

        return LocalDateTime.of(
                this.selectedDate.getValue(),
                LocalTime.of(this.selectedHour.getValue(), this.selectedMinute.getValue()));
    }

    /**
     * <h2>arrivalOrDepartures method</h2>
     * <li>Determines whether to search for arrivals or departures based on the selected radio button.</li>
     * <li>Calls the appropriate method from {@link SingleStopHandler} to retrieve stop times.</li>
     * <li>Sets the {@link ListView} to display the either departure or arrival inforamtions</li>
     * @param stopName The name of the stop to search for.
     * @param chosenDateTime The date and time for which to retrieve stop times.
     * @return A list of {@link SingleStop} objects containing the stop informations.
     */
    private List<SingleStop> arrivalOrDepartures(String stopName, LocalDateTime chosenDateTime) {
        List<SingleStop> singleStops = null;
        if (searchForDepartures.isSelected()){
            singleStops = SingleStopHandler.getAllDepartureTimes(stopName, chosenDateTime);

            this.nextStopTimes.setCellFactory(_ -> new DepartureTimeListViewCell());
        } else if (searchForArrivals.isSelected()){
            singleStops = SingleStopHandler.getAllArrivalTimes(stopName, chosenDateTime);

            this.nextStopTimes.setCellFactory(_ -> new ArrivalTimeListViewCell());
        }

        return singleStops;
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
    //endregion
}