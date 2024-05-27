package rus.warehouse.trading_company.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.repositories.CompanyRepository;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddCompanyController implements Initializable {

    @FXML
    private TextField cityField;

    @FXML
    private TextField houseField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField streetField;

    @FXML
    private Button backBtn;

    @FXML
    void addCompany(ActionEvent event) {
        Company company = new Company(null, nameField.getText(), cityField.getText(), streetField.getText(), houseField.getText(), numberField.getText());
        if (CompanyRepository.createCompany(company).equals("true")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
            alert.setTitle("Добавление поставщика");
            alert.setHeaderText("Поставщик добавлен!");
            alert.show();

            Stage stageOld = (Stage) backBtn.getScene().getWindow();
            stageOld.close();
        } else if (CompanyRepository.createCompany(company).equals("exist")) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Компания с таким названием уже есть в базе!", ButtonType.OK);
            alert.setTitle("Добавление поставщика");
            alert.setHeaderText("Ошибка при добавлении поставщика!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка добавление поставщика по названию (норма)");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Повторите попытку или обратитесь к специалисту", ButtonType.OK);
            alert.setTitle("Добавление поставщика");
            alert.setHeaderText("Ошибка при добавлении поставщика!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка добавление поставщика");
        }
    }

    @FXML
    void cancelAddCompany(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberField.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) {
                if (!numberField.getText().matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$")) {
                    numberField.setText("");
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Номер должен содержать только цифры, количество цифр равняется 11", ButtonType.OK);
                    alert.setTitle("Ошибка ввода");
                    alert.setHeaderText("Ошибка при вводе номера телефона!");
                    alert.show();
                }
            }
        });
    }
}