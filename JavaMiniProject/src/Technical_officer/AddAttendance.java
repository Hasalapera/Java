package Technical_officer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddAttendance extends JFrame {
    private JPanel mainPanel;
    private JLabel title;
    private JPanel btnPanel;
    private JTextField sidLabel;
    private JTextField aidLabel;
    private JTextField courseLabel;
    private JTextField lechourLabel;
    private JTextField weekLabel;
    private JTextField dayLabel;
    private JTextField statusLabel;
    private JTextField ctypeLabel;
    private JButton addbutton;
    private JLabel aid;
    private JLabel sid;
    private JLabel lechours;
    private JLabel week;
    private JLabel day;
    private JLabel status;
    private JLabel ctype;

    public AddAttendance() {

        setTitle("Add Attendance");
        setContentPane(mainPanel);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertAttendance();
            }
        });
    }

    private Connection con;
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3308/techlms";
            String user = "root";
            String password = "1234"; // Change if you use a password
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    private void insertAttendance() {
        String aid = aidLabel.getText();
        String sid = sidLabel.getText();
        String course = courseLabel.getText();
        String lechours = lechourLabel.getText();
        String week = weekLabel.getText();
        String day = dayLabel.getText();
        String status = statusLabel.getText();
        String ctype = ctypeLabel.getText();

        try {
            // Connect to the database
            connectToDatabase();
            // Prepare SQL query
            String sql = "INSERT INTO attendance (Attendance_id, Stu_id, Course_code, Lec_hour, Week_No, Day_No, Status, Course_Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, aid);
            pstmt.setString(2, sid);
            pstmt.setString(3, course);
            pstmt.setString(4, lechours);
            pstmt.setString(5, week);
            pstmt.setString(6, day);
            pstmt.setString(7, status);
            pstmt.setString(8, ctype);

            // Execute insert
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Attendance Added Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to Add Attendance.");
            }

            pstmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        aidLabel.setText("");
        sidLabel.setText("");
        courseLabel.setText("");
        lechourLabel.setText("");
        weekLabel.setText("");
        dayLabel.setText("");
        statusLabel.setText("");
        ctypeLabel.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddAttendance().setVisible(true));
    }
}
