package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rus.warehouse.trading_company.models.Company;
import rus.warehouse.trading_company.models.UserClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @Expose
    private LocalDateTime date;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal costWithVAT;
    @Expose
    private UserClient userClient;
    @Expose
    private List<PurchaseProduct> productList;
}
