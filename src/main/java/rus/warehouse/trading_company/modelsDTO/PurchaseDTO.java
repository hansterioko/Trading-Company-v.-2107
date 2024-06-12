package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rus.warehouse.trading_company.models.Company;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDTO {
    @Expose
    private LocalDateTime date;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal costWithVAT;
    @Expose
    private Company company;
    @Expose
    private List<PurchaseProduct> productList;
}
