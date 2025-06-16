package de.apaschold.apabfahrteninfo;

import de.apaschold.apabfahrteninfo.ui.GuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        GuiController.getInstance().setMainStage(stage);
        GuiController.getInstance().openDirectRouteSearch();

    }

    public static void main(String[] args) {
        launch();
    }
}