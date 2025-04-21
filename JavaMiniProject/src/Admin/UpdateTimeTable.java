package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseConnection;
import database.Session;

public class UpdateTimeTable {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton updateButton;
    private JButton CLEARButton;

    public UpdateTimeTable() {
        // Update button functionality
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimeTableInDB();
            }
        });

        // Clear button functionality
        CLEARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
    }

    private void updateTimeTableInDB() {
        /*String url = "jdbc:mysql://localhost:3308/techlms";
        String username = "root"; // your DB username
        String password = "1234"; // your DB password*/

        // Get values from text fields
        String timetableId = textField1.getText();
        String adId = textField2.getText();
        String department = textField3.getText();
        String courseCode = textField4.getText();
        String courseName = textField5.getText();
        String time = textField6.getText();
        String day = textField7.getText();

        // SQL UPDATE statement
        String query = "UPDATE timetable SET ad_id = ?, department = ?, course_code = ?, course_name = ?, time = ?, day = ? WHERE timetable_id = ?";

        try {
            Connection conn = DatabaseConnection.connect(); // use your own method
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, adId);
            ps.setString(2, department);
            ps.setString(3, courseCode);
            ps.setString(4, courseName);
            ps.setString(5, time);
            ps.setString(6, day);
            ps.setString(7, timetableId);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Time table updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Time table ID not found.");
            }

            ps.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
        textField5.setText("");
        textField6.setText("");
        textField7.setText("");
    }

    // Optional: run GUI
    public static void main(String[] args) {
        JFrame frame = new JFrame("Update Time Table");
        frame.setContentPane(new UpdateTimeTable().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}


