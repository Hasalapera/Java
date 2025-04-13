import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

public class Grade_GPU extends JFrame {
    private JPanel MainFrame;
    private JTextField Stu_number;
    private JButton showButton;
    private JTable Grade_GPA_Table;
    private JButton showButton2;

    public Grade_GPU() {
        setContentPane(MainFrame);
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Grade And GPA");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        showButton.addActionListener(new ActionListener() {
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
        showButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowTable();
            }
        });
    }

    private void ShowTable(String studentId) {
        String url = "jdbc:mysql://localhost:3306/techmis";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_id FROM marks");
            ResultSet courseRs = courseStmt.executeQuery();
            List<String> course_codes = new ArrayList<>();
            while (courseRs.next()) {
                course_codes.add(courseRs.getString("Course_id"));
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

                double totalWeightedGPA = 0.0;
                double totalCredits = 0.0;

                for (int i = 0; i < course_codes.size(); i++) {
                    String courseId = course_codes.get(i);

                    double totalMarks = Calculatetotalmarks(studentId, courseId);
                    String grade = CalculateGrade(totalMarks);
                    double credit = getCredit(courseId);
                    double gpa = getGpaValue(grade);

                    row[i + 1] = grade;

                    totalWeightedGPA += gpa * credit;
                    totalCredits += credit;
                }

                if (totalCredits > 0) {
                    double finalGPA = totalWeightedGPA / totalCredits;
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

    private void ShowTable() {
        String url = "jdbc:mysql://localhost:3306/techmis";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_id FROM marks");
            ResultSet courseRs = courseStmt.executeQuery();
            List<String> course_codes = new ArrayList<>();
            while (courseRs.next()) {
                course_codes.add(courseRs.getString("Course_id"));
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

                double totalWeightedGPA = 0.0;
                double totalCredits = 0.0;

                for (int i = 0; i < course_codes.size(); i++) {
                    String courseId = course_codes.get(i);

                    double totalMarks = Calculatetotalmarks(stu_id, courseId);
                    String grade = CalculateGrade(totalMarks);
                    double credit = getCredit(courseId);
                    double gpa = getGpaValue(grade);

                    row[i + 1] = grade;

                    totalWeightedGPA += gpa * credit;
                    totalCredits += credit;
                }

                if (totalCredits > 0) {
                    double finalGPA = totalWeightedGPA / totalCredits;
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

    private double Calculatetotalmarks(String stu_id, String course_id) {
        double totalmarks = 0.0;
        String url = "jdbc:mysql://localhost:3306/techmis";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement markpstm = con.prepareStatement(
                    "SELECT Quiz_01, Quiz_02, Quiz_03, Quiz_04, Assignment, Mid_theory, Mid_practical, End_theory, End_practical " +
                            "FROM Marks WHERE Stu_id = ? AND Course_id = ?"
            );
            markpstm.setString(1, stu_id);
            markpstm.setString(2, course_id);

            ResultSet rs = markpstm.executeQuery();
            if (rs.next()) {
                double q1 = rs.getDouble("Quiz_01");
                double q2 = rs.getDouble("Quiz_02");
                double q3 = rs.getDouble("Quiz_03");
                double q4 = rs.getDouble("Quiz_04");
                double assignment = rs.getDouble("Assignment");
                double mid_theory = rs.getDouble("Mid_theory");
                double mid_practical = rs.getDouble("Mid_practical");
                double end_theory = rs.getDouble("End_theory");
                double end_practical = rs.getDouble("End_practical");

                double[] quizzes = {q1, q2, q3, q4};
                Arrays.sort(quizzes);
                double quizTotal = quizzes[2] + quizzes[3];

                totalmarks = quizTotal + assignment + mid_theory + mid_practical + end_theory + end_practical;
            }

            con.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }

        return totalmarks;
    }

    private String CalculateGrade(double totalmarks) {
        if (totalmarks >= 85) return "A+";
        else if (totalmarks >= 70) return "A";
        else if (totalmarks >= 65) return "A-";
        else if (totalmarks >= 60) return "B+";
        else if (totalmarks >= 55) return "B";
        else if (totalmarks >= 50) return "B-";
        else if (totalmarks >= 45) return "C+";
        else if (totalmarks >= 40) return "C";
        else if (totalmarks >= 35) return "C-";
        else if (totalmarks >= 30) return "D+";
        else if (totalmarks >= 25) return "D";
        else return "E";
    }

    private double getCredit(String course_id) {
        double credit = 0.0;
        String url = "jdbc:mysql://localhost:3306/techmis";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement ps = con.prepareStatement("SELECT Credit FROM course WHERE Course_code = ?");
            ps.setString(1, course_id);
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
