package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javafx.scene.control.CheckBox;

@Data
public class Company {
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

    public Company(Integer id, String name, String city, String street, String house, String phone) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.house = house;
        this.phone = phone;
        this.checkbox = new CheckBox();
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house='" + house + '\'' +
                ", phone='" + phone + '\'' +
                ", checkbox=" + checkbox.isSelected() +
                '}';
    }
}
