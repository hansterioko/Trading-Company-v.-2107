package rus.warehouse.trading_company.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
    public Pagination purchasePagination;
    public Button searchBtn;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public Button providerBtn;
    public Button filterBtn;
    public Label countRowsLabel;
    @FXML
    private Button backBtn;
    @FXML
    private Button detailPurchaseBtn;

    @FXML
    private TableColumn<Purchase, String> companyColumn;

    @FXML
    private TableColumn<Purchase, Integer> costColumn;

    @FXML
    private TableColumn<Purchase, String> dateColumn;

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

    // Для кнопки деталей о закупке
    private boolean isSelectRow = false;

    @FXML
    void backAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(RunApplication.class.getResource("css/main.css").toExternalForm());
            stage.setTitle("Управление складом");
            stage.setScene(scene);
            stage.show();

            Stage stageOld = (Stage) backBtn.getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно главного меню", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия главного окна! " + e.toString());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/searchBtn.png").toExternalForm()));
        providerBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/providerBtn.png").toExternalForm()));
        filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));

        companyColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("company"));
        costColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Purchase, String>("date"));
        idColumn.setCellValueFactory(new PropertyValueFactory<Purchase, Integer>("id"));

        getPurchase();
        //moreColumn.cellFactoryProperty().get().call()
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
        countRowsLabel.setText("Всего записей: " + pagedDataDTO.getTotal());

        if (pagedDataDTO.getTotal() == 0){
            purchasePagination.setPageCount(1);
        }
        else {
            purchasePagination.setPageCount(Math.toIntExact(Math.round(Math.ceil((double) pagedDataDTO.getTotal() / 14))));
        }

        for (Purchase purchase:
                purchaseList) {
            data.add(new Purchase(purchase.getId(), purchase.getDate(), purchase.getPrice(), purchase.getCostWithVAT(), purchase.getCompany()));
        }

        detailPurchaseBtn.setDisable(true);
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
        startDate = null;
        endDate = null;
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
            stage.setTitle("Выбор поставщиков");
            stage.setScene(scene);
            stage.showAndWait();

            getPurchase();
            //System.out.println(listProviders + "КОНТРОЛЛЕР ПУРЧ");

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно выбора поставщиков", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
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

    public void addNewPurchaseBtn(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-purchase-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Добавление закупки");
            stage.setScene(scene);
            stage.show();

            Stage stageOld = (Stage) backBtn.getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления закупок", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления закупок! " + e.toString());
        }
    }

    public void detailPurchaseAction(ActionEvent actionEvent) throws IOException {
        //System.out.println(purchaseTable.getSelectionModel().getSelectedItem());
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("detail-purchase-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            DetailPurchaseController detailPurchaseController = new DetailPurchaseController(purchaseTable.getSelectionModel().getSelectedItem().getId());
            fxmlLoader.setController(detailPurchaseController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Подробнее о закупке");
            stage.setScene(scene);
            stage.showAndWait();


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления закупок", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления закупок! " + e.toString());
        }


        purchaseTable.getSelectionModel().clearSelection();
        detailPurchaseBtn.setDisable(true);
    }

    public void clickOnTable(MouseEvent mouseEvent) {
        if (!Objects.isNull(purchaseTable.getSelectionModel().getSelectedItem())){
            detailPurchaseBtn.setDisable(false);
        }
    }

    @FXML
    void reportAction(ActionEvent event) {

    }
}
