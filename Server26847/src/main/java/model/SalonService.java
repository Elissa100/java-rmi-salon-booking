package model;
import javax.persistence.*;
import java.io.Serializable;
@Entity @Table(name="services")
public class SalonService implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    private String name; private Double price; private Integer durationMins;
    public SalonService() {}
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; } public void setPrice(Double price) { this.price = price; }
    public Integer getDurationMins() { return durationMins; } public void setDurationMins(Integer durationMins) { this.durationMins = durationMins; }
    public String toString() { return name + " ($" + price + ")"; }
}
