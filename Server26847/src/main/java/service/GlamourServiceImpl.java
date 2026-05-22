package service;
import dao.GenericDao;
import model.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class GlamourServiceImpl extends UnicastRemoteObject implements IGlamourService {
    private GenericDao<Client> clientDao = new GenericDao<>(Client.class);
    private GenericDao<SalonService> serviceDao = new GenericDao<>(SalonService.class);
    private GenericDao<Staff> staffDao = new GenericDao<>(Staff.class);
    private GenericDao<Appointment> apptDao = new GenericDao<>(Appointment.class);

    public GlamourServiceImpl() throws RemoteException { super(); }

    public Client login(String email, String password) throws RemoteException {
        if(email == null || password == null) return null;
        for(Client c : clientDao.findAll()) {
            if(c.getEmail().equals(email) && c.getPassword().equals(password)) return c;
        }
        return null;
    }

    public String generateOtp(String email) throws RemoteException {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        System.out.println("\n*** OTP Notification ***");
        System.out.println("Email: " + email + " | Code: " + otp);
        System.out.println("************************\n");
        return otp;
    }

    public Client register(Client c) throws RemoteException {
        if(!c.getEmail().contains("@")) throw new RemoteException("Invalid email format");
        if(c.getPassword().length() < 4) throw new RemoteException("Password too short");
        for(Client ex : clientDao.findAll()) {
            if(ex.getEmail().equals(c.getEmail())) throw new RemoteException("Email already exists");
        }
        return clientDao.save(c);
    }

    public List<SalonService> getServices() throws RemoteException { return serviceDao.findAll(); }

    public SalonService saveService(SalonService s) throws RemoteException {
        if(s.getPrice() <= 0) throw new RemoteException("Price must be > 0");
        return serviceDao.save(s);
    }

    public List<Staff> getStaff() throws RemoteException { return staffDao.findAll(); }

    public Staff saveStaff(Staff s) throws RemoteException { return staffDao.save(s); }

    public List<Appointment> getAppointments() throws RemoteException {
        List<Appointment> list = apptDao.findAll();
        // FIX: Convert Hibernate PersistentBag to Standard Java ArrayList for RMI Client
        for(Appointment a : list) {
            if(a.getServices() != null) {
                a.setServices(new ArrayList<>(a.getServices()));
            }
        }
        return list;
    }

    public Appointment bookAppointment(Appointment a) throws RemoteException {
        if(a.getApptDate().isEmpty()) throw new RemoteException("Date cannot be empty");
        if(a.getServices() == null || a.getServices().isEmpty()) throw new RemoteException("Select a service");
        a.setStatus("CONFIRMED");
        for(Appointment ex : apptDao.findAll()) {
            if(ex.getStaff().getId().equals(a.getStaff().getId()) && ex.getApptDate().equals(a.getApptDate()) && ex.getApptTime().equals(a.getApptTime())) {
                throw new RemoteException("Staff is already booked at this specific time!");
            }
        }

        Appointment saved = apptDao.save(a);

        // FIX: Convert Hibernate PersistentBag to Standard Java ArrayList before sending back
        if(saved.getServices() != null) {
            saved.setServices(new ArrayList<>(saved.getServices()));
        }

        return saved;
    }
}