package de.apaschold.apabfahrteninfo.ui;

import de.apaschold.apabfahrteninfo.Main;
import de.apaschold.apabfahrteninfo.texts.AppTexts;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * <h2>GuiController class</h2>
 * <li>Singleton class that manages the GUI of the application.</li>
 * <li>Handles the opening of different views such as recently searched stops, single stop search, and direct route search.</li>
 * <li>Stores the mainstage and a list with recently used stops as variables</li>
 */
public class GuiController {
    //0.constants
    //endregion

    //1. attributes
    private static GuiController instance;
    private Stage mainStage;
    private List<String> recentlyUsedStops;
    private String recentlyUsedStop;
    //endregion

    //2. constructors
    private GuiController() {
    }
    //endregion

    //3. getInstance method
    public static synchronized GuiController getInstance() {
        if (instance == null) {
            instance = new GuiController();
        }
        return instance;
    }
    //endregion

    //4. getter and setter methods
    public List<String> getRecentlyUsedStops() {
        return recentlyUsedStops;
    }

    public void setRecentlyUsedStops(List<String> recentlyUsedStops) {
        this.recentlyUsedStops = recentlyUsedStops;
    }

    public String getRecentlyUsedStop() {
        return recentlyUsedStop;
    }

    public void setRecentlyUsedStop(String recentlyUsedStop) {
        this.recentlyUsedStop = recentlyUsedStop;
    }
    //endregion

    //5. setStages and open views
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void openRecentlyUsedStops() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("recently-used-stops-layout.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 550, 400);
            this.mainStage.setTitle(AppTexts.RECENTLY_USED_STOPS_TITLE);
            this.mainStage.setScene(scene);
            this.mainStage.show();
        } catch (Exception e) {
            System.err.println(
                    String.format(AppTexts.ERROR_LOADING_VIEW, AppTexts.DIRECT_ROUTE_SEARCH_TITLE)
                            + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    public void openSingleStopSearch() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("single-stop-search-layout.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 900, 500);
            this.mainStage.setTitle(AppTexts.SINGLE_STOP_SEARCH_TITLE);
            this.mainStage.setScene(scene);
            this.mainStage.show();
        } catch (Exception e) {
            System.err.println(
                    String.format(AppTexts.ERROR_LOADING_VIEW, AppTexts.SINGLE_STOP_SEARCH_TITLE)
                            + e.getMessage()
            );
            e.printStackTrace();
        }
    }

    public void openDirectRouteSearch() {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("direct-route-search-layout.fxml"));

        try {
            Scene scene = new Scene(fxmlLoader.load(), 750, 550);
            this.mainStage.setTitle(AppTexts.DIRECT_ROUTE_SEARCH_TITLE);
            this.mainStage.setScene(scene);
            this.mainStage.show();
        } catch (Exception e) {
            System.err.println(
                    String.format(AppTexts.ERROR_LOADING_VIEW, AppTexts.DIRECT_ROUTE_SEARCH_TITLE)
                            + e.getMessage()
            );
            e.printStackTrace();
        }
    }
}
