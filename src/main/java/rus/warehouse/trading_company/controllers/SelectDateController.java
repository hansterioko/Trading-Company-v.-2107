package rus.warehouse.trading_company.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;

import java.time.LocalDateTime;

public class SelectDateController {
    @FXML
    private Button backBtn;

    @FXML
    private LocalDateTimeTextField purchaseDateField;

    AddPurchaseController addPurchaseController;

    AddOrderController addOrderController = null;

    public SelectDateController(AddPurchaseController addPurchaseController) {
        this.addPurchaseController = addPurchaseController;
    }

    public SelectDateController(AddOrderController addOrderController) {
        this.addOrderController = addOrderController;
    }

    @FXML
    void createPurchase(ActionEvent event) {
        if (addOrderController != null){
            addOrderController.setStartCreate(true);
            addOrderController.setOrderDate(purchaseDateField.getLocalDateTime());
        }
        else {
            addPurchaseController.setStartCreate(true);
            addPurchaseController.setDatePurchase(purchaseDateField.getLocalDateTime());
        }

        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }



    @FXML
    void returnBack(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }
}
