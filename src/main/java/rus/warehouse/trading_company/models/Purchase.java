package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    @Expose
    private Integer id;
    @Expose
    private String date;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal costWithVAT;
    @Expose
    private String company;
}
