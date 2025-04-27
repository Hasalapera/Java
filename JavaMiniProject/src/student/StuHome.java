package student;

import Admin.ViewUserProfiles;
import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import net.proteanit.sql.DbUtils;


public class StuHome extends JFrame {
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JPanel btnPanel;
    private JPanel cardMainPanel;
    private JButton profileButton;
    private JButton coursesButton;
    private JButton gradeAndGPAButton;
    private JButton attendanceButton;
    private JButton medicalButton;
    private JButton timeTableButton;
    private JTextField fNameTxt;
    private JTextField lNameTxt;
    private JTextField addressTxt;
    private JTextField emailTxt;
    private JTextField pNoTxt;
    private JTextField roleTxt;
    private JPanel imgMainPanel;
    private JPanel imgPanel;
    private JLabel roleLbl;
    private JLabel pNoLbl;
    private JLabel emailLbl;
    private JLabel addressLbl;
    private JLabel lNameLbl;
    private JLabel fNameLbl;
    private JPanel detailPanel;
    private JLabel profileHeadingLbl;
    private JPanel profileCard;
    private JLabel imageLbl;
    private JButton noticeButton;
    private JLabel stuManaSysLbl;
    private JLabel FoTLbl;
    private JPanel coursesCard;
    private JLabel CoursesHeadingLbl;
    private JComboBox comboBoxCourses;
    private JLabel selectCourseLbl;
    private JPanel gradeGPACard;
    private JLabel gradeGPAHeadingLbl;
    private JComboBox selectCrsComboBox;
    private JTextField yourGradeTxt;
    private JTextField sgpaTxt;
    private JTextArea gradetxtArea;
    private JPanel gradeTxtAreaPanel;
    private JLabel yourGradeLbl;
    private JLabel selectCrsLbl;
    private JLabel sgpaLbl;
    private JButton logOutButton;
    private JPanel attendanceCard;
    private JPanel medicalCard;
    private JPanel timeTableCard;
    private JPanel noticeCard;
    private JLabel attendanceHeadingLbl;
    private JLabel medicalHeadingLbl;
    private JLabel noticeHeadingLbl;
    private JComboBox selectAttCourseCombo;
    private JComboBox selectCrsTypeCombo;
    private JLabel selectAttCourseLbl;
    private JButton clearButton;
    private JButton OKButton;
    private JPanel attViewPanel;
    private JTable mediDetailsTable;
    private JPanel mediDetailsTblPanel;
    private JComboBox selectTitleCombo;
    private JLabel selectTitleLbl;
    private JLabel timeTableHeadingLbl;
    private JPanel noticeTxtAreaPanel;
    private JTextArea noticeTxtArea;
    private JPanel timeTablePanel;
    private JTable timeTableTable;
    private JTable attTable;
    private JScrollPane attScrollPane;
    private JScrollPane mediScrollPane;
    private JScrollPane noticeScrollPane;
    private JScrollPane timeTableScrollPane;
    private JButton updateProfileButton;
    private JTable Course_materials;
    private JButton updateTimeTableButton;
    private JButton addButton;
    private JButton checkEligibilityButton;
    private JButton checkAttendanceEligibilityButton;
    private JButton deleteProfilePictureButton;
    private JButton okButtonCourses;
    private JPanel displayDetailsPanel;
    private JButton downloadButton;


//    private String[] courseCodes = {
//            "ICT2113",  // Index 0
//            "ICT2122",  // Index 1
//            "ICT2132",  // Index 2
//            "ICT2142",  // Index 3
//            "ICT2152"   // Index 4
//    };

    Connection con;
    PreparedStatement pst;
    ResultSet rs;


