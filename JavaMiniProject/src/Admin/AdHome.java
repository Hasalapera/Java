package Admin;

import javax.swing.*;
import java.awt.CardLayout;
import java.sql.Connection;

import database.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;



public class AdHome {
    private JPanel mainPanel;
    private JButton profileButton;
    private JButton courseButton;
    private JButton timeTableButton;
    private JButton noticeButton;
    private JPanel headingpanel;
    private JPanel buttonpanel;
    private JPanel cardpanel;
    private JPanel Profilecard;
    private JTextField textField1;
    private JTextField textField2;
    private JButton updateProfileButton;
    private JPanel profilepanel;
    private JPanel imagemainpanel;
    private JPanel imagepanel;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JButton logOutButton;
    private JTable CourseTable;
    private JPanel CourseCard;
    private JButton addCourseButton;
    private JButton updateCourseButton;
    private JButton deleteButton;
    private JPanel TimeTableCard;
    private JTable Timetable1;
    private JButton addTimeTableButton;
    private JButton updateTimeTableButton;
    private JButton deleteButton1;
    private JScrollPane TimeTable;
    private JPanel buton;

    public AdHome() {
        courseButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardpanel.getLayout();
            cl.show(cardpanel, "Course");
            loadCourseData();// Make sure "course" matches the card name you set in Designer
        });

        profileButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardpanel.getLayout();
            cl.show(cardpanel, "Profile");
            loadAdminProfile();
        });

        updateProfileButton.addActionListener(e -> {
            // Open the UpdateProfile window when the update profile button is clicked
            JFrame frame = new JFrame("Update Profile");
            frame.setContentPane(new UpdateProfile().panel1);  // Ensure 'Mainpanel' is the public panel name in UpdateProfile
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // Close this window but keep the app running
            frame.pack();
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });

        timeTableButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) cardpanel.getLayout();
            cl.show(cardpanel, "TimeTable"); // The string must match the name you set in your CardLayout
           loadTimeTableData(); // Optional: load data from DB
        });


        addCourseButton.addActionListener(e -> {
            JFrame frame = new JFrame("Add Course");
            frame.setContentPane(new AddCourse().panel1);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // center it
            frame.setVisible(true);
        });

        updateCourseButton.addActionListener(e -> {
            JFrame frame = new JFrame("Update Course");
            frame.setContentPane(new UpdateCourse().panel1); // use mainPanel if that's your variable name
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = CourseTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a course to delete.");
                return;
            }

            // Get course ID (or any unique key)
            String courseId = CourseTable.getValueAt(selectedRow, 0).toString(); // assuming column 0 is course_id

            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete Course ID: " + courseId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                deleteCourseFromDatabase(courseId);
                ((DefaultTableModel) CourseTable.getModel()).removeRow(selectedRow); // remove from table
            }
        });

        addTimeTableButton.addActionListener(e -> {
            JFrame frame = new JFrame("Add Time Table");
            frame.setContentPane(new AddTimeTable().panel1); // make sure `panel1` is public
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // center it
            frame.setVisible(true);
        });

        updateTimeTableButton.addActionListener(e -> {
            JFrame frame = new JFrame("Update Time Table");
            frame.setContentPane(new UpdateTimeTable().panel1); // Make sure panel1 is public in UpdateTimeTable
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });

        deleteButton1.addActionListener(e -> {
            int selectedRow = Timetable1.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a timetable entry to delete.");
                return;
            }

            // Get the primary key (timetable_id) from the selected row
            String timetableId = Timetable1.getValueAt(selectedRow, 0).toString(); // assuming column 0 is timetable_id

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete TimeTable ID: " + timetableId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                deleteTimetableFromDatabase(timetableId);
                ((DefaultTableModel) Timetable1.getModel()).removeRow(selectedRow); // Remove row from JTable
            }
        });

//        updateProfileButton.addActionListener(e -> {
//            JFrame frame = new JFrame("Update Admin Profile");
//            frame.setContentPane(new AdminUpdateProfile().mainPanel); // Make sure panel1 is public in AdminUpdateProfile
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.pack();
//            frame.setLocationRelativeTo(null); // Center the frame
//            frame.setVisible(true);
//        });





    }
    private void loadAdminProfile() {
        try {
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM user WHERE role = 'Admin' LIMIT 1"; // Adjust if you use a session user ID
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                textField1.setText(rs.getString("Fname"));
                textField2.setText(rs.getString("Lname"));
                textField3.setText(rs.getString("Address"));
                textField4.setText(rs.getString("Email"));
                textField5.setText(rs.getString("Phone_no"));
                textField6.setText("Admin"); // Static assignment
            } else {
                JOptionPane.showMessageDialog(null, "No Admin profile found.");
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading admin profile.");
        }
    }



    private void deleteCourseFromDatabase(String courseId) {
        try {
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();
            int rowsAffected = stmt.executeUpdate("DELETE FROM course WHERE course_id = '" + courseId + "'");

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Course deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Course not found or could not be deleted.");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting course from database.");
        }
    }



    private void loadCourseData() {
        try {
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM course");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Course ID");
            model.addColumn("Course Code");
            model.addColumn("Lecturer ID");
            model.addColumn("Course Type");
            model.addColumn("Course Name");
            model.addColumn("Credit");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("course_id"),
                        rs.getString("course_code"),
                        rs.getString("lec_id"),
                        rs.getString("course_type"),
                        rs.getString("course_name"),
                        rs.getInt("credit")
                });
            }

            CourseTable.setModel(model);

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading course data");
        }
    }

    private void loadTimeTableData() {
        try {
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM timetable");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("TimeTable ID");
            model.addColumn("Admin ID");
            model.addColumn("Department");
            model.addColumn("Course Code");
            model.addColumn("Course Name");
            model.addColumn("Time");
            model.addColumn("Day");

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("timetable_id"),
                        rs.getString("ad_id"),
                        rs.getString("department"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("time"),
                        rs.getString("day")
                });
            }

            Timetable1.setModel(model);

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading timetable data");
        }
    }

    private void deleteTimetableFromDatabase(String timetableId) {
        try {
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();

            int rowsAffected = stmt.executeUpdate("DELETE FROM timetable WHERE timetable_id = '" + timetableId + "'");

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Timetable entry deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Timetable ID not found or could not be deleted.");
            }

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting timetable from database.");
        }
    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Admin Home");
            frame.setContentPane(new AdHome().mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null); // Center the window
            frame.setVisible(true);
        });
    }


//    public static void main(String[] args) {
//        new AdHome();
    }


