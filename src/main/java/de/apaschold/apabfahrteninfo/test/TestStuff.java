package de.apaschold.apabfahrteninfo.test;

import de.apaschold.apabfahrteninfo.logic.DbWriter;
import de.apaschold.apabfahrteninfo.logic.TxtFileManager;

import java.util.List;

public class TestStuff {
    public static void main(String[] args) {
        DbWriter dbWriter = new DbWriter();

        dbWriter.updateAllTables();
    }
}
