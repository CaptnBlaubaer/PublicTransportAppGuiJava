package de.apaschold.apabfahrteninfo.test;

import de.apaschold.apabfahrteninfo.logic.DepartureHandler;
import de.apaschold.apabfahrteninfo.logic.db.DbReader;
import de.apaschold.apabfahrteninfo.logic.db.DbWriter;

import java.time.LocalDateTime;

public class TestStuff {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nownow = LocalDateTime.now();

        System.out.println(nownow.isBefore(now));
    }
}
