package de.apaschold.apabfahrteninfo.ui.directroutesearch;

import de.apaschold.apabfahrteninfo.ui.GuiController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class DirectRouteSearchController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private void goToSingleStopSearch() {
        GuiController.getInstance().openSingleStopSearch();
    }
}
