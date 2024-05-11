package rus.warehouse.trading_company.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;

import java.io.IOException;
import java.time.LocalTime;

public class MainController {

    public BorderPane warehousePanel;

    public void warehouseClick(MouseEvent mouseEvent) {
        
    }

    public void OrderClick(MouseEvent mouseEvent) {
    }

    public void purchClick(MouseEvent mouseEvent) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("purchase-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
//            String CHTO = this.getClass().getResource("purchase-view.fxml").toString();
//            System.out.println(CHTO);
            scene.getStylesheets().add(RunApplication.class.getResource("css/purchase.css").toExternalForm());
            stage.setTitle("Управление закупками");
            stage.setScene(scene);
            stage.show();

            Stage stageOld = (Stage) warehousePanel.getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            System.out.println(LocalTime.now() + "   Ошибка открытия окна закупок!");
        }
    }
}