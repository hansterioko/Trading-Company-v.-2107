package rus.warehouse.trading_company.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rus.warehouse.trading_company.RunApplication;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddPurchaseController implements Initializable {
    @FXML
    private Button deleteToolBtn;

    @FXML
    private Button editToolBtn;

    @FXML
    private Button infoToolBtn;

    private String idCompany;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        infoToolBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/toolbar/info.png").toExternalForm()));
        infoToolBtn.setOnMouseEntered(event -> {
            infoToolBtn.setTooltip(new Tooltip("Информация о товаре"));
        });
        deleteToolBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/toolbar/erase.png").toExternalForm()));
        deleteToolBtn.setOnMouseEntered(event -> {
            deleteToolBtn.setTooltip(new Tooltip("Удалить товар"));
        });
        editToolBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/toolbar/edit.png").toExternalForm()));
        editToolBtn.setOnMouseEntered(event -> {
            editToolBtn.setTooltip(new Tooltip("Редактирование товара"));
        });
    }

    public void setIdCompany(String idCompany) {
        this.idCompany = idCompany;
    }

    public void selectCompany(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("selectPurchaseCompany-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            SelectPurchaseCompanyController selectPurchaseCompanyController = new SelectPurchaseCompanyController(this);
            fxmlLoader.setController(selectPurchaseCompanyController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Выбор поставщика");
            //stage.getIcons().add(new Image(RunApplication.class.getResource("images/providerBtn.png").toString()));
            stage.setScene(scene);
            stage.showAndWait();

            //getPurchase();
            //System.out.println(listProviders + "КОНТРОЛЛЕР ПУРЧ");

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно выбора поставщика", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора поставщика!" + e.toString());
        }
    }

    public void addProduct(ActionEvent actionEvent) {   // Куда??? в Обзервейбл???
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-product-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Добавление товара");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно добавления товара", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна добавления товара! " + e.toString());
        }
    }

    public void backBtn(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("purchase-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(RunApplication.class.getResource("css/purchase.css").toExternalForm());
            stage.setTitle("Управление закупками");
            stage.setScene(scene);
            stage.show();

            Stage stageOld = (Stage) deleteToolBtn.getScene().getWindow();
            stageOld.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно закупок", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна закупок! " + e.toString());
        }
    }
}
