package student;

import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;


public class ShowCAEligibility extends JFrame {
    private JLabel eligibilityLbl;
    private JPanel mainPanel;
    private JPanel eligibilityPanel;
    private JTable eligibilityTable;
    private JButton exitBtn;
    private JScrollPane eligibilityScrollPane;

    public ShowCAEligibility() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("CA Eligibility");
        setSize(2000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);



//        checkCAEligibility();

        String[] caEligibility = {"Course Code", "Course Name", "Eligibility Status"};
        DefaultTableModel model = new DefaultTableModel(null, caEligibility);

        eligibilityTable.setModel(model);

        checkCAEligibility();
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StuHome();
                dispose();
//                CardLayout cl = (CardLayout) cardpanel.getLayout();
//                cl.show(cardpanel, returnToCard);
            }
        });
    }

    public void checkCAEligibility(){
        Connection con = DatabaseConnection.connect();
        try{
            String sql = "SELECT m.Stu_id, m.Course_code,c.Course_name, m.Quiz_01, m.Quiz_02, m.Quiz_03, m.Quiz_04, m.Assignment_01, m.Assignment_02, m.Mid_theory, m.Mid_practical FROM Marks m JOIN Course c ON m.Course_code = c.Course_code WHERE m.Stu_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Session.loggedInUsername);

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel tableModel = (DefaultTableModel) eligibilityTable.getModel();

            while (rs.next()) {
                String stuId = rs.getString("Stu_id");
                String courseCode = rs.getString("Course_code");
                String courseName = rs.getString("Course_name");

                Double quiz01 = rs.getDouble("Quiz_01");
                Double quiz02 = rs.getDouble("Quiz_02");
                Double quiz03 = rs.getDouble("Quiz_03");
                Double quiz04 = rs.getDouble("Quiz_04");


                double[] quizArray;

                if(courseCode.equals("ICT2122")){
                    quizArray = new double[]{quiz01, quiz02, quiz03, quiz04};
                }else {
                    quizArray = new double[]{quiz01, quiz02, quiz03};
                }

                Arrays.sort(quizArray);

                double quizTotal;
                if(courseCode.equals("ICT2122")){
                    quizTotal = quizArray[3] + quizArray[2] + quizArray[1];
                    quizTotal = (quizTotal/ 3) * 0.10;
                }else{
                    quizTotal = quizArray[2] + quizArray[1];
                    quizTotal = (quizTotal/ 2) * 0.10;
                }

                // Assignment

                double ass1 = rs.getDouble("Assignment_01");
                double ass2 = rs.getDouble("Assignment_02");
                double assAvg = (ass1 + ass2) / 2;

                double assMarks;
                if (courseCode.equals("ICT2122")){
                    assMarks = assAvg * 0.10;
                }else{
                    assMarks = assAvg * 0.20;
                }

                double midTheory = rs.getDouble("Mid_theory");
                double midPractical = rs.getDouble("Mid_practical");
                double midAvg = (midTheory + midPractical)/2;
                double midMarks = midAvg * 0.20;

                double totalCAMarks = quizTotal + assMarks + midMarks;

                double fullCAMark;
                boolean isEligible = false;
                if(courseCode.equals("ICT2122") || courseCode.equals("ICT2133") || courseCode.equals("ICT2152")){
                    fullCAMark = 30;
                    if(totalCAMarks >= (fullCAMark/2) - 0.5){
                        isEligible = true;
                    }
                }else{
                    fullCAMark = 40;
                    if(totalCAMarks >= (fullCAMark/2) - 0.5){
                        isEligible = true;
                    }
                }

                String CAEligibilityStatus;
                if(isEligible){
                    CAEligibilityStatus = "Eligible";
                }else {
                    CAEligibilityStatus = "Not Eligible";
                }

                tableModel.addRow(new Object[] {courseCode, courseName, CAEligibilityStatus});

                if(isEligible) {
                    System.out.println("Student_id: " + stuId + " Course_code: " + courseCode + " Total CAMarks: " + String.format("%.2f", totalCAMarks) + " - Eligible");
                } else {
                    System.out.println("Student_id: " + stuId + " Course_code: " + courseCode + " Total CAMarks: " + String.format("%.2f", totalCAMarks) + " - Not Eligible");
                }


            }
        }catch (Exception e){
            System.out.println("Error in get Highest Quizes: " + e.getMessage());
        }
    }

//    public static void main(String[] args) {
//        new ShowCAEligibility();
//    }
}
