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
import rus.warehouse.trading_company.models.Company;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    public Pagination purchasePagination;
    public Button searchBtn;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public Button providerBtn;
    public Button filterBtn;
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

    private String listProviders = "";

    // Для getPurchase()
    private Number currentIndex = 0;
    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private String sortFilterDate = "DESC";

    @FXML
    void backAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/searchBtn.png").toExternalForm()));
        providerBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/providerBtn.png").toExternalForm()));
        filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));

        companyColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("company"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase, LocalDateTime>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));
        moreColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Button>("button"));

        getPurchase();
        purchasePagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {changePage(oldIndex, newIndex);});
    }

    public void setListProviders(String listProviders) {
        this.listProviders = listProviders;
    }

    // Получает список закупок из репозитория и приводит их из ДТО в норм модель
    private void getPurchase(){
        ObservableList<Purchase> data = FXCollections.observableArrayList();
        PagedDataDTO<Purchase> pagedDataDTO = PurchaseRepositories.getAll(currentIndex, startDate, endDate, listProviders, sortFilterDate);
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
            currentIndex = newIndex;
            getPurchase();
        }
    }

    public void searchByDateClick(MouseEvent mouseEvent) {
        currentIndex = 0;
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();
        purchasePagination.setCurrentPageIndex(0);

        getPurchase();
    }

    public void dropDateClick(MouseEvent mouseEvent) {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        currentIndex = 0;
        listProviders = ""; // Очищаем выбранных поставщиков
        purchasePagination.setCurrentPageIndex(0);

        getPurchase();
    }

    public void chooseProviderClick(MouseEvent mouseEvent) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("selectCompany-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            SelectCompanyController selectCompanyController = new SelectCompanyController(this);
            fxmlLoader.setController(selectCompanyController);
            Scene scene = new Scene(fxmlLoader.load());
//            String CHTO = this.getClass().getResource("purchase-view.fxml").toString();
//            System.out.println(CHTO);
            //scene.getStylesheets().add(RunApplication.class.getResource("css/purchase.css").toExternalForm());
            stage.setTitle("Выбор поставщиков");
            //stage.getIcons().add(new Image(RunApplication.class.getResource("images/providerBtn.png").toString()));
            stage.setScene(scene);
            stage.showAndWait();

            getPurchase();
            //System.out.println(listProviders + "КОНТРОЛЛЕР ПУРЧ");

        } catch (IOException e) {
            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора поставщика!");
        }
    }

    public void filterClick(MouseEvent mouseEvent) {
        if (filterBtn.getText().equals("По убыванию")){
            filterBtn.setText("По возрастанию");
            filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowUp.png").toExternalForm()));
            sortFilterDate = "ASC";
        }
        else{
            filterBtn.setText("По убыванию");
            filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));
            sortFilterDate = "DESC";
        }

        getPurchase();
    }
}
