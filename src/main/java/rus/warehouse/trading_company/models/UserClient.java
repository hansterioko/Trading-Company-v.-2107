package rus.warehouse.trading_company.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserClient {
    private int id;
    private String name;
    private String city;
    private String street;
    private String house;
    private String phone;
}
