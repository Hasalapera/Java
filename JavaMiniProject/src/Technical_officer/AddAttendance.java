package Technical_officer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

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

    private Connection con;

    public AddAttendance() {
        setTitle("Add Attendance");
        setContentPane(mainPanel);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        // Auto-generate and display Attendance ID when form is opened
        String newId = generateAttendanceId();
        aidLabel.setText(newId);
        aidLabel.setEditable(false); // Prevent editing

        addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertAttendance();
            }
        });
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/fot_management_system";
            String user = "root";
            String password = ""; // Change if needed
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    private String generateAttendanceId() {
        String newId = "A001"; // Default starting ID
        try {
            connectToDatabase();
            String query = "SELECT Attendance_id FROM attendance ORDER BY Attendance_id DESC LIMIT 1";
            PreparedStatement pstmt = con.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String lastId = rs.getString("Attendance_id"); // e.g., A015
                int num = Integer.parseInt(lastId.substring(1)); // Get 15
                num++; // Increment
                newId = String.format("A%03d", num); // Format as A016
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating Attendance ID: " + e.getMessage());
        }
        return newId;
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
            connectToDatabase();
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

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Attendance Added Successfully!");
                clearFields();
                // Generate next ID
                String nextId = generateAttendanceId();
                aidLabel.setText(nextId);
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
        // Do not clear aidLabel since it's auto-generated
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
