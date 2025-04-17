package Technical_officer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class toHome extends  JFrame {
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JLabel stuManaSysLbl;
    private JLabel FoTLbl;
    private JButton logOutButton;
    private JPanel btnPanel;
    private JButton profileButton;
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
    private JButton updateProfileButton;
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
    private JPanel attendanceCard;
    private JLabel headingLabel;
    private JPanel medicalCard;
    private JLabel heading;
    private JPanel viewPanel;
    private JButton add;
    private JButton update;
    private JPanel buttons;
    private JPanel view;
    private JPanel buttonPanel;
    private JButton addbtn;
    private JButton updatebtn;

    public toHome() {

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

        cardMainPanel.add(profileCard, "profileCard");
        cardMainPanel.add(attendanceCard, "attendanceCard");
        cardMainPanel.add(medicalCard, "medicalCard");
        cardMainPanel.add(timeTableCard, "timeTableCard");
        cardMainPanel.add(noticeCard, "noticeCard");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("to home");
        setSize(400,400);
        setVisible(true);
        setLocationRelativeTo(null);

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "profileCard");
            }
        });
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "attendanceCard");
            }
        });
        medicalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "medicalCard");
            }
        });
        timeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "timeTableCard");
            }
        });
        noticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "noticeCard");
            }
        });
    }

    public static void main(String[] args) {
        new toHome();
    }
}
