module pl.kalinowski.riotaccountmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    opens pl.kalinowski.riotaccountmanager to javafx.fxml;
    exports pl.kalinowski.riotaccountmanager;
}