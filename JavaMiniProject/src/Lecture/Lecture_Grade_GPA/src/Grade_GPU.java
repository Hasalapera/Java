import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

public class Grade_GPU extends JFrame {
    private JPanel MainFrame;
    private JTextField Stu_number;
    private JButton uniqshowButton;
    private JTable Grade_GPA_Table;
    private JButton allshowButton;

    public Grade_GPU() {
        setContentPane(MainFrame);
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Grade And GPA");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        uniqshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = Stu_number.getText().trim();
                if (!studentId.isEmpty()) {
                    ShowTable(studentId);
                } else {
                    JOptionPane.showMessageDialog(MainFrame, "Please enter a student ID.");
                }
            }
        });
        allshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowTable();
            }
        });
    }

    private void ShowTable(String studentId) {

        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM marks");
            ResultSet courseRs = courseStmt.executeQuery();
            List<String> course_codes = new ArrayList<>();
            while (courseRs.next()) {
                course_codes.add(courseRs.getString("Course_code"));
            }

            // Table column headers
            String[] columns = new String[course_codes.size() + 2];
            columns[0] = "Stu_id";
            for (int i = 0; i < course_codes.size(); i++) {
                columns[i + 1] = course_codes.get(i);
            }
            columns[columns.length - 1] = "GPA";
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            // Only get the requested student
            PreparedStatement studentStmt = con.prepareStatement("SELECT DISTINCT Stu_id FROM marks WHERE Stu_id = ?");
            studentStmt.setString(1, studentId);
            ResultSet studentRs = studentStmt.executeQuery();

            if (!studentRs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(MainFrame, "Student ID not found!");
            }

            while (studentRs.next()) {
                String stu_id = studentRs.getString("Stu_id");
                Object[] row = new Object[columns.length];
                row[0] = stu_id;

                double totalGPA = 0.0;
                double totalCredits = 0.0;

                for (int i = 0; i < course_codes.size(); i++) {
                    String courseId = course_codes.get(i);

                    double totalMarks = Calculatetotalmarks(studentId,courseId);
                    String grade = CalculateGrade(totalMarks);
                    double credit = getCredit(courseId);
                    double gpa = getGpaValue(grade);

                    row[i + 1] = grade;

                    totalGPA += gpa * credit;
                    totalCredits += credit;
                }

                if (totalCredits > 0) {
                    double finalGPA = totalGPA / totalCredits;
                    row[columns.length - 1] = String.format("%.2f", finalGPA);
                } else {
                    row[columns.length - 1] = "Hold";
                }

                model.addRow(row);
            }

            Grade_GPA_Table.setModel(model);
            Stu_number.setText("");
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private void ShowTable() {

        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM marks");
            ResultSet courseRs = courseStmt.executeQuery();

            List<String> course_codes = new ArrayList<>();

            while (courseRs.next()) {
                course_codes.add(courseRs.getString("Course_code"));
            }

            // Table column headers
            String[] columns = new String[course_codes.size() + 2];
            columns[0] = "Stu_id";
            for (int i = 0; i < course_codes.size(); i++) {
                columns[i + 1] = course_codes.get(i);
            }
            columns[columns.length - 1] = "GPA";
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            // Get all student IDs
            PreparedStatement studentStmt = con.prepareStatement("SELECT DISTINCT Stu_id FROM marks");
            ResultSet studentRs = studentStmt.executeQuery();

            while (studentRs.next()) {
                String stu_id = studentRs.getString("Stu_id");
                Object[] row = new Object[columns.length];
                row[0] = stu_id;

                double totalGPA = 0.0;
                double totalCredits = 0.0;

                for (int i = 0; i < course_codes.size(); i++) {
                    String courseId = course_codes.get(i);

                    double totalMarks = Calculatetotalmarks(stu_id,courseId);
                    String grade = CalculateGrade(totalMarks);
                    double credit = getCredit(courseId);
                    double gpa = getGpaValue(grade);

                    row[i + 1] = grade;

                    totalGPA += gpa * credit;
                    totalCredits += credit;
                }

                if (totalCredits > 0) {
                    double finalGPA = totalGPA / totalCredits;
                    row[columns.length - 1] = String.format("%.2f", finalGPA);
                } else {
                    row[columns.length - 1] = "Hold";
                }

                model.addRow(row);
            }

            Grade_GPA_Table.setModel(model);
            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private double Calculatetotalmarks(String stu_id, String courseId) {

        double Final_marks=0.0;

        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement markpstm = con.prepareStatement(
                    "SELECT Quiz_01, Quiz_02, Quiz_03, Quiz_04, Assignment_01,Assignment_02, Mid_theory, Mid_practical, End_theory, End_practical " +
                            "FROM Marks WHERE Stu_id = ? AND Course_code = ?"
            );
            markpstm.setString(1, stu_id);
            markpstm.setString(2, courseId);

            ResultSet rs = markpstm.executeQuery();
            if (rs.next()) {
                double q1 = rs.getDouble("Quiz_01");
                double q2 = rs.getDouble("Quiz_02");
                double q3 = rs.getDouble("Quiz_03");
                double q4 = rs.getDouble("Quiz_04");
                double assignment01 = rs.getDouble("Assignment_01");
                double assignment02 = rs.getDouble("Assignment_02");
                double mid_theory = rs.getDouble("Mid_theory");
                double mid_practical = rs.getDouble("Mid_practical");
                double end_theory = rs.getDouble("End_theory");
                double end_practical = rs.getDouble("End_practical");


                double[] Quizzes = {q1, q2, q3, q4};
                Arrays.sort(Quizzes);
                double[] Assignments = {assignment01, assignment02};


                switch (courseId.trim()){

                    case "ICT2113":
                        double quizMark2113 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                        double midtermMark2113 = (mid_practical + mid_theory) / 2 * 0.20;
                        double Finalmarks2113=(end_theory*0.40)+(end_practical*0.30);
                        Final_marks=quizMark2113+midtermMark2113+Finalmarks2113;
                        break;

                    case "ICT2122":
                        double quizMark2122 = (Quizzes[3] + Quizzes[2]+Quizzes[1]) / 3 * 0.10;
                        Final_marks=quizMark2122+(Assignments[0]*0.10)+(mid_theory*0.20)+(end_theory*0.60);

                        break;

                    case "ICT2132":
                        double quizMark2133 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                        double assessmentMark2133 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                        double Finalmarks2133=(end_practical*0.70);
                        Final_marks=quizMark2133+assessmentMark2133+Finalmarks2133;
                        break;

                    case "ICT2142":

                        double AssessmentMark2142 = (Assignments[0])* 0.20;
                        double MidtermMark2142 = (mid_practical)* 0.20;
                        Final_marks=AssessmentMark2142+MidtermMark2142+(end_practical*0.60);
                        break;

                    case "ICT2152":

                        double quizMark2152 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                        double assessmentMark2152 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                        Final_marks=quizMark2152+assessmentMark2152+(end_theory*0.70);
                        break;
                }
            }

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
return Final_marks;
    }

    private String CalculateGrade(double Final_marks) {
        if (Final_marks >= 85) return "A+";
        else if (Final_marks >= 70) return "A";
        else if (Final_marks >= 65) return "A-";
        else if (Final_marks >= 60) return "B+";
        else if (Final_marks >= 55) return "B";
        else if (Final_marks >= 50) return "B-";
        else if (Final_marks >= 45) return "C+";
        else if (Final_marks >= 40) return "C";
        else if (Final_marks >= 35) return "C-";
        else if (Final_marks >= 30) return "D+";
        else if (Final_marks >= 25) return "D";
        else return "E";
    }

    private double getCredit(String Course_codes) {
        double credit = 0.0;
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement ps = con.prepareStatement("SELECT Credit FROM course WHERE Course_code = ?");
            ps.setString(1, Course_codes);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                credit = rs.getDouble("Credit");
            }

            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }

        return credit;
    }

    private double getGpaValue(String grade) {
        switch (grade) {
            case "A+": case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            case "E": default: return 0.0;
        }
    }

    public static void main(String[] args) {
        new Grade_GPU();
    }
}
