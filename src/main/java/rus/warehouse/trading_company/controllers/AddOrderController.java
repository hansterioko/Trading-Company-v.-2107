package rus.warehouse.trading_company.controllers;

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import lombok.Getter;
import lombok.Setter;
import rus.warehouse.trading_company.FXModels.OrderProductFX;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.helpers.ValueOfMeasuring;
import rus.warehouse.trading_company.helpers.ValueOfSortingWarehouse;
import rus.warehouse.trading_company.models.Product;
import rus.warehouse.trading_company.models.UserClient;
import rus.warehouse.trading_company.modelsDTO.OrderDTO;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;
import rus.warehouse.trading_company.modelsDTO.PurchaseDTO;
import rus.warehouse.trading_company.modelsDTO.PurchaseProduct;
import rus.warehouse.trading_company.repositories.OrderRepository;
import rus.warehouse.trading_company.repositories.ProductRepository;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class AddOrderController implements Initializable {

    @FXML
    private Button backBtn;


    // Лист с уже использующимися товарами

    ObservableList<Product> useProductList = FXCollections.observableArrayList();

    // Таблица со склада
    @FXML
    private TableColumn<Product, Number> countColumn;

    @FXML
    private TableColumn<Product, String> dateExpireColumn;

    @FXML
    private TableColumn<Product, String> priceColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Number> idColumn;

    @FXML
    private TableView<Product> productTable;

    ObservableList<Product> productObservableList = FXCollections.observableArrayList();

    // -------------------------------------

    // Таблица с товарами в поставке

    @FXML
    private TableColumn<OrderProductFX, Number> idOrdColumn;

    @FXML
    private TableColumn<OrderProductFX, String> nameOrdColumn;

    @FXML
    private TableView<OrderProductFX> orderProdTable;

    @FXML
    private TableColumn<OrderProductFX, String> countOrdColumn;

    @FXML
    private TableColumn<OrderProductFX, String> priceOrdColumn;

    @FXML
    private TableColumn<OrderProductFX, String> vatOrdColumn;

    ObservableList<OrderProductFX> orderObservableList = FXCollections.observableArrayList();

    // ----------------------------------------

    @FXML
    private Label countRowsLabel;

    @FXML
    private Button detailProductBtn;

    @FXML
    private Button filterBtn;

    @FXML
    private Pagination productPagination;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<ValueOfSortingWarehouse> typeSortingCBox;

    @FXML
    private Button moveProdBtn;

    @Getter
    @Setter
    private LocalDateTime orderDate = null;

    @Setter
    private boolean startCreate = false;

    @Getter
    @Setter
    private UserClient client = null;

    // Параметры для отправки запроса
    private String sortBy = "DESC";
    private String typeSorting = "BYID";
    private Number currentPageIndex = 0;
    private String isCheckExpirated = "NO";
    private String search = "";

    @FXML
    void backAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("order-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
//            String CHTO = this.getClass().getResource("purchase-view.fxml").toString();
//            System.out.println(CHTO);
            scene.getStylesheets().add(RunApplication.class.getResource("css/purchase.css").toExternalForm());
            stage.setTitle("Поставки");
            stage.setScene(scene);
            stage.show();

            Stage stageOld = (Stage) backBtn.getScene().getWindow();
            stageOld.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно поставок", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна поставок! " + e.toString());
        }
    }

    @FXML
    void selectClient(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("selectOrderClient-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            SelectOrderController selectOrderController = new SelectOrderController(this);
            fxmlLoader.setController(selectOrderController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Выбор получателя");
            //stage.getIcons().add(new Image(RunApplication.class.getResource("images/providerBtn.png").toString()));
            stage.setScene(scene);
            stage.showAndWait();

            //getPurchase();
            //System.out.println(listProviders + "КОНТРОЛЛЕР ПУРЧ");

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно выбора поулчателя", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора получателя!" + e.toString());
        }
    }

    @FXML
    void clickOnOrdTable(MouseEvent event) {
        productTable.getSelectionModel().select(null);

        if (!Objects.isNull(orderProdTable.getSelectionModel().getSelectedItem())){
            detailProductBtn.setDisable(false);
        }
    }

    @FXML
    void moveProduct(ActionEvent event) {
        Product moveProd = productTable.getSelectionModel().getSelectedItem();
        OrderProductFX orderProduct = new OrderProductFX(moveProd.getId(), moveProd.getName(), -1, moveProd.getCategory()
        , moveProd.getTypePackaging(), moveProd.getCharacteristic(), moveProd.getUnit()
        , new BigDecimal("0.00"), 0, moveProd.getCountOnWarehouse(), moveProd.getDateOfManufacture(), moveProd.getDateExpiration());

        orderObservableList.add(orderProduct);
        orderProdTable.setItems(orderObservableList);

        useProductList.add(moveProd);
        //System.out.println(useProductList.toString());
        getProdList();
    }

    @FXML
    void clearSearch(ActionEvent event) {
    // Параметры для отправки запроса
        sortBy = "DESC";
        typeSorting = "BYID";
        currentPageIndex = 0;
        isCheckExpirated = "NO";
        search = "";
        // ---------------------
        searchField.setText("");
        typeSortingCBox.getSelectionModel().select(ValueOfSortingWarehouse.BYID);
        filterBtn.setText("По убыванию");
        filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));
        detailProductBtn.setDisable(true);

        getProdList();
    }

    @FXML
    void clickOnTable(MouseEvent event) {
        if (!Objects.isNull(productTable.getSelectionModel().getSelectedItem())){
            detailProductBtn.setDisable(false);
        }

        if (!Objects.isNull(productTable.getSelectionModel().getSelectedItem())){
            moveProdBtn.setDisable(false);
        }

        orderProdTable.getSelectionModel().select(null);
    }

    @FXML
    void clickToPaginationPanel(MouseEvent event) {

    }

    private void changePage(Number oldIndex, Number newIndex){
        System.out.println(newIndex);
        if (oldIndex != newIndex){
            currentPageIndex = newIndex;

            getProdList();
        }
    }

    @FXML
    void createOrder(ActionEvent event) {
        if (client == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось создать поставку", ButtonType.OK);
            alert.setTitle("Ошибка создания поставки");
            alert.setHeaderText("Выберите компанию-получателя!");
            alert.show();
            return;
        }
        if(orderObservableList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось создать поставку", ButtonType.OK);
            alert.setTitle("Ошибка создания поставки");
            alert.setHeaderText("Отсутствуют товары в поставке!");
            alert.show();
            return;
        }
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("select-date-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            SelectDateController selectDateController = new SelectDateController(this);
            fxmlLoader.setController(selectDateController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Выбор даты поставки");
            stage.setScene(scene);
            stage.showAndWait();

            if (startCreate){
                boolean finishCreate = true;
                BigDecimal cost = new BigDecimal("0.00");
                BigDecimal costWithVAT = new BigDecimal("0.00");
                List <OrderProductFX> productList = new ArrayList<>();
                OrderDTO orderDTO = new OrderDTO();
                for (OrderProductFX prod:
                        orderObservableList) {
                    if (prod.getCount() <= 0 || prod.getPrice().compareTo(new BigDecimal("0.00")) <= 0 || prod.getVat() <= -1){
                        finishCreate = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось создать поставку", ButtonType.OK);
                        alert.setTitle("Ошибка создания поставки");
                        alert.setHeaderText("Не все данные о товарах заполнены!");
                        alert.show();

                        break;
                    }
                    cost = cost.add(prod.getPrice().multiply(BigDecimal.valueOf(prod.getCount())));
                    costWithVAT = costWithVAT.add(prod.getPrice().multiply(BigDecimal.valueOf(prod.getCount())).multiply(BigDecimal.valueOf(100 - prod.getVat())).divide(BigDecimal.valueOf(100)));
                    productList.add(prod);
                }

                if (finishCreate){
                    orderDTO.setDate(orderDate);
                    orderDTO.setPrice(cost);
                    orderDTO.setProductList(productList);
                    orderDTO.setCostWithVAT(costWithVAT);
                    orderDTO.setUserClient(client);

                    OrderRepository.createOrder(orderDTO);

                    try {
                        stage = new Stage();
                        fxmlLoader = new FXMLLoader(RunApplication.class.getResource("order-view.fxml"));
                        scene = new Scene(fxmlLoader.load());
//            String CHTO = this.getClass().getResource("purchase-view.fxml").toString();
//            System.out.println(CHTO);
                        scene.getStylesheets().add(RunApplication.class.getResource("css/purchase.css").toExternalForm());
                        stage.setTitle("Поставки");
                        stage.setScene(scene);
                        stage.show();

                        Stage stageOld = (Stage) backBtn.getScene().getWindow();
                        stageOld.close();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно поставок", ButtonType.OK);
                        alert.setTitle("Ошибка подключения");
                        alert.setHeaderText("Проверьте подключение к интернету!");
                        alert.show();
                        System.out.println(LocalTime.now() + "   Ошибка открытия окна поставок! " + e.toString());
                    }
                }
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно выбора даты", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора даты! " + e.toString());
        }
    }

    @FXML
    void deleteProduct(ActionEvent event) {
        OrderProductFX orderProductFX = orderProdTable.getSelectionModel().getSelectedItem();

        if (orderProductFX != null){
            for (Product product:
                 useProductList) {
                if (product.getId().equals(orderProductFX.getId())){
                    useProductList.remove(product);
                    break;
                }
            }
        }

        orderObservableList.remove(orderProductFX);

        getProdList();
    }

    @FXML
    void detailProductAction(ActionEvent event) {
        try {
            PurchaseProduct pProd = new PurchaseProduct();
            if (productTable.getSelectionModel().getSelectedItem() != null){
                pProd = new PurchaseProduct(productTable.getSelectionModel().getSelectedItem().getId(), productTable.getSelectionModel().getSelectedItem().getName(), productTable.getSelectionModel().getSelectedItem().getVat()
                        ,productTable.getSelectionModel().getSelectedItem().getCategory(), productTable.getSelectionModel().getSelectedItem().getTypePackaging()
                        ,productTable.getSelectionModel().getSelectedItem().getCharacteristic(), productTable.getSelectionModel().getSelectedItem().getUnit()
                        ,productTable.getSelectionModel().getSelectedItem().getPrice(), productTable.getSelectionModel().getSelectedItem().getCountOnWarehouse(), productTable.getSelectionModel().getSelectedItem().getDateOfManufacture()
                        ,productTable.getSelectionModel().getSelectedItem().getDateExpiration());
            }
            else{
                pProd = new PurchaseProduct(orderProdTable.getSelectionModel().getSelectedItem().getId(), orderProdTable.getSelectionModel().getSelectedItem().getName(), orderProdTable.getSelectionModel().getSelectedItem().getVat()
                        ,orderProdTable.getSelectionModel().getSelectedItem().getCategory(), orderProdTable.getSelectionModel().getSelectedItem().getTypePackaging()
                        ,orderProdTable.getSelectionModel().getSelectedItem().getCharacteristic(), orderProdTable.getSelectionModel().getSelectedItem().getUnit()
                        ,orderProdTable.getSelectionModel().getSelectedItem().getPrice(), orderProdTable.getSelectionModel().getSelectedItem().getCountOnWarehouse(), orderProdTable.getSelectionModel().getSelectedItem().getManufactureDate()
                        ,orderProdTable.getSelectionModel().getSelectedItem().getDateOfExpiration());
            }

            orderProdTable.getSelectionModel().select(null);
            productTable.getSelectionModel().select(null);
            //System.out.println(pProd);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-product-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            AddProductController addProductController = new AddProductController(pProd);
            fxmlLoader.setController(addProductController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Информация о товаре");
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно информации о товаре", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна информации о товаре! " + e.toString());
        }
    }

    @FXML
    void filterByAscDesc(ActionEvent event) {
        if (filterBtn.getText().equals("По убыванию")){
            filterBtn.setText("По возрастанию");
            filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowUp.png").toExternalForm()));
            sortBy = "ASC";
        }
        else{
            filterBtn.setText("По убыванию");
            filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));
            sortBy = "DESC";
        }

        getProdList();
    }

    @FXML
    void searchByProdName(ActionEvent event) {
        search = searchField.getText();

        getProdList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/searchBtn.png").toExternalForm()));
        filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));
        // Для верхней таблицы
        setProductColumns();

        // для нижей таблицы
        setOrderColumns();

        getProdList();

        setComboBox();

        productPagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {changePage(oldIndex, newIndex);});
    }

    private void setOrderColumns(){
        vatOrdColumn.setCellFactory(column -> {
            TableCell<OrderProductFX, String> cell = TextFieldTableCell.<OrderProductFX>forTableColumn().call(column);
            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow() == null) {
                    cell.setEditable(false);
                } else {
                    try {
                        Integer.parseInt(newValue);
                        if (newValue.equals("20") || newValue.equals("0") || newValue.equals("10")){
//                            cell.setText(newValue);
                            cell.setStyle(null);
                            orderProdTable.getSelectionModel().getSelectedItem().setCount(Integer.parseInt(newValue));
                        }
                        else{
                            cell.setStyle("-fx-background-color: #CC5555");
                            cell.itemProperty().setValue(null);
                        }
                    } catch (NumberFormatException e) {
                        cell.setStyle("-fx-background-color: #CC5555");
                        cell.itemProperty().setValue(null);
                    }
                }
            });
            return cell;
        });

        //priceOrdColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        priceOrdColumn.setCellFactory(column -> {
            TableCell<OrderProductFX, String> cell = TextFieldTableCell.<OrderProductFX>forTableColumn().call(column);
            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow() == null) {
                    cell.setEditable(false);
                } else {
                    try {
                        System.out.println(cell.getTableRow().getIndex() + " " + cell.getIndex());
                        BigDecimal newVal = new BigDecimal(newValue).setScale(2);
                        if (newVal.compareTo(new BigDecimal("0.01")) >= 0){
                            DecimalFormat formatter = new DecimalFormat("0.00");
                            cell.setText(formatter.format(newVal));
                            cell.setStyle(null);
                            orderProdTable.getSelectionModel().getSelectedItem().setPrice(newVal);
                            //System.out.println(formatter.format(newVal));
                        }
                        else{
                            cell.setStyle("-fx-background-color: #CC5555");
                            cell.itemProperty().setValue(null);
                        }
                    } catch (Exception e) {
                        cell.setStyle("-fx-background-color: #CC5555");
                        cell.itemProperty().setValue(null);
                    }
                }
            });

            return cell;
        });
        nameOrdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()
        ));
        countOrdColumn.setCellFactory(column -> {
            TableCell<OrderProductFX, String> cell = TextFieldTableCell.<OrderProductFX>forTableColumn().call(column);
            cell.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (cell.getTableRow() == null) {
                    cell.setEditable(false);
                } else {
                    try {
                        int newVal = Integer.parseInt(newValue);
                        System.out.println(newVal);
                        if (newVal > 0 & newVal <= orderProdTable.getSelectionModel().getSelectedItem().getCountOnWarehouse()){
                            cell.setText("newValue");
                            cell.setStyle(null);
                            orderProdTable.getSelectionModel().getSelectedItem().setCount(newVal);
                        }
                        else{
                            //cell.setStyle("-fx-background-color: #CC5555");
                            cell.itemProperty().setValue(null);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        cell.setStyle("-fx-background-color: #CC5555");
                        cell.itemProperty().setValue(null);
                    }
                }
            });

            return cell;
        });
    }

    private void setProductColumns(){
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()
        ));
        dateExpireColumn.setCellValueFactory(cellData -> cellData.getValue().getDateExpiration() == null ? new SimpleStringProperty("-") : new SimpleStringProperty(cellData.getValue().getDateExpiration().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrice()
                .multiply(BigDecimal.valueOf(100 - cellData.getValue().getVat()))
                .divide(BigDecimal.valueOf(100)).toString()));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()
        ));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCountOnWarehouse()
        ));
    }

    private void getProdList(){
        productObservableList.clear();
        PagedDataDTO<Product> pagedDataDTO = ProductRepository.getAll(currentPageIndex, isCheckExpirated, search, typeSorting, sortBy);
        List<Product> productList = pagedDataDTO.getData();

        for (Product product:
                productList) {
            productObservableList.add(product);
        }

        productObservableList = FXCollections.observableArrayList(productObservableList.stream().filter(Predicate.not(useProductList::contains)).toList());
        productObservableList = FXCollections.observableArrayList(productObservableList.stream().filter(product -> product.getCountOnWarehouse() > 0).toList());
        productTable.setItems(productObservableList);

        if (pagedDataDTO.getTotal() == 0){
            productPagination.setPageCount(1);
        }
        else {
            productPagination.setPageCount(Math.toIntExact(Math.round(Math.ceil((double) productObservableList.size() / 14))));
        }
//        System.out.println(productObservableList.toString());
        countRowsLabel.setText("Всего записей: " + productObservableList.size());
    }

    private void setComboBox() {
        typeSortingCBox.getItems().setAll(ValueOfSortingWarehouse.values());
        typeSortingCBox.setConverter(new StringConverter<ValueOfSortingWarehouse>() {
            @Override
            public String toString(ValueOfSortingWarehouse status) {
                return status == null ? null : status.toString();
            }

            @Override
            public ValueOfSortingWarehouse fromString(String string) {
                return null;
            }
        });

        typeSortingCBox.getSelectionModel().select(ValueOfSortingWarehouse.BYID);

        typeSortingCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            switch (newValue){
                case BYID:
                    typeSorting = "BYID";
                    break;
                case BYDATEEXPIRATED:
                    typeSorting = "BYDATEEXPIRATED";
                    break;
                case BYDATEMANUFACTURE:
                    typeSorting = "BYDATEMANUFACTURE";
                    break;
                case BYPRICE:
                    typeSorting = "BYPRICE";
                    break;
            }

            currentPageIndex = 0;
            getProdList();
        });
    }
}
