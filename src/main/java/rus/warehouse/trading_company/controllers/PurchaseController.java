package rus.warehouse.trading_company.controllers;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    public Pagination purchasePagination;
    public Button searchBtn;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public Button providerBtn;
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

    private Integer currentPageIndex = 0;

    @FXML
    void backAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/searchBtn.png").toExternalForm()));
        providerBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/providerBtn.png").toExternalForm()));

        companyColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("company"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase, LocalDateTime>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));
        moreColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Button>("button"));

        getPurchase(0, null, null);
        purchasePagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {changePage(oldIndex, newIndex);});
    }

    // Получает список закупок из репозитория и приводит их из ДТО в норм модель
    private void getPurchase(Number newIndex, LocalDate startDate, LocalDate endDate){
        ObservableList<Purchase> data = FXCollections.observableArrayList();
        System.out.println(currentPageIndex);
        PagedDataDTO<Purchase> pagedDataDTO = PurchaseRepositories.getAll(newIndex, startDate, endDate);
        List<Purchase> purchaseList = pagedDataDTO.getData();

        if (pagedDataDTO.getTotal() == 0){
            purchasePagination.setPageCount(1);
        }
        else {
            purchasePagination.setPageCount(Math.toIntExact(Math.round(Math.ceil((double) pagedDataDTO.getTotal() / 14))));
        }

        for (Purchase purchase:
                purchaseList) {
            data.add(new Purchase(purchase.getId(), purchase.getDate(), purchase.getPrice(), purchase.getCompany()));
        }

        purchaseTable.setItems(data);
    }


    public void clickToPaginationPanel(MouseEvent mouseEvent) {
//        Integer newIndex = purchasePagination.getCurrentPageIndex();
//        System.out.println(newIndex);
//        if (currentPageIndex != newIndex){
//            currentPageIndex = newIndex;
//            getPurchase();
//        }
    }

    private void changePage(Number oldIndex, Number newIndex){
        System.out.println(newIndex);
        if (oldIndex != newIndex){
            getPurchase(newIndex, startDatePicker.getValue(), endDatePicker.getValue());
        }
    }

    public void searchByDateClick(MouseEvent mouseEvent) {
        getPurchase(0, startDatePicker.getValue(), endDatePicker.getValue());
        purchasePagination.setCurrentPageIndex(0);
    }

    public void dropDateClick(MouseEvent mouseEvent) {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        getPurchase(0, startDatePicker.getValue(), endDatePicker.getValue());
        purchasePagination.setCurrentPageIndex(0);
    }

    public void chooseProviderClick(MouseEvent mouseEvent) throws IOException {
        //try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("selectCompany-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(fxmlLoader.load());
//            String CHTO = this.getClass().getResource("purchase-view.fxml").toString();
//            System.out.println(CHTO);
            //scene.getStylesheets().add(RunApplication.class.getResource("css/purchase.css").toExternalForm());
            stage.setTitle("Выбор поставщиков");
            //stage.getIcons().add(new Image(RunApplication.class.getResource("images/providerBtn.png").toString()));
            stage.setScene(scene);
            stage.showAndWait();


//        } catch (IOException e) {
//            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора поставщика!");
//        }
    }
}
