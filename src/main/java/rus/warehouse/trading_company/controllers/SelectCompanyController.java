package rus.warehouse.trading_company.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.repositories.CompanyRepository;

import java.net.URL;
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

    ObservableList<Company> observableList = FXCollections.observableArrayList();

    public void okClick(MouseEvent mouseEvent) {
        System.out.println(observableList);
        for (Company company:
             observableList) {
            if(company.getCheckbox().isSelected())
            System.out.println(company);
        }
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
        for (Company company:
             CompanyRepository.getAll()) {
            company.setCheckbox(new CheckBox());
            observableList.add(company);
        }
        chooseCompanyTable.setItems(observableList);
    }
}
