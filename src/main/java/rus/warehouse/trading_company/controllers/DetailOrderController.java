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
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Order_list;
import rus.warehouse.trading_company.models.UserClient;
import rus.warehouse.trading_company.modelsDTO.Order_listDTO;
import rus.warehouse.trading_company.modelsDTO.PurchaseProduct;
import rus.warehouse.trading_company.repositories.Order_listRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class DetailOrderController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private TextField clientNameField;

    @FXML
    private TextField costField;

    @FXML
    private TableColumn<Order_list, Number> countColumn;

    @FXML
    private TableColumn<Order_list, Number> countWarehouseColumn;

    @FXML
    private TableColumn<Order_list, Number> idColumn;

    @FXML
    private TableColumn<Order_list, String> nameColumn;

    @FXML
    private TableColumn<Order_list, Number> priceColumn;

    @FXML
    private TextField priceVATField;

    @FXML
    private TableView<Order_list> productTable;

    private Integer idOrder;

    private ObservableList observableList = FXCollections.observableArrayList();

    private UserClient client = null;

    @FXML
    private LocalDateTimeTextField dateOrderField;

    @FXML
    void closeForm(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }
    @FXML
    void clientInfo(MouseEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-client-view.fxml"));
            AddClientController addClientController = new AddClientController(client);
            fxmlLoader.setController(addClientController);
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

    public DetailOrderController(Integer idOrder) {
        this.idOrder = idOrder;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Order_listDTO orderListDTO = Order_listRepository.getByIdOrder(idOrder);

        if(orderListDTO != null){
            setField(orderListDTO);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Обратитесь к специалисту", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            return;
        }
        List<Order_list> order_lists = orderListDTO.getProdLists();
        client = orderListDTO.getOrder().getUserclient();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));

        countWarehouseColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProduct().getCountOnWarehouse()
        ));
        priceColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getProduct().getPrice()
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProduct().getId()
        ));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()
        ));

        for (Order_list orderList:
                order_lists) {
            observableList.add(orderList);
        }
        productTable.setItems(observableList);
        System.out.println("DATAIL   " + orderListDTO);
    }

    private void setField(Order_listDTO orderListDTO){
        priceVATField.setText(orderListDTO.getOrder().getCostWithVat().toString());
        costField.setText(orderListDTO.getOrder().getPrice().toString());
        dateOrderField.setLocalDateTime(orderListDTO.getOrder().getDate());
        clientNameField.setText(orderListDTO.getOrder().getUserclient().getName());
    }
}
