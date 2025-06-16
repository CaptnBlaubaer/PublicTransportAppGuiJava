package de.apaschold.apabfahrteninfo.test;

import de.apaschold.apabfahrteninfo.logic.RouteHandler;
import de.apaschold.apabfahrteninfo.logic.db.DbReader;

public class TestStuff {
    public static void main(String[] args) {
        new RouteHandler().findRoutes();
    }
}
