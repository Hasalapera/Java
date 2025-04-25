package Admin;

import javax.swing.*;
import database.DatabaseConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUserProfile extends JFrame {
    private JPanel MainPanel;
    private JPanel DetailPanel;
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
    private JButton addDetailsToProfileButton;
    private JButton clearButton;
    private JTextField textField11;
    private JButton addPhotoButton;
    private JButton exitBtn;

    public AddUserProfile() {
        setContentPane(MainPanel);
        setTitle("Add User Profile");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 800);
        setVisible(true);
        textField11.setVisible(true);



        addDetailsToProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUserProfile();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();

            }
        });

        addPhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadProfilePicture();
            }

        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdHome(); // go back to admin home
                dispose();
            }
        });
    }

    private void addUserProfile() {
        String sql = "INSERT INTO user (UserName, Fname, Lname, DoB, Role, Enrollment_Date, Address, Email, Phone_No, Password, Profile_pic) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, textField1.getText());
            pstmt.setString(2, textField2.getText());
            pstmt.setString(3, textField3.getText());
            pstmt.setString(4, textField4.getText());
            pstmt.setString(5, textField5.getText());
            pstmt.setString(6, textField6.getText());
            pstmt.setString(7, textField7.getText());
            pstmt.setString(8, textField8.getText());

            try {
                pstmt.setInt(9, Integer.parseInt(textField9.getText()));
            } catch (NumberFormatException ex) {
                pstmt.setNull(9, java.sql.Types.INTEGER);
            }

            pstmt.setString(10, textField10.getText());
            pstmt.setString(11, textField11.getText());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "User profile added successfully.");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add user profile.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");

        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField11.setText(selectedFile.getName());
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
        new AddUserProfile();
    }
}




