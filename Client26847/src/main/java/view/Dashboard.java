package view;
import model.*;
import service.IGlamourService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends JFrame {
    private IGlamourService service;
    private Client currentUser;
    
    public Dashboard(Client user, IGlamourService service) {
        this.currentUser = user; this.service = service;
        setTitle("Glamour Salon Dashboard (" + user.getRole() + ") - 26847");
        setSize(800, 600); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JTabbedPane tabs = new JTabbedPane();
        if("ADMIN".equals(user.getRole())) {
            tabs.addTab("Manage Services", createServicesPanel());
            tabs.addTab("Manage Staff", createStaffPanel());
            tabs.addTab("All Appointments & Reports", createApptAdminPanel());
        } else {
            tabs.addTab("Book Appointment", createBookingPanel());
        }
        add(tabs);
    }
    
    private JPanel createServicesPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new Object[]{"ID", "Name", "Price ($)", "Duration (mins)"}, 0);
        try {
            for(SalonService s : service.getServices()) tm.addRow(new Object[]{s.getId(), s.getName(), s.getPrice(), s.getDurationMins()});
        } catch(Exception ignored) {}
        
        JTable table = new JTable(tm);
        JButton addBtn = new JButton("Add Service");
        addBtn.addActionListener(e -> {
            try {
                SalonService s = new SalonService();
                s.setName(JOptionPane.showInputDialog("Service Name:"));
                s.setPrice(Double.parseDouble(JOptionPane.showInputDialog("Price ($):")));
                s.setDurationMins(Integer.parseInt(JOptionPane.showInputDialog("Duration (mins):")));
                s = service.saveService(s);
                tm.addRow(new Object[]{s.getId(), s.getName(), s.getPrice(), s.getDurationMins()});
                JOptionPane.showMessageDialog(this, "Service Added!");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        p.add(new JScrollPane(table), BorderLayout.CENTER); p.add(addBtn, BorderLayout.SOUTH);
        return p;
    }
    
    private JPanel createStaffPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new Object[]{"ID", "Name", "Role"}, 0);
        try {
            for(Staff s : service.getStaff()) tm.addRow(new Object[]{s.getId(), s.getNames(), s.getRole()});
        } catch(Exception ignored) {}
        
        JTable table = new JTable(tm);
        JButton addBtn = new JButton("Add Staff");
        addBtn.addActionListener(e -> {
            try {
                Staff s = new Staff();
                s.setNames(JOptionPane.showInputDialog("Staff Name:"));
                s.setRole(JOptionPane.showInputDialog("Role (e.g. Hair Stylist):"));
                StaffProfile sp = new StaffProfile(); sp.setPhone("123"); sp.setSpecialty(s.getRole());
                s.setProfile(sp);
                s = service.saveStaff(s);
                tm.addRow(new Object[]{s.getId(), s.getNames(), s.getRole()});
                JOptionPane.showMessageDialog(this, "Staff Added!");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        p.add(new JScrollPane(table), BorderLayout.CENTER); p.add(addBtn, BorderLayout.SOUTH);
        return p;
    }
    
    private JPanel createBookingPanel() {
        JPanel p = new JPanel(new GridLayout(6, 2, 10, 10));
        JComboBox<SalonService> srvBox = new JComboBox<>();
        JComboBox<Staff> stfBox = new JComboBox<>();
        JTextField dateF = new JTextField("2026-06-01");
        JTextField timeF = new JTextField("10:00");
        JButton bookBtn = new JButton("CONFIRM BOOKING");
        try {
            for(SalonService s : service.getServices()) srvBox.addItem(s);
            for(Staff st : service.getStaff()) stfBox.addItem(st);
        } catch(Exception ignored) {}
        
        p.add(new JLabel("Select Service:")); p.add(srvBox);
        p.add(new JLabel("Select Staff:")); p.add(stfBox);
        p.add(new JLabel("Date (YYYY-MM-DD):")); p.add(dateF);
        p.add(new JLabel("Time (HH:MM):")); p.add(timeF);
        p.add(bookBtn);
        
        bookBtn.addActionListener(e -> {
            try {
                Appointment a = new Appointment();
                a.setApptDate(dateF.getText()); a.setApptTime(timeF.getText());
                a.setClient(currentUser); a.setStaff((Staff)stfBox.getSelectedItem());
                List<SalonService> srvs = new ArrayList<>(); srvs.add((SalonService)srvBox.getSelectedItem());
                a.setServices(srvs);
                service.bookAppointment(a);
                JOptionPane.showMessageDialog(this, "Booked successfully!");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        return p;
    }
    
    private JPanel createApptAdminPanel() {
        JPanel p = new JPanel(new BorderLayout());
        DefaultTableModel tm = new DefaultTableModel(new Object[]{"ID", "Date", "Time", "Client", "Staff"}, 0);
        try {
            for(Appointment a : service.getAppointments()) {
                tm.addRow(new Object[]{a.getId(), a.getApptDate(), a.getApptTime(), a.getClient().getNames(), a.getStaff().getNames()});
            }
        } catch(Exception ignored) {}
        
        JTable table = new JTable(tm);
        JButton exportBtn = new JButton("Export to CSV (Report)");
        exportBtn.addActionListener(e -> {
            try {
                java.io.FileWriter fw = new java.io.FileWriter("Appointments_Report.csv");
                fw.write("ID,Date,Time,Client,Staff,Status\n");
                for(Appointment a : service.getAppointments()) {
                    fw.write(a.getId()+","+a.getApptDate()+","+a.getApptTime()+","+a.getClient().getNames()+","+a.getStaff().getNames()+","+a.getStatus()+"\n");
                }
                fw.close();
                JOptionPane.showMessageDialog(this, "Exported successfully to Appointments_Report.csv!");
            } catch(Exception ex) { ex.printStackTrace(); }
        });
        p.add(new JScrollPane(table), BorderLayout.CENTER); p.add(exportBtn, BorderLayout.SOUTH);
        return p;
    }
}
