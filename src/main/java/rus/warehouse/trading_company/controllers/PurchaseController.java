package rus.warehouse.trading_company.controllers;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rus.warehouse.trading_company.models.Purchase;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<Purchase, String> companyColumn;

    @FXML
    private TableColumn<Purchase, Integer> costColumn;

    @FXML
    private TableColumn<Purchase, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<Purchase, Integer> idColumn;

    @FXML
    private TableColumn<Purchase, Button> moreColumn;

    @FXML
    private TableView<Purchase> purchaseTable;

    @FXML
    void backAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Purchase> data = FXCollections.observableArrayList(new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),
                new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"));

        companyColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("company"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase, LocalDateTime>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));
        moreColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Button>("button"));

        purchaseTable.setItems(data);
    }
}
