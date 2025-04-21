package Lecture;

import javax.swing.*;
import java.awt.*;

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

    public Lec_profileupdate() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Home");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
