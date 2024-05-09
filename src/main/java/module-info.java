module rus.warehouse.trading_company {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires static lombok;

    opens rus.warehouse.trading_company.models to javafx.base, javafx.graphics, javafx.fxml, com.google.gson; // Начали выводиться данные в TableView
    opens rus.warehouse.trading_company to javafx.fxml, com.google.gson, javafx.base, javafx.graphics;
    opens rus.warehouse.trading_company.modelsDTO to com.google.gson;
    exports rus.warehouse.trading_company;
    exports rus.warehouse.trading_company.controllers;
    opens rus.warehouse.trading_company.controllers to javafx.fxml;
}