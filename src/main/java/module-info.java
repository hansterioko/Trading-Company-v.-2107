module rus.warehouse.trading_company {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires com.google.gson;
    requires static lombok;
    requires javafx.swing;
    requires jfxtras.controls;
    requires jfxtras.common;
    requires jfxtras.fxml;
    requires org.apache.poi.ooxml;

    opens rus.warehouse.trading_company.models to javafx.base, javafx.graphics, javafx.fxml, com.google.gson; // Начали выводиться данные в TableView
    opens rus.warehouse.trading_company to javafx.fxml, com.google.gson, javafx.base, javafx.graphics, jfxtras.controls;
    opens rus.warehouse.trading_company.modelsDTO to javafx.base, javafx.graphics, javafx.fxml, com.google.gson;
    opens rus.warehouse.trading_company.FXModels to com.google.gson;
    exports rus.warehouse.trading_company;
    exports rus.warehouse.trading_company.controllers;
    opens rus.warehouse.trading_company.controllers to javafx.fxml;
}