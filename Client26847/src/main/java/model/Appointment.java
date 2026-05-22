package model;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity @Table(name="appointments")
public class Appointment implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
    private String apptDate, apptTime, status;
    @ManyToOne @JoinColumn(name="client_id") private Client client;
    @ManyToOne @JoinColumn(name="staff_id") private Staff staff;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="appt_services", joinColumns=@JoinColumn(name="appointment_id"), inverseJoinColumns=@JoinColumn(name="service_id"))
    private List<SalonService> services;
    public Appointment() {}
    public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
    public String getApptDate() { return apptDate; } public void setApptDate(String apptDate) { this.apptDate = apptDate; }
    public String getApptTime() { return apptTime; } public void setApptTime(String apptTime) { this.apptTime = apptTime; }
    public String getStatus() { return status; } public void setStatus(String status) { this.status = status; }
    public Client getClient() { return client; } public void setClient(Client client) { this.client = client; }
    public Staff getStaff() { return staff; } public void setStaff(Staff staff) { this.staff = staff; }
    public List<SalonService> getServices() { return services; } public void setServices(List<SalonService> services) { this.services = services; }
}
