package de.apaschold.apabfahrteninfo.ui.recentlyusedstops;

import de.apaschold.apabfahrteninfo.logic.filehandling.TextFileManager;
import de.apaschold.apabfahrteninfo.ui.GuiController;
import de.apaschold.apabfahrteninfo.ui.singlestop.SingleStopController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <h2>RecentlyUsedStopsController class</h2>
 * <li>Controller for the entrance view.</li>
 * <li>Displays recently used stops, which can be used to directly search for a stop.</li>
 */
public class RecentlyUsedStopsController implements Initializable {
    //0.constants
    //endregion

    //1. FXML attributes
    @FXML
    private ListView<String> recentlyUsedStops;
    //endregion

    /**
     * <h2>initialize method</h2>
     * <li>Initializes the list of recently used stops from a .csv file and displays in {@link ListView}</li>
     * <li>Stores the List in the GuiController Variable</li>
     */
    //2. constructors
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> frequentlyUsedStops = TextFileManager.getInstance()
                .readTextFile(TextFileManager.FILE_NAME_OFTEN_USED_STOP_NAMES);
        GuiController.getInstance().setRecentlyUsedStops(frequentlyUsedStops);

        recentlyUsedStops.getItems().setAll(frequentlyUsedStops);
    }
    //endregion

    @FXML
    private void goToSingleStopSearch(){
        GuiController.getInstance().openSingleStopSearch();
    }

    /**
     * <h2>Recently Used Stop Selected</h2>
     * <li>If stop is selected from {@link ListView}, stores the selected stop in GuiController and redirects to
     * {@link SingleStopController}</li>
     */
    @FXML
    private void recentlyUsedStopSelected(){
        String selectedStop = this.recentlyUsedStops.getSelectionModel().getSelectedItem();
        GuiController.getInstance().setRecentlyUsedStop(selectedStop);

        GuiController.getInstance().openSingleStopSearch();
    }

}
