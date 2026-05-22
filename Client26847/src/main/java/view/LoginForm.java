package view;
import model.Client;
import service.IGlamourService;
import javax.swing.*;
import java.awt.*;
public class LoginForm extends JFrame {
    public LoginForm(IGlamourService service) {
        setTitle("Login - Glamour Salon 26847");
        setSize(350, 250); setLocationRelativeTo(null); setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));
        
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton regBtn = new JButton("Register");
        
        add(new JLabel("  Email:")); add(emailField);
        add(new JLabel("  Password:")); add(passField);
        add(loginBtn); add(regBtn);
        
        loginBtn.addActionListener(e -> {
            try {
                Client c = service.login(emailField.getText(), new String(passField.getPassword()));
                if(c != null) {
                    String otp = service.generateOtp(c.getEmail());
                    String input = JOptionPane.showInputDialog(this, "OTP sent to Server console! Check terminal & enter here:");
                    if(otp.equals(input)) {
                        new Dashboard(c, service).setVisible(true);
                        dispose();
                    } else { JOptionPane.showMessageDialog(this, "Invalid OTP Code!"); }
                } else { JOptionPane.showMessageDialog(this, "Invalid Credentials!"); }
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        
        regBtn.addActionListener(e -> {
            try {
                Client c = new Client();
                c.setNames(JOptionPane.showInputDialog("Enter Full Name:"));
                c.setEmail(emailField.getText());
                c.setPassword(new String(passField.getPassword()));
                c.setPhone(JOptionPane.showInputDialog("Enter Phone:"));
                c.setRole("CLIENT");
                service.register(c);
                JOptionPane.showMessageDialog(this, "Registered! Now click Login.");
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
    }
}
