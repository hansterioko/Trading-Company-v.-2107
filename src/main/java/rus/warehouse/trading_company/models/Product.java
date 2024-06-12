package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import javafx.scene.image.Image;
import lombok.Data;
import rus.warehouse.trading_company.helpers.ValueOfExpiration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private int vat;
    @Expose
    private String category;
    @Expose
    private String typePackaging;
    @Expose
    private String characteristic;
    @Expose
    private String unit;
    @Expose
    private BigDecimal price;
    @Expose
    private Integer countOnWarehouse;
    @Expose
    private LocalDateTime dateOfManufacture;
    @Expose
    private LocalDateTime dateExpiration;
}