    public StuHome() {
//        JFrame frame = new JFrame("Student Management System - Home");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Home");
        setSize(2000, 890);
        setLocationRelativeTo(null);
        setVisible(true);

        displayProfileDetails();
        showProfilePicture(imageLbl);
//        getAllAttendanceCounts();

        selectTitleCombo.setSelectedIndex(-1);  // Ensure no initial selection
        noticeTxtArea.setText("");

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

        profileButton.setFocusPainted(false);
        coursesButton.setFocusPainted(false);
        attendanceButton.setFocusPainted(false);
        medicalButton.setFocusPainted(false);
        timeTableButton.setFocusPainted(false);
        noticeButton.setFocusPainted(false);
        gradeAndGPAButton.setFocusPainted(false);

        // Button actions to switch cards
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("hi");
                cardLayout.show(cardMainPanel, "profileCard");
            }
        });

        coursesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "coursesCard");

            }
        });

        gradeAndGPAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "gradeGPACard");
                calculateGPA();
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

                String[] mediStatus = {"Medical_id", "Course_code", "Course_name", "Week_No", "Day_No", "Status"};
                DefaultTableModel model = new DefaultTableModel(null, mediStatus);
                mediDetailsTable.setModel(model);
                viewMedicalStatus();
            }
        });

        timeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "timeTableCard");
                showTimeTable();
            }
        });

        noticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "noticeCard");
                addNoticeTitlesToComboBox();
                selectTitleCombo.setSelectedIndex(-1); // Reset selection
                noticeTxtArea.setText(""); // Clear previous content
            }

        });


        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateStudentProfile();
                dispose();
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
        selectCrsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectCrsComboBox.getSelectedIndex();

//                String selectedCourseCode = courseCodes[selectedIndex];
                List<String> courseCodes = getAllCourseCodes();

                getGrade(courseCodes.get(selectedIndex));
//                selectCrsComboBox.setSelectedIndex(-1);
            }
        });

        selectTitleCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Only proceed if an item is selected (index not -1)
                if (selectTitleCombo.getSelectedIndex() != -1) {
                    String selectedTitle = (String) selectTitleCombo.getSelectedItem();
                    System.out.println("Selected Title: " + selectedTitle);
                    displayNoticeContent(selectedTitle);
                } else {
                    noticeTxtArea.setText(""); // Clear if nothing selected
                }
            }
        });

        checkEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowCAEligibility();
                dispose();
//                ShowCAEligibility showCAEligibility = new ShowCAEligibility(cardMainPanel, "cardMain");
//                cardMainPanel.add(showCAEligibility, "showCAEligibilityCard");
//
//                CardLayout cl = (CardLayout) cardMainPanel.getLayout();
//                cl.show(coursesCard, "showCAEligibilityCard");
            }
        });
        selectAttCourseCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectAttCourseCombo.getSelectedIndex();
//                String selectedCourseCode = courseCodes[selectedIndex];
                List<String> selectedCourseCodes = getAllCourseCodes();
                System.out.println("Selected CourseCode: " + selectedCourseCodes.get(selectedIndex));
            }
        });

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectAttCourseCombo.getSelectedIndex();
                if (selectedIndex >= 0) {
//                    String selectedCourseCode = courseCodes[selectedIndex];
                    List<String> selectedCourseCodes = getAllCourseCodes();
                    System.out.println("Selected CourseCode: " + selectedCourseCodes.get(selectedIndex));
                    viewAttendance(selectedCourseCodes.get(selectedIndex));
                }else {
                    JOptionPane.showMessageDialog(null, "Please select a course");
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Reset ComboBox selection to no selection
                selectAttCourseCombo.setSelectedIndex(0);  // or 0 if you want the first item

                //Clear the table model (remove all data)
                DefaultTableModel model = new DefaultTableModel(
                        new String[]{"Attendance Id", "Lecture Hour", "Week No", "Day No", "Status", "Course Type"}, 0
                );
                attTable.setModel(model);
            }
        });
//        checkAttendanceEligibilityButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
        checkAttendanceEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowAttendanceEligibility();
                dispose();
            }
        });
        deleteProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProfilePicture(imageLbl);
                deleteProfilePictureButton.setEnabled(false);
            }
        });


        comboBoxCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int SelectedIndex = comboBoxCourses.getSelectedIndex();
                List<String> selectedCourseCodeForMaterials = getAllCourseCodes();
                System.out.println("Selected CourseCode: " + selectedCourseCodeForMaterials.get(SelectedIndex));
//                getGrade(selectedCourseCodeForMaterials.get(SelectedIndex));
            }
        });
        okButtonCourses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxCourses.getSelectedIndex();
                if (selectedIndex >= 0) {
//                    String selectedCourseCode = courseCodes[selectedIndex];
                    List<String> selectedCourseCodeForMaterials = getAllCourseCodes();
                    String selectedCourseCode = selectedCourseCodeForMaterials.get(selectedIndex);
                    System.out.println("Selected CourseCode: " + selectedCourseCodeForMaterials.get(selectedIndex));
                    viewCourseMaterials(selectedCourseCode);
                }else {
                    JOptionPane.showMessageDialog(null, "Please select a course");
                }
            }
        });
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = Course_materials.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a material to download.");
                    return;
                }

                // Get all required values from the selected row
                String courseCode = Course_materials.getValueAt(selectedRow, 1).toString();
                String fileName = Course_materials.getValueAt(selectedRow, 3).toString();

                // Call download method with all parameters
                downloadMaterial(Session.loggedInUsername, courseCode, fileName);
            }
        });

    }


public double calculateGPA() {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    double totalGradePoints = 0;
    int totalCredits = 0;

    try {
        con = DatabaseConnection.connect();
        // Query to get final exam marks and credits
        String sql = "SELECT g.Final_Exam_Mark, c.Credit " +
                "FROM grade_letters g " +
                "JOIN course c ON g.Course_code = c.Course_code " +
                "WHERE g.Stu_id = ?";
        pstmt = con.prepareStatement(sql);
        pstmt.setString(1, Session.loggedInUsername);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            double finalMark = rs.getDouble("Final_Exam_Mark");
            int credits = rs.getInt("Credit");

            double gradePoints = convertMarkToPoints(finalMark);
            totalGradePoints += gradePoints * credits;
            totalCredits += credits;
        }

        if (totalCredits > 0) {
            double gpa = totalGradePoints / totalCredits;
            sgpaTxt.setText(String.format("%.2f", gpa));
            return gpa;

        } else {
            JOptionPane.showMessageDialog(null, "Please select a grade");
            return 0.0; // No courses found
        }
    } catch (Exception e) {
        System.out.println("Error in calculating GPA: " + e.getMessage());
        e.printStackTrace();
        return 0.0;
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (con != null) con.close();
        } catch (SQLException se) {
            System.out.println("Error closing resources: " + se.getMessage());
        }
    }
}

    private double convertMarkToPoints(double finalMark) {
        if (finalMark >= 90) return 4.0;
        if (finalMark >= 84) return 4.0;
        if (finalMark >= 75) return 3.7;
        if (finalMark >= 70) return 3.3;
        if (finalMark >= 65) return 3.0;
        if (finalMark >= 60) return 2.7;
        if (finalMark >= 55) return 2.3;
        if (finalMark >= 50) return 2.0;
        if (finalMark >= 45) return 1.7;
        if (finalMark >= 40) return 1.3;
        if (finalMark >= 35) return 1.0;
        return 0.0;
    }

    public void getGrade(String courseCode) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.connect();  // Your method to connect to DB
            String sql = "SELECT Stu_id, Course_code, Final_Exam_Mark, Grade FROM Grade_Letters WHERE Course_code = ? AND Stu_id = ?";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, courseCode);
            pstmt.setString(2, Session.loggedInUsername);  // Assuming you have a Session class

            rs = pstmt.executeQuery();

            if (rs.next()) {
                String grade = rs.getString("Grade");
                yourGradeTxt.setText(grade);

                System.out.println("Course Code: " + courseCode + " | Grade: " + grade);
            } else {
                JOptionPane.showMessageDialog(null, "No Grades Found");
            }
        } catch (Exception e) {
            System.out.println("Error in getting grade: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException se) {
                System.out.println("Error closing resources: " + se.getMessage());
            }
        }
    }


    // ******* Display Profile Details *****************

    public void displayProfileDetails(){
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
            System.out.println("Error in display Profile Details: " + e.getMessage());
        }
    }

    // ******* Time Table *****************

    public void showTimeTable() {
        con = DatabaseConnection.connect();

        try {
            String sql = "SELECT * FROM TimeTable";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            String[] columnNames = {"Department", "Course_Code", "Course_Name", "Time", "Day"};
            DefaultTableModel model = new DefaultTableModel(null, columnNames);

            while (rs.next()) {
                // Get the day number from the database
                int dayNumber = rs.getInt("Day");

                // Map the day number to the corresponding weekday name
                String dayName = mapDayNumberToName(dayNumber);

                model.addRow(new Object[]{
                        rs.getString("Department"),
                        rs.getString("Course_Code"),
                        rs.getString("Course_Name"),
                        rs.getString("Time"),
                        dayName
                });
            }
            timeTableTable.setModel(model);
        } catch (Exception e) {
            System.out.println("Error in display Time Table: " + e.getMessage());
        }
    }

    // Map day numbers to weekday names
    private String mapDayNumberToName(int dayNumber) {
        switch (dayNumber) {
            case 1: return "Monday";
            case 2: return "Tuesday";
            case 3: return "Wednesday";
            case 4: return "Thursday";
            case 5: return "Friday";
            default: return "Unknown";
        }
    }

    // Add Notice titles to the combo box *****************

    public void addNoticeTitlesToComboBox(){
        con = DatabaseConnection.connect();
        try {
            Connection conn = DatabaseConnection.connect();
            String sql = "SELECT * FROM Notice";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            selectTitleCombo.removeAllItems(); // Clear existing items

            // Add a placeholder or leave empty (optional)
            // selectTitleCombo.addItem("-- Select a Notice --");

            while (rs.next()) {
                String title = rs.getString("Title");
                selectTitleCombo.addItem(title);
                System.out.println("Title: " + title);
            }

            // Set initial selection to -1 (no selection)
            selectTitleCombo.setSelectedIndex(-1);
        } catch(Exception e) {
            System.out.println("Error in addNoticeTitlesToComboBox: " + e.getMessage());
        }

    }

    public void displayNoticeContent(String title) {

        try {
            // Establish connection to the database to get the NoticeId based on the title
            Connection con = DatabaseConnection.connect();
            String sql = "SELECT Notice_id FROM Notice WHERE Title = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            // If the notice exists in the database
            if (rs.next()) {
                String noticeId = rs.getString("Notice_id");
                // Construct the file path using the noticeId
                File noticeFile = new File("notices/notice_" + noticeId + ".txt");
                System.out.println("Looking for file at: " + noticeFile.getAbsolutePath());


                // Check if the file exists
                if (!noticeFile.exists()) {
                    System.out.println("File not found: " + noticeFile.getName());
                    noticeTxtArea.setText("Notice file not found.");
                    return;
                }

                // Debug: Print the absolute path to ensure it's correct
                System.out.println("Looking for file at: " + noticeFile.getAbsolutePath());

                // Read content from the file
                try (BufferedReader reader = new BufferedReader(new FileReader(noticeFile))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }

                    // Display the content in JTextArea
                    noticeTxtArea.setText(content.toString());
                } catch (IOException e) {
                    System.out.println("Error reading file: " + e.getMessage());
                    noticeTxtArea.setText("Error reading the notice file.");
                    e.printStackTrace();
                }
            } else {
                noticeTxtArea.setText("Notice not found in the database.");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching Notice ID: " + e.getMessage());
            e.printStackTrace();
            noticeTxtArea.setText("Error fetching notice details.");
        }
    }


    //************ Medical *******************

    public void viewMedicalStatus(){
        Connection con = DatabaseConnection.connect();
        try{
            System.out.println("LoggedIn Username = [" + Session.loggedInUsername + "]");
            String sql = "SELECT med.Medical_id, med.Course_code, med.Week_No, med.Day_No, med.Status, c.Course_Name " +
                    "FROM Medical med " +
                    "JOIN Course c ON med.Course_code = c.Course_code " +
                    "JOIN Student s ON med.Stu_id = s.Stu_id " +
                    "JOIN User u ON s.UserName = u.UserName " +
                    "WHERE u.UserName = ?";


            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Session.loggedInUsername);

            System.out.println("Executing query: " + sql);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) mediDetailsTable.getModel();
            model.setRowCount(0);
            int count = 0;
            boolean found = false;
            while (rs.next()) {
                count++;
                found = true;
                String medId = rs.getString("Medical_id");
                String courseCode = rs.getString("Course_code");
                String courseName = rs.getString("Course_Name");
                String weekNo = rs.getString("Week_No");
                String dayNo = rs.getString("Day_No");
                String status = rs.getString("Status");

                model.addRow(new Object[]{medId, courseCode, courseName, weekNo, dayNo, status});
            }
            System.out.println("Total Records Found: " + count);
