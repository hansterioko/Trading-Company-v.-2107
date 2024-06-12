package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseProduct {
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
    private Integer count;
    @Expose
    private LocalDateTime manufactureDate;
    @Expose
    private LocalDateTime dateOfExpiration;

    // Только с датой выпуска
    public PurchaseProduct(String name, int vat, String category, String characteristic, String unit, BigDecimal price, LocalDateTime manufactureDate, Integer count, String typePackaging) {
        this.name = name;
        this.vat = vat;
        this.category = category;
        this.characteristic = characteristic;
        this.unit = unit;
        this.price = price;
        this.manufactureDate = manufactureDate;
        this.count = count;
        this.typePackaging = typePackaging;
    }

    // с Датой выпуска и сроком годности
    public PurchaseProduct(String name, int vat, String category, String characteristic, String unit, BigDecimal price, LocalDateTime manufactureDate, LocalDateTime dateOfExpiration, Integer count, String typePackaging) {
        this.name = name;
        this.vat = vat;
        this.category = category;
        this.characteristic = characteristic;
        this.unit = unit;
        this.price = price;
        this.manufactureDate = manufactureDate;
        this.dateOfExpiration = dateOfExpiration;
        this.count = count;
        this.typePackaging = typePackaging;
    }

    // Без даты выпуска и срока годности
    public PurchaseProduct(String name, int vat, String category, String characteristic, String unit, BigDecimal price, Integer count, String typePackaging) {
        this.name = name;
        this.vat = vat;
        this.category = category;
        this.characteristic = characteristic;
        this.unit = unit;
        this.price = price;
        this.count = count;
        this.typePackaging = typePackaging;
    }
}
