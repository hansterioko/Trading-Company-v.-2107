package rus.warehouse.trading_company.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Order;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;
import rus.warehouse.trading_company.repositories.OrderRepository;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class OrderController implements Initializable {

    // Таблица

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> clientColumn;

    @FXML
    private TableColumn<Order, String> costColumn;

    @FXML
    private TableColumn<Order, String> dateColumn;

    @FXML
    private TableColumn<Order, Number> idColumn;

    @FXML
    private Button backBtn;

    @FXML
    private Button clientsBtn;

    @FXML
    private Label countRowsLabel;

    @FXML
    private Button detailOrderBtn;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button filterBtn;

    @FXML
    private Pagination orderPagination;

    @FXML
    private Button reportBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private DatePicker startDatePicker;


    // Для getOrders()
    //ObservableList<Order> observableList = FXCollections.observableArrayList();

    private Number currentIndex = 0;
    private LocalDate startDate = null;
    private LocalDate endDate = null;
    private String sortFilterDate = "DESC";
    private Number currentPageIndex = 0;
    private String listClients = "";

    @FXML
    void addNewOrderAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-order-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Добавление поставки");
            stage.setScene(scene);
            stage.show();

            Stage stageOld = (Stage) backBtn.getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления поставки", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления поставки! " + e.toString());
        }
    }

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

    @FXML
    void chooseClientsClick(MouseEvent event) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("selectCompany-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            SelectClientController selectClientController = new SelectClientController(this);
            fxmlLoader.setController(selectClientController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Выбор поставщиков");
            stage.setScene(scene);
            stage.showAndWait();

            getOrders();
            //System.out.println(listProviders + "КОНТРОЛЛЕР ПУРЧ");

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно выбора поставщиков", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора поставщика!");
        }
    }

    @FXML
    void clearAction(ActionEvent event) {
//        startDatePicker.setValue(null);
//        endDatePicker.setValue(null);
//        startDate = null;
//        endDate = null;
//        currentIndex = 0;
//        listClients = ""; // Очищаем выбранных поставщиков
//        orderPagination.setCurrentPageIndex(0);
//
//        getOrders();
    }

    @FXML
    void clickOnTable(MouseEvent event) {
        if (!Objects.isNull(orderTable.getSelectionModel().getSelectedItem())){
            detailOrderBtn.setDisable(false);
        }
    }

    @FXML
    void clickToPaginationPanel(MouseEvent event) {

    }

    @FXML
    void detailOrderAction(ActionEvent event) throws IOException {
       // try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("detail-order-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            DetailOrderController detailOrderController = new DetailOrderController(orderTable.getSelectionModel().getSelectedItem().getId());
            fxmlLoader.setController(detailOrderController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Подробнее о поставке");
            stage.setScene(scene);
            stage.showAndWait();

//        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления закупок", ButtonType.OK);
//            alert.setTitle("Ошибка подключения");
//            alert.setHeaderText("Проверьте подключение к интернету!");
//            alert.show();
//            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления поставок! " + e.getMessage());
//        }


        orderTable.getSelectionModel().clearSelection();
        detailOrderBtn.setDisable(true);
    }

    @FXML
    void dropDateClick(MouseEvent event) {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        startDate = null;
        endDate = null;
        currentIndex = 0;
        listClients = ""; // Очищаем выбранных поставщиков
        orderPagination.setCurrentPageIndex(0);

        orderTable.getSelectionModel().select(null);
        getOrders();
    }

    @FXML
    void filterClick(MouseEvent event) {
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

        getOrders();
    }

    @FXML
    void reportAction(ActionEvent event) {

    }

    @FXML
    void searchAction(ActionEvent event) {
        currentIndex = 0;
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();
        orderPagination.setCurrentPageIndex(0);

        getOrders();
    }

    @FXML
    void searchByDateClick(MouseEvent event) {
//        currentIndex = 0;
//        startDate = startDatePicker.getValue();
//        endDate = endDatePicker.getValue();
//        orderPagination.setCurrentPageIndex(0);
//
//        getOrders();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/searchBtn.png").toExternalForm()));
        clientsBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/providerBtn.png").toExternalForm()));
        filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));

        clientColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()
                .getUserclient().getName()
        ));
        costColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()
                .getPrice().toString()
        ));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()
        ));

        getOrders();
        //moreColumn.cellFactoryProperty().get().call()
        orderPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {changePage(oldIndex, newIndex);});
    }

    private void changePage(Number oldIndex, Number newIndex){
        System.out.println(newIndex);
        if (oldIndex != newIndex){
            currentPageIndex = newIndex;

            getOrders();
        }
    }

    private void getOrders(){
        ObservableList<Order> data = FXCollections.observableArrayList();
        PagedDataDTO<Order> pagedDataDTO = OrderRepository.getAll(currentIndex, startDate, endDate, listClients, sortFilterDate);
        List<Order> orderList = pagedDataDTO.getData();
        countRowsLabel.setText("Всего записей: " + pagedDataDTO.getTotal());

        if (pagedDataDTO.getTotal() == 0){
            orderPagination.setPageCount(1);
        }
        else {
            orderPagination.setPageCount(Math.toIntExact(Math.round(Math.ceil((double) pagedDataDTO.getTotal() / 14))));
        }

        for (Order order:
                orderList) {
            data.add(new Order(order.getId(), order.getPrice(), order.getCostWithVat(), order.getUserclient(), order.getDate()));
        }

        detailOrderBtn.setDisable(true);
        orderTable.setItems(data);
    }

    public void setListClients(String listClients) {
        this.listClients = listClients;
    }
}
