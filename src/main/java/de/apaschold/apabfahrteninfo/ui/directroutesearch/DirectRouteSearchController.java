package de.apaschold.apabfahrteninfo.ui.directroutesearch;

import de.apaschold.apabfahrteninfo.logic.RouteHandler;
import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.model.Route;
import de.apaschold.apabfahrteninfo.texts.AppTexts;
import de.apaschold.apabfahrteninfo.ui.GuiController;
import de.apaschold.apabfahrteninfo.ui.ListCellShowTwoDigits;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * <h2>DirectRouteSearchController class</h2>
 * <li>Controller for the direct route search view.</li>
 * <li>Handles user input and displays next connections between two chosen stops for chosen date and time</li>
 */
public class DirectRouteSearchController implements Initializable {
    //0. constants
    //endregion

    //1. attributes
    private List<String> availableStops;
    private boolean departureActivated = true; // true for departures, false for arrivals
    //endregion

    //2. FXMLs
    @FXML
    private TextField departureStop;

    @FXML
    private TextField arrivalStop;

    @FXML
    private ListView<String> suggestedStops;

    @FXML
    private DatePicker selectedDate;

    @FXML
    private ComboBox<Integer> selectedHour;

    @FXML
    private ComboBox<Integer> selectedMinute;

    @FXML
    private TableView<Route> nextRoutes;
    @FXML
    private TableColumn<Route, String> routeNumberColumn;
    @FXML
    private TableColumn<Route, String> directionColumn;
    @FXML
    private TableColumn<Route, String> departureTimeColumn;
    @FXML
    private TableColumn<Route, String> arrivalTimeColumn;

    @FXML
    private RadioButton departureSearch;
    //endregion

    //3. initialization method
    /**
     * <h2>initialize method</h2>
     * <li>Loading list of available stops from database with {@link DbReader}</li>
     * <li>Sets the hour and minute pickers ({@link ComboBox}-objects)</li>
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.availableStops = DbReader.getAllStops();

        setUpHourPickerAndMinutePicker();
    }
    //endregion

    //4. FXML methods

    /**
     * <h2>setDepartureStopSearch method</h2>
     * <li>Assigns the {@link ListView} to display the suggested stops for the departure {@link TextField}</li>
     * <li>Calls the method to show stop suggestions for {@link TextField} input</li>
     */
    @FXML
    private void setDepartureStopSearch() {
        if (!this.departureActivated) {
            departureActivated = true;
        }
        showStopSuggestions();
    }

    /**
     * <h2>setDepartureStopSearch method</h2>
     * <li>Assigns the {@link ListView} to display the suggested stops for the arrival {@link TextField}</li>
     * <li>Calls the method to show stop suggestions for {@link TextField} input</li>
     */
    @FXML
    private void setArrivalStopSearch() {
        if (this.departureActivated) {
            departureActivated = false;
        }
        showStopSuggestions();
    }

    /**
     * <h2>showNextRoutes method</h2>
     * <li>Called either by the {@link ListView} containing the suggested stops onClick</li>
     * <li>Assigns the {@link ListView} value to the respective {@link TextField}</li>
     * <li>Checks if the chosen date and time is in the future, otherwise shows an alert message</li>
     * <li>Calls the {@link RouteHandler} to find routes between the two stops for the chosen date and time</li>
     * <li>Populates the {@link TableView} with the found routes</li>
     */
    @FXML
    private void showNextRoutes() {
        String selectedStop = this.suggestedStops.getSelectionModel().getSelectedItem();

        if (selectedStop == null || selectedStop.isBlank()) {
            showAlertMessageStopNameIsBlank();
            return;
        }

        if (this.departureActivated) {
            this.departureStop.setText(selectedStop);
        } else {
            this.arrivalStop.setText(selectedStop);
        }

        LocalDateTime chosenDateTime = getChosenDateTime();
        /*
         * chosenDateTime shall be similar or equal to now DateTime
         * minusSeconds is necessary as nowDateTime contains Seconds, while chosenDateTime not
         */
        if (!this.departureStop.getText().isBlank() && !this.arrivalStop.getText().isBlank()) {
            if (LocalDateTime.now().minusSeconds(59).isBefore(chosenDateTime)) {
                List<Route> foundRoutes = RouteHandler.findRoutes(
                        this.departureStop.getText(),
                        this.arrivalStop.getText(),
                        chosenDateTime,
                        this.departureSearch.isSelected()
                );

                populateNextRoutesTable(foundRoutes);
            } else {
                showAlertMessageDateTimeIsDeprecated();
            }
        }
    }

    /**
     * <h2>setNow method</h2>
     * <li>Sets the  {@link DatePicker} and the time picker {@link ComboBox} to the current date and time.</li>
     */
    @FXML
    private void setNow() {
        this.selectedDate.setValue(LocalDateTime.now().toLocalDate());
        this.selectedHour.setValue(LocalTime.now().getHour());
        this.selectedMinute.setValue(LocalTime.now().getMinute());
    }

    @FXML
    private void goToSingleStopSearch() {
        GuiController.getInstance().openSingleStopSearch();
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
     * <h2>showStopSuggestions method</h2>
     * <li>Bound to the search bar "onKeyTyped"</li>
     * <li>Filters the list of available stops based on the text entered in the search bar.</li>
     * <li>Updates the stop suggestions {@link ListView} with the filtered results.</li>
     */
    private void showStopSuggestions() {
        String stopNameFragment = this.departureActivated ? this.departureStop.getText() : this.arrivalStop.getText();

        List<String> suggestions = this.availableStops.stream()
                .filter(stop -> stop.toLowerCase().contains(stopNameFragment.toLowerCase()))
                .toList();

        this.suggestedStops.getItems().setAll(suggestions);
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
     * <h2>populateNextRoutesTable method</h2>
     * <li>Populates the {@link TableView} with the found routes.</li>
     * <li>If no routes are found, it shows an alert message.</li>
     * @param foundRoutes List of {@link Route} objects to be displayed in the table.
     */
    private void populateNextRoutesTable(List<Route> foundRoutes) {
        if (!foundRoutes.isEmpty()) {
            this.nextRoutes.getItems().addAll(foundRoutes);

            this.routeNumberColumn.setCellValueFactory(data -> data.getValue().routeNumberProperty());
            this.directionColumn.setCellValueFactory(data -> data.getValue().directionProperty());
            this.departureTimeColumn.setCellValueFactory(data -> data.getValue().departureTimeProperty());
            this.arrivalTimeColumn.setCellValueFactory(data -> data.getValue().arrivalTimeProperty());
        } else {
            showAlertMessageNoRoutesFound();
        }
    }

    private void showAlertMessageNoRoutesFound() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(AppTexts.INFORMATION_NO_ROUTES_FOUND_TITLE);
        alert.setContentText(AppTexts.INFORMATION_NO_ROUTES_FOUND_CONTENT);
        alert.show();
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
        alert.setHeaderText(AppTexts.ALERT_EMPTY_STOP_NAME_CONTENT);
        alert.show();
    }
}
