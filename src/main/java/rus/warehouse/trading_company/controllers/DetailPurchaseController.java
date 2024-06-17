package rus.warehouse.trading_company.controllers;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.scene.control.LocalDateTimeTextField;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.Product;
import rus.warehouse.trading_company.models.Purchase_list;
import rus.warehouse.trading_company.modelsDTO.PurchaseDTO;
import rus.warehouse.trading_company.modelsDTO.PurchaseProduct;
import rus.warehouse.trading_company.modelsDTO.Purchase_listDTO;
import rus.warehouse.trading_company.repositories.Purchase_listRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class DetailPurchaseController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField costField;

    @FXML
    private TableColumn<Purchase_list, Number> countColumn;

    @FXML
    private TableColumn<Purchase_list, Number> countWarehouseColumn;

    @FXML
    private LocalDateTimeTextField datePurchField;

    @FXML
    private TableColumn<Purchase_list, Number> idColumn;

    @FXML
    private TableColumn<Purchase_list, String> nameColumn;

    @FXML
    private TableColumn<Purchase_list, Number> priceColumn;

    @FXML
    private TextField priceVATField;

    @FXML
    private TableView<Purchase_list> productTable;

    private Integer idPurchase;

    private ObservableList observableList = FXCollections.observableArrayList();

    private Company company = null;

    @FXML
    void closeForm(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }

    @FXML
    void infoAboutCompany(ActionEvent event) throws IOException {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-company-view.fxml"));
        AddCompanyController addCompanyController = new AddCompanyController(company);
        fxmlLoader.setController(addCompanyController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Информация о поставщике");
            stage.setScene(scene);
            stage.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно c информацией о поставщике", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна информации о поставщике! " + e.toString());
        }
    }

    @FXML
    void infoAboutProd(ActionEvent event) {
        try {
            PurchaseProduct pProd = new PurchaseProduct(productTable.getSelectionModel().getSelectedItem().getProduct().getId(), productTable.getSelectionModel().getSelectedItem().getProduct().getName(), productTable.getSelectionModel().getSelectedItem().getProduct().getVat()
            ,productTable.getSelectionModel().getSelectedItem().getProduct().getCategory(), productTable.getSelectionModel().getSelectedItem().getProduct().getTypePackaging()
            ,productTable.getSelectionModel().getSelectedItem().getProduct().getCharacteristic(), productTable.getSelectionModel().getSelectedItem().getProduct().getUnit()
            ,productTable.getSelectionModel().getSelectedItem().getProduct().getPrice(), productTable.getSelectionModel().getSelectedItem().getCount(), productTable.getSelectionModel().getSelectedItem().getProduct().getDateOfManufacture()
            ,productTable.getSelectionModel().getSelectedItem().getProduct().getDateExpiration());
            System.out.println(pProd);
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

    public DetailPurchaseController(Integer idPurchase) {
        this.idPurchase = idPurchase;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Purchase_listDTO purchaseListDTO = Purchase_listRepository.getByIdPurchase(idPurchase);

        if(purchaseListDTO != null){
            setField(purchaseListDTO);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Обратитесь к специалисту", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            return;
        }
        List<Purchase_list> purchaseList = purchaseListDTO.getProdLists();
        company = purchaseListDTO.getPurchase().getCompany();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        countWarehouseColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProduct().getCountOnWarehouse()
        ));
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getPrice()
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProduct().getId()
        ));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()
        ));

        for (Purchase_list purchase_list:
             purchaseList) {
            observableList.add(purchase_list);
        }
        productTable.setItems(observableList);
        System.out.println("DATAIL   " + purchaseListDTO);
    }

    private void setField(Purchase_listDTO purchase_listDTO){
        priceVATField.setText(purchase_listDTO.getPurchase().getCostWithVAT().toString());
        costField.setText(purchase_listDTO.getPurchase().getPrice().toString());
        datePurchField.setLocalDateTime(purchase_listDTO.getPurchase().getDate());
        companyNameField.setText(purchase_listDTO.getPurchase().getCompany().getName());
    }
}
