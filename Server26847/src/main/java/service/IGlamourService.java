package service;
import model.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
public interface IGlamourService extends Remote {
    Client login(String email, String password) throws RemoteException;
    String generateOtp(String email) throws RemoteException;
    Client register(Client c) throws RemoteException;
    List<SalonService> getServices() throws RemoteException;
    SalonService saveService(SalonService s) throws RemoteException;
    List<Staff> getStaff() throws RemoteException;
    Staff saveStaff(Staff s) throws RemoteException;
    List<Appointment> getAppointments() throws RemoteException;
    Appointment bookAppointment(Appointment a) throws RemoteException;
}
