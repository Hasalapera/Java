package Technical_officer;

import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class updateAttendance extends JFrame {
    private JPanel mainPanel;
    private JLabel title;
    private JPanel btnPanel;
    private JTextField sidField;
    private JTextField courseField;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField ctypeField;
    private JTextField statusField;
    private JLabel stuidLabel;
    private JLabel courseLabel;
    private JLabel weekLabel;
    private JLabel dayLabel;
    private JLabel ctypeLabel;
    private JLabel statusLabel;
    private JButton submit;

    public updateAttendance() {

        setContentPane(mainPanel);
        setTitle("Update Attendance");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 890);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAttendanceStatus();
            }
        });
    }
    Connection con;

    private void updateAttendanceStatus() {

        con = DatabaseConnection.connect();

        String studentId = sidField.getText();
        String courseCode = courseField.getText();
        String weekNo = weekField.getText();
        String dayNo = dayField.getText();
        String courseType = ctypeField.getText();
        String status = statusField.getText();

        if (studentId.isEmpty() || courseCode.isEmpty() || weekNo.isEmpty() || dayNo.isEmpty() || courseType.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        String sql = "UPDATE attendance SET status = ? WHERE Stu_id = ? AND course_code = ? AND week_no = ? AND day_no = ? AND course_type = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, studentId);
            stmt.setString(3, courseCode);
            stmt.setString(4, weekNo);
            stmt.setString(5, dayNo);
            stmt.setString(6, courseType);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Attendance status updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No matching record found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating status: " + e.getMessage());
        }
    }


    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new updateAttendance());
    }*/
}
