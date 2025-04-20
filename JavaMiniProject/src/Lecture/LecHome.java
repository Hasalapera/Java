package Lecture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public LecHome() {

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Lecture Home");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        addMarksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "AddmarksCard");
            }
        });
    }

    public static void main(String[] args) {
        new LecHome();
    }
}
