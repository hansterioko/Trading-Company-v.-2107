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
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.models.UserClient;
import rus.warehouse.trading_company.repositories.CompanyRepository;
import rus.warehouse.trading_company.repositories.UserClientRepository;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SelectCompanyController implements Initializable {
    @FXML
    private TableColumn<Company, CheckBox> chekColumn;

    @FXML
    private TableView<Company> chooseCompanyTable;

    @FXML
    private TableColumn<Company, String> cityColumn;

    @FXML
    private TableColumn<Company, String> contactColumn;

    @FXML
    private TableColumn<Company, String> houseColumn;

    @FXML
    private TableColumn<Company, Integer> idColumn;

    @FXML
    private TableColumn<Company, String> nameColumn;

    @FXML
    private Button okBtn;

    @FXML
    private TableColumn<Company, String> streetColumn;

    private String listIdCompany = "";

    private PurchaseController purchaseController;

    public SelectCompanyController(PurchaseController purchaseController) {
        this.purchaseController = purchaseController;
    }

    ObservableList<Company> observableList = FXCollections.observableArrayList();

    public void okClick(MouseEvent mouseEvent) {
        boolean flag = false;
//        System.out.println(observableList);
        for (Company company:
             observableList) {
            if(company.getCheckbox().isSelected()){
                flag = true;
                listIdCompany += "," + company.getId();
            }
        }
        if (flag){
            listIdCompany = listIdCompany.substring(1);
        }
        if (!flag){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Ни одна из компаний не выбрана!", new ButtonType("Отмена"), ButtonType.OK);
            alert.setTitle("Ошибка фильтра");
            alert.setHeaderText("Закрыть окно фильтра?");

            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                Stage stageOld = (Stage) okBtn.getScene().getWindow();
                stageOld.close();
            }
        }
        //System.out.println(listIdCompany + "В СЕЛЕКТ");
        purchaseController.setListProviders(listIdCompany);
        Stage stageOld = (Stage) okBtn.getScene().getWindow();
        stageOld.close();
//        System.out.println(listIdCompany);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        ObservableList<Company> observableList = FXCollections.observableArrayList(new Company(1, "fs", "fdss", "fdsf", "fsdf", "987"),
//                new Company(1, "fs", "fdss", "fdsf", "fsdf", "987"),
//                new Company(1, "fs", "fdss", "fdsf", "fsdf", "987"));

        idColumn.setCellValueFactory(new PropertyValueFactory<Company, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("street"));
        houseColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("house"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("phone"));
        chekColumn.setCellValueFactory(new PropertyValueFactory<Company, CheckBox>("checkbox"));

        getAllCompany();

//        this.observableList = observableList;
    }

    private void getAllCompany(){
        List<Company> companyList = CompanyRepository.getAll();

        if (companyList != null){
            for (Company company:
                    companyList) {
                company.setCheckbox(new CheckBox());
                observableList.add(company);
            }
            chooseCompanyTable.setItems(observableList);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось открыть окно выбора поставщика", ButtonType.OK);
            alert.setTitle("Ошибка фильрации");
            alert.setHeaderText("Поставщики не были добавлены");
            alert.show();
        }
    }
}
