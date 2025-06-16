package de.apaschold.apabfahrteninfo.ui.frequentlyusedstops;

import de.apaschold.apabfahrteninfo.logic.filehandling.TextFileManager;
import de.apaschold.apabfahrteninfo.ui.GuiController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RecentlyUsedStopsController implements Initializable {
    //0.constants
    //endregion

    //1. FXML attributes
    @FXML
    private ListView<String> recentlyUsedStops;
    //endregion

    //2. constructors
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> frequentlyUsedStops = TextFileManager.getInstance()
                .readTextFile(TextFileManager.FILE_NAME_OFTEN_USED_STOP_NAMES);
        GuiController.getInstance().setFrequentlyUsedStops(frequentlyUsedStops);

        recentlyUsedStops.getItems().setAll(frequentlyUsedStops);
    }
    //endregion

    @FXML
    private void goToSingleStopSearch(){
        GuiController.getInstance().openSingleStopSearch();
    }

    @FXML
    private void recentlyUsedStopSelected(){
        String selectedStop = this.recentlyUsedStops.getSelectionModel().getSelectedItem();
        GuiController.getInstance().setRecentlyUsedStop(selectedStop);

        GuiController.getInstance().openSingleStopSearch();
    }

}
