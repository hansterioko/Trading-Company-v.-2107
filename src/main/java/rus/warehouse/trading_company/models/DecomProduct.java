package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecomProduct {
    @Expose
    private int id;
    @Expose
    private Product product;
    @Expose
    private String comment;
    @Expose
    private Integer decCount;
    @Expose
    private LocalDateTime date;
}
