package rus.warehouse.trading_company.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDateTimeTextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private TextField categoryField;

    @FXML
    private Spinner<?> countSpinner;

    @FXML
    private TextField dateHoldField;

    @FXML
    private LocalDateTimeTextField manufactureDatePicker;

    @FXML
    private ComboBox<?> measureCBox;

    @FXML
    private TextField nameField;

    @FXML
    private Spinner<?> priceSpinner;

    @FXML
    private TextArea summaryField;

    @FXML
    private TextField unitField;

    @FXML
    private Spinner<?> vatSpinner;

    @FXML
    void addPoroduct(ActionEvent event) {

    }

    @FXML
    void backToLastScene(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateHoldField.setDisable(true);
        measureCBox.setDisable(true);
    }

    public void keyDateListener(KeyEvent keyEvent) {
       // 5 мая 2024 г., 22:58:31
        //System.out.println(manufactureDatePicker.getText());

    }

    public void exit(MouseEvent mouseEvent) {
        if (!Objects.isNull(manufactureDatePicker.getText()) & !manufactureDatePicker.getText().trim().equals("")){
            dateHoldField.setDisable(false);
            measureCBox.setDisable(false);
        }
        else {
            dateHoldField.setDisable(true);
            measureCBox.setDisable(true);
        }
    }
}
