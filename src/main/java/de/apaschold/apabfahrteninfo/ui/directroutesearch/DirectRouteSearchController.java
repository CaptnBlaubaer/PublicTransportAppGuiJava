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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.availableStops = DbReader.getAllStops();

        setUpHourPickerAndMinutePicker();
    }


    //endregion

    //4. FXML methods
    @FXML
    private void setDepartureStopSearch() {
        if (!this.departureActivated) {
            departureActivated = true;
        }
        showStopSuggestions();
    }

    @FXML
    private void setArrivalStopSearch() {
        if (this.departureActivated) {
            departureActivated = false;
        }
        showStopSuggestions();
    }

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

    private void showStopSuggestions() {
        String stopNameFragment = this.departureActivated ? this.departureStop.getText() : this.arrivalStop.getText();

        List<String> suggestions = this.availableStops.stream()
                .filter(stop -> stop.toLowerCase().contains(stopNameFragment.toLowerCase()))
                .toList();

        this.suggestedStops.getItems().setAll(suggestions);
    }

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
