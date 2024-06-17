package rus.warehouse.trading_company.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.UserClient;
import rus.warehouse.trading_company.repositories.CompanyRepository;
import rus.warehouse.trading_company.repositories.UserClientRepository;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

@NoArgsConstructor
public class AddClientController implements Initializable {

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
    private Button addBtn;

    private UserClient client = null;

    public AddClientController(UserClient client) {
        this.client = client;
        //Platform.runLater(() -> backBtn.setText("Закрыть"));
    }

    @FXML
    void addClient(ActionEvent event) {
        if(!numberField.getText().isEmpty() && !cityField.getText().isEmpty() && !streetField.getText().isEmpty() &&!houseField.getText().isEmpty() && !numberField.getText().isEmpty()){
            UserClient client = new UserClient(null, nameField.getText(), cityField.getText(), streetField.getText(), houseField.getText(), numberField.getText());
            if (UserClientRepository.createClient(client).equals("true")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                alert.setTitle("Добавление получаетля");
                alert.setHeaderText("Получатель добавлен!");
                alert.show();

                Stage stageOld = (Stage) backBtn.getScene().getWindow();
                stageOld.close();
            } else if (UserClientRepository.createClient(client).equals("exist")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Получатель с таким названием уже есть в базе!", ButtonType.OK);
                alert.setTitle("Добавление получателя");
                alert.setHeaderText("Ошибка при добавлении получателя!");
                alert.show();
                System.out.println(LocalTime.now() + "   Ошибка добавление получателя по названию (норма ? ремни что ли)");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Повторите попытку или обратитесь к специалисту", ButtonType.OK);
                alert.setTitle("Добавление получателя");
                alert.setHeaderText("Ошибка при добавлении получателя!");
                alert.show();
                System.out.println(LocalTime.now() + "   Ошибка добавления получателя");
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не все поля заполнены!", ButtonType.OK);
            alert.setTitle("Добавление получателя");
            alert.setHeaderText("Ошибка при добавлении получателя!");
            alert.show();
            //System.out.println(LocalTime.now() + "   Ошибка добавление поставщика");
        }

    }

    @FXML
    void cancelAddClient(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (client != null){
            numberField.setEditable(false);
            nameField.setEditable(false);
            houseField.setEditable(false);
            streetField.setEditable(false);
            cityField.setEditable(false);

            addBtn.setVisible(false);

            numberField.setText(client.getPhone());
            nameField.setText(client.getName());
            cityField.setText(client.getCity());
            houseField.setText(client.getHouse());
            streetField.setText(client.getStreet());
        }

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