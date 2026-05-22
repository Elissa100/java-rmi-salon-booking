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

    public SalonService updateService(SalonService s) throws RemoteException {
        if(s.getPrice() <= 0) throw new RemoteException("Price must be > 0");
        return serviceDao.save(s);
    }

    public void deleteService(Integer id) throws RemoteException {
        try { serviceDao.delete(id); }
        catch(Exception e) { throw new RemoteException("Cannot delete! Service is linked to an appointment."); }
    }

    public List<Staff> getStaff() throws RemoteException { return staffDao.findAll(); }

    public Staff saveStaff(Staff s) throws RemoteException { return staffDao.save(s); }

    public Staff updateStaff(Staff s) throws RemoteException { return staffDao.save(s); }

    public void deleteStaff(Integer id) throws RemoteException {
        try { staffDao.delete(id); }
        catch(Exception e) { throw new RemoteException("Cannot delete! Staff is linked to an appointment."); }
    }

    public List<Appointment> getAppointments() throws RemoteException {
        List<Appointment> list = apptDao.findAll();
        for(Appointment a : list) {
            if(a.getServices() != null) a.setServices(new ArrayList<>(a.getServices()));
        }
        return list;
    }

    public Appointment bookAppointment(Appointment a) throws RemoteException {
        if(a.getApptDate().isEmpty()) throw new RemoteException("Date cannot be empty");
        if(a.getServices() == null || a.getServices().isEmpty()) throw new RemoteException("Select a service");

        // convert appointment time to minutes
        int newStart = parseTimeToMinutes(a.getApptTime());
        if (newStart == -1) throw new RemoteException("Invalid time format. Please use HH:MM");

        //calculate appointment duration
        int duration = 0;
        for (SalonService s : a.getServices()) {
            duration += (s.getDurationMins() != null) ? s.getDurationMins() : 30; // default 30 mins fallback
        }
        int newEnd = newStart + duration;

        // Check for overlaps with existing appointments on the same date for the same staff member
        for (Appointment ex : apptDao.findAll()) {
            if (ex.getStaff().getId().equals(a.getStaff().getId()) && ex.getApptDate().equals(a.getApptDate())) {
                int exStart = parseTimeToMinutes(ex.getApptTime());
                if (exStart == -1) continue;

                int exDuration = 0;
                if (ex.getServices() != null) {
                    for (SalonService s : ex.getServices()) {
                        exDuration += (s.getDurationMins() != null) ? s.getDurationMins() : 30;
                    }
                }
                int exEnd = exStart + exDuration;

                // overlap formula
                if (newStart < exEnd && exStart < newEnd) {
                    throw new RemoteException("Staff is already busy during this time!\n" +
                            "They have an appointment from " + ex.getApptTime() + " to " + formatMinutesToTime(exEnd));
                }
            }
        }

        a.setStatus("CONFIRMED");
        Appointment saved = apptDao.save(a);
        if(saved.getServices() != null) saved.setServices(new ArrayList<>(saved.getServices()));
        return saved;
    }

    // turn HH:MM format to total minutes
    private int parseTimeToMinutes(String timeStr) {
        try {
            String[] parts = timeStr.trim().split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            return hours * 60 + minutes;
        } catch (Exception e) {
            return -1;
        }
    }

    //convert total minutes to HH:MM format
    private String formatMinutesToTime(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}