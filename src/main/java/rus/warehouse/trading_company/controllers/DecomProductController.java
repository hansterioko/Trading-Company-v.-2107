package rus.warehouse.trading_company.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rus.warehouse.trading_company.repositories.DecomProductRepository;

import java.net.URL;
import java.util.ResourceBundle;

public class DecomProductController implements Initializable {

    @FXML
    private Label countWareLabel;

    @FXML
    private Spinner<Integer> decomCountSpinner;

    @FXML
    private TextArea summaryField;

    private Integer countonWarehouse;

    Integer idProd;

    public DecomProductController(Integer countonWarehouse, Integer idProd) {
        this.countonWarehouse = countonWarehouse;
        this.idProd = idProd;
        countWareLabel.setText("Кол-во на складе: " + countonWarehouse);
    }

    @FXML
    void addDecom(ActionEvent event) {
        if (decomCountSpinner.getValue() != 0){
            DecomProductRepository.addDecomProd(idProd, decomCountSpinner.getValue(), summaryField.getText());
        }

        Stage stageOld = (Stage) countWareLabel.getScene().getWindow();
        stageOld.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decomCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, countonWarehouse,1,1));
        TextFormatter<Integer> formatterVAT = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());

                return change;
            }
            return null;
        });
        decomCountSpinner.getEditor().setTextFormatter(formatterVAT);
    }
}
