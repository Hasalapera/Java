package Lecture;

import database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Att_CA extends JFrame{
    private JTextField stu_numbertextField;
    private JButton stu_numbershowButton;
    private JTable Eligibilitytable;
    private JPanel MainPanle;

    Connection con;
    String User;

    public Att_CA(String User_ID) {

        setContentPane(MainPanle);
        setSize(2000, 890);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Student Eligibility");
        setResizable(true);
        setLocationRelativeTo(null);

        User=User_ID;

        attendancepluscaforall(User_ID);

        stu_numbershowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Stu_id=stu_numbertextField.getText();
                if(Stu_id.isEmpty()){
                    JOptionPane.showMessageDialog(MainPanle,"Please enter student number","Error",JOptionPane.ERROR_MESSAGE);
                }
                else {
                    if(isStudentExist(Stu_id)){
                        attendancepluscaforone(Stu_id,User_ID);
                    }
                   else {
                       JOptionPane.showMessageDialog(MainPanle,"Student Not Found","Error",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public boolean isStudentExist(String studentId) {
        con = DatabaseConnection.connect();

        try {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM student WHERE Stu_id = ?");
            pst.setString(1, studentId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainPanle, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<String> coursecodeselection(String User_ID) {
        List<String> Course_code = new ArrayList<>();
        con = DatabaseConnection.connect();

        try {
            PreparedStatement pstm = con.prepareStatement("SELECT Course_code FROM course WHERE Lec_id = ?");
            pstm.setString(1, User_ID);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Course_code.add(rs.getString("Course_code"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainPanle, "Connection Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        return Course_code;
    }

    private void attendancepluscaforall(String User_ID){

        con= DatabaseConnection.connect();

        try{

            List<String> courseCodes = coursecodeselection(User_ID);

            String[] Column = {"Student ID", "Course Code", "Attendance %", "CA Marks", "Eligibility"};
            DefaultTableModel model = new DefaultTableModel( Column, 0){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (String Course_code : courseCodes) {
                PreparedStatement pstm = con.prepareStatement("SELECT DISTINCT Stu_id FROM attendance WHERE Course_Code=?");
                pstm.setString(1, Course_code);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    String stuId = rs.getString("Stu_id");

                    double attendancePercentage = getStudentAttendancePercentage(stuId, Course_code);
                    double caMarks = CAmarks(stuId, Course_code);

                    String eligibility;
                    double caCutoff = getCACutoff(Course_code);
                    if (attendancePercentage >= 80 && caMarks >= caCutoff) {
                        eligibility = "Eligible";
                    } else {
                        eligibility = "Not Eligible";
                    }

                    model.addRow(new Object[]{
                            stuId,
                            Course_code,
                            String.format("%.2f", attendancePercentage) + "%",
                            String.format("%.2f", caMarks),
                            eligibility
                    });
                }

            }
            Eligibilitytable.setModel(model);

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainPanle,e);
        }
    }

    private void attendancepluscaforone(String Stu_id,String User_ID) {

            List<String> courseCodes = coursecodeselection(User_ID);

            String[] Column = {"Student ID", "Course Code", "Attendance %", "CA Marks", "Eligibility"};
            DefaultTableModel model = new DefaultTableModel(null, Column){
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            for (String Course_code : courseCodes) {
                double attendancePercentage = getStudentAttendancePercentage(Stu_id, Course_code);
                double caMarks = CAmarks(Stu_id, Course_code);

                String eligibility;
                double caCutoff = getCACutoff(Course_code);
                if (attendancePercentage >= 80 && caMarks >= caCutoff) {
                    eligibility = "Eligible";
                } else {
                    eligibility = "Not Eligible";
                }

                model.addRow(new Object[]{
                        Stu_id,
                        Course_code,
                        String.format("%.2f", attendancePercentage) + "%",
                        String.format("%.2f", caMarks),
                        eligibility
                });
            }

            Eligibilitytable.setModel(model);
    }

    private double CAmarks(String Stu_id,String Course_code) {

        double CA_marks = 0.0;
        double assignment1, assignment2, Quiz_01, Quiz_02, Quiz_03, Quiz_04, midtermtheory, midtermpractical,quizMark2113,quizMark2122
                ,quizMark2132,quizMark2152,assessmentMark2132,AssessmentMark2142,MidtermMark2142,midtermMark2113,assessmentMark2152;

        con= DatabaseConnection.connect();

        try {

            PreparedStatement pstm = con.prepareStatement("select * from marks where Course_code=? AND Stu_Id=?");
            pstm.setString(1,Course_code);
            pstm.setString(2,Stu_id);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                 assignment1 = rs.getDouble("Assignment_01");
                 assignment2 = rs.getDouble("Assignment_02");
                 Quiz_01 = rs.getDouble("Quiz_01");
                 Quiz_02 = rs.getDouble("Quiz_02");
                 Quiz_03 = rs.getDouble("Quiz_03");
                 Quiz_04 = rs.getDouble("Quiz_04");
                 midtermtheory = rs.getDouble("Mid_theory");
                 midtermpractical = rs.getDouble("Mid_practical");

                double[] Quizzes = {Quiz_01, Quiz_02, Quiz_03, Quiz_04};
                Arrays.sort(Quizzes);
                double[] Assignments = {assignment1, assignment2};

                switch (Course_code){
                    case "ICT2113":

                         quizMark2113 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                         midtermMark2113 = (midtermpractical + midtermtheory) / 2 * 0.20;
                        CA_marks=quizMark2113+midtermMark2113;
                        break;

                    case "ICT2122":

                         quizMark2122 = (Quizzes[3] + Quizzes[2]+Quizzes[1]) / 3 * 0.10;
                        CA_marks=quizMark2122+(Assignments[0]*0.10)+(midtermtheory*0.20);
                        break;

                    case "ICT2132":

                         quizMark2132 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                         assessmentMark2132 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                        CA_marks=quizMark2132+assessmentMark2132;
                        break;

                    case "ICT2142":

                         AssessmentMark2142 = (Assignments[0])* 0.20;
                         MidtermMark2142 = (midtermpractical)* 0.20;
                        CA_marks=AssessmentMark2142+MidtermMark2142;
                        break;

                    case "ICT2152":

                         quizMark2152 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                         assessmentMark2152 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                        CA_marks=quizMark2152+assessmentMark2152;
                        break;

                }
            }
        }catch(SQLException e){
                JOptionPane.showMessageDialog(MainPanle, e);
            }
            return CA_marks;
    }

    private double getStudentAttendancePercentage(String Stu_id, String Course_code) {

        con= DatabaseConnection.connect();

        double totalTheory = 0, presentTheory = 0;
        double totalPractical = 0, presentPractical = 0;

        try {
            PreparedStatement typeCheck = con.prepareStatement(
                    "SELECT DISTINCT Course_type FROM attendance WHERE Stu_id = ? AND Course_Code = ?");
            typeCheck.setString(1, Stu_id);
            typeCheck.setString(2, Course_code);
            ResultSet typeRS = typeCheck.executeQuery();

            boolean hasTheory = false;
            boolean hasPractical = false;

            while (typeRS.next()) {
                String type = typeRS.getString("Course_type");
                if (type.equalsIgnoreCase("Theory")) hasTheory = true;
                if (type.equalsIgnoreCase("Practical")) hasPractical = true;
            }

            // Calculate Theory attendance
            if (hasTheory) {
                PreparedStatement pstmTheory = con.prepareStatement(
                        "SELECT Lec_hour, Status FROM attendance WHERE Stu_id = ? AND Course_Code = ? AND Course_type = ?");
                pstmTheory.setString(1, Stu_id);
                pstmTheory.setString(2, Course_code);
                pstmTheory.setString(3, "Theory");

                ResultSet rsTheory = pstmTheory.executeQuery();
                while (rsTheory.next()) {
                    double lecHour = rsTheory.getDouble("Lec_hour");
                    String status = rsTheory.getString("Status");

                    totalTheory += lecHour;

                    if (status.equalsIgnoreCase("Present")) {
                        presentTheory += lecHour;
                    } else if (status.equalsIgnoreCase("Medical")) {
                        String medicalStatus = checkMedicalStatus(Stu_id, Course_code);
                        if (medicalStatus.equalsIgnoreCase("Approved")) {
                            presentTheory += lecHour;
                        }
                    }
                }
            }

            // Calculate Practical attendance
            if (hasPractical) {
                PreparedStatement pstmPractical = con.prepareStatement(
                        "SELECT Lec_hour, Status FROM attendance WHERE Stu_id = ? AND Course_Code = ? AND Course_type = ?");
                pstmPractical.setString(1,Stu_id );
                pstmPractical.setString(2, Course_code);
                pstmPractical.setString(3, "Practical");

                ResultSet rsPractical = pstmPractical.executeQuery();
                while (rsPractical.next()) {
                    double lecHour = rsPractical.getDouble("Lec_hour");
                    String status = rsPractical.getString("Status");

                    totalPractical += lecHour;

                    if (status.equalsIgnoreCase("Present")) {
                        presentPractical += lecHour;
                    } else if (status.equalsIgnoreCase("Medical")) {
                        String medicalStatus = checkMedicalStatus(Stu_id, Course_code);
                        if (medicalStatus.equalsIgnoreCase("Approved")) {
                            presentPractical += lecHour;
                        }
                    }
                }
            }

            if (hasTheory && hasPractical) {
                double totalAll = totalTheory + totalPractical;
                double presentAll = presentTheory + presentPractical;
                return (totalAll > 0) ? (presentAll / totalAll) * 100 : 0;
            } else if (hasTheory) {
                return (totalTheory > 0) ? (presentTheory / totalTheory) * 100 : 0;
            } else if (hasPractical) {
                return (totalPractical > 0) ? (presentPractical / totalPractical) * 100 : 0;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainPanle, e);
        }

        return 0.0;
    }


    public String checkMedicalStatus( String Stu_id, String Course_code) {

        con= DatabaseConnection.connect();

        try {
            PreparedStatement med = con.prepareStatement("SELECT Status FROM medical WHERE Stu_id = ? AND Course_code = ?");
            med.setString(1, Stu_id);
            med.setString(2, Course_code);
            ResultSet rs = med.executeQuery();

            while (rs.next()) {
                if (rs.getString("Status").equalsIgnoreCase("Approved")) {
                    return "Approved";
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainPanle, e);
        }
        return "None";
    }

    private double getCACutoff(String Course_code) {
        switch (Course_code) {
            case "ICT2113": return 15.0;
            case "ICT2122": return 19.5;
            case "ICT2132": return 15.0;
            case "ICT2142": return 19.5;
            case "ICT2152": return 15.0;
            default: return 20.0;
        }
    }

}
