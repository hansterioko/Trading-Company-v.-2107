package rus.warehouse.trading_company.modelsDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class PurchaseProduct {
    private String name;
    private int vat;
    private String category;
    private String typePackaging;
    private String characteristic;
    private String unit;
    private BigDecimal price;
    private Integer count;
    private LocalDateTime manufactureDate;
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
