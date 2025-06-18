package de.apaschold.apabfahrteninfo.texts;

public class AppTexts {
    //0. constant
    public static final String APP_NAME = "FahrplanApp - ";
    public static final String RECENTLY_USED_STOPS_TITLE = APP_NAME + "Zuletzt verwendete Haltestellen";
    public static final String SINGLE_STOP_SEARCH_TITLE = APP_NAME + "An-/Abfahrtszeiten für einzelne Haltestelle";
    public static final String DIRECT_ROUTE_SEARCH_TITLE = APP_NAME + "Direkte Verbindungen zwischen zwei Haltestellen";

    public static final String ALERT_DEPRECATED_DATE_TIME_TITLE = "Abfahrtszeit ungültig";
    public static final String ALERT_DEPRECATED_DATE_TIME_CONTENT = "Die gewählte Abfahrtszeit liegt in der Vergangenheit.";
    public static final String ALERT_EMPTY_STOP_NAME_TITEL = "Ungültiger Haltestellenname";
    public static final String ALERT_EMPTY_STOP_NAME_CONTENT = "Haltestellenname(n) darf nicht leer sein";
    public static final String INFORMATION_NO_ROUTES_FOUND_TITLE = "Keine Routen gefunden";
    public static final String INFORMATION_NO_ROUTES_FOUND_CONTENT = "Es wurde keine Direktverbindung gefunden.";

    public static final String ERROR_LOADING_VIEW = "ERROR loading %s view: %s";
    //endregion

    //1. attributes
    //endregion

    //2. constructors
    private AppTexts(){}
    //endregion
}
