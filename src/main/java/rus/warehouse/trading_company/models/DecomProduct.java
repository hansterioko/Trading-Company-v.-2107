package rus.warehouse.trading_company.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecomProduct {
    private int id;
    private Product product;
    private String comment;
    private Integer decCount;
    private LocalDateTime date;
}
