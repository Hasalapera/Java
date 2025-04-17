package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import database.DatabaseConnection;


public class UpdateProfile {
    public JPanel panel1;
    private JPanel Mainpanel;
    private JPanel ProfilePanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JTextField textField8;
    private JTextField textField9;
    private JTextField textField10;
    private JTextField textField11;
    private JButton uploadImageButton;
    private JButton updateProfileButton;
    private JButton clearButton;

    public UpdateProfile() {


        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserProfile();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });


    }



    private void updateUserProfile() {
        String username = textField1.getText().trim();
        String fname = textField2.getText().trim();
        String lname = textField3.getText().trim();
        String dob = textField4.getText().trim(); // Format: yyyy-MM-dd
        String role = textField5.getText().trim();
        String enrollmentDate = textField6.getText().trim(); // Format: yyyy-MM-dd
        String address = textField7.getText().trim();
        String email = textField8.getText().trim();
        String phone = textField9.getText().trim(); // Make sure it's an integer
        String password = textField10.getText().trim();
        String profile = textField11.getText().trim();

        Connection conn = null;
       PreparedStatement stmt = null;

        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3308/techlms", "root", "1234");
            conn  = DatabaseConnection.connect(); // use your own method
//            PreparedStatement ps = conn.prepareStatement(sql);

            String sql = "UPDATE user SET Fname=?, Lname=?, DoB=?, Role=?, Enrollment_Date=?, Address=?, Email=?, Phone_No=?, Password=?, profile=? WHERE UserName=?";
            stmt = conn.prepareStatement(sql);

            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setDate(3, dob.isEmpty() ? null : Date.valueOf(dob));
            stmt.setString(4, role.isEmpty() ? null : role);
            stmt.setDate(5, enrollmentDate.isEmpty() ? null : Date.valueOf(enrollmentDate));
            stmt.setString(6, address);
            stmt.setString(7, email);

            // Parse phone number as integer if not empty
            if (!phone.isEmpty()) {
                stmt.setInt(8, Integer.parseInt(phone));
            } else {
                stmt.setNull(8, java.sql.Types.INTEGER);
            }

            stmt.setString(9, password);
            stmt.setString(10, profile);
            stmt.setString(11, username);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No user found with the given username.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        textField8.setText("");
        textField9.setText("");
        textField10.setText("");
        textField11.setText("");
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Update Profile");
        frame.setContentPane(new UpdateProfile().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
