package Lecture;

import database.DatabaseConnection;
import database.Session;
import student.Login;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private JTextField Stu_number;
    private JButton gradegpuuniqshowButton;
    private JButton gradegpuallshowButton;
    private JTable Grade_GPA_Table;
    private JPanel GradeMainpanle;

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

    String User;

    public LecHome(String User_ID) {

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Lecture Home");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setVisible(true);

        displayProfileDetils();
        showProfilePicture(imageLbl);

        User=User_ID;

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
                showmarkstable(User_ID);
                coursecodeselection(User_ID);
            }
        });
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    new Lec_profileupdate();
                dispose();

            }
        });
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addmarks(User_ID);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectdeletingrecord(User_ID);
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = ""                        ;
                new Login();
                dispose();
            }
        });
        gradeAndGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "gradeGPACard");
                Gradegpushowtable();
            }
        });
        gradegpuallshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            Gradegpushowtable();
            }
        });
        gradegpuuniqshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = Stu_number.getText().trim();
                if (!studentId.isEmpty()) {
                    Gradegpushowtable(studentId);
                } else {
                    JOptionPane.showMessageDialog(MainFrame, "Please enter a student ID.");
                }
            }
        });
    }

//    ***** get courses ********

    public List<String> getAllCourseCodes() {
        List<String> courseCodes = new ArrayList<>();
        Connection con = DatabaseConnection.connect();
        try {
            String sql = "SELECT Course_code FROM Course";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courseCodes.add(rs.getString("Course_code"));
            }
        } catch (Exception e) {
            System.out.println("Error in getAllCourseCodes: " + e.getMessage());
        }
        return courseCodes;
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

    // ******* Add marks *****************

    public void showmarkstable(String User_ID){
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";
        try {

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e){
                JOptionPane.showMessageDialog(MainFrame, e);
            }
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM marks WHERE Lec_id=?"); {

                pst.setString(1, User_ID);
                ResultSet rs = pst.executeQuery();

                DefaultTableModel model=new DefaultTableModel();

                model.addColumn("Mark ID");
                model.addColumn("Student ID");
                model.addColumn("Lecturer ID");
                model.addColumn("Course Code");
                model.addColumn("Assignment 01");
                model.addColumn("Assignment 02");
                model.addColumn("Quiz_01");
                model.addColumn("Quiz_02");
                model.addColumn("Quiz_03");
                model.addColumn("Quiz_04");
                model.addColumn("Mid Exam Theory");
                model.addColumn("Mid Exam Practical");
                model.addColumn("End Exam Theory");
                model.addColumn("End Exam Practical");

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("Mark_id"),
                            rs.getString("Stu_id"),
                            rs.getString("Lec_id"),
                            rs.getString("Course_code"),
                            rs.getString("Assignment_01"),
                            rs.getString("Assignment_02"),
                            rs.getString("Quiz_01"),
                            rs.getString("Quiz_02"),
                            rs.getString("Quiz_03"),
                            rs.getString("Quiz_04"),
                            rs.getString("Mid_theory"),
                            rs.getString("Mid_practical"),
                            rs.getString("End_theory"),
                            rs.getString("End_practical")
                    });
                }
                marktable.setModel(model);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    public void addmarks(String User_ID){
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        String MarkID = Mark_id_textfield.getText();
        String studentId = student_id_textField.getText();
        String coursecode = (String) coursecodecomboBox.getSelectedItem();
        String markType = (String) mark_type_comboBox.getSelectedItem();
        String mark = mark_textField.getText();

        if (studentId.isEmpty() || MarkID.isEmpty() || mark.isEmpty()) {
            JOptionPane.showMessageDialog(MainFrame, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double markValue;

        try {
            markValue = Double.parseDouble(mark);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(MainFrame, "Only Accept numeric values", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(MainFrame, e);
            }

            // 1. Check if student + course already exists
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM marks WHERE Stu_id=? AND Course_code=?");
            checkStmt.setString(1, studentId);
            checkStmt.setString(2, coursecode);
            ResultSet rs = checkStmt.executeQuery();

            // 2. If not exists, insert with all zero values
            if (!rs.next()) {
                // Prepare the SQL query to insert values into the table
                PreparedStatement insertStmt = conn.prepareStatement(
                        "INSERT INTO marks (Mark_id,Stu_id, Course_code,Lec_id, Assignment_01,Assignment_02, Quiz_01, Quiz_02, Quiz_03, Quiz_04, Mid_theory, Mid_practical, End_theory, End_practical) " +
                                "VALUES (?,?, ?, ?, 0,0,0,0,0,0,0,0,0,0)");

                // Set the parameters for the SQL statement
                insertStmt.setString(1, MarkID);
                insertStmt.setString(2, studentId);
                insertStmt.setString(3, coursecode);
                insertStmt.setString(4, User_ID);

                insertStmt.executeUpdate();
            }

            // 3. Update the selected category with the actual mark
            String updateQuery = "UPDATE marks SET " + markType + "=? WHERE Stu_id=? AND Course_code=? AND Mark_id=?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setDouble(1, markValue);
            updateStmt.setString(2, studentId);
            updateStmt.setString(3, coursecode);
            updateStmt.setString(4, MarkID);
            updateStmt.executeUpdate();
            if(updateStmt.executeUpdate() == 0){
                JOptionPane.showMessageDialog(MainFrame, "No record was updated", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(MainFrame, "Mark updated successfully!");
            }
            student_id_textField.setText("");
            Mark_id_textfield.setText("");
            mark_textField.setText("");
            mark_type_comboBox.setSelectedIndex(-1);
            coursecodecomboBox.setSelectedIndex(-1);

            showmarkstable(User);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    public void deleteRecordFromTable(String markID){
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(MainFrame, e);
            }
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pstm = con.prepareStatement("DELETE FROM marks WHERE Mark_id=?");
            pstm.setString(1,markID);
            int rowsAffected = pstm.executeUpdate();
            if(rowsAffected<0){
                JOptionPane.showMessageDialog(MainFrame, "No record was deleted", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    public void selectdeletingrecord(String User){
        int selectedRow = marktable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MainFrame, "Please select a row to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
        else {
            String markID = marktable.getModel().getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(MainFrame, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteRecordFromTable(markID);
                showmarkstable(User);
            }}
    }

    public void coursecodeselection(String User_ID){
        String url = "jdbc:mysql://localhost:3306/techlms";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT Course_code FROM course WHERE Lec_id = ?");
            ps.setString(1, User_ID);
            ResultSet rs = ps.executeQuery();

            coursecodecomboBox.removeAllItems();
            while (rs.next()) {
                coursecodecomboBox.addItem(rs.getString("Course_code"));
            }

            coursecodecomboBox.setSelectedIndex(-1);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    // ******* Grade and GPU *****************

    private void Gradegpushowtable(String studentId) {
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
                JOptionPane.showMessageDialog(MainFrame, "Student ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            while (studentRs.next()) {
                String stu_id = studentRs.getString("Stu_id");
                Object[] row = new Object[columns.length];
                row[0] = stu_id;

                double totalGPA = 0.0;
                double totalCredits = 0.0;

                for (int i = 0; i < course_codes.size(); i++) {
                    String courseId = course_codes.get(i);
                    double totalMarks = Calculatetotalmarks(stu_id, courseId);

                    double credit = getCredit(courseId);
                    String grade = CalculateGrade(totalMarks);
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

    private void Gradegpushowtable() {

        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            // Get course codes from marks table
            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM marks");
            ResultSet courseRs = courseStmt.executeQuery();

            List<String> course_codes = new ArrayList<>();
            while (courseRs.next()) {
                course_codes.add(courseRs.getString("Course_code"));
            }

            // Set up table columns
            String[] columns = new String[course_codes.size() + 2];
            columns[0] = "Stu_id";
            for (int i = 0; i < course_codes.size(); i++) {
                columns[i + 1] = course_codes.get(i);
            }
            columns[columns.length - 1] = "GPA";
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            // Get distinct student IDs
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

                    double totalMarks = Calculatetotalmarks(stu_id, courseId);
                    double credit = getCredit(courseId);
                    String grade = CalculateGrade(totalMarks);
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

    private double Calculatetotalmarks(String stu_id,String course_Id) {

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
            markpstm.setString(2, course_Id);

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


                switch (course_Id.trim()){

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

    private double getCredit(String course_Id) {
        double credit = 0.0;
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            PreparedStatement ps = con.prepareStatement("SELECT Credit FROM course WHERE Course_code = ?");
            ps.setString(1, course_Id);
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

}
