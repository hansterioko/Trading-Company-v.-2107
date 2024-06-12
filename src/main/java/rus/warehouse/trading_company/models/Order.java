package rus.warehouse.trading_company.models;

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
    private Integer id;
    private BigDecimal price;
    private BigDecimal costWithVat;
    private UserClient userclient;
    private LocalDateTime date;
}
