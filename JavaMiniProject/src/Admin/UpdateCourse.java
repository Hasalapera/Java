package Admin;

//import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import java.sql.SQLException;
import java.sql.DriverManager;
import database.DatabaseConnection;
import database.Session;

public class UpdateCourse extends JFrame {
    public JPanel panel1;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton UPDATEButton;
    private JButton CLEARButton;

//    private void createUIComponents() {
        // TODO: place custom component creation code here
//    }

    public UpdateCourse() {
        setContentPane(panel1);
        setTitle("Update Course");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        setVisible(true);

        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCourseInDB();
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
    }


    private void updateCourseInDB() {
        // Database connection settings
        /*String url = "jdbc:mysql://localhost:3308/techlms";
        String username = "root";  // replace with your DB username
        String password = "1234";      // replace with your DB password*/

        // Get input values
        String courseId = textField1.getText();
        String courseCode = textField2.getText();
        String lecId = textField3.getText();
        String courseType = textField4.getText();
        String courseName = textField5.getText();
        String credit = textField6.getText();

        // SQL UPDATE statement
        String query = "UPDATE course SET course_code = ?, lec_id = ?, course_type = ?, course_name = ?, credit = ? WHERE course_id = ?";

        try {
            // Connect to database
            Connection conn = DatabaseConnection.connect(); // use your own method
            PreparedStatement ps = conn.prepareStatement(query);

            // Set parameters for the query
            ps.setString(1, courseCode);
            ps.setString(2, lecId);
            ps.setString(3, courseType);
            ps.setString(4, courseName);
            ps.setString(5, credit);
            ps.setString(6, courseId);

            // Execute update
            int updated = ps.executeUpdate();

            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Course updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Course ID not found.");
            }

            // Close connection
            ps.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    // Optional: for testing the GUI
    public static void main(String[] args) {
        new UpdateCourse();
    }
}


