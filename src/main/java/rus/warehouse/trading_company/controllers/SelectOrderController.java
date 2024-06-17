package rus.warehouse.trading_company.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.UserClient;
import rus.warehouse.trading_company.repositories.CompanyRepository;
import rus.warehouse.trading_company.repositories.UserClientRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class SelectOrderController implements Initializable {
    @FXML
    private TableView<UserClient> chooseClientTable;

    @FXML
    private TableColumn<UserClient, String> cityColumn;

    @FXML
    private TableColumn<UserClient, String> contactColumn;

    @FXML
    private TableColumn<UserClient, String> houseColumn;

    @FXML
    private TableColumn<UserClient, Integer> idColumn;

    @FXML
    private TableColumn<UserClient, String> nameColumn;

    @FXML
    private Button okBtn;

    @FXML
    private TableColumn<UserClient, String> streetColumn;

    private AddOrderController addOrderController;

    private UserClient newClient = null;

    private boolean clientNotNull = false;

    ObservableList<UserClient> observableList = FXCollections.observableArrayList();

    @FXML
    void addNewClient(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-company-view.fxml"));
            AddClientController addClientController = new AddClientController();
            fxmlLoader.setController(addClientController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Добавление получателя");
            stage.setScene(scene);
            stage.showAndWait();

            getAllClients();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления получателя", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления получателя! " + e.toString());
        }
    }

    @FXML
    void okClick(MouseEvent event) {
        if (!Objects.isNull(chooseClientTable.getSelectionModel().getSelectedItem())){
            addOrderController.setClient(chooseClientTable.getSelectionModel().getSelectedItem());
            Stage stageOld = (Stage) okBtn.getScene().getWindow();
            stageOld.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Для выбора получателя нажмите на соответствующую строку в таблице", ButtonType.OK);
            alert.setTitle("Ошибка выбора получателя");
            alert.setHeaderText("Получатель не выбран");
            alert.show();
        }
    }

    public SelectOrderController(AddOrderController addOrderController) {
        this.addOrderController = addOrderController;
        if (addOrderController.getClient() != null){
            clientNotNull = true;
        }
    }

    private void getAllClients(){
        observableList = FXCollections.observableArrayList();

        for (UserClient client:
                UserClientRepository.getAll()) {
            client.setCheckbox(new CheckBox());
            observableList.add(client);
        }
        chooseClientTable.setItems(observableList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<UserClient, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("street"));
        houseColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("house"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("phone"));

        getAllClients();

        if (clientNotNull){
            for (UserClient client:
                    observableList) {
                if(client.getName().equals(addOrderController.getClient().getName())){
                    // System.out.println("POBEDA");
                    newClient = client;
                }
            }
            chooseClientTable.getSelectionModel().select(newClient);
        }
    }
}
