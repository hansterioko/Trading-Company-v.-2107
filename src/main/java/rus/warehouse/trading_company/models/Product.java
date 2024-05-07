package rus.warehouse.trading_company.models;

import javafx.scene.image.Image;
import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private int vat;
    private String category;
    private String characteristic;
    private String Unit;
    private Integer price;
    private Integer countOnWarehouse;
    private Image photo;
}
