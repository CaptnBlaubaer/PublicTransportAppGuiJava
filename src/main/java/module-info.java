module de.apaschold.apabfahrteninfo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens de.apaschold.apabfahrteninfo to javafx.fxml;
    exports de.apaschold.apabfahrteninfo;
    exports de.apaschold.apabfahrteninfo.ui.singlestop;
    opens de.apaschold.apabfahrteninfo.ui.singlestop to javafx.fxml;
    exports de.apaschold.apabfahrteninfo.ui.frequentlyusedstops;
    opens de.apaschold.apabfahrteninfo.ui.frequentlyusedstops to javafx.fxml;
    exports de.apaschold.apabfahrteninfo.ui;
    opens de.apaschold.apabfahrteninfo.ui to javafx.fxml;
    exports de.apaschold.apabfahrteninfo.ui.directroutesearch;
    opens de.apaschold.apabfahrteninfo.ui.directroutesearch to javafx.fxml;
}