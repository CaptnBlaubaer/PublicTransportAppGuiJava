package de.apaschold.apabfahrteninfo.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtFileManager {
    //0. constants
    private static final String FOLDER_PATH = "src/main/resources/de/apaschold/apabfahrteninfo/data/";
    //endregion

    //1. attributes
    private static TxtFileManager instance;
    //endregion

    //2. constructors
    private TxtFileManager() {
    }
    //endregion

    //3. getInstance Method


    public static TxtFileManager getInstance() {
        if (instance == null) {
            instance = new TxtFileManager();
        }
        return instance;
    }

    public List<String> readTxtFile(String fileName) {
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
}
