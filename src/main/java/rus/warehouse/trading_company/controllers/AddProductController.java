package rus.warehouse.trading_company.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import rus.warehouse.trading_company.helpers.ValueOfExpiration;
import rus.warehouse.trading_company.helpers.ValueOfVisibleExpiration;
import rus.warehouse.trading_company.models.Product;
import rus.warehouse.trading_company.modelsDTO.PurchaseProduct;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddProductController implements Initializable {

    @FXML
    private Spinner<Integer> dateHoldSpinner;

    @FXML
    private Button backBtn;

    @FXML
    private TextField categoryField;

    @FXML
    private Spinner<Integer> countSpinner;

//  Срок годности
    @FXML
    private LocalDateTimeTextField manufactureDatePicker;

    @FXML
    private ComboBox<ValueOfExpiration> measureCBox;

    @FXML
    private ComboBox<ValueOfVisibleExpiration> typeExpirationCBox;

    @FXML
    private LocalDateTimeTextField dateExpirateField;


    @FXML
    private Label measuringExpirationLabel;

    @FXML
    private Label endDateExpirateLabel;

    @FXML
    private Label expirationDateLabel;

    @FXML
    private Label unitOfExpirateLabel;

//  --------------------------------

    @FXML
    private TextField nameField;

    @FXML
    private Spinner<Integer> priceSpinner;

    @FXML
    private Spinner<Integer> priceKopeckSpinner;

    @FXML
    private TextArea summaryField;

    @FXML
    private TextField typePackagingField;

    @FXML
    private TextField unitField;

    @FXML
    private Spinner<Integer> vatSpinner;

    private AddPurchaseController addPurchaseController;

    @FXML
    private Button addProdBtn;

    // Для типа контроллера
    private String typeController;

    @FXML
    void addProduct(ActionEvent event) {
        //System.out.println(measureCBox.getValue());
        if (!nameField.getText().trim().equals("") && vatSpinner.getValue() != null && countSpinner.getValue() != null && (priceKopeckSpinner.getValue() != null || priceSpinner.getValue() != null) && (priceKopeckSpinner.getValue() != 0 || priceSpinner.getValue() != 0) && !unitField.getText().trim().equals("")){
            Double costD = Double.valueOf(priceSpinner.getValue()) + (Double.valueOf(priceKopeckSpinner.getValue()) * 0.01);
            BigDecimal cost = new BigDecimal(costD.toString());

            if(manufactureDatePicker.getLocalDateTime() != null)
                if (typeExpirationCBox.getSelectionModel().getSelectedItem().equals(ValueOfVisibleExpiration.NOTHING)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Срок годности не указан", new ButtonType("Да", ButtonBar.ButtonData.APPLY), new ButtonType("Нет"));
                    alert.setTitle("Срок годности");
                    alert.setHeaderText("Продолжить без указания срока годности?");
                    alert.showAndWait();

                    if (alert.getResult() == ButtonType.APPLY){
                        // добавление без срока годности
                        addPurchaseController.addNewProduct(typeController, new PurchaseProduct(nameField.getText(), vatSpinner.getValue(), categoryField.getText(), summaryField.getText(), unitField.getText(), cost, manufactureDatePicker.getLocalDateTime(), countSpinner.getValue(), typePackagingField.getText()));
                        Stage stageOld = (Stage) backBtn.getScene().getWindow();
                        stageOld.close();
                    }
                } else if (typeExpirationCBox.getSelectionModel().getSelectedItem().equals(ValueOfVisibleExpiration.DIGITAL)) {
                    if (dateHoldSpinner.getValue() != 0){
                        // добавляем со спинеров числом
                        LocalDateTime endDateExpiration = manufactureDatePicker.getLocalDateTime();

                        switch (measureCBox.getValue()){
                            case ValueOfExpiration.DAY:
                                endDateExpiration.plusDays(dateHoldSpinner.getValue());
                                break;
                            case ValueOfExpiration.HOUR:
                                endDateExpiration.plusHours(dateHoldSpinner.getValue());
                                break;
                            case ValueOfExpiration.WEEK:
                                endDateExpiration.plusWeeks(dateHoldSpinner.getValue());
                                break;
                            case ValueOfExpiration.YEAR:
                                endDateExpiration.plusYears(dateHoldSpinner.getValue());
                                break;
                        }

                        addPurchaseController.addNewProduct(typeController, new PurchaseProduct(nameField.getText(), vatSpinner.getValue(), categoryField.getText(), summaryField.getText(), unitField.getText(), cost, manufactureDatePicker.getLocalDateTime(), endDateExpiration, countSpinner.getValue(), typePackagingField.getText()));
                        Stage stageOld = (Stage) backBtn.getScene().getWindow();
                        stageOld.close();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Не все поля заполнены!", ButtonType.OK);
                        alert.setTitle("Ошибка добавления");
                        alert.setHeaderText("Укажите срок годности!");
                        alert.show();
                    }
                }
                else {
                    if (dateExpirateField.getLocalDateTime() != null){
                        // добавляем со конечной датой
                        addPurchaseController.addNewProduct(typeController, new PurchaseProduct(nameField.getText(), vatSpinner.getValue(), categoryField.getText(), summaryField.getText(), unitField.getText(), cost, manufactureDatePicker.getLocalDateTime(), dateExpirateField.getLocalDateTime(), countSpinner.getValue(), typePackagingField.getText()));
                        Stage stageOld = (Stage) backBtn.getScene().getWindow();
                        stageOld.close();
                    }
                    else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Не все поля заполнены!", ButtonType.OK);
                        alert.setTitle("Ошибка добавления");
                        alert.setHeaderText("Укажите срок годности!");
                        alert.show();
                    }
                }
            else{
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Дата выпуска не указана");
                ButtonType yesTypeBtn = new ButtonType("Да");
                ButtonType noTypeBtn = new ButtonType("Нет");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(yesTypeBtn, noTypeBtn);

                alert.setTitle("Дата производства");
                alert.setHeaderText("Продолжить без указания даты производства?");
//                alert.showAndWait();

                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == yesTypeBtn){
                    // добавление без даты выпуска и срока годности
                    addPurchaseController.addNewProduct(typeController, new PurchaseProduct(nameField.getText(), vatSpinner.getValue(), categoryField.getText(), summaryField.getText(), unitField.getText(), cost, countSpinner.getValue(), typePackagingField.getText()));
                    Stage stageOld = (Stage) backBtn.getScene().getWindow();
                    stageOld.close();
                }
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не все поля заполнены!", ButtonType.OK);
            alert.setTitle("Ошибка добавления");
            alert.setHeaderText("Заполните обязательные поля!");
            alert.showAndWait();
        }
    }

    @FXML
    void backToLastScene(ActionEvent event) {
        Stage stageOld = (Stage) backBtn.getScene().getWindow();
        stageOld.close();
    }


    public AddProductController(AddPurchaseController addPurchaseController, String type) {
        this.addPurchaseController = addPurchaseController;
        typeController = type;
        if (type.equals("INFO")){
            //addProdBtn.setVisible(false);
            Platform.runLater(()->addProdBtn.setVisible(false));
        }
        if (type.equals("EDIT")){
            //addProdBtn.setText("Изменить");
            Platform.runLater(()->addProdBtn.setText("Изменить"));
        }
    }

    public AddProductController(AddPurchaseController addPurchaseController, String type, PurchaseProduct product) {
        this.addPurchaseController = addPurchaseController;
        typeController = type;
        if (type.equals("INFO")){
            //addProdBtn.setVisible(false);
            Platform.runLater(()->addProdBtn.setVisible(false));
            Platform.runLater(()->setField(product));
            Platform.runLater(this::setFieldNoEdit);

        }
        if (type.equals("EDIT")){
            //addProdBtn.setText("Изменить");
            Platform.runLater(()->addProdBtn.setText("Изменить"));
            Platform.runLater(()->setField(product));
        }
    }

    private void setField(PurchaseProduct product){
        nameField.setText(product.getName());
        priceSpinner.getValueFactory().setValue((int)Math.floor(product.getPrice().doubleValue()));
        priceKopeckSpinner.getValueFactory().setValue((int)Math.floor(product.getPrice().doubleValue() * 100) % 100);
        countSpinner.getValueFactory().setValue(product.getCount());
        categoryField.setText(product.getCategory());
        summaryField.setText(product.getCharacteristic());
        unitField.setText(product.getUnit());
        typePackagingField.setText(product.getTypePackaging());
        vatSpinner.getValueFactory().setValue(product.getVat());
        if (product.getManufactureDate() != null){
            manufactureDatePicker.setLocalDateTime(product.getManufactureDate());

            measuringExpirationLabel.setDisable(false);
            typeExpirationCBox.setDisable(false);

            if (product.getDateOfExpiration() != null){
                typeExpirationCBox.getSelectionModel().select(ValueOfVisibleExpiration.DATE);
                dateExpirateField.setLocalDateTime(product.getDateOfExpiration());
                dateExpirateField.setVisible(true);
            }
            else{
                typeExpirationCBox.getSelectionModel().select(ValueOfVisibleExpiration.NOTHING);
            }
        }
    }

    private void setFieldNoEdit(){
        nameField.setEditable(false);
        priceSpinner.setEditable(false);
        priceKopeckSpinner.setEditable(false);
        countSpinner.setEditable(false);
        categoryField.setEditable(false);
        summaryField.setEditable(false);
        unitField.setEditable(false);
        typePackagingField.setEditable(false);
        vatSpinner.setEditable(false);
            manufactureDatePicker.setEditable(false);

            measuringExpirationLabel.setDisable(false);
            typeExpirationCBox.setEditable(false);
            dateExpirateField.setEditable(false);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateHoldSpinner.setDisable(true);
        measureCBox.setDisable(true);


        setSpinners();
        setComboBox();
        if (!typeController.equals(("INFO"))){
            vatSpinner.increment(0);
        }
    }

    public void exit(MouseEvent mouseEvent) {
        if (!Objects.isNull(manufactureDatePicker.getText()) & !manufactureDatePicker.getText().trim().equals("")){
            endDateExpirateLabel.setDisable(false);
            dateExpirateField.setDisable(false);

            measuringExpirationLabel.setDisable(false);
            typeExpirationCBox.setDisable(false);

            expirationDateLabel.setDisable(false);
            measureCBox.setDisable(false);
            dateHoldSpinner.setDisable(false);
            unitOfExpirateLabel.setDisable(false);
        }
        else {
            endDateExpirateLabel.setDisable(true);
            dateExpirateField.setDisable(true);

            measuringExpirationLabel.setDisable(true);
            typeExpirationCBox.setDisable(true);

            expirationDateLabel.setDisable(true);
            measureCBox.setDisable(true);
            dateHoldSpinner.setDisable(true);
            unitOfExpirateLabel.setDisable(true);
        }
    }

    private void setSpinners(){
        if (!typeController.equals("INFO")){
            vatSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20,0,10));
            countSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,1,1));
            priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,0,1));
            priceKopeckSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100,0,1));
            dateHoldSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,1,1));
        }
        else {
            vatSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20,0,0));
            countSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,1,0));
            priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,0,0));
            priceKopeckSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100,0,0));
            dateHoldSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,1,0));
        }
        TextFormatter<Integer> formatterVAT = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());

                return change;
            }
            return null;
        });
        vatSpinner.getEditor().setTextFormatter(formatterVAT);

        TextFormatter<Integer> formatterCount = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());

                return change;
            }
            return null;
        });
        countSpinner.getEditor().setTextFormatter(formatterCount);

        TextFormatter<Integer> formatterPrice = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());

                return change;
            }
            return null;
        });
        priceSpinner.getEditor().setTextFormatter(formatterPrice);

        TextFormatter<Integer> formatterKopeck = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());

                return change;
            }
            return null;
        });
        priceKopeckSpinner.getEditor().setTextFormatter(formatterKopeck);

        TextFormatter<Integer> formatterHolder = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());

                return change;
            }
            return null;
        });
        dateHoldSpinner.getEditor().setTextFormatter(formatterHolder);
    }

    private void setComboBox(){
        measureCBox.getItems().setAll(ValueOfExpiration.values());
        measureCBox.setConverter(new StringConverter<ValueOfExpiration>() {
            @Override
            public String toString(ValueOfExpiration status) {
                return status == null ? null : status.toString();
            }
            @Override
            public ValueOfExpiration fromString(String string) {
                return null;
            }
        });

        typeExpirationCBox.getItems().setAll(ValueOfVisibleExpiration.values());

        // Задаём значение по умолчанию
        typeExpirationCBox.getSelectionModel().select(ValueOfVisibleExpiration.NOTHING);
        measureCBox.getSelectionModel().select(ValueOfExpiration.DAY);

        typeExpirationCBox.setConverter(new StringConverter<ValueOfVisibleExpiration>() {
            @Override
            public String toString(ValueOfVisibleExpiration status) {
                return status == null ? null : status.toString();
            }
            @Override
            public ValueOfVisibleExpiration fromString(String string) {
                return null;
            }
        });

        typeExpirationCBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue == ValueOfVisibleExpiration.NOTHING){
                endDateExpirateLabel.setVisible(false);
                dateExpirateField.setVisible(false);

                expirationDateLabel.setVisible(false);
                measureCBox.setVisible(false);
                dateHoldSpinner.setVisible(false);
                unitOfExpirateLabel.setVisible(false);
            }

            if (newValue == ValueOfVisibleExpiration.DATE){
                endDateExpirateLabel.setVisible(true);
                dateExpirateField.setVisible(true);

                expirationDateLabel.setVisible(false);
                measureCBox.setVisible(false);
                dateHoldSpinner.setVisible(false);
                unitOfExpirateLabel.setVisible(false);
            }

            if (newValue == ValueOfVisibleExpiration.DIGITAL){
                endDateExpirateLabel.setVisible(false);
                dateExpirateField.setVisible(false);

                expirationDateLabel.setVisible(true);
                measureCBox.setVisible(true);
                dateHoldSpinner.setVisible(true);
                unitOfExpirateLabel.setVisible(true);
            }
        });
    }
}
