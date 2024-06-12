package rus.warehouse.trading_company.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import rus.warehouse.trading_company.RunApplication;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.Product;
import rus.warehouse.trading_company.modelsDTO.PurchaseDTO;
import rus.warehouse.trading_company.modelsDTO.PurchaseProduct;
import rus.warehouse.trading_company.repositories.PurchaseRepositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.*;

public class AddPurchaseController implements Initializable {
    @FXML
    private Button deleteToolBtn;

    @FXML
    private Button editToolBtn;

    @FXML
    private Button infoToolBtn;

    @FXML
    private Button cloneToolBtn;

    @Getter
    private Company company = null;

//  Таблица и колонки
    @FXML
    private TableColumn<PurchaseProduct, Integer> idColumn;

    @FXML
    private TableColumn<PurchaseProduct, String> nameColumn;

    @FXML
    private TableColumn<PurchaseProduct, BigDecimal> priceColumn;

    @FXML
    private TableColumn<PurchaseProduct, String> categoryColumn;

    @FXML
    private TableColumn<PurchaseProduct, Integer> countColumn;

    @FXML
    private TableView<PurchaseProduct> productTable;
    //--------------------------------------------------------

    private ObservableList<PurchaseProduct> observableList = FXCollections.observableArrayList();

    //
    @Setter @Getter
    private LocalDateTime datePurchase = null;
    @Setter @Getter
    private boolean startCreate = false;

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
        cloneToolBtn.graphicProperty().setValue(new ImageView(RunApplication.class.getResource("images/toolbar/clone-icon.png").toExternalForm()));
        cloneToolBtn.setOnMouseEntered(event -> {
            cloneToolBtn.setTooltip(new Tooltip("Клонирование товара"));
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<PurchaseProduct, String>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<PurchaseProduct, String>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<PurchaseProduct, BigDecimal>("price"));
        idColumn.setCellValueFactory(new PropertyValueFactory<PurchaseProduct, Integer>("id"));
        countColumn.setCellValueFactory(new PropertyValueFactory<PurchaseProduct, Integer>("count"));

        productTable.setItems(observableList);
    }

    public void setIdCompany(Company company) {
        this.company = company;
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
            stage.initModality(Modality.APPLICATION_MODAL);
            AddProductController addProductController = new AddProductController(this, "ADD");
            fxmlLoader.setController(addProductController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Добавление товара");
            stage.setScene(scene);
            stage.showAndWait();

            productTable.setItems(observableList);
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


    @FXML
    void cloneSelectProduct(ActionEvent event) {
        if (productTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Никакой товар не клонирован", ButtonType.OK);
            alert.setTitle("Ошибка клонирования");
            alert.setHeaderText("Выберите товар!");
            alert.show();
        }
        else{
            observableList.add(productTable.getSelectionModel().getSelectedItem());
            productTable.setItems(observableList);
        }
    }

    public void addNewProduct(String type, PurchaseProduct product){
        System.out.println(type + " and " + product);
        if (type.equals("EDIT")){
            int index = productTable.getSelectionModel().getSelectedIndex();
            observableList.remove(index);
            observableList.add(index, product);
        }
        else{
            observableList.add(product);
        }
    }

    @FXML
    void deleteSelectProduct(ActionEvent event) {
        if (productTable.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Никакой товар не был удалён", ButtonType.OK);
            alert.setTitle("Ошибка удаления");
            alert.setHeaderText("Выберите товар!");
            alert.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Удаление \"" + productTable.getSelectionModel().getSelectedItem().getName() + "\"");
            alert.setTitle("Удаление товара");
            alert.setHeaderText("Вы действительно хотите удалить выбранный товар?");
            ButtonType yesTypeBtn = new ButtonType("Да");
            ButtonType noTypeBtn = new ButtonType("Нет");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(yesTypeBtn, noTypeBtn);

            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == yesTypeBtn){
                productTable.getItems().remove(
                        productTable.getSelectionModel().getSelectedItem()
                );
            }

            productTable.setItems(observableList);
        }
    }

