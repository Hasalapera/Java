package student;

import database.DatabaseConnection;
import database.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowAttendanceEligibility extends JFrame {
    private JPanel mainPanel;
    private JScrollPane attEligiScrollPane;
    private JPanel attEligiPanel;
    private JLabel attEligiLbl;
    private JTable attEligiTable;
    private JButton exitButton;

    public ShowAttendanceEligibility() {
        setTitle("Attendance Eligibility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(2000, 1000);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);

        String[] attEligibility = {"Course Code", "Course Name", "Eligibility Status"};
        DefaultTableModel model = new DefaultTableModel(null, attEligibility);

        attEligiTable.setModel(model);
        getAllAttendanceCounts();
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StuHome();
                dispose();
            }
        });
    }

    public void checkAndShowAttendanceEligibility(String Course_code){
        Connection con = DatabaseConnection.connect();
        try{
            // 1. Get Present Count
            System.out.println("LoggedIn Username = [" + Session.loggedInUsername + "]");
            String sql = "SELECT count(*) FROM Attendance a JOIN Course c ON  a.Course_code = c.Course_code " +
                    "JOIN Student s ON a.Stu_id = s.Stu_id " +
                    "JOIN User u ON s.UserName = u.UserName " +
                    "WHERE u.UserName = ? AND a.Course_code = ? AND a.Status = 'Present'";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, Session.loggedInUsername);
            pstmt.setString(2, Course_code);

//            System.out.println("Executing query: " + sql);
            ResultSet rs = pstmt.executeQuery();

            int presentCount = 0;
            if (rs.next()) {
                presentCount = rs.getInt(1);
                System.out.println("Total Records Found: " + presentCount);
            }

            // 2. Get Total Count
            String sql2 = "SELECT COUNT(*) FROM Attendance a " +
                    "JOIN Course c ON a.Course_code = c.Course_code " +
                    "JOIN Student s ON a.Stu_id = s.Stu_id " +
                    "JOIN User u ON s.UserName = u.UserName " +
                    "WHERE u.UserName = ? AND a.Course_code = ?";

            PreparedStatement pstmt2 = con.prepareStatement(sql2);
            pstmt2.setString(1, Session.loggedInUsername);
            pstmt2.setString(2, Course_code);

            ResultSet rs2 = pstmt2.executeQuery();

            int totalCount = 0;
            if (rs2.next()) {
                totalCount = rs2.getInt(1);
                System.out.println("Total Records Found: " + totalCount);
            }
//            double attendancePercentage = ((double) presentCount / totalCount) * 100;
//            System.out.println("Attendance Percentage for course " + Course_code + ": " + attendancePercentage + "%");

            // 3. Get Medical Count
            int medicalCount = getApprovedMediCount(Course_code);

            // 4. Calculate Percentage
            double adjustPercentage = calculateAttendancePercentageAfterMedical(presentCount, totalCount, medicalCount);

            // 5. Check eligibility
            if(checkAttEligibility(adjustPercentage)){
                System.out.println("Eligible for course: " + Course_code + " with " + adjustPercentage + "% (including medicals)");
            }else {
                System.out.println("Not Eligible for course: " + Course_code + " with " + adjustPercentage + "% (including medicals)");
            }

            String status = checkAttEligibility(adjustPercentage) ? "Eligible" : "Not Eligible";

            // Add result to table
            DefaultTableModel model = (DefaultTableModel) attEligiTable.getModel();
            model.addRow(new Object[]{Course_code, getCourseName(Course_code), status});


        }catch (Exception e){
            System.out.println("Error in Get AttendanceCount: " + e.getMessage());
        }
    }


    public double calculateAttendancePercentage(int presentCount, int totalCount){
        if (totalCount == 0){
            return 0.0;
        }
        return ((double) presentCount / totalCount) * 100;
    }

    public int getApprovedMediCount(String Course_code){
        Connection con = DatabaseConnection.connect();
        int medicalCount = 0;
        try{
            System.out.println("LoggedIn Username = [" + Session.loggedInUsername + "]");
            String sql = "SELECT COUNT(*) FROM medical m "+
                    "JOIN Course c ON m.Course_code = c.Course_code " +
                    "JOIN Student s ON m.Stu_id = s.Stu_id " +
                    "JOIN User u ON s.UserName = u.UserName " +
                    "WHERE u.UserName = ? AND m.Course_code = ? AND m.Status = 'Approved'";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, Session.loggedInUsername);
            pstmt.setString(2, Course_code);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                medicalCount = rs.getInt(1);
            }
        }catch (Exception e){
            System.out.println("Error in view Approved Medical Count: " + e.getMessage());
        }
        return medicalCount;
    }


    public double calculateAttendancePercentageAfterMedical(int presentCount, int totalCount, int medicalCount){
        if (totalCount == 0){
            return 0.0;
        }
        return ((double) (presentCount + medicalCount) / totalCount) * 100;
    }


    //********** check attendance eligibility *************
    public boolean checkAttEligibility(double attendancePercentage){
        return attendancePercentage >= 80;
    }

    public void getAllAttendanceCounts() {
        List<String> courseCodes = getAllCourseCodes();  // dynamically load from DB
        // for each
        for (String courseCode : courseCodes) {
            checkAndShowAttendanceEligibility(courseCode);
        }
        // for loop
//        for (int i = 0; i < courseCodes.size(); i++) {
//            String courseCode = courseCodes.get(i);
//            getAttendanceCount(courseCode);
//        }
    }

// ********** Get course codes from database *********************

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

    // ********* Get Course Name by Course Code *********
    public String getCourseName(String courseCode) {
        Connection con = DatabaseConnection.connect();
        String courseName = "Unknown";  // default if not found

        try {
            String sql = "SELECT Course_name FROM Course WHERE Course_code = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                courseName = rs.getString("Course_name");
            }
        } catch (Exception e) {
            System.out.println("Error in getCourseName: " + e.getMessage());
        }

        return courseName;
    }


    public static void main(String[] args) {
        new ShowAttendanceEligibility().getAllAttendanceCounts();
    }

}
