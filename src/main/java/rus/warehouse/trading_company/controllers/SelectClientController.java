package rus.warehouse.trading_company.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.UserClient;
import rus.warehouse.trading_company.repositories.CompanyRepository;
import rus.warehouse.trading_company.repositories.UserClientRepository;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectClientController implements Initializable {
    @FXML
    private TableColumn<Company, CheckBox> chekColumn;

    @FXML
    private TableView<UserClient> chooseCompanyTable;

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

    private String listIdClient = "";

    private OrderController orderController;

    public SelectClientController(OrderController orderController) {
        this.orderController = orderController;
    }

    ObservableList<UserClient> observableList = FXCollections.observableArrayList();

    public void okClick(MouseEvent mouseEvent) {
        boolean flag = false;
//        System.out.println(observableList);
        for (UserClient userClient:
             observableList) {
            if(userClient.getCheckbox().isSelected()){
                flag = true;
                listIdClient += "," + userClient.getId();
            }
        }
        if (flag){
            listIdClient = listIdClient.substring(1);
        }
        if (!flag){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ни один из получателей не выбран!", new ButtonType("Отмена"), ButtonType.OK);
            alert.setTitle("Ошибка фильтра");
            alert.setHeaderText("Закрыть окно фильтра?");

            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                Stage stageOld = (Stage) okBtn.getScene().getWindow();
                stageOld.close();
            }
        }
        //System.out.println(listIdCompany + "В СЕЛЕКТ");
        orderController.setListClients(listIdClient);
        Stage stageOld = (Stage) okBtn.getScene().getWindow();
        stageOld.close();
//        System.out.println(listIdCompany);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ObservableList<Company> observableList = FXCollections.observableArrayList(new Company(1, "fs", "fdss", "fdsf", "fsdf", "987"),
//                new Company(1, "fs", "fdss", "fdsf", "fsdf", "987"),
//                new Company(1, "fs", "fdss", "fdsf", "fsdf", "987"));

        idColumn.setCellValueFactory(new PropertyValueFactory<UserClient, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("street"));
        houseColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("house"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<UserClient, String>("phone"));
        chekColumn.setCellValueFactory(new PropertyValueFactory<Company, CheckBox>("checkbox"));

        getAllClient();

//        this.observableList = observableList;
    }

    private void getAllClient(){
        List<UserClient> userList = UserClientRepository.getAll();

        if (userList != null){
            for (UserClient client:
                    userList) {
                client.setCheckbox(new CheckBox());
                observableList.add(client);
            }
            chooseCompanyTable.setItems(observableList);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось открыть окно выбора получателя", ButtonType.OK);
            alert.setTitle("Ошибка фильрации");
            alert.setHeaderText("Получатели не были добавлены");
            alert.show();
        }
    }
}
