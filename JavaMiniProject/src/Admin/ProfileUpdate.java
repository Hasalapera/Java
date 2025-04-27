package Admin;

import javax.swing.*;
import database.DatabaseConnection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ProfileUpdate extends JFrame{
    public JPanel MainPanel;
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
    private JButton updateProfileButton;
    private JButton clearButton;
    private JTextField textField11;
    private JButton addPhotoButton;
    private JButton exitBtn;

    public ProfileUpdate() {
        setContentPane(MainPanel);
        setTitle("Update Profile");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        setLocationRelativeTo(null);
        setSize(2000, 800);
        setVisible(true);



        // UPDATE button logic
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });

        // CLEAR button logic
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
                new AdHome();
                dispose();
            }
        });
    }

    public void updateProfile() {
        String sql = "UPDATE user SET Fname=?, Lname=?, DoB=?, Role=?, Enrollment_Date=?, Address=?, Email=?, Phone_No=?, Password=?, Profile_pic=? WHERE UserName=?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, textField2.getText());
            pstmt.setString(2, textField3.getText());
            pstmt.setString(3, textField4.getText());
            pstmt.setString(4, textField5.getText());
            pstmt.setString(5, textField6.getText());
            pstmt.setString(6, textField7.getText());
            pstmt.setString(7, textField8.getText());

            // Convert phone number to int (handle empty or invalid input gracefully)
            try {
                pstmt.setInt(8, Integer.parseInt(textField9.getText()));
            } catch (NumberFormatException ex) {
                pstmt.setNull(8, java.sql.Types.INTEGER);
            }

            pstmt.setString(9, textField10.getText());
            pstmt.setString(10, textField11.getText());

            pstmt.setString(11, textField1.getText()); // WHERE UserName = ?

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No profile found for that username.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating profile: " + ex.getMessage());
        }
    }

    public void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");

        // Optional: allow only image files
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getName();

            // Set the selected file path into the text field
            textField11.setText(imagePath);
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
        new ProfileUpdate();
    }
}


