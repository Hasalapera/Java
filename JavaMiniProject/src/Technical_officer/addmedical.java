package Technical_officer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class addmedical extends JFrame {
    private JPanel MainPanel;
    private JLabel titleLabel;
    private JPanel btnPanel;
    private JTextField stuidField;
    private JTextField midField;
    private JTextField courseField;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField statusField;
    private JLabel midLabel;
    private JLabel stuidLabel;
    private JLabel courseLabel;
    private JLabel weekLabel;
    private JLabel dayLabel;
    private JLabel statusLabel;
    private JTextField aidField;
    private JLabel aidLabel;
    private JButton addButton;
    private JTextField ctypeField;
    private JLabel ctype;
    private JButton findbutton;

    public addmedical() {
        setTitle("Add Medical");
        setContentPane(MainPanel);
        setSize(400, 400);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertMedical();
            }
        });
        findbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchAttendanceId();
            }
        });
    }

    private Connection con;

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/fot_management_system";
            String user = "root";
            String password = ""; // Change if you use a password
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    private void fetchAttendanceId() {
        String sid = stuidField.getText();
        String week = weekField.getText();
        String day = dayField.getText();
        String course = courseField.getText();
        String ctype = ctypeField.getText();

        if (sid.isEmpty() || week.isEmpty() || day.isEmpty() || course.isEmpty() || ctype.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields to fetch Attendance ID.");
            return;
        }

        connectToDatabase();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT Attendance_id FROM attendance WHERE Stu_id = ? AND week_No = ? AND day_No = ? AND Course_code = ? AND Course_type = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, sid);
            pstmt.setString(2, week);
            pstmt.setString(3, day);
            pstmt.setString(4, course);
            pstmt.setString(5, ctype);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                String attendanceId = rs.getString("Attendance_id");
                aidField.setText(attendanceId);
                JOptionPane.showMessageDialog(this, "Attendance ID fetched successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No matching attendance record found.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching attendance ID: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void insertMedical() {
        String mid = midField.getText();
        String sid = stuidField.getText();
        String aid = aidField.getText();
        String course = courseField.getText();
        String week = weekField.getText();
        String day = dayField.getText();
        String status = statusField.getText();
        String ctype = ctypeField.getText();

        connectToDatabase();
        PreparedStatement pstmt = null;

        try {
            String sql = "INSERT INTO medical (Medical_id, Stu_id, Course_code, week_No, day_No, Status, Attendance_id,Course_type) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mid);
            pstmt.setString(2, sid);
            pstmt.setString(3, course);   // corrected: course instead of aid
            pstmt.setString(4, week);
            pstmt.setString(5, day);
            pstmt.setString(6, status);
            pstmt.setString(7, aid);
            pstmt.setString(8,ctype);// corrected: aid placed here

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Medical record added successfully!");
                updateAttendanceStatus(aid); // Update attendance status
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add record.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateAttendanceStatus(String attendanceId) {
        connectToDatabase();
        PreparedStatement pstmt = null;

        try {
            String sql = "UPDATE attendance SET status = 'Medical' WHERE Attendance_id = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, attendanceId);

            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Attendance status updated to 'Medical'.");
            } else {
                System.out.println("No attendance record found with the given ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update attendance status: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new addmedical().setVisible(true));
    }
}
