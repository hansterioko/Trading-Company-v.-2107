package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order_list {
    @Expose
    private Integer id;
    @Expose
    private Integer count;
    // Туть нет
    private Order order;
    @Expose
    private Product product;
}
