package Lecture;

import database.DatabaseConnection;
import student.StuHome;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Lec_profileupdate extends JFrame {
    private JPanel mainPanel;
    private JLabel updateProfileMainLbl;
    private JPanel detailPanel;
    private JLabel firstNameLbl;
    private JTextField firstNameTxt;
    private JLabel lastNameLbl;
    private JTextField lastNameTxt;
    private JLabel addressLbl;
    private JLabel emailLbl;
    private JLabel pNoLbl;
    private JTextField addressTxt;
    private JTextField emailTxt;
    private JTextField pNoTxt;
    private JTextField proPicTxt;
    private JButton uploadAnImageButton;
    private JButton updateProfileButton;
    private JButton cancelButton;

    public Lec_profileupdate(String User_ID) {
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Student Home");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
        uploadAnImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadProfilePicture();
            }
        });
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatelecturedetails(User_ID);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LecHome(User_ID);
                dispose();
            }
        });
    }

    public void updatelecturedetails(String User_ID) {
        String firstName = firstNameTxt.getText();
        String lastName = lastNameTxt.getText();
        String address = addressTxt.getText();
        String email = emailTxt.getText();
        String pNo = pNoTxt.getText();
//        String proPic = proPicTxt.getText();

        File file = new File(proPicTxt.getText());
        String proPic = file.getName();

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.connect();
            String sql = "UPDATE User SET FName = ?, LName = ?, Address = ?, Email = ?, Phone_No = ?, Profile_pic = ? WHERE UserName = ?";
            pst = con.prepareStatement(sql);

            pst.setString(1, firstName);
            pst.setString(2, lastName);
            pst.setString(3, address);
            pst.setString(4, email);
            pst.setString(5, pNo);
            pst.setString(6, proPic);
            pst.setString(7,User_ID);

            int i = pst.executeUpdate();

            if (i > 0) {
                JOptionPane.showMessageDialog(null, "Lecture Profile Updated");
            } else {
                JOptionPane.showMessageDialog(null, "Lecture Profile Not Updated");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "User Update Failed");
            e.printStackTrace();
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
            String imagePath = selectedFile.getAbsolutePath();

            // Set the selected file path into the text field
            proPicTxt.setText(imagePath);
        }
    }
}
