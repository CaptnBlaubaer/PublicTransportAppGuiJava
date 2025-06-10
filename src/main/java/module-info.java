module de.apaschold.apabfahrteninfo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens de.apaschold.apabfahrteninfo to javafx.fxml;
    exports de.apaschold.apabfahrteninfo;
}