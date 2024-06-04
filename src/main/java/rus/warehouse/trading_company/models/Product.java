package rus.warehouse.trading_company.models;

import javafx.scene.image.Image;
import lombok.Data;
import rus.warehouse.trading_company.helpers.ValueOfExpiration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private int id;
    private String name;
    private int vat;
    private String category;
    private String typePackaging;
    private String characteristic;
    private String unit;
    private Integer countOnWarehouse;
    private LocalDateTime manufactureDate;
    private LocalDateTime dateOfExpiration;
}
