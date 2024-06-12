package rus.warehouse.trading_company.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.repositories.CompanyRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class SelectPurchaseCompanyController implements Initializable {

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


    private AddPurchaseController addPurchaseController;
    private Boolean companyNotNull = false;

    Company newCompany = new Company();

    ObservableList<Company> observableList = FXCollections.observableArrayList();

    public SelectPurchaseCompanyController(AddPurchaseController addPurchaseController) {
        this.addPurchaseController = addPurchaseController;
        if (addPurchaseController.getCompany() != null){
            companyNotNull = true;
        }
    }

    public void okClick(MouseEvent mouseEvent) {
        if (!Objects.isNull(chooseCompanyTable.getSelectionModel().getSelectedItem())){
            addPurchaseController.setIdCompany(chooseCompanyTable.getSelectionModel().getSelectedItem());
            Stage stageOld = (Stage) okBtn.getScene().getWindow();
            stageOld.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Для выбора поставщика нажмите на соответствующую строку в таблице", ButtonType.OK);
            alert.setTitle("Ошибка выбора поставщика");
            alert.setHeaderText("Поставщик не выбран");
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<Company, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("city"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("street"));
        houseColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("house"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Company, String>("phone"));

        getAllCompany();

        if (companyNotNull){
            for (Company company:
                 observableList) {
                if(company.getName().equals(addPurchaseController.getCompany().getName())){
                    System.out.println("POBEDA");
                    newCompany = company;
                }
            }
            chooseCompanyTable.getSelectionModel().select(newCompany);
        }
//        this.observableList = observableList;
    }

    private void getAllCompany(){
        observableList = FXCollections.observableArrayList();

        for (Company company:
             CompanyRepository.getAll()) {
            company.setCheckbox(new CheckBox());
            observableList.add(company);
        }
        chooseCompanyTable.setItems(observableList);
    }

    public void addNewCompanyBtn(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-company-view.fxml"));
            AddCompanyController addCompanyController = new AddCompanyController();
            fxmlLoader.setController(addCompanyController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Добавление поставщика");
            stage.setScene(scene);
            stage.showAndWait();

            getAllCompany();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления поставщика", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления поставщика! " + e.toString());
        }
    }
}
