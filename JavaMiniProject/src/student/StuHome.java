package student;

import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
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
    private JPanel displayDetailsPanel;
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
    private JButton checkEligibilityButton;
    private JButton checkAttendanceEligibilityButton;
    private JButton deleteProfilePictureButton;

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


    public StuHome() {
//        JFrame frame = new JFrame("Student Management System - Home");
        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Home");
        setSize(2000, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

        displayProfileDetils();
        showProfilePicture(imageLbl);
//        getAllAttendanceCounts();

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());

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
                getSGPA();
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
            }
        });
        selectCrsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectCrsComboBox.getSelectedIndex();

                String selectedCourseCode = courseCodes[selectedIndex];

                getGrade(selectedCourseCode);
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
        checkEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ShowCAEligibility();
//                dispose();
            }
        });
        selectAttCourseCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectAttCourseCombo.getSelectedIndex();
                String selectedCourseCode = courseCodes[selectedIndex];
                System.out.println("Selected CourseCode: " + selectedCourseCode);
            }
        });

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = selectAttCourseCombo.getSelectedIndex();
                if (selectedIndex >= 0) {
                    String selectedCourseCode = courseCodes[selectedIndex];
                    System.out.println("Selected CourseCode: " + selectedCourseCode);
                    viewAttendance(selectedCourseCode);
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
        checkAttendanceEligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
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
    }

    // ******* Grade & GPA *****************

    public void getSGPA(){
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try{
            con = DatabaseConnection.connect();
            String sql = "{CALL Calculate_SGPA_For_Student(?)}";
            cstmt = con.prepareCall(sql);

            cstmt.setString(1, Session.loggedInUsername);

            rs = cstmt.executeQuery();

            if(rs.next()){
                double sgpa = rs.getDouble("SGPA");
                System.out.println("SGPA retrieved: " + sgpa);

                sgpaTxt.setText(String.format("%.2f", sgpa));
            }else{
                JOptionPane.showMessageDialog(null, "No SGPA Found");
            }

        } catch (Exception e) {
            System.out.println("error");
        }finally {
            // Close resources properly
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getGrade(String courseCode){
        Connection con = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.connect();
//            String sql = "{CALL GetGradeByStudentAndCourse (?, ?)}";
            String sql = "{CALL Get_Grade_By_Course_And_Student(?, ?)}";
            cstmt = con.prepareCall(sql);

            cstmt.setString(1, courseCode);
            cstmt.setString(2, Session.loggedInUsername);

            rs = cstmt.executeQuery();

            if(rs.next()){
                String grade = rs.getString("Grade");
                yourGradeTxt.setText(grade);

                System.out.println("Course Code: " + courseCode + " | Grade: " + grade); // Print to console

            } else {
                JOptionPane.showMessageDialog(null, "No Grades Found");
            }
        } catch (Exception e) {
            System.out.println("Error in getting grade: " + e.getMessage());
            e.printStackTrace();  // This will print the full stack trace to the console
        } finally {
            // Close resources to avoid memory leaks
            try {
                if (rs != null) rs.close();
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (SQLException se) {
                System.out.println("Error closing resources: " + se.getMessage());
            }
        }
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
                    new String[]{"Attendance Id", "Lectur Hour", "Week No", "Day No", "Status", "Course Type"}, 0
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

    //***************** show profile picture ************************

//    public void showProfilePicture(JLabel imageLbl) {
//        Connection con = DatabaseConnection.connect();
//        try {
//            String sql = "SELECT Profile_pic FROM User WHERE UserName = ?";
//            PreparedStatement pst = con.prepareStatement(sql);
//            pst.setString(1, Session.loggedInUsername);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                String fileName = rs.getString("Profile_pic");
//
//                // Handle null or empty from database
//                if (fileName == null || fileName.trim().isEmpty()) {
//                    fileName = "default.png";
//                }
//
//                String path = "JavaMiniProject/user_Pro_Pic/" + fileName;
//                File imageFile = new File(path);
//
//                if (!imageFile.exists()) {
//                    path = "JavaMiniProject/user_Pro_Pic/default.png";
////                    imageFile = new File(path);
//                }else{
//                    ImageIcon imageIcon = new ImageIcon(path);
//
//                    // Protect against zero-size label
//                    int width = imageLbl.getWidth() > 0 ? imageLbl.getWidth() : 150;
//                    int height = imageLbl.getHeight() > 0 ? imageLbl.getHeight() : 150;
//
//                    Image image = imageIcon.getImage().getScaledInstance(
//                            width,
//                            height,
//                            Image.SCALE_SMOOTH
//                    );
//                    imageLbl.setIcon(new ImageIcon(image));
//                }
//
//                imageLbl.repaint();  // refresh
//            }
//        } catch (Exception e) {
//            System.out.println("Error in show profile picture: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

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

    public void deleteProfilePicture(JLabel imageLbl) {
        Connection con = DatabaseConnection.connect();
        try{
            String sql = "UPDATE User SET Profile_pic = NULL WHERE UserName = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, Session.loggedInUsername);

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




    public static void main(String[] args) {
        new StuHome();
    }
}
