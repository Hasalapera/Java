package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import database.DatabaseConnection;

import javax.swing.table.DefaultTableModel;

import database.Session;
import student.Login;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;



public class AdHome extends JFrame {
    private JPanel mainPanel;
    private JButton AdminprofileButton;
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
    private JPanel NoticeCard;
    private JButton addNoticeButton;
    private JButton deleteNoticeButton;
    private JButton viewNoticeButton;
    private JTextArea textArea1;
    private JTable NoticeTable;
    private JButton deleteProfilePictureButton;
    private JLabel imgDisplayLbl;
    private JTextField userNametxt;
    private JTextField fNametxt;
    private JTextField lNametxt;
    private JTextField dobTxt;
    private JTextField roleTxt;
    private JTextField enrollTxt;
    private JTextField addressTxt;
    private JTextField emailTxt;
    private JTextField pNoTxt;
    private JTextField pwTxt;
    private JTextField proPicTxt;
    private JButton addPhotoButton;
    private JButton clearButton;
    private JButton updateProfileButton1;
    private JButton exitButton;
    private JButton UserProfilesButton;
    private JPanel updateUProfilesPanel;
    private JPanel noticeDisplaybtnPanel;
    private JButton viewAllUsersButton;
    private JButton addNewUserButton;
    //private JTable noticeTable;

    Connection con;
    PreparedStatement pst;
    ResultSet rs;



    public AdHome() {
        setContentPane(mainPanel); // IntelliJ should generate and initialize this
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Admin Home");
        pack(); // Adjusts window size to fit content
        setSize(2000, 830);
        setLocationRelativeTo(null);
        setVisible(true);

        // Load admin profile automatically when AdHome is initialized
        loadAdminProfile();  // Call to load the admin details right after the window is shown
        showProfilePicture(imgDisplayLbl);

        UserProfilesButton.setFocusPainted(false);
        AdminprofileButton.setFocusPainted(false);
        courseButton.setFocusPainted(false);
        timeTableButton.setFocusPainted(false);
        noticeButton.setFocusPainted(false);

        UserProfilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "updateUProfilesPanel");
            }
        });

        deleteProfilePictureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteProfilePicture(imgDisplayLbl);
                deleteProfilePictureButton.setEnabled(false);
            }
        });

        addPhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadProfilePicture();
            }
        });
        updateProfileButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProfile();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        AdminprofileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "Profile");
                loadAdminProfile();
            }
        });
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProfileUpdate();
                dispose();
            }
        });

        viewAllUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewUserProfiles viewPanel = new ViewUserProfiles(cardpanel, "PreviousCardName");
                cardpanel.add(viewPanel.Mainpanel, "ViewUsersCard");

                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "ViewUsersCard");


//               new ViewUserProfiles();

            }
        });


        addNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "AddUserCard");
                new AddUserProfile();
                dispose();


            }
        });

        timeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "TimeTableCard");
                loadTimeTableData();
            }
        });
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCourse();
                dispose();
            }
        });
        updateCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateCourse();
                dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = CourseTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a course to delete.");
                    return;
                }

                // Get course ID from selected row (assuming course_id is at column index 0)
                String courseId = CourseTable.getValueAt(selectedRow, 0).toString();

                // Confirm delete action
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete Course ID: " + courseId + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                // Perform deletion if confirmed
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteCourseFromDatabase(courseId); // delete from DB
                    loadCourseData(); // refresh JTable with fresh data from DB
                }
            }
        });

        addTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddTimeTable();
                dispose();
            }
        });
        updateTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UpdateTimeTable();
                dispose();
            }
        });
        deleteButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedTimetableEntry();
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });
        noticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "Notice");
                loadNoticeData();
            }
        });
        viewNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSelectedNoticeContent();
            }
        });
        addNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNotices();
                dispose();
            }
        });
        deleteNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = NoticeTable.getSelectedRow();

                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a course to delete.");
                    return;
                }

                // Get course ID from selected row (assuming course_id is at column index 0)
                String noticeId = NoticeTable.getValueAt(selectedRow, 0).toString();

                // Confirm delete action
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete Notice ID: " + noticeId + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

                // Perform deletion if confirmed
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteNotice(noticeId); // delete from DB
                    loadCourseData(); // refresh JTable with fresh data from DB
                }
            }

        });
        courseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cardpanel.getLayout();
                cl.show(cardpanel, "Course");
                loadCourseData();
            }
        });
