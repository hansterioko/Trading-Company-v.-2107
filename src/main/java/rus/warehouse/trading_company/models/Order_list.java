package rus.warehouse.trading_company.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order_list {
    private Integer id;
    private Integer count;
    private Order order;
    private Product product;
}
