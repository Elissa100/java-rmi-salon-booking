package model;
import javax.persistence.*;
import java.io.Serializable;
@Entity @Table(name="staff_profiles")
public class StaffProfile implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    private String phone, specialty;
    public StaffProfile() {}
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialty() { return specialty; } public void setSpecialty(String specialty) { this.specialty = specialty; }
}
