package rus.warehouse.trading_company.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import lombok.ToString;
import rus.warehouse.trading_company.models.DecomProduct;
import rus.warehouse.trading_company.repositories.DecomProductRepository;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DecProdHouseController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<DecomProduct, Number> commentColumn;

    @FXML
    private TableColumn<DecomProduct, String> dateColumn;

    @FXML
    private TableColumn<DecomProduct, Number> idColumn;

    @FXML
    private TableColumn<DecomProduct, String> nameColumn;

    @FXML
    private TableColumn<DecomProduct, Number> countColumn;

    @FXML
    private TableView<DecomProduct> productTable;

    @FXML
    void backAction(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()
        ));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()
        ));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDecCount()
        ));

        ObservableList<DecomProduct> observableList = FXCollections.observableArrayList();

        for (DecomProduct decProd:
                DecomProductRepository.getAll()) {
            observableList.add(decProd);
        }

        productTable.setItems(observableList);
    }
}
