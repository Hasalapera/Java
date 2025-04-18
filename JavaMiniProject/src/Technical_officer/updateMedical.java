package Technical_officer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class updateMedical extends JFrame {
    private JPanel mainPanel;
    private JLabel title;
    private JPanel btnPanel;
    private JTextField courseField;
    private JTextField sidField;
    private JTextField weekField;
    private JTextField dayField;
    private JTextField ctypeField;
    private JTextField statusField;
    private JButton submit;
    private JLabel sidLabel;
    private JLabel courseLabel;
    private JLabel weekLable;
    private JLabel dayLabel;
    private JLabel ctypeLabel;
    private JLabel statusLabel;

    public updateMedical() {

        setContentPane(mainPanel);
        setTitle("Update Medical Card");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);


        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMedicalStatus();
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

    private void updateMedicalStatus() {
        connectToDatabase();

        String studentId = sidField.getText().trim();
        String courseCode = courseField.getText().trim();
        String week = weekField.getText().trim();
        String day = dayField.getText().trim();
        String status = statusField.getText().trim();

        if (studentId.isEmpty() || courseCode.isEmpty() || week.isEmpty() ||
                day.isEmpty()  || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        String sql = "UPDATE medical SET status = ? WHERE stu_id = ? AND course_code = ? AND week_no = ? AND day_no = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, studentId);
            stmt.setString(3, courseCode);
            stmt.setString(4, week);
            stmt.setString(5, day);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Medical status updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update. Please check your inputs.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new updateMedical());
    }
}