//        updateCourseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                new UpdateCourse();
//                dispose();
//            }
//        });


    }

    private void clearFields() {
        userNametxt.setText("");
        fNametxt.setText("");
        lNametxt.setText("");
        dobTxt.setText("");
        roleTxt.setText("");
        enrollTxt.setText("");
        addressTxt.setText("");
        emailTxt.setText("");
        pNoTxt.setText("");
        pwTxt.setText("");
        proPicTxt.setText("");
    }

    public void updateProfile() {
        String sql = "UPDATE user SET Fname=?, Lname=?, DoB=?, Role=?, Enrollment_Date=?, Address=?, Email=?, Phone_No=?, Password=?, Profile_pic=? WHERE UserName=?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fNametxt.getText());
            pstmt.setString(2, lNametxt.getText());
            pstmt.setString(3, dobTxt.getText());
            pstmt.setString(4, roleTxt.getText());
            pstmt.setString(5, enrollTxt.getText());
            pstmt.setString(6, addressTxt.getText());
            pstmt.setString(7, emailTxt.getText());

            // Convert phone number to int (handle empty or invalid input gracefully)
            try {
                pstmt.setInt(8, Integer.parseInt(pNoTxt.getText()));
            } catch (NumberFormatException ex) {
                pstmt.setNull(8, java.sql.Types.INTEGER);
            }

            pstmt.setString(9, pwTxt.getText());
            pstmt.setString(10, proPicTxt.getText());

            pstmt.setString(11, userNametxt.getText()); // WHERE UserName = ?

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "Profile updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "No profile found for that username.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating profile: " + ex.getMessage());
        }
    }

    public void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");

        // Optional: allow only image files
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getName();

            // Set the selected file path into the text field
            proPicTxt.setText(imagePath);
        }
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

    public void showProfilePicture(JLabel imgDisplayLbl) {
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
                int width = imgDisplayLbl.getWidth();
                int height = imgDisplayLbl.getHeight();

                // Default size safety check (in case label not ready)
                if (width == 0 || height == 0) {
                    width = 150;
                    height = 150;
                }

                Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imgDisplayLbl.setIcon(new ImageIcon(image));
                imgDisplayLbl.repaint(); // Refresh label to show updated image
            }
        } catch (Exception e) {
            System.out.println("Error in show Profile Picture: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteProfilePicture(JLabel imgDisplayLbl) {
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
                int width = imgDisplayLbl.getWidth();
                int height = imgDisplayLbl.getHeight();

                if (width == 0 || height == 0) {
                    width = 150;
                    height = 150;
                }

                ImageIcon imageIcon = new ImageIcon(path);
                Image image = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imgDisplayLbl.setIcon(new ImageIcon(image));
                imgDisplayLbl.repaint();  // Refresh label

                System.out.println("Profile picture deleted successfully.");
            } else {
                System.out.println("No profile picture was found or username invalid.");
            }

        } catch (Exception e) {
            System.out.println("Error in deleteProfilePicture: " + e.getMessage());
            e.printStackTrace();
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
            model.addColumn("TimeTable_ID");
            model.addColumn("Admin ID");
            model.addColumn("Department");
            model.addColumn("Course Code");
            model.addColumn("Course Name");
            model.addColumn("Time");
            model.addColumn("Day");

            while (rs.next()) {
                String dayNumber = rs.getString("day"); // Get the day number from DB
                String dayName = convertDayNumberToName(dayNumber); // Convert to day name

                model.addRow(new Object[]{
                        rs.getString("timetable_id"),
                        rs.getString("ad_id"),
                        rs.getString("department"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getString("time"),
                        dayName
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

    private void deleteSelectedTimetableEntry() {
        int selectedRow = Timetable1.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a timetable entry to delete.");
            return;
        }

        String timetableId = Timetable1.getValueAt(selectedRow, 0).toString(); // column 0 = timetable_id

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to delete ID: " + timetableId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            deleteTimetableFromDatabase(timetableId);  // Delete from DB
            loadTimeTableData();                       // Refresh table after delete
        }
    }


    // This helper method converts numeric days to names
    private String convertDayNumberToName(String dayNumber) {
        switch (dayNumber) {
            case "1": return "Monday";
            case "2": return "Tuesday";
            case "3": return "Wednesday";
            case "4": return "Thursday";
            default: return "Unknown";
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

    private void loadNoticeData() {
        try {
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM notice");

            // Define column names
            String[] columnNames = {"Notice ID", "Admin ID", "Title", "Date"};

            // Use DefaultTableModel to fill JTable
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            while (rs.next()) {
                String id = rs.getString("Notice_id");
                String adId = rs.getString("Ad_id");
                String title = rs.getString("Title");
                Date date = rs.getDate("Date");

                Object[] row = {id, adId, title, date};
                tableModel.addRow(row);
            }

            // Set model to your JTable
            NoticeTable.setModel(tableModel);


            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading notice data");
        }
    }

    private void loadSelectedNoticeContent() {
        int selectedRow = NoticeTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a notice to view.");
            return;
        }

        String noticeId = NoticeTable.getValueAt(selectedRow, 0).toString(); // assume column 0 = notice ID
        String filename = "notice_" + noticeId + ".txt";
        File file = new File("notices/" + filename);

        System.out.println("Trying to read from: " + file.getName());

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            textArea1.setText(content.toString());

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Unable to load notice content from file: " + file.getName());
        }
    }

    private void deleteNotice(String noticeId) {
        try {
            // Step 1: Delete from the database
            Connection conn = DatabaseConnection.connect();
            Statement stmt = conn.createStatement();

            // Execute the delete query
            String deleteQuery = "DELETE FROM notice WHERE Notice_id = '" + noticeId + "'";
            int rowsAffected = stmt.executeUpdate(deleteQuery);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Notice deleted successfully.");

                // Step 2: Remove the selected row from the JTable
                DefaultTableModel model = (DefaultTableModel) NoticeTable.getModel();
                int selectedRow = NoticeTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Remove the row from the JTable
                    model.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Notice not found or could not be deleted.");
            }

            // Step 3: Close connections
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting notice.");
        }
    }

    public static void main(String[] args) {
        new AdHome();
    }



}


