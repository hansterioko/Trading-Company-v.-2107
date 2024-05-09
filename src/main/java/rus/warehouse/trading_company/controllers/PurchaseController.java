package rus.warehouse.trading_company.controllers;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    public Pagination purchasePagination;
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
//        ObservableList<Purchase> data = FXCollections.observableArrayList(new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),
//                new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"),new Purchase(1, LocalDateTime.now(), 245, "Ок"));

        companyColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("company"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase, LocalDateTime>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));
        moreColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Button>("button"));
        ObservableList<Purchase> data = getPurchase();


        purchaseTable.setItems(data);


    }

    // Получает список закупок из репозитория и приводит их из ДТО в норм модель
    private ObservableList<Purchase> getPurchase(){
        ObservableList<Purchase> data = FXCollections.observableArrayList();
        PagedDataDTO<Purchase> pagedDataDTO = PurchaseRepositories.getAll();
        List<Purchase> purchaseList = pagedDataDTO.getData();
        purchasePagination.setPageCount(Math.toIntExact(Math.round(Math.ceil((double) pagedDataDTO.getTotal() / 14))));

        for (Purchase purchase:
                purchaseList) {
            data.add(new Purchase(purchase.getId(), purchase.getDate(), purchase.getPrice(), purchase.getCompany()));
        }

        return data;
    }
}
