module rus.warehouse.trading_company {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires static lombok;

    opens rus.warehouse.trading_company.models to javafx.base, javafx.graphics, javafx.fxml; // Начали выводиться данные в TableView
    opens rus.warehouse.trading_company to javafx.fxml, com.google.gson, javafx.base, javafx.graphics;
    exports rus.warehouse.trading_company;
    exports rus.warehouse.trading_company.controllers;
    opens rus.warehouse.trading_company.controllers to javafx.fxml;
}