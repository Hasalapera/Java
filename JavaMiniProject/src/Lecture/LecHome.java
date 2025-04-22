package Lecture;

import database.DatabaseConnection;
import student.Login;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.nio.file.*;

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
    private JButton CAEligibilityButton;
    private JButton undergraduateDetailsButton;
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
    private JPanel CACard;
    private JPanel noticeCard;
    private JLabel noticeHeadingLbl;
    private JLabel selectTitleLbl;
    private JComboBox selectTitleCombo;
    private JPanel noticeTxtAreaPanel;
    private JScrollPane noticeScrollPane;
    private JTextArea noticeTxtArea;
    private JPanel UgdetailsCard;
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
    private JButton uniqugdetailsshowButton;
    private JButton allugdetailsshowButton;
    private JTable Stu_details_table;
    private JTextField ugStudentNumber;
    private JButton attuniqoneshowButton;
    private JButton AllshowButton;
    private JButton allAttendanceButton;
    private JButton allMedicelsButton;
    private JButton attcaeligibilityButton;
    private JTable Attendance_table;
    private JTextField attStu_number;
    private JTextField CAstu_numbertextField;
    private JButton Uniq_stu_CA_button;
    private JTable CAEligibilitytable;
    private JButton AllCAbutton;
    private JButton Addmaterialsbtm;
    private JPanel AddmaterialsCard;
    private JTable Materials_Table;
    private JButton lecmaterialsDeletebutton;
    private JComboBox lecmaterialscoursecodedropdown;
    private JButton lecmaterialsAddbutton;

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

        User=User_ID;

        displayProfileDetils(User_ID);
        showProfilePicture( User,imageLbl);

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

                    new Lec_profileupdate(User_ID);

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
        undergraduateDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "UgdetailsCard");
                allUgraduatesDetails();
            }
        });
        allugdetailsshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allUgraduatesDetails();
            }
        });
        uniqugdetailsshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String st_Number = ugStudentNumber.getText().trim();
                if (st_Number.isEmpty()) {
                    JOptionPane.showMessageDialog(MainFrame, "Please enter a student ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    filterUgraduatesDetails(st_Number);
                }
            }
        });
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "attendanceCard");
            }
        });
        allAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attendanceTable(User_ID);
            }
        });
        allMedicelsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allMedicels(User_ID);
            }
        });

        AllshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allstudentattendanceprecent(User_ID);
            }
        });
        attuniqoneshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String attstunum=attStu_number.getText().trim();
                if (attstunum.isEmpty()){
                    JOptionPane.showMessageDialog(MainFrame, "Please enter a student ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    uniqstudentattendancepresent(attstunum,User_ID);
                }
            }
        });
        CAEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "CACard");
            }
        });
        AllCAbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allcamarks(User);
            }
        });
        Uniq_stu_CA_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CA_Stu_Number =CAstu_numbertextField.getText().trim();
                if(CA_Stu_Number.isEmpty()){
                    JOptionPane.showMessageDialog(MainFrame, "Please enter a student ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    uniqcamarks(CA_Stu_Number,User);
                }
            }
        });
        attcaeligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            new Att_CA();
            }
        });
        selectTitleCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    // Get the selected title
                    String selectedTitle = (String) selectTitleCombo.getSelectedItem();
                    System.out.println("Selected Title: " + selectedTitle);
                    // Display the content for the selected title
                    if (selectedTitle != null) {
                        displayNoticeContent(selectedTitle);
                    }
                }
        });
        noticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "noticeCard");
                addNoticeTitlesToComboBox();
            }
        });
        deleteProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProfilePicture(User,imageLbl);
                deleteProfilePictureButton.setEnabled(false);
            }
        });
        Addmaterialsbtm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "AddmaterialsCard");
                showmaterilstable(User_ID);
                populateCourseComboBox(User_ID);
            }
        });
        lecmaterialsAddbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadfile(User_ID);
            }
        });
        lecmaterialsDeletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletematerial(User_ID);
            }
        });
        Mark_id_textfield.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Mark_id_textfield.getText().isEmpty()) {
                    String generatedId = generateNextMarkID();
                    Mark_id_textfield.setText(generatedId);
                }
            }
        });
    }

    // ******* Display Profile Details *****************

    public void displayProfileDetils(String User_ID){
        con = DatabaseConnection.connect();

        try {
            String sql = "SELECT FName, LName, Address, Email, Phone_No, Role FROM User WHERE UserName = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, User_ID);

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

    // *******  Profile Picture *****************

    public void showProfilePicture(String User,JLabel imageLbl) {
        Connection con = DatabaseConnection.connect();
        try {
            String sql = "SELECT Profile_pic FROM user WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1,User);
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

    public void deleteProfilePicture(String User,JLabel imageLbl) {
        Connection con = DatabaseConnection.connect();
        try{
            String sql = "UPDATE user SET Profile_pic = NULL WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, User);

            int result = pst.executeUpdate();

            if (result > 0) {
                // Set default image after deletion
                String path = "JavaMiniProject/user_Pro_Pic/default.png";

                // Get label size
                int width = imageLbl.getWidth();
                int height = imageLbl.getHeight();

                if (width == 0 || height == 0) {
                    width = 150;
                    height = 150;
                }

                ImageIcon imageIcon = new ImageIcon(path);
                Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLbl.setIcon(new ImageIcon(image));
                imageLbl.repaint();  // Refresh label

                System.out.println("Profile picture deleted successfully.");
            } else {
                System.out.println("No profile picture was found or username invalid.");
            }

        } catch (Exception e) {
            System.out.println("Error in deleteProfilePicture: " + e.getMessage());
        }
    }

    // ******* Add marks *****************

    public void showmarkstable(String User_ID){
       con = DatabaseConnection.connect();
        try {

            PreparedStatement pst = con.prepareStatement("SELECT * FROM marks WHERE Lec_id=?"); {

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

    private String generateNextMarkID() {
        String nextId = "MK001"; // default ID
        try {
            con = DatabaseConnection.connect();
            String sql = "SELECT Mark_id FROM marks ORDER BY Mark_id DESC LIMIT 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String lastId = rs.getString("Mark_id"); // e.g., MK005
                int num = Integer.parseInt(lastId.substring(2)); // remove 'MK'
                num++;
                nextId = String.format("MK%03d", num); // MK006
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(MainFrame, "Failed to generate Mark ID: " + e.getMessage());
        }

        return nextId;
    }

    public void addmarks(String User_ID){
        con = DatabaseConnection.connect();

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
            PreparedStatement checkStmt = con.prepareStatement("SELECT * FROM marks WHERE Stu_id=? AND Course_code=?");
            checkStmt.setString(1, studentId);
            checkStmt.setString(2, coursecode);
            ResultSet rs = checkStmt.executeQuery();

            // 2. If not exists, insert with all zero values
            if (!rs.next()) {
                // Prepare the SQL query to insert values into the table
                PreparedStatement insertStmt = con.prepareStatement(
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
            PreparedStatement updateStmt = con.prepareStatement(updateQuery);
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
        con = DatabaseConnection.connect();

        try {
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
        con = DatabaseConnection.connect();

        try {

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
        con = DatabaseConnection.connect();

        try {

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

        con = DatabaseConnection.connect();

        try {

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

        con = DatabaseConnection.connect();

        try {
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

        con = DatabaseConnection.connect();

        try {

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

    // ******* Undergraduate Details *****************

    private void filterUgraduatesDetails(String stu_number) {

        con = DatabaseConnection.connect();
        try {

            if (stu_number.startsWith("tg")||stu_number.startsWith("TG")) {

                PreparedStatement pstm = con.prepareStatement("SELECT * FROM user WHERE UPPER(UserName) LIKE ?");
                pstm.setString(1, stu_number);

                ResultSet rs = pstm.executeQuery();

                String[] columnNames = {"Student ID", "Name","Date Of Birth", "Enrollment Date", "Address", "Email", "Phone Number"};
                DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("UserName"),
                            rs.getString("Fname")+" "+rs.getString("Lname"),
                            rs.getString("DoB"),
                            rs.getString("Enrollment_Date"),
                            rs.getString("Address"),
                            rs.getString("Email"),
                            rs.getString("Phone_No")
                    });
                }

                Stu_details_table.setModel(model);
                ugStudentNumber.setText("");

                rs.close();
                pstm.close();
                con.close();
            }
            else {
                JOptionPane.showMessageDialog(null, "Access denied", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(MainFrame, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void allUgraduatesDetails(){

        con = DatabaseConnection.connect();

        try{
            PreparedStatement pstm = con.prepareStatement(" SELECT * FROM user WHERE LOWER(UserName)LIKE ?");
            pstm.setString(1,"tg%");
            ResultSet rs = pstm.executeQuery();

            String[] columnNames = {"Student ID", "Name","Date Of Birth", "Enrollment Date", "Address", "Email", "Phone Number"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("UserName"),
                        rs.getString("Fname")+" "+rs.getString("Lname"),
                        rs.getString("DoB"),
                        rs.getString("Enrollment_Date"),
                        rs.getString("Address"),
                        rs.getString("Email"),
                        rs.getString("Phone_No")
                });
            }

            Stu_details_table.setModel(model);
            Stu_number.setText("");

            rs.close();
            pstm.close();
            con.close();

        }catch (SQLException x){
            JOptionPane.showMessageDialog(MainFrame,x);
        }
    }

    // ******* Attendance Eligibility *****************

    private void attendanceTable(String User) {

        con = DatabaseConnection.connect();
        try {

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM course WHERE Lec_id = ?");
            courseStmt.setString(1, User);
            ResultSet courseRs = courseStmt.executeQuery();

            List<String> lecturerCourses = new ArrayList<>();
            while (courseRs.next()) {
                lecturerCourses.add(courseRs.getString("Course_code"));
            }

            String[] Column = new String[3 + 15];
            Column[0] = "Stu_id";
            Column[1] = "Course_code";
            Column[2] = "Course_type";
            for (int i = 1; i <= 15; i++) {
                Column[2 + i] = "Week " + i;
            }

            Map<String, String[]> studentMap = new LinkedHashMap<>();

            for (String Course_code : lecturerCourses) {
                PreparedStatement pstm = con.prepareStatement("SELECT * FROM attendance WHERE Course_code = ?");
                pstm.setString(1, Course_code);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    String Stu_id = rs.getString("Stu_id");
                    String Course_type = rs.getString("Course_type");
                    String status = rs.getString("Status");
                    int week_no = rs.getInt("Week_No");

                    String key = Stu_id + "_" + Course_code + "_" + Course_type;

                    if (!studentMap.containsKey(key)) {
                        String[] row = new String[3 + 15];
                        row[0] = Stu_id;
                        row[1] = Course_code;
                        row[2] = Course_type;
                        studentMap.put(key, row);
                    }

                    String[] existingRow = studentMap.get(key);
                    if (week_no >= 1 && week_no <= 15) {
                        existingRow[2 + week_no] = status;
                    }
                }
            }

            String[][] data = new String[studentMap.size()][3 + 15];
            int i = 0;
            for (String[] row : studentMap.values()) {
                data[i++] = row;
            }

            Attendance_table.setModel(new javax.swing.table.DefaultTableModel(data, Column));
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private void allMedicels(String User) {

        con=DatabaseConnection.connect();

        try{

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM course WHERE Lec_id = ?");
            courseStmt.setString(1, User);
            ResultSet courseRs = courseStmt.executeQuery();

            List<String> lecturerCourses = new ArrayList<>();
            while (courseRs.next()) {
                lecturerCourses.add(courseRs.getString("Course_code"));
            }

            String[] Column = {"Stu_id", "Course_code", "Week_No", "Day_No", "Status"};
            DefaultTableModel model = new DefaultTableModel(null, Column);
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            boolean found = false;

            for (String Course_code : lecturerCourses) {
                PreparedStatement pstm = con.prepareStatement("SELECT * FROM Medical WHERE Course_code=? ORDER BY Stu_id");
                pstm.setString(1, Course_code);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    found = true;
                    String Stu_id = rs.getString("Stu_id");
                    String Medi_Course_code = rs.getString("Course_code");
                    String Week_No = rs.getString("Week_No");
                    String Day_No = rs.getString("Day_No");
                    String Status = rs.getString("Status");

                    model.addRow(new String[]{Stu_id, Medi_Course_code, Week_No, Day_No, Status});
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(MainFrame, "Medical records not found", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Attendance_table.setModel(model);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    private void allstudentattendanceprecent(String User) {

        con=DatabaseConnection.connect();

        try {
            String[] Column = {"Stu_id", "Course_Code", "Percentage", "Eligibility"};
            DefaultTableModel model = new DefaultTableModel(null, Column);
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM course WHERE Lec_id = ?");
            courseStmt.setString(1, User);
            ResultSet courseRS = courseStmt.executeQuery();

            while (courseRS.next()) {
                String Course_code = courseRS.getString("Course_code");

                PreparedStatement studentStmt = con.prepareStatement("SELECT DISTINCT Stu_id FROM attendance WHERE Course_Code=?");
                studentStmt.setString(1, Course_code);
                ResultSet studentRS = studentStmt.executeQuery();

                while (studentRS.next()){
                    String Stu_id = studentRS.getString("Stu_id");

                    PreparedStatement typeCheck = con.prepareStatement("SELECT DISTINCT Course_type FROM attendance WHERE Stu_id = ? AND Course_Code = ?");
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

                    double totalTheory = 0, presentTheory = 0;
                    double totalPractical = 0, presentPractical = 0;

                    PreparedStatement pstmTheory = con.prepareStatement("SELECT Lec_hour, Status FROM attendance WHERE Stu_id = ? AND Course_Code = ? AND Course_type = ?");
                    pstmTheory.setString(1, Stu_id);
                    pstmTheory.setString(2, Course_code);
                    pstmTheory.setString(3, "Theory");
                    ResultSet rsTheory = pstmTheory.executeQuery();
                    while (rsTheory.next()) {
                        String status = rsTheory.getString("Status");
                        double lecHour = rsTheory.getDouble("Lec_hour");

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
                    PreparedStatement pstmPractical = con.prepareStatement("SELECT Lec_hour, Status FROM attendance WHERE Stu_id = ? AND Course_Code = ? AND Course_type = ?");
                    pstmPractical.setString(1, Stu_id);
                    pstmPractical.setString(2, Course_code);
                    pstmPractical.setString(3, "Practical");
                    ResultSet rsPractical = pstmPractical.executeQuery();

                    while (rsPractical.next()) {
                        String status = rsPractical.getString("Status");
                        double lecHour = rsPractical.getDouble("Lec_hour");

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

                    double theoryPercent =(presentTheory / totalTheory) * 100;
                    double practicalPercent =(presentPractical / totalPractical) * 100;
                    double totalAll = totalTheory + totalPractical;
                    double presentAll = presentTheory + presentPractical;
                    double combinedPercent =(presentAll / totalAll) * 100;

                    String theoryEligibility = (theoryPercent >= 80) ? "Eligible" : "Not Eligible";
                    String practicalEligibility = (practicalPercent >= 80) ? "Eligible" : "Not Eligible";
                    String combinedEligibility = (combinedPercent >= 80) ? "Eligible" : "Not Eligible";

                    if (hasTheory && hasPractical) {
                        model.addRow(new Object[]{Stu_id, Course_code + "-T", String.format("%.2f", theoryPercent) + "%", theoryEligibility});
                        model.addRow(new Object[]{Stu_id, Course_code + "-P", String.format("%.2f", practicalPercent) + "%", practicalEligibility});
                        model.addRow(new Object[]{Stu_id, Course_code + "T,P", String.format("%.2f", combinedPercent) + "%", combinedEligibility});

                    } else if (hasTheory) {
                        model.addRow(new Object[]{Stu_id, Course_code, String.format("%.2f", theoryPercent) + "%", theoryEligibility});

                    } else if (hasPractical) {
                        model.addRow(new Object[]{Stu_id, Course_code, String.format("%.2f", practicalPercent) + "%", practicalEligibility});

                    }

                }

            }

            Attendance_table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private void uniqstudentattendancepresent(String Stu_id,String User) {

        con = DatabaseConnection.connect();

        try {
            String[] Column = {"Stu_id", "Course_Code", "Percentage", "Eligibility"};
            DefaultTableModel model = new DefaultTableModel(null, Column);
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            PreparedStatement coursestm = con.prepareStatement("SELECT DISTINCT Course_code FROM course WHERE Lec_id = ?");
            coursestm.setString(1, User);
            ResultSet courseRS = coursestm.executeQuery();

            while (courseRS.next()) {
                String Course_code1 = courseRS.getString("Course_code");

                PreparedStatement typeCheck = con.prepareStatement("SELECT DISTINCT Course_type FROM attendance WHERE Stu_id = ? AND Course_Code = ?");
                typeCheck.setString(1, Stu_id);
                typeCheck.setString(2, Course_code1);
                ResultSet typeRS = typeCheck.executeQuery();

                boolean hasTheory = false;
                boolean hasPractical = false;

                while (typeRS.next()) {
                    String type = typeRS.getString("Course_type");
                    if (type.equalsIgnoreCase("Theory")) hasTheory = true;
                    if (type.equalsIgnoreCase("Practical")) hasPractical = true;
                }

                double totalTheory = 0, presentTheory = 0;
                double totalPractical = 0, presentPractical = 0;

                PreparedStatement pstmTheory = con.prepareStatement("SELECT Lec_hour, Status FROM attendance WHERE Stu_id = ? AND Course_Code = ? AND Course_type = ?");
                pstmTheory.setString(1, Stu_id);
                pstmTheory.setString(2, Course_code1);
                pstmTheory.setString(3, "Theory");
                ResultSet rsTheory = pstmTheory.executeQuery();
                while (rsTheory.next()) {
                    double lecHour = rsTheory.getDouble("Lec_hour");
                    String status = rsTheory.getString("Status");
                    totalTheory += lecHour;
                    if (status.equalsIgnoreCase("Present")) {
                        presentTheory += lecHour;
                    } else if (status.equalsIgnoreCase("Medical")) {
                        String medicalStatus = checkMedicalStatus(Stu_id, Course_code1);
                        if (medicalStatus.equalsIgnoreCase("Approved")) {
                            presentTheory += lecHour;
                        }
                    }
                }

                PreparedStatement pstmPractical = con.prepareStatement("SELECT Lec_hour, Status FROM attendance WHERE Stu_id = ? AND Course_Code = ? AND Course_type = ?");
                pstmPractical.setString(1, Stu_id);
                pstmPractical.setString(2, Course_code1);
                pstmPractical.setString(3, "Practical");
                ResultSet rsPractical = pstmPractical.executeQuery();

                while (rsPractical.next()) {
                    double lecHour = rsPractical.getDouble("Lec_hour");
                    String status = rsPractical.getString("Status");
                    totalPractical += lecHour;
                    if (status.equalsIgnoreCase("Present")) {
                        presentPractical += lecHour;
                    } else if (status.equalsIgnoreCase("Medical")) {
                        String medicalStatus = checkMedicalStatus(Stu_id, Course_code1);
                        if (medicalStatus.equalsIgnoreCase("Approved")) {
                            presentPractical += lecHour;
                        }
                    }
                }

                double theoryPercent = (presentTheory / totalTheory) * 100;
                double practicalPercent = (presentPractical / totalPractical) * 100;
                double totalAll = totalTheory + totalPractical;
                double presentAll = presentTheory + presentPractical;
                double combinedPercent = (presentAll / totalAll) * 100;

                String theoryEligibility = (theoryPercent >= 80) ? "Eligible" : "Not Eligible";
                String practicalEligibility = (practicalPercent >= 80) ? "Eligible" : "Not Eligible";
                String combinedEligibility = (combinedPercent >= 80) ? "Eligible" : "Not Eligible";

                if (hasTheory && hasPractical) {
                    model.addRow(new Object[]{Stu_id, Course_code1 + "-T", String.format("%.2f", theoryPercent) + "%", theoryEligibility});
                    model.addRow(new Object[]{Stu_id, Course_code1 + "-P", String.format("%.2f", practicalPercent) + "%", practicalEligibility});
                    model.addRow(new Object[]{Stu_id, Course_code1 + "-T,P", String.format("%.2f", combinedPercent) + "%", combinedEligibility});

                } else if (hasTheory) {
                    model.addRow(new Object[]{Stu_id, Course_code1, String.format("%.2f", theoryPercent) + "%", theoryEligibility});
                } else if (hasPractical) {
                    model.addRow(new Object[]{Stu_id, Course_code1, String.format("%.2f", practicalPercent) + "%", practicalEligibility});
                }

            }

            Attendance_table.setModel(model);
            showsingleattendancerecord(Stu_id,User);

        }catch(Exception e){
                JOptionPane.showMessageDialog(MainFrame, e);
            }
        }

    private void showsingleattendancerecord(String Stu_id,String User){

        con=DatabaseConnection.connect();

        try{
            PreparedStatement getCourses = con.prepareStatement("SELECT DISTINCT Course_Code FROM course WHERE Lec_id = ?");
            getCourses.setString(1, User);
            ResultSet courseRS = getCourses.executeQuery();

            String[] Column =new String[3+15];
            Column[0]="Stu_id";
            Column[1]="Course_code";
            Column[2]="Course_type";
            for (int i = 1; i <= 15; i++) {
                Column[2 + i] = "Week " + i;
            }

            Map<String,String[]>studentmap=new LinkedHashMap<>();

            while (courseRS.next()){
                String Course_code = courseRS.getString("Course_Code");

                PreparedStatement pstm = con.prepareStatement("SELECT * FROM attendance WHERE Stu_id=? AND Course_code=? ORDER BY Stu_id, Course_code, Week_No");
                pstm.setString(1,Stu_id);
                pstm.setString(2,Course_code);
                ResultSet rs = pstm.executeQuery();

                while (rs.next()) {
                    String Course_type=rs.getString("Course_type");
                    String status=rs.getString("Status");

                    int week_no=rs.getInt("Week_No");

                    String key=Stu_id+"_"+Course_code+"_"+Course_type;

                    if(!studentmap.containsKey(key)){
                        String[] row=new String[3+15];
                        row[0]=Stu_id;
                        row[1]=Course_code;
                        row[2]=Course_type;
                        studentmap.put(key,row);
                    }
                    String[] existingRow=studentmap.get(key);
                    existingRow[2+week_no]=status;

                }

            }
            String[][] data = new String[studentmap.size()][3 + 15];
            int i = 0;
            for (String[] row : studentmap.values()) {
                data[i++] = row;
            }

            JFrame attendanceFrame = new JFrame("Attendance Records - Student: " + Stu_id);
            attendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            attendanceFrame.setSize(1000, 400); // Wider for more weeks
            JTable table = new JTable(data, Column);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);
            attendanceFrame.add(scrollPane);
            attendanceFrame.setVisible(true);
        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    public String checkMedicalStatus(String Stu_id, String User) {

        con=DatabaseConnection.connect();

        try {
            PreparedStatement getCourses = con.prepareStatement("SELECT DISTINCT Course_Code FROM course WHERE Lec_id = ?"
            );
            getCourses.setString(1, User);
            ResultSet courseRS = getCourses.executeQuery();

            while (courseRS.next()) {
                String Course_code = courseRS.getString("Course_Code");

                PreparedStatement med = con.prepareStatement("SELECT Status FROM medical WHERE Stu_id = ? AND Course_code = ?");
                med.setString(1, Stu_id);
                med.setString(2, Course_code);
                ResultSet rs = med.executeQuery();

                while (rs.next()) {
                    if (rs.getString("Status").equalsIgnoreCase("Approved")) {
                        return "Approved";
                    }
                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
        return "None";
    }

    // ******* CA Eligibility *****************

    private void allcamarks(String User){

        double CA_marks=0.0;

        con=DatabaseConnection.connect();

        try{

            DefaultTableModel model=new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                    "Student_ID","Course Code","Quiz_01", "Quiz_02", "Quiz_03", "Quiz_04",
                    "Assignment_01", "Assignment_02", "Mid_Theory", "Mid_Practical",
                    "CA_Marks", "Eligibility"

            });

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM course WHERE Lec_id = ?");
            courseStmt.setString(1, User);
            ResultSet courseRS = courseStmt.executeQuery();

            while (courseRS.next()) {
                String Course_code = courseRS.getString("Course_code");

                PreparedStatement pstm=con.prepareStatement("select * from marks where Course_code=?");
                pstm.setString(1,Course_code);
                ResultSet rs=pstm.executeQuery();

                while(rs.next()){
                    String stu_id=rs.getString("Stu_id");
                    double assignment1=rs.getDouble("Assignment_01");
                    double assignment2=rs.getDouble("Assignment_02");
                    double Quiz_01=rs.getDouble("Quiz_01");
                    double Quiz_02=rs.getDouble("Quiz_02");
                    double Quiz_03=rs.getDouble("Quiz_03");
                    double Quiz_04=rs.getDouble("Quiz_04");
                    double midtermtheory=rs.getDouble("Mid_theory");
                    double midtermpractical=rs.getDouble("Mid_practical");


                    double CA_cutoff1=19.5;
                    double CA_cutoff2=15.0;
                    String Eligibility="";

                    double[] Quizzes = {Quiz_01, Quiz_02, Quiz_03, Quiz_04};
                    Arrays.sort(Quizzes);
                    double[] Assignments = {assignment1, assignment2};


                    switch (Course_code){
                        case "ICT2113":

                            double quizMark2113 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                            double midtermMark2113 = (midtermpractical + midtermtheory) / 2 * 0.20;
                            CA_marks=quizMark2113+midtermMark2113;
                            if(CA_marks>=CA_cutoff2){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2122":

                            double quizMark2122 = (Quizzes[3] + Quizzes[2]+Quizzes[1]) / 3 * 0.10;
                            CA_marks=quizMark2122+(Assignments[0]*0.10)+(midtermtheory*0.20);
                            if(CA_marks>=CA_cutoff1){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2132":

                            double quizMark2132 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                            double assessmentMark2132 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                            CA_marks=quizMark2132+assessmentMark2132;
                            if(CA_marks>=CA_cutoff2){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2142":

                            double AssessmentMark2142 = (Assignments[0])* 0.20;
                            double MidtermMark2142 = (midtermpractical)* 0.20;
                            CA_marks=AssessmentMark2142+MidtermMark2142;
                            if(CA_marks>=CA_cutoff1){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2152":

                            double quizMark2152 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                            double assessmentMark2152 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                            CA_marks=quizMark2152+assessmentMark2152;
                            if(CA_marks>=CA_cutoff2){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                    }
                    model.addRow(new Object[]{
                            stu_id,Course_code,
                            Quiz_01, Quiz_02, Quiz_03, Quiz_04,
                            assignment1, assignment2,
                            midtermtheory, midtermpractical,
                            String.format("%.2f", CA_marks),
                            Eligibility
                    });
                }
            }



            CAEligibilitytable.setModel(model);

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame, e);
        }
//        return CA_marks;
    }

    private double uniqcamarks(String CA_Stu_Number,String User){

        double CA_marks=0.0;

        con=DatabaseConnection.connect();

        try{

            DefaultTableModel model=new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                    "Student_ID","Course Code", "Quiz_01", "Quiz_02", "Quiz_03", "Quiz_04",
                    "Assignment_01", "Assignment_02", "Mid_Theory", "Mid_Practical",
                    "CA_Marks", "Eligibility"

            });

            PreparedStatement courseStmt = con.prepareStatement("SELECT DISTINCT Course_code FROM course WHERE Lec_id = ?");
            courseStmt.setString(1, User);
            ResultSet courseRS = courseStmt.executeQuery();

            while (courseRS.next()) {
                String Course_code = courseRS.getString("Course_code");

                PreparedStatement pstm=con.prepareStatement("select * from marks where Stu_id=? AND Course_code=?");
                pstm.setString(1,CA_Stu_Number);
                pstm.setString(2,Course_code);
                ResultSet rs=pstm.executeQuery();

                while(rs.next()){

                    double assignment1=rs.getDouble("Assignment_01");
                    double assignment2=rs.getDouble("Assignment_02");
                    double Quiz_01=rs.getDouble("Quiz_01");
                    double Quiz_02=rs.getDouble("Quiz_02");
                    double Quiz_03=rs.getDouble("Quiz_03");
                    double Quiz_04=rs.getDouble("Quiz_04");
                    double midtermtheory=rs.getDouble("Mid_theory");
                    double midtermpractical=rs.getDouble("Mid_practical");


                    double CA_cutoff1=19.5;
                    double CA_cutoff2=15.0;
                    String Eligibility="";

                    double[] Quizzes = {Quiz_01, Quiz_02, Quiz_03, Quiz_04};
                    double[] Assignments = {assignment1, assignment2};

                    switch (Course_code){
                        case "ICT2113":
                            Arrays.sort(Quizzes);
                            double quizMark2113 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                            double midtermMark2113 = (midtermpractical + midtermtheory) / 2 * 0.20;
                            CA_marks=quizMark2113+midtermMark2113;
                            if(CA_marks>=CA_cutoff2){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2122":
                            Arrays.sort(Quizzes);
                            double quizMark2122 = (Quizzes[3] + Quizzes[2]+Quizzes[1]) / 3 * 0.10;
                            CA_marks=quizMark2122+(Assignments[0]*0.10)+(midtermtheory*0.20);
                            if(CA_marks>=CA_cutoff1){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2132":
                            Arrays.sort(Quizzes);
                            double quizMark2133 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                            double assessmentMark2133 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                            CA_marks=quizMark2133+assessmentMark2133;
                            if(CA_marks>=CA_cutoff2){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2142":
                            Arrays.sort(Quizzes);
                            double AssessmentMark2142 = (Assignments[0])* 0.20;
                            double MidtermMark2142 = (midtermpractical)* 0.20;
                            CA_marks=AssessmentMark2142+MidtermMark2142;
                            if(CA_marks>=CA_cutoff1){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                        case "ICT2152":
                            Arrays.sort(Quizzes);
                            double quizMark2152 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                            double assessmentMark2152 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                            CA_marks=quizMark2152+assessmentMark2152;
                            if(CA_marks>=CA_cutoff2){
                                Eligibility="Eligible";
                            }
                            else{
                                Eligibility="Not Eligible";
                            }
                            break;
                    }
                    model.addRow(new Object[]{
                            CA_Stu_Number,Course_code,
                            Quiz_01, Quiz_02, Quiz_03, Quiz_04,
                            assignment1, assignment2,
                            midtermtheory, midtermpractical,
                            String.format("%.2f", CA_marks),
                            Eligibility });
            }

            }

            CAEligibilitytable.setModel(model);
            CAstu_numbertextField.setText("");

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame, e);
        }
        return CA_marks;
    }

    // ******* Notice *****************

    public void addNoticeTitlesToComboBox(){
        con = DatabaseConnection.connect();

        try{
            Connection conn = DatabaseConnection.connect();
            String sql = "SELECT * FROM Notice";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            selectTitleCombo.removeAllItems();

            while (rs.next()) {
                String title = rs.getString("Title");
                selectTitleCombo.addItem(title); // Add each title to the combo box
                System.out.println("Title: " + title);
            }
        }catch(Exception e){
            System.out.println("Error in add Notice Titles To ComboBox: " + e.getMessage());
        }
    }

    public void displayNoticeContent(String title) {
        try {
            noticeTxtArea.setText("");
            // Establish connection to the database to get the NoticeId based on the title
            Connection con = DatabaseConnection.connect();
            String sql = "SELECT Notice_id FROM Notice WHERE Title = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String noticeId = rs.getString("Notice_id");

                // Read content from the corresponding text file (e.g., notice_1.txt)
                File noticeFile = new File("JavaMiniProject/notices/notice_" + noticeId + ".txt");
                System.out.println("noticeFile: " + noticeId+ " Displayed");
                BufferedReader reader = new BufferedReader(new FileReader(noticeFile));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }

                // Display the content in the JTextArea
                noticeTxtArea.setText(content.toString());
            }
        } catch (SQLException | IOException e) {
            System.out.println("Error in display Notice Content: " + e.getMessage());
        }
    }

    // ******* Add Materials *****************

    private boolean listenerAdded = false;

    private void showmaterilstable(String User) {

        con=DatabaseConnection.connect();

        try{
            PreparedStatement ps = con.prepareStatement(
                    "SELECT Material_id,Course_code,Lec_id, file_path, uploaded_on FROM lecture_materials WHERE lec_id = ?");
            ps.setString(1, User);
            ResultSet rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable ( int row, int column){
                return false;
            }
            };
            model.addColumn("Material ID");
            model.addColumn("Course Code");
            model.addColumn("Lec ID");
            model.addColumn("File Path");
            model.addColumn("Uploaded On");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Material_id"),
                        rs.getString("Course_code"),
                        rs.getString("Lec_id"),
                        rs.getString("File_path"),
                        rs.getTimestamp("Uploaded_on")
                });
            }

            Materials_Table.setModel(model);
            Materials_Table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);



            if (!listenerAdded) {
                Materials_Table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount()==2) {
                        int column = Materials_Table.columnAtPoint(e.getPoint());
                        int row = Materials_Table.rowAtPoint(e.getPoint());

                        if (column == 3 && row != -1) {
                            String filePath = Materials_Table.getValueAt(row, column).toString();
                            openMaterial(filePath);
                        }
                    }
                    }
                });
                listenerAdded = true;
            }

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    private void openMaterial(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file); // Opens the file using the default system application
            } catch (IOException e) {
                JOptionPane.showMessageDialog(MainFrame, "Error opening the file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(MainFrame, "File does not exist.");
        }
    }

    private void addmaterials(String path, String user, String courseCode) {
        con = DatabaseConnection.connect();

        try {
            // Step 1: Generate new Mark_ID
            String newMarkId = genaratenextmaterialID();

            // Step 2: Insert material info
            String sql = "INSERT INTO lecture_materials (Material_id, Course_code,Lec_id,File_path) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newMarkId);
            ps.setString(2, courseCode);
            ps.setString(3, User);
            ps.setString(4, path);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(MainFrame, "Lecture material uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(MainFrame, "Failed to upload material.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            lecmaterialscoursecodedropdown.setSelectedIndex(-1);
            populateCourseComboBox(user);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String genaratenextmaterialID() throws SQLException {
        String sql = "SELECT Material_id FROM lecture_materials ORDER BY Material_id DESC LIMIT 1";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String nextId = "M001"; // default starting ID

        if (rs.next()) {
            String lastId = rs.getString("Material_id");
            int num = Integer.parseInt(lastId.substring(1));
            num++; // increment
            nextId = String.format("M%03d", num); // M006
        }

        return nextId;
    }

    private void uploadfile(String User) {

        String Course_code = (String) lecmaterialscoursecodedropdown.getSelectedItem();
        if (Course_code == null) {
            JOptionPane.showMessageDialog(MainFrame, "Please select a course first!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select File");
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, Word, Excel Files", "pdf", "doc", "docx", "xls", "xlsx");
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String fileName = selectedFile.getName();

            // Base folder path
            String baseFolder = "C:\\Users\\ASUS\\Desktop\\Git\\JavaMiniProject\\course_materials";

            // Ensure the course code subfolder is created inside the base folder
            String destFolderPath = baseFolder + File.separator + Course_code;

            File destDir = new File(destFolderPath);
            if (!destDir.exists()) {
                destDir.mkdirs();  // Create course_code folder if not exist
            }

            // Define the full destination file path
            File destFile = new File(destDir, fileName);

            try {
                // Copy the file to the destination folder
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                addmaterials(destFile.getAbsolutePath(), User, Course_code);
                showmaterilstable(User);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "File upload failed: " + ex.getMessage());
            }
        }

    }

    private void deletematerial(String User) {
        con = DatabaseConnection.connect();

        try {
            int selectedRow = Materials_Table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(MainFrame, "Please select to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String materialID = Materials_Table.getModel().getValueAt(selectedRow, 0).toString();
            String filePath = Materials_Table.getModel().getValueAt(selectedRow, 3).toString();

            int confirm = JOptionPane.showConfirmDialog(MainFrame, "Are you sure you want to delete this material?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Delete the file from disk
                File file = new File(filePath);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("File deleted.");
                    } else {
                        System.out.println("Could not delete file.");
                    }
                }

                // Delete record from DB
                PreparedStatement ps = con.prepareStatement("DELETE FROM lecture_materials WHERE Material_id = ?");
                ps.setString(1, materialID);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(MainFrame, "Material deleted successfully.");
                    showmaterilstable(User);
                } else {
                    JOptionPane.showMessageDialog(MainFrame, "No material deleted.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private void populateCourseComboBox(String User) {
        con=DatabaseConnection.connect();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT Course_code FROM course WHERE Lec_id = ?");
            ps.setString(1, User);
            ResultSet rs = ps.executeQuery();

            lecmaterialscoursecodedropdown.removeAllItems();
            while (rs.next()) {
                lecmaterialscoursecodedropdown.addItem(rs.getString("Course_code"));
            }
            lecmaterialscoursecodedropdown.setSelectedIndex(-1);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

}
