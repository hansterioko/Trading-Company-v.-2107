package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import lombok.Data;
import rus.warehouse.trading_company.models.Purchase;
import rus.warehouse.trading_company.models.Purchase_list;

import java.util.List;


@Data
public class Purchase_listDTO {
    @Expose
    private List<Purchase_list> prodLists;

    @Expose
    private PurchaseDTO purchase;
}
