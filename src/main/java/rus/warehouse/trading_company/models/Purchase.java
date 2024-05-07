package rus.warehouse.trading_company.models;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Purchase {
    private Integer id;
    private LocalDateTime date;
    private Integer price;
    private String company;
    private Button button;

    public Purchase(Integer id, LocalDateTime date, Integer price, String company) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.company = company;
        this.button = new Button("Подробнее");
    }
}
