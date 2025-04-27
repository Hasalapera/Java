package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import database.DatabaseConnection;


public class AddCourse extends JFrame {
    public JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton addCourseButton;
    private JButton clearButton;
    private JButton EXITButton;


    public AddCourse() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        setLocationRelativeTo(null);
        setTitle("Add Course");
        setSize(2000, 800);
        setVisible(true);


        // Add course to database
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertCourseToDB();
            }
        });

        // Clear all fields
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        EXITButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdHome(); // go back to admin home
                dispose();
            }
        });
    }

    private void insertCourseToDB() {
        // Get data from fields
        String courseId = textField1.getText();
        String courseCode = textField2.getText();
        String lecId = textField3.getText();
        String courseType = textField4.getText();
        String courseName = textField5.getText();
        String credit = textField6.getText(); // assuming credit is a text field too

        // SQL Insert Query
        String query = "INSERT INTO course (course_id, course_code, lec_id, course_type, course_name, credit) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection conn = DatabaseConnection.connect(); // use your own method
            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, courseId);
            ps.setString(2, courseCode);
            ps.setString(3, lecId);
            ps.setString(4, courseType);
            ps.setString(5, courseName);
            ps.setString(6, credit);

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(null, "Course added successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add course.");
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
    }

    // Optional: test GUI
    public static void main(String[] args) {
        new AddCourse().setVisible(true);
    }
}


