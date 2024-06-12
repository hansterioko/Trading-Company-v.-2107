package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import rus.warehouse.trading_company.models.Company;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetPurchaseDTO {
    @Expose
    private Integer id;
    @Expose
    private LocalDateTime date;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal costWithVAT;
    @Expose
    private Company company;
}
