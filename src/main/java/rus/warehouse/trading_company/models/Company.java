package rus.warehouse.trading_company.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;

import javafx.scene.control.CheckBox;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(city, company.city) && Objects.equals(street, company.street) && Objects.equals(house, company.house) && Objects.equals(phone, company.phone) && Objects.equals(checkbox, company.checkbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, street, house, phone, checkbox);
    }
}
