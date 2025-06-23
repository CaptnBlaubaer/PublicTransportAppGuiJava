package de.apaschold.apabfahrteninfo.logic.filehandling;

import de.apaschold.apabfahrteninfo.ui.GuiController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>TextFileManager</h2>
 * This class is responsible for reading and writing text files.
 * It follows the Singleton design pattern to ensure only one instance of this class exists.
 */
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
    //endregion

    //4. read'n'write methods
    /**
     * <h2>readTextFile</h2>
     * Reads a text file and returns its content as a list of strings, where each string is a line from the file.
     *
     * @param fileName the name of the file to read
     * @return a list of strings containing the lines of the file
     */
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

    /**
     * <h2>updateRecentlyUsedStopsCsv</h2>
     * <li>write method</li>
     * Updates the CSV file containing recently used stops by adding a new stop name.
     * If the stop name already exists, it will not be added again.
     * The file will contain a maximum of 10 recently used stops.
     *
     * @param selectedStopName the name of the stop to add to the recently used stops
     */
    public void updateRecentlyUsedStopsCsv(String selectedStopName){
        List<String> recentlyUsedStops = GuiController.getInstance().getRecentlyUsedStops();

        if(!recentlyUsedStops.contains(selectedStopName)) {
            recentlyUsedStops.addFirst(selectedStopName);

            if (recentlyUsedStops.size() > 10) {
                recentlyUsedStops.remove(10);
            }

            try (FileWriter writer = new FileWriter(FILE_PATH_STOP_NAMES, StandardCharsets.UTF_8)) {
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
