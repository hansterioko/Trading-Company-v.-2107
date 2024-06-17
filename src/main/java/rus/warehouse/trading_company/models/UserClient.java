package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import javafx.scene.control.CheckBox;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserClient {
    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private String city;
    @Expose
    private String street;
    @Expose
    private String house;
    @Expose
    private String phone;
    @Expose(serialize = false, deserialize = false)
    private CheckBox checkbox;

    public UserClient(Integer id, String name, String city, String street, String house, String phone) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.house = house;
        this.phone = phone;
        checkbox = new CheckBox();
    }
}