//            if(count == 0){
//                JOptionPane.showMessageDialog(null, "No Records Found");
//                System.out.println("No Records Found");
//            }
            if(!found){
                JOptionPane.showMessageDialog(null, "No Medical Found");
                System.out.println("No Medical Found");
            }
        } catch (Exception e) {
            System.out.println("Error in view Medical Status: " + e.getMessage());
        }
    }

// ************* Attendance **********************

    public void viewAttendance(String Course_code) {
        Connection con = DatabaseConnection.connect();
        try {
            System.out.println("LoggedIn Username = [" + Session.loggedInUsername + "]");
            String sql = "SELECT a.Attendance_id, a.Lec_hour, a.Week_No, a.Day_No, a.Status, a.Course_type " +
                    "FROM Attendance a " +
                    "JOIN Course c ON c.Course_code = a.Course_code " +
                    "JOIN Student s ON a.Stu_id = s.Stu_id " +
                    "JOIN User u ON s.UserName = u.UserName " +
                    "WHERE u.UserName = ? AND a.Course_code = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Session.loggedInUsername);
            pstmt.setString(2, Course_code);

            System.out.println("Executing query: " + sql);

            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Attendance Id", "Lecture Hour", "Week No", "Day No", "Status", "Course Type"}, 0
            );

            while (rs.next()) {
                String attendanceID = rs.getString("Attendance_id");
                String lecHour = rs.getString("Lec_hour");
                String weekNo = rs.getString("Week_No");
                String dayNo = rs.getString("Day_No");
                String status = rs.getString("Status");
                String courseType = rs.getString("Course_type");

                model.addRow(new Object[]{attendanceID, lecHour, weekNo, dayNo, status, courseType});

            }
            attTable.setModel(model);

        } catch (Exception e) {
            System.out.println("Error in view Attendance Eligibility: " + e.getMessage());
        }
    }

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

                String path = "user_Pro_Pic/" + fileName;
                File imageFile = new File(path);

                // If image file does not exist, fallback to default image
                if (!imageFile.exists()) {
                    path = "user_Pro_Pic/default.png";
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

    public void deleteProfilePicture(JLabel imageLbl) {
        Connection con = DatabaseConnection.connect();
        try{
            String sql = "UPDATE User SET Profile_pic = NULL WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, Session.loggedInUsername);

            int result = pst.executeUpdate();

            if (result > 0) {
                // Set default image after deletion
                String path = "user_Pro_Pic/default.png";

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
            e.printStackTrace();
        }
    }

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
            System.out.println("Error in get All CourseCodes: " + e.getMessage());
        }
        return courseCodes;
    }

    // ************* Course materials **********************

    private boolean listenerAdded = false;

    public void viewCourseMaterials(String courseCode) {
        Connection con = DatabaseConnection.connect();
        try{
            String sql = "SELECT Material_id,Course_code,Lec_id, file_path, uploaded_on FROM lecture_materials WHERE Course_code = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {     // make jtable uneditable
                    return false;
                }
            };
            model.addColumn("Material ID");
            model.addColumn("Course Code");
            model.addColumn("Lecturer ID");
            model.addColumn("Material");
            model.addColumn("Uploaded On");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("Material_id"),
                        rs.getString("Course_code"),
                        rs.getString("Lec_id"),
                        rs.getString("file_path"),
                        rs.getTimestamp("uploaded_on")
                });
            }

            Course_materials.setModel(model);
            Course_materials.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            if (!listenerAdded) {
                Course_materials.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        int row = Course_materials.rowAtPoint(e.getPoint());
                        int col = Course_materials.columnAtPoint(e.getPoint());

                        if (row >= 0 && col == 3) {
                            String fileName = Course_materials.getValueAt(row, col).toString();
                            String courseCode = Course_materials.getValueAt(row, 1).toString();

                            String filePath = "course_materials" + File.separator + courseCode + File.separator + fileName;
                            openMaterial(filePath);
                        }
                    }
                });
                listenerAdded = true;
            }

        } catch (Exception e) {
            System.out.println("Error in view Course Materials: " + e.getMessage());
        }
    }

    private void openMaterial(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(mainPanel, "Error opening the file: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(mainPanel, "File does not exist.");
        }
    }

    private void downloadMaterial(String user, String courseCode, String fileName) {

        String filePath = "course_materials" + File.separator + courseCode + File.separator + fileName;

        File sourceFile = new File(filePath);
        if (!sourceFile.exists()) {
            JOptionPane.showMessageDialog(mainPanel, "The requested file does not exist: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");
        fileChooser.setSelectedFile(new File(fileName));
        int userSelection = fileChooser.showSaveDialog(mainPanel);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();

            // Ensure the file is not overwritten without confirmation
            if (destinationFile.exists()) {
                int option = JOptionPane.showConfirmDialog(null, "File already exists. Do you want to overwrite?", "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try {
                Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(mainPanel, "File downloaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainPanel, "Error downloading file: " + e.getMessage());
            }

        }
    }



    public static void main(String[] args) {
        new StuHome();
    }
}
