package rus.warehouse.trading_company.modelsDTO;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rus.warehouse.trading_company.models.Product;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecomProductDTO {
    @Expose
    private Integer idProduct;
    @Expose
    private String comment;
    @Expose
    private Integer decCount;
}
