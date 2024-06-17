package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Expose
    private Integer id;
    @Expose
    private BigDecimal price;
    @Expose
    private BigDecimal costWithVat;
    @Expose
    private UserClient userclient;
    @Expose
    private LocalDateTime date;
}
