package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase_list {
    @Expose
    private Integer id;
    @Expose
    private Product product;
    private Purchase purchase;
    @Expose
    private Integer count;
}
