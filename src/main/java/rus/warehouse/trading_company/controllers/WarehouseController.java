package rus.warehouse.trading_company.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.helpers.ValueOfExpiration;
import rus.warehouse.trading_company.helpers.ValueOfSortingWarehouse;
import rus.warehouse.trading_company.helpers.ValueOfVisibleExpiration;
import rus.warehouse.trading_company.models.Product;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.modelsDTO.PagedDataDTO;
import rus.warehouse.trading_company.modelsDTO.PurchaseProduct;
import rus.warehouse.trading_company.repositories.ProductRepository;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WarehouseController implements Initializable {

    // Для таблицы
    public TableView<Product> productTable;
    public TableColumn<Product, String> nameColumn;
    public TableColumn<Product, Number> priceColumn;
    public TableColumn<Product, String> dateExpireColumn;
    public TableColumn<Product, Number> idColumn;
    public Button detailProductBtn;
    @FXML
    private TableColumn<Product, Number> countColumn;
    //----------------------------------------
    public Label countRowsLabel;
    public Pagination purchasePagination;
    public Button backBtn;
    public TextField searchField;
    public Button searchBtn;
    public Button filterBtn;

    @FXML
    private ComboBox<ValueOfSortingWarehouse> typeSortingCBox;

    private ObservableList observableList = FXCollections.observableArrayList();

    // Параметры для отправки запроса
    private String sortBy = "DESC";
    private String typeSorting = "BYID";
    private Number currentPageIndex = 0;
    private String isCheckExpirated = "NO";

    private String search = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/searchBtn.png").toExternalForm()));
        filterBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/arrowDown.png").toExternalForm()));

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()
        ));
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrice()
        ));
        dateExpireColumn.setCellValueFactory(cellData -> cellData.getValue().getDateExpiration() == null ? new SimpleStringProperty("-") : new SimpleStringProperty(cellData.getValue().getDateExpiration().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()
        ));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCountOnWarehouse()
        ));

        getProdList();
        setComboBox();
        purchasePagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {changePage(oldIndex, newIndex);});
    }

    private void changePage(Number oldIndex, Number newIndex){
        System.out.println(newIndex);
        if (oldIndex != newIndex){
            currentPageIndex = newIndex;

            getProdList();
        }
    }

    private void getProdList(){
        observableList.clear();
        PagedDataDTO<Product> pagedDataDTO = ProductRepository.getAll(currentPageIndex, isCheckExpirated, search, typeSorting, sortBy);
        List<Product> productList = pagedDataDTO.getData();
        countRowsLabel.setText("Всего записей: " + pagedDataDTO.getTotal());

        if (pagedDataDTO.getTotal() == 0){
            purchasePagination.setPageCount(1);
        }
        else {
            purchasePagination.setPageCount(Math.toIntExact(Math.round(Math.ceil((double) pagedDataDTO.getTotal() / 14))));
        }

        for (Product product:
                productList) {
            observableList.add(product);
        }

        productTable.setItems(observableList);
    }

    public void clickOnTable(MouseEvent mouseEvent) {
        if (!Objects.isNull(productTable.getSelectionModel().getSelectedItem())){
            detailProductBtn.setDisable(false);
        }
    }

    public void clickToPaginationPanel(MouseEvent mouseEvent) {
    }

    public void backAction(ActionEvent actionEvent) {
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

    public void checkExpiredProduct(ActionEvent actionEvent) {
        isCheckExpirated = "YES";
        currentPageIndex = 0;

        getProdList();
    }

    public void filterByAscDesc(ActionEvent actionEvent) {
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

    public void searchByProdName(ActionEvent actionEvent) {
        search = searchField.getText();

        getProdList();
    }


    public void detailProductAction(ActionEvent actionEvent) {
        try {
            PurchaseProduct pProd = new PurchaseProduct(productTable.getSelectionModel().getSelectedItem().getId(), productTable.getSelectionModel().getSelectedItem().getName(), productTable.getSelectionModel().getSelectedItem().getVat()
                    ,productTable.getSelectionModel().getSelectedItem().getCategory(), productTable.getSelectionModel().getSelectedItem().getTypePackaging()
                    ,productTable.getSelectionModel().getSelectedItem().getCharacteristic(), productTable.getSelectionModel().getSelectedItem().getUnit()
                    ,productTable.getSelectionModel().getSelectedItem().getPrice(), productTable.getSelectionModel().getSelectedItem().getCountOnWarehouse(), productTable.getSelectionModel().getSelectedItem().getDateOfManufacture()
                    ,productTable.getSelectionModel().getSelectedItem().getDateExpiration());
            System.out.println(pProd);
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-product-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            AddProductController addProductController = new AddProductController(pProd, true);
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

    public void clearSearch(ActionEvent actionEvent) {
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
    void reportAction(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("decomhouse-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(fxmlLoader.load());
            //scene.getStylesheets().add(RunApplication.class.getResource("css/main.css").toExternalForm());
            stage.setTitle("Список списанных товаров");
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно списанных товаров", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия списанных товаров! " + e.toString());
        }
    }
}
