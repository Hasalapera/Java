package Lecture;

import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LecHome extends JFrame {
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JLabel stuManaSysLbl;
    private JLabel FoTLbl;
    private JButton logOutButton;
    private JPanel btnPanel;
    private JButton profileButton;
    private JButton addMarksButton;
    private JButton gradeAndGPAButton;
    private JButton attendanceButton;
    private JButton medicalButton;
    private JButton timeTableButton;
    private JButton noticeButton;
    private JPanel cardMainPanel;
    private JPanel profileCard;
    private JLabel profileHeadingLbl;
    private JPanel detailPanel;
    private JLabel fNameLbl;
    private JTextField fNameTxt;
    private JLabel lNameLbl;
    private JTextField lNameTxt;
    private JLabel addressLbl;
    private JTextField addressTxt;
    private JLabel emailLbl;
    private JTextField emailTxt;
    private JLabel pNoLbl;
    private JLabel roleLbl;
    private JTextField pNoTxt;
    private JTextField roleTxt;
    private JPanel imgMainPanel;
    private JPanel imgPanel;
    private JLabel imageLbl;
    private JButton deleteProfilePictureButton;
    private JButton updateProfileButton;
    private JPanel AddmarksCard;
    private JPanel gradeGPACard;
    private JLabel gradeGPAHeadingLbl;
    private JLabel sgpaLbl;
    private JTextField sgpaTxt;
    private JComboBox selectCrsComboBox;
    private JLabel selectCrsLbl;
    private JLabel yourGradeLbl;
    private JTextField yourGradeTxt;
    private JPanel gradeTxtAreaPanel;
    private JTextArea gradetxtArea;
    private JPanel attendanceCard;
    private JLabel attendanceHeadingLbl;
    private JLabel selectAttCourseLbl;
    private JComboBox selectAttCourseCombo;
    private JPanel attViewPanel;
    private JScrollPane attScrollPane;
    private JTable attTable;
    private JButton checkAttendanceEligibilityButton;
    private JButton clearButton;
    private JButton OKButton;
    private JPanel medicalCard;
    private JLabel medicalHeadingLbl;
    private JPanel mediDetailsTblPanel;
    private JScrollPane mediScrollPane;
    private JTable mediDetailsTable;
    private JPanel noticeCard;
    private JLabel noticeHeadingLbl;
    private JLabel selectTitleLbl;
    private JComboBox selectTitleCombo;
    private JPanel noticeTxtAreaPanel;
    private JScrollPane noticeScrollPane;
    private JTextArea noticeTxtArea;
    private JPanel timeTableCard;
    private JLabel timeTableHeadingLbl;
    private JPanel timeTablePanel;
    private JScrollPane timeTableScrollPane;
    private JTable timeTableTable;
    private JPanel MainFrame;
    private JTextField Mark_id_textfield;
    private JTextField student_id_textField;
    private JTextField mark_textField;
    private JButton ADDButton;
    private JButton deleteButton;
    private JComboBox mark_type_comboBox;
    private JComboBox coursecodecomboBox;
    private JTable marktable;

    private String[] courseCodes = {
            "ICT2113",  // Index 0
            "ICT2122",  // Index 1
            "ICT2132",  // Index 2
            "ICT2142",  // Index 3
            "ICT2152"   // Index 4
    };

    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public LecHome() {

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Lecture Home");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);

        displayProfileDetils();
        showProfilePicture(imageLbl);

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "profileCard");
            }
        });
        addMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "AddmarksCard");
            }
        });
    }

    // ******* Display Profile Details *****************

    public void displayProfileDetils(){
        con = DatabaseConnection.connect();

        try {
            String sql = "SELECT FName, LName, Address, Email, Phone_No, Role FROM User WHERE UserName = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, Session.loggedInUsername);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                String fName1 = rs.getString("FName");
                fNameTxt.setText(fName1);
                System.out.println("fName1: " + fName1);
                String lName1 = rs.getString("LName");
                lNameTxt.setText(lName1);
                System.out.println("lName1: " + lName1);
                String address1 = rs.getString("Address");
                addressTxt.setText(address1);
                System.out.println("address1: " + address1);
                String email1 = rs.getString("Email");
                emailTxt.setText(email1);
                System.out.println("email1: " + email1);
                String phoneNo1 = rs.getString("Phone_No");
                pNoTxt.setText(phoneNo1);
                System.out.println("phoneNo1: " + phoneNo1);
                String role1 = rs.getString("Role");
                roleTxt.setText(role1);
                System.out.println("role1: " + role1);

            }else {
                JOptionPane.showMessageDialog(null, "No Profile Found");
            }
        }catch (Exception e){
            System.out.println("Error in displayProfileDetils: " + e.getMessage());
        }
    }

    // ******* Display Profile Picture *****************

    public void showProfilePicture(JLabel imageLbl) {
        Connection con = DatabaseConnection.connect();
        try {
            String sql = "SELECT Profile_pic FROM User WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, Session.loggedInUsername);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String fileName = rs.getString("Profile_pic");

                // If no profile picture set in DB, use default
                if (fileName == null || fileName.trim().isEmpty()) {
                    fileName = "default.png";
                }

                String path = "JavaMiniProject/user_Pro_Pic/" + fileName;
                File imageFile = new File(path);

                // If image file does not exist, fallback to default image
                if (!imageFile.exists()) {
                    path = "JavaMiniProject/user_Pro_Pic/default.png";
                }

                // Load and Resize Image to fit JLabel
                ImageIcon imageIcon = new ImageIcon(path);

                // Get JLabel size (designed from GUI builder)
                int width = imageLbl.getWidth();
                int height = imageLbl.getHeight();

                // Default size safety check (in case label not ready)
                if (width == 0 || height == 0) {
                    width = 150;
                    height = 150;
                }

                Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLbl.setIcon(new ImageIcon(image));
                imageLbl.repaint(); // Refresh label to show updated image
            }
        } catch (Exception e) {
            System.out.println("Error in showProfilePicture: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        new LecHome();
//    }
}
