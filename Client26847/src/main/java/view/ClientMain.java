package view;
import service.IGlamourService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class ClientMain {
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 5000);
            IGlamourService service = (IGlamourService) reg.lookup("glamour-service");
            new LoginForm(service).setVisible(true);
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Cannot connect to server. Is it running?");
        }
    }
}
