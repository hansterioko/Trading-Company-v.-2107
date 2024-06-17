package rus.warehouse.trading_company.FXModels;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductFX {
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
    private Integer count;
    @Expose
    private Integer countOnWarehouse;
    @Expose
    private LocalDateTime manufactureDate;
    @Expose
    private LocalDateTime dateOfExpiration;

//    public OrderProductFX(Integer id, String name, Integer vat, String category, String typePackaging, String characteristic, String unit, BigDecimal price, Integer count, Integer countOnWarehouse, LocalDateTime dateOfManufacture, LocalDateTime dateExpiration) {
//        this.id = id;
//        this.name = name;
//        this.vat = vat;
//        this.typePackaging = typePackaging;
//        this.category = category;
//        this.characteristic = characteristic;
//        this.unit = unit;
//        this.count = count;
//        this.countOnWarehouse = countOnWarehouse;
//        this.dateOfExpiration = dateExpiration;
//        this.manufactureDate = dateOfManufacture;
//    }
}
