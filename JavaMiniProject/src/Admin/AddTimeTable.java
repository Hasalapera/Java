package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseConnection;
import database.Session;


public class AddTimeTable {
    private JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JButton addDetailsButton;
    private JButton CLEARButton;

    public AddTimeTable() {
        addDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertTimeTableIntoDB();
            }
        });

        CLEARButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

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


    private void insertTimeTableIntoDB() {
        /*String url = "jdbc:mysql://localhost:3308/techlms";
        String username = "root";    // replace with your DB username
        String password = "1234";    // replace with your DB password*/

        // Get field values
        String timetableId = textField1.getText();
        String adId = textField2.getText();
        String department = textField3.getText();
        String courseCode = textField4.getText();
        String courseName = textField5.getText();
        String time = textField6.getText();
        String day = textField7.getText();

        // SQL INSERT query
        String query = "INSERT INTO timetable (timetable_id, ad_id, department, course_code, course_name, time, day) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.connect(); // use your own method
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, timetableId);
            ps.setString(2, adId);
            ps.setString(3, department);
            ps.setString(4, courseCode);
            ps.setString(5, courseName);
            ps.setString(6, time);
            ps.setString(7, day);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Time table details added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add details.");
            }

            ps.close();
            conn.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    // Optional: run GUI
    public static void main(String[] args) {
        JFrame frame = new JFrame("Add Time Table");
        frame.setContentPane(new AddTimeTable().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

