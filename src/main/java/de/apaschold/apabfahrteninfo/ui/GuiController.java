package de.apaschold.apabfahrteninfo.ui;

import de.apaschold.apabfahrteninfo.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class GuiController {
    //0.constants
    //endregion

    //1. attributes
    private static GuiController instance;
    private Stage mainStage;
    private List<String> frequentlyUsedStops;
    private String recentlyUsedStop;
    //endregion

    //2. constructors
    private GuiController() {}
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
    public List<String> getFrequentlyUsedStops() {
        return frequentlyUsedStops;
    }

    public void setFrequentlyUsedStops(List<String> frequentlyUsedStops) {
        this.frequentlyUsedStops = frequentlyUsedStops;
    }

    public String getRecentlyUsedStop() {
        return recentlyUsedStop;
    }

    public void setRecentlyUsedStop(String recentlyUsedStop) {
        this.recentlyUsedStop = recentlyUsedStop;
    }
    //endregion

    //5. setStages
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void openFrequentlySearchedStops(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("recently-used-stops-layout.fxml"));

        try{
            Scene scene = new Scene(fxmlLoader.load(), 550, 400);
            this.mainStage.setTitle("Fahrplan App - Häufig genutzte Haltestellen");
            this.mainStage.setScene(scene);
            this.mainStage.show();
        } catch (Exception e) {
            System.err.println("Error loading frequently searched stops view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void openSingleStopSearch(){
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("single-stop-search-layout.fxml"));

        try{
            Scene scene = new Scene(fxmlLoader.load(), 900, 500);
            this.mainStage.setTitle("Fahrplan App - Haltestelle suchen");
            this.mainStage.setScene(scene);
            this.mainStage.show();
        } catch (Exception e) {
            System.err.println("Error loading single stop search view: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
