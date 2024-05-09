package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import rus.warehouse.trading_company.models.Purchase;

import java.util.List;

@Data
public class PagedDataDTO<T> {
    @Expose
    private List<T> data;

    @Expose
    private Integer total;
}
