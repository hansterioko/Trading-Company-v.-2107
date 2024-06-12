package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import rus.warehouse.trading_company.models.Order;
import rus.warehouse.trading_company.models.Order_list;
import rus.warehouse.trading_company.models.Purchase_list;

import java.util.List;

public class Order_listDTO {
    @Expose
    private List<Order_list> prodLists;

    @Expose
    private Order order;
}
