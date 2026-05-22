package model;
import javax.persistence.*;
import java.io.Serializable;
@Entity @Table(name="clients")
public class Client implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    private String names, email, phone, password, role;
    public Client() {}
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getNames() { return names; } public void setNames(String names) { this.names = names; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; } public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; } public void setRole(String role) { this.role = role; }
    public String toString() { return names; }
}
