package de.apaschold.apabfahrteninfo;
/**
 * <h2>Application description</h2>
 * <li>App supplies information about public transport in MDV region</li>
 * <li>App is based on the data from www.mdv.de/downloads</li>
 * <li>gives informations for single stops and shows direct routes between two spots</li>
 */

import de.apaschold.apabfahrteninfo.ui.GuiController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * <li>Main class of the application.</li>
 * <li>Starts the JavaFX application and opens the recently searched stops view.</li>
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) {
        GuiController.getInstance().setMainStage(stage);
        GuiController.getInstance().openRecentlyUsedStops();
    }

    public static void main(String[] args) {
        launch();
    }
}