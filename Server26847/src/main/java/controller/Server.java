package controller;
import dao.GenericDao;
import model.Client;
import service.GlamourServiceImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Server {
    public static void main(String[] args) {
        try {
            GenericDao<Client> dao = new GenericDao<>(Client.class);
            if(dao.findAll().isEmpty()) {
                Client admin = new Client();
                admin.setNames("System Admin"); admin.setEmail("admin@glamour.com");
                admin.setPassword("admin123"); admin.setRole("ADMIN");
                dao.save(admin);
            }
            
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");
            Registry reg = LocateRegistry.createRegistry(5000);
            reg.rebind("glamour-service", new GlamourServiceImpl());
            System.out.println("==============================================");
            System.out.println("  Glamour Salon Server (26847) Running");
            System.out.println("  Port: 5000 | Services Registered!");
            System.out.println("==============================================");
        } catch(Exception e) { e.printStackTrace(); }
    }
}
