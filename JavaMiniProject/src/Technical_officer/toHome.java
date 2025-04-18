package Technical_officer;

import database.DatabaseConnection;
import database.Session;
import student.Login;
import student.UpdateStudentProfile;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;

public class toHome extends  JFrame {
    private JPanel mainPanel;
    private JPanel headingPanel;
    private JLabel stuManaSysLbl;
    private JLabel FoTLbl;
    private JButton logOutButton;
    private JPanel btnPanel;
    private JButton profileButton;
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
    private JButton updateProfileButton;
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
    private JPanel attendanceCard;
    private JLabel headingLabel;
    private JPanel medicalCard;
    private JLabel heading;
    private JPanel viewPanel;
    private JButton add;
    private JButton update;
    private JPanel buttons;
    private JPanel view;
    private JPanel buttonPanel;
    private JButton addbtn;
    private JButton updatebtn;
    private JTable timeTableTable;

    public toHome() {

        CardLayout cardLayout = (CardLayout) (cardMainPanel.getLayout());
        displayProfileDetils();

        cardMainPanel.add(profileCard, "profileCard");
        cardMainPanel.add(attendanceCard, "attendanceCard");
        cardMainPanel.add(medicalCard, "medicalCard");
        cardMainPanel.add(timeTableCard, "timeTableCard");
        cardMainPanel.add(noticeCard, "noticeCard");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("to home");
        setSize(400,400);
        setVisible(true);
        setLocationRelativeTo(null);

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = ""                        ;
                new Login();
            }
        });

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "profileCard");
            }
        });
        updateProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new updateTOprofile();
                dispose();
            }
        });
        attendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "attendanceCard");
                showAttendancetable();
            }
        });
        medicalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardMainPanel, "medicalCard");
                showMedicaltable();
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

        //Add attendance
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddAttendance addForm = new AddAttendance();
                addForm.setVisible(true);// make sure class name matches
            }
        });

        //Update attendance
        update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAttendance updateForm = new updateAttendance();
                updateForm.setVisible(true);
            }
        });

        //Add medical
        addbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addmedical addmedi = new addmedical();
                addmedi.setVisible(true);
            }
        });

        //Update medical
        updatebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMedical upmedi = new  updateMedical();
                upmedi.setVisible(true);
            }
        });
    }

    private Connection con;
    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3308/techlms";
            String user = "root";
            String password = "1234"; // Change if you use a password
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
        }
    }

    //Attendance table
    private void showAttendancetable() {
        try{
            connectToDatabase();
            Statement st = con.createStatement();
            String query = "select * from attendance";
            ResultSet rs = st.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Vector<String> columnNames = new Vector<String>();
            for(int i=1;i<=columnCount;i++){
                columnNames.add(rsmd.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while(rs.next()){
                Vector<Object> row = new Vector<>();
                for(int i=1;i<=columnCount;i++){
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            JTable table = new JTable(data,columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(580,300));

            viewPanel.removeAll();
            viewPanel.setLayout(new BorderLayout());
            viewPanel.add(scrollPane,BorderLayout.CENTER);
            viewPanel.revalidate();
            viewPanel.repaint();

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,"Database error");
        }
    }

    //Medical
    private void showMedicaltable() {
        try {
            connectToDatabase();
            Statement st = con.createStatement();
            String query = "SELECT * FROM medical";
            ResultSet rs = st.executeQuery(query);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            Vector<String> columnNames = new Vector<String>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(rsmd.getColumnName(i));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }

            JTable table = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(580, 300));

            view.removeAll();
            view.setLayout(new BorderLayout());
            view.add(scrollPane, BorderLayout.CENTER);
            view.revalidate();
            view.repaint();

            rs.close();
            st.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error while loading medical table");
        }
    }

    //Timetable
    public void showTimeTable() {
        try {
            connectToDatabase();
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

    //Notices
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
                File noticeFile = new File("notices/notice_" + noticeId + ".txt");
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

    //Profile
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

    public static void main(String[] args) {
        new toHome();
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
