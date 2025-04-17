package Technical_officer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

    private void insertMedical() {
        String mid = midField.getText();
        String sid = stuidField.getText();
        String aid = aidField.getText();
        String course = courseField.getText();
        String week = weekField.getText();
        String day = dayField.getText();
        String status = statusField.getText();

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Connect to DB
            connectToDatabase();
            String sql = "INSERT INTO medical (Medical_id, Stu_id, Course_code, week_No, day_No, Status,Attendance_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, mid);
            pstmt.setString(2, sid);
            pstmt.setString(3, aid);
            pstmt.setString(4, course);
            pstmt.setString(5, week);
            pstmt.setString(6, day);
            pstmt.setString(7, status);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Medical record added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add record.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new addmedical().setVisible(true));
    }
}
