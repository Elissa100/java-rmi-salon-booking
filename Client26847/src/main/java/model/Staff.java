package model;
import javax.persistence.*;
import java.io.Serializable;
@Entity @Table(name="staffs")
public class Staff implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    private String names, role;
    @OneToOne(cascade = CascadeType.ALL) @JoinColumn(name="profile_id") private StaffProfile profile;
    public Staff() {}
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getNames() { return names; } public void setNames(String names) { this.names = names; }
    public String getRole() { return role; } public void setRole(String role) { this.role = role; }
    public StaffProfile getProfile() { return profile; } public void setProfile(StaffProfile profile) { this.profile = profile; }
    public String toString() { return names + " (" + role + ")"; }
}