    @FXML
    void readFromFile(ActionEvent event) {
        Stage stage = (Stage) deleteToolBtn.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выбор накладной");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Exel", "*.xls*")
        );
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println(file.getAbsolutePath());

            try {
                FileInputStream fileInputStream = new FileInputStream(file);

                Workbook workbook = WorkbookFactory.create(fileInputStream);
                Sheet sheet = workbook.getSheetAt(0);

                Iterator<Row> rowIterator = sheet.rowIterator();

                boolean readProduct = false;
                Integer numbProdColumn = 0;

                PurchaseProduct product = new PurchaseProduct();



                rowIterator = sheet.rowIterator();

                while (rowIterator.hasNext()){

                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    if (readProduct){
                        product = new PurchaseProduct();
                    }
                    while (cellIterator.hasNext()){
                        Cell cell = cellIterator.next();

                        //System.out.println(cell.getCellType().toString());
                        switch (cell.getCellType()){
//                            case BLANK:
//                                if(readProduct){
//                                    cell.setCellValue("-");
//                                    System.out.println(cell.getStringCellValue());
//                                }

                            case NUMERIC:
                                if (readProduct){
                                   //System.out.println(cell.getNumericCellValue() + "  " + numbProdColumn);
                                    switch (numbProdColumn){
                                        case 0:
                                            numbProdColumn++;
                                            break;
                                        case 1:
                                            product.setName(String.valueOf(cell.getNumericCellValue()));
                                            numbProdColumn++;
                                            break;
                                        case 2:
                                            product.setCharacteristic("Код: "+ String.valueOf(cell.getNumericCellValue()));
                                            numbProdColumn++;
                                            break;
                                        case 3:
                                            product.setUnit(String.valueOf(cell.getNumericCellValue()));
                                            numbProdColumn++;
                                            break;
                                        case 4:
                                            numbProdColumn++;
                                            break;
                                        case 5:
                                            product.setTypePackaging(String.valueOf(cell.getNumericCellValue()));
                                            numbProdColumn++;
                                            break;
                                        case 6:
                                            product.setTypePackaging(product.getTypePackaging() + " по " + String.format("%.0f", cell.getNumericCellValue()));
                                            numbProdColumn++;
                                            break;
                                        case 7:
                                            numbProdColumn++;
                                            break;
                                        case 8:
                                            numbProdColumn++;
                                            break;
                                        case 9:
                                            product.setCount((int) cell.getNumericCellValue());
                                            numbProdColumn++;
                                            break;
                                        case 10:
                                            product.setPrice(BigDecimal.valueOf(cell.getNumericCellValue()));
                                            numbProdColumn++;
                                            break;
                                        case 11:
                                            numbProdColumn++;
                                            break;
                                        case 12:
                                            product.setVat((int) cell.getNumericCellValue());
                                            numbProdColumn++;
                                            break;
                                        case 13:
                                            numbProdColumn++;
                                            break;
                                        case 14:
                                            numbProdColumn = 0;
                                            System.out.println(product);
                                            observableList.add(product);
                                            break;
                                    }
                                }
                                break;

                                case STRING:
                                    //System.out.println(cell.getStringCellValue());
                                    if (cell.getStringCellValue().trim().equals("Итого")){
                                        readProduct = false;
                                    }
                                    if (readProduct) {
                                        System.out.println(cell.getStringCellValue() + " " + numbProdColumn);
                                        switch (numbProdColumn) {
                                            case 0:
                                                numbProdColumn++;
                                                break;
                                            case 1:
                                                if (!cell.getStringCellValue().equals("-")){
                                                    product.setName(cell.getStringCellValue());
                                                }
                                                numbProdColumn++;
                                                break;
                                            case 2:
                                                if (!cell.getStringCellValue().equals("-")){
                                                    product.setCharacteristic("Код: " + cell.getStringCellValue());
                                                }
                                                numbProdColumn++;
                                                break;
                                            case 3:
                                                if (!cell.getStringCellValue().equals("-")){
                                                    product.setUnit(cell.getStringCellValue());
                                                }
                                                numbProdColumn++;
                                                break;
                                            case 4:
                                                numbProdColumn++;
                                                break;
                                            case 5:
                                                if (!cell.getStringCellValue().equals("-")){
                                                    product.setTypePackaging(cell.getStringCellValue());
                                                }
                                                numbProdColumn++;
                                                break;
                                            case 6:
                                                if (!cell.getStringCellValue().equals("-")){
                                                    product.setTypePackaging(" по " + cell.getStringCellValue());
                                                }
                                                numbProdColumn++;
                                                break;
                                            case 7:
                                                numbProdColumn++;
                                                break;
                                            case 8:
                                                numbProdColumn++;
                                                break;
                                            case 9:
                                                numbProdColumn++;
                                                break;
                                            case 10:
                                                numbProdColumn++;
                                                break;
                                            case 11:
                                                numbProdColumn++;
                                                break;
                                            case 12:
                                                numbProdColumn++;
                                                break;
                                            case 13:
                                                numbProdColumn++;
                                                break;
                                            case 14:
                                                numbProdColumn = 0;
                                                System.out.println(product);
                                                observableList.add(product);
                                                break;
                                        }
                                    }
                                    if (cell.getStringCellValue().trim().equals("сумма,\n" +
                                            "руб. коп.")){
                                        System.out.println("TUTTTT");
                                        readProduct = true;
                                    }
                                    break;
                            }
                    }
                    productTable.setItems(observableList);
                }

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Убедитесь в правильности выбора файла", ButtonType.OK);
                alert.setTitle("Чтение накладной");
                alert.setHeaderText("Файл не читается!");
                alert.show();

                throw new RuntimeException(e.toString());
            }
        }
    }

    @FXML
    void editSelectedProduct(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-product-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            AddProductController addProductController = new AddProductController(this, "EDIT", productTable.getSelectionModel().getSelectedItem());
            fxmlLoader.setController(addProductController);
            stage.setTitle("Редактирование товаре");
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.showAndWait();

            productTable.setItems(observableList);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно редактирования товара", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна редактирования товара! " + e.toString());
        }
    }

    @FXML
    void infoAboutSelectProduct(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add-product-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            AddProductController addProductController = new AddProductController(this, "INFO", productTable.getSelectionModel().getSelectedItem());
            fxmlLoader.setController(addProductController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Информация о товаре");
            stage.setScene(scene);
            stage.showAndWait();

            productTable.setItems(observableList);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно информации о товаре", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна информации о товаре! " + e.toString());
        }
    }

    @FXML
    void createPurchase(ActionEvent event) {
        if (company == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось создать закупку", ButtonType.OK);
            alert.setTitle("Ошибка создания закупки");
            alert.setHeaderText("Выберите компанию-поставщика!");
            alert.show();
            return;
        }
        if(observableList.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось создать закупку", ButtonType.OK);
            alert.setTitle("Ошибка создания закупки");
            alert.setHeaderText("Отсутствуют товары в закупке!");
            alert.show();
            return;
        }
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("select-date-view.fxml"));
            stage.initModality(Modality.APPLICATION_MODAL);
            SelectDateController selectDateController = new SelectDateController(this);
            fxmlLoader.setController(selectDateController);
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Выбор даты закупки");
            stage.setScene(scene);
            stage.showAndWait();

            if (startCreate){
                BigDecimal cost = new BigDecimal("0.00");
                BigDecimal costWithVAT = new BigDecimal("0.00");
                List <PurchaseProduct> productList = new ArrayList<>();
                PurchaseDTO purchaseDTO = new PurchaseDTO();
                for (PurchaseProduct prod:
                     observableList) {
                     cost = cost.add(prod.getPrice().multiply(BigDecimal.valueOf(prod.getCount())));
                        costWithVAT = costWithVAT.add(prod.getPrice().multiply(BigDecimal.valueOf(prod.getCount())).multiply(BigDecimal.valueOf(100 - prod.getVat())).divide(BigDecimal.valueOf(100)));
                        productList.add(prod);
                }

                purchaseDTO.setDate(datePurchase);
                purchaseDTO.setPrice(cost);
                purchaseDTO.setProductList(productList);
                purchaseDTO.setCostWithVAT(costWithVAT);
                purchaseDTO.setCompany(company);

                PurchaseRepositories.createPurchase(purchaseDTO);

                try {
                     stage = new Stage();
                     fxmlLoader = new FXMLLoader(RunApplication.class.getResource("purchase-view.fxml"));
                     scene = new Scene(fxmlLoader.load());
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

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось открыть окно выбора даты", ButtonType.OK);
            alert.setTitle("Ошибка подключения");
            alert.setHeaderText("Проверьте подключение к интернету!");
            alert.show();
            System.out.println(LocalTime.now() + "   Ошибка открытия окна выбора даты! " + e.toString());
        }
    }
}
