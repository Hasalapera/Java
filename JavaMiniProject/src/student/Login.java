package student;

import Lecture.LecHome;
import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Technical_officer.toHome;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JTextField userNameTextField;
    private JPasswordField passwordPasswordField;
    private JButton cancelButton;
    private JButton logInButton;
    private JLabel LogInLbl;
    private JPanel insideMainPanel;
    private JPanel welcomePanel;
    private JLabel stuManSysLbl;
    private JLabel FoTLabel;
    private JLabel welcomeLabel;
    private JPanel loginFormMain;
    private JPanel loginFormPanel;
    private JPanel loginFormInsidePanel;

    private String userName, password;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private DatabaseConnection databaseConnection;

    public Login() {
        setContentPane(mainPanel);
        setTitle("Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1024, 768);
        setVisible(true);

//        frame.add(mainPanel);
//        frame.setLocationRelativeTo(null);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);

        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Login button clicked");
                loginAuthentication();
//                new StuHome(userName);
//                new toHome().setVisible(true);

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userNameTextField.setText("");
                passwordPasswordField.setText("");
            }
        });
    }

    public void loginAuthentication() {
        String inputUserName = userNameTextField.getText();
        String inputPassword = String.valueOf(passwordPasswordField.getPassword()); // Use getPassword() for JPasswordField

        if (inputUserName.isEmpty() || inputPassword.isEmpty()) {
            JOptionPane.showMessageDialog(loginFormMain, "Please enter both username and password!");
            return;
        }

        databaseConnection = new DatabaseConnection();
        connection = databaseConnection.connect();

        try {
            String sql = "SELECT userName, Password FROM user WHERE userName = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, inputUserName);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String dbuserName = resultSet.getString("userName");
                String dbpassword = resultSet.getString("Password");

                if (inputPassword.equals(dbpassword)) {
                    String userNamelower = inputUserName.toLowerCase();

                    Session.loggedInUsername = dbuserName;

                    if (userNamelower.startsWith("admin")) {
                        JOptionPane.showMessageDialog(null, "Admin Login Successful");
                    } else if (userNamelower.startsWith("tg")) {
                        JOptionPane.showMessageDialog(null, "Student Login Successful");
                        new StuHome();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        topFrame.dispose();
                    } else if (userNamelower.startsWith("lec")) {
                        JOptionPane.showMessageDialog(null, "Lecturer Login Successful");
                        new LecHome(dbuserName);
                    } else if (userNamelower.startsWith("tech")) {
                        JOptionPane.showMessageDialog(null, "Technical Officer Login Successful");
                        new toHome();
                        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        topFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Unknown user role!");
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFormMain, "Incorrect password!");
                }
            } else {
                JOptionPane.showMessageDialog(loginFormMain, "User not found!");
            }
        } catch (Exception e) {
            System.out.println("Error in Display...." + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
