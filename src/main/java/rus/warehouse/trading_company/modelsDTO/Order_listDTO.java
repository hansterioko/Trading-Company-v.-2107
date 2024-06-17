package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rus.warehouse.trading_company.models.Order;
import rus.warehouse.trading_company.models.Order_list;
import rus.warehouse.trading_company.models.Purchase_list;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order_listDTO {
    @Expose
    private List<Order_list> prodLists;

    @Expose
    private Order order;
}
