package de.apaschold.apabfahrteninfo.logic.filehandling;

import de.apaschold.apabfahrteninfo.ui.GuiController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TextFileManager {
    //0. constants
    private static final String FOLDER_PATH = "src/main/resources/de/apaschold/apabfahrteninfo/data/";
    public static final String FILE_NAME_OFTEN_USED_STOP_NAMES = "frequentlySearchedStops.csv";
    private static final String FILE_PATH_STOP_NAMES = FOLDER_PATH + FILE_NAME_OFTEN_USED_STOP_NAMES;
    //endregion

    //1. attributes
    private static TextFileManager instance;
    //endregion

    //2. constructors
    private TextFileManager() {
    }
    //endregion

    //3. getInstance Method


    public static synchronized TextFileManager getInstance() {
        if (instance == null) {
            instance = new TextFileManager();
        }
        return instance;
    }

    public List<String> readTextFile(String fileName) {
        List<String> txtFileSeperatedLines = new ArrayList<>();

        File file = new File(FOLDER_PATH + fileName);

        try(FileReader reader = new FileReader(file);
            BufferedReader in = new BufferedReader(reader)) {

            String fileLine;
            boolean eof = false;

            while (!eof) {
                fileLine = in.readLine();
                if (fileLine == null) {
                    eof = true;
                } else {
                    txtFileSeperatedLines.add(fileLine);
                }
            }

        } catch (IOException e){
            System.err.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
        }

        return txtFileSeperatedLines;
    }

    public void updateRecentlyUsedStopsCsv(String selectedStopName){
        List<String> recentlyUsedStops = GuiController.getInstance().getRecentlyUsedStops();

        if(!recentlyUsedStops.contains(selectedStopName)) {
            recentlyUsedStops.add(0, selectedStopName);

            if (recentlyUsedStops.size() > 10) {
                recentlyUsedStops.remove(10);
            }


            try (FileWriter writer = new FileWriter(FILE_PATH_STOP_NAMES, StandardCharsets.UTF_8);) {
                for (String stopName : recentlyUsedStops) {
                    writer.write(stopName + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error saving to File: " + FILE_PATH_STOP_NAMES);
                e.printStackTrace();
            }
        }
    }
}
