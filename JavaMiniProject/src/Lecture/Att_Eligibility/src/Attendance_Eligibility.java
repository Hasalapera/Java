import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Attendance_Eligibility extends JFrame {
    private JPanel MainFrame;
    private JTextField Stu_number;
    private JButton uniqoneshowButton;
    private JTable Attendance_table;
    private JButton AllshowButton;
    private JButton allAttendanceButton;
    private JButton allMedicelsButton;
    private JButton eligibilityButton;

    public Attendance_Eligibility() {
        setContentPane(MainFrame);
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Eligibility");
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        String Course_code="ICT2142";

        AllshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allstudentattendanceprecent(Course_code);
            }
        });
        uniqoneshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Stu_id=Stu_number.getText().trim();
                if(Stu_id.isEmpty()){
                    JOptionPane.showMessageDialog(MainFrame,"Please enter student number");
                    return;
                }
                    uniqstudentattendancepresent(Stu_id, Course_code);

            }
        });
        allAttendanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attendanceTable(Course_code);
            }
        });
        allMedicelsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allMedicels(Course_code);
            }
        });
        eligibilityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void attendanceTable(String Course_code) {
        String url="jdbc:mysql://localhost:3306/techlms";
        String user="root";
        String password="";

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(MainFrame, e);
            }
            Connection con = DriverManager.getConnection(url, user, password);
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM attendance WHERE Course_code = ?");
            pstm.setString(1, Course_code);
            ResultSet rs = pstm.executeQuery();

            String[] Column =new String[3+15];
            Column[0]="Stu_id";
            Column[1]="Course_code";
            Column[2]="Course_type";
            for (int i = 1; i <= 15; i++) {
                Column[2 + i] = "Week " + i;
            }

            Map<String,String[]>studentmap=new LinkedHashMap<>();

            while (rs.next()) {
                String Stu_id=rs.getString("Stu_id");
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
                if (week_no >= 1 && week_no <= 15) {
                    existingRow[2 + week_no] = status;
                }

            }
            String[][] data=new String[studentmap.size()][3+15];
            int i=0;
            for (String[] row:studentmap.values()) {
                data[i++]=row;
            }

            Attendance_table.setModel(new javax.swing.table.DefaultTableModel(data,Column));
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    private void allMedicels(String Course_code){
            String url="jdbc:mysql://localhost:3306/techlms";
            String user="root";
            String password="";

            try{
                try{
                    Class.forName("com.mysql.cj.jdbc.Driver");
                }catch (ClassNotFoundException e){
                    JOptionPane.showMessageDialog(MainFrame,e);
                }
                Connection con = DriverManager.getConnection(url, user, password);

                PreparedStatement pstm = con.prepareStatement("SELECT * FROM Medical WHERE Course_code=? ORDER BY Stu_id");
                pstm.setString(1,Course_code);
                ResultSet rs = pstm.executeQuery();

                String[] Column ={"Stu_id","Course_code","Week_No","Day_No","Status"};
                DefaultTableModel model=new DefaultTableModel(null,Column);
                Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(MainFrame, "Medical's Not Found");
                    return;
                }

                while (rs.next()) {
                    String Stu_id=rs.getString("Stu_id");
                    String Medi_Course_code=rs.getString("Course_code");
                    String Week_No=rs.getString("Week_No");
                    String Day_No=rs.getString("Day_No");
                    String Status=rs.getString("Status");

                   model.addRow(new String[]{Stu_id,Medi_Course_code,Week_No,Day_No,Status});
                }
                Attendance_table.setModel(model);
            }catch (SQLException e){
                JOptionPane.showMessageDialog(MainFrame,e);
            }
    }

    private void allstudentattendanceprecent(String Course_code){

        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            String[] Column = {"Stu_id", "Course_Code", "Percentage", "Eligibility"};
            DefaultTableModel model = new DefaultTableModel(null, Column);
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            PreparedStatement coursepstm = con.prepareStatement("SELECT DISTINCT Stu_id FROM attendance WHERE Course_Code=?");
            coursepstm.setString(1, Course_code);
            ResultSet course_rs = coursepstm.executeQuery();
            while (course_rs.next()) {
            String Stu_id = course_rs.getString("Stu_id");

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
                        String medicalStatus = checkMedicalStatus(con, Stu_id, Course_code);
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
                        String medicalStatus = checkMedicalStatus(con, Stu_id, Course_code);
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
            Attendance_table.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private void uniqstudentattendancepresent(String Stu_id, String Course_code){

        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            String[] Column = {"Stu_id", "Course_Code", "Percentage", "Eligibility"};
            DefaultTableModel model = new DefaultTableModel(null, Column);
            Attendance_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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
                    double lecHour = rsTheory.getDouble("Lec_hour");
                    String status = rsTheory.getString("Status");
                    totalTheory += lecHour;
                    if (status.equalsIgnoreCase("Present")) {
                        presentTheory += lecHour;
                    }else if(status.equalsIgnoreCase("Medical")){
                        String medicalStatus = checkMedicalStatus(con, Stu_id, Course_code);
                        if(medicalStatus.equalsIgnoreCase("Approved")){
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
                    double lecHour = rsPractical.getDouble("Lec_hour");
                    String status = rsPractical.getString("Status");
                    totalPractical += lecHour;
                    if (status.equalsIgnoreCase("Present")) {
                        presentPractical += lecHour;
                    }else if(status.equalsIgnoreCase("Medical")){
                        String medicalStatus = checkMedicalStatus(con, Stu_id, Course_code);
                        if(medicalStatus.equalsIgnoreCase("Approved")){
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
                    model.addRow(new Object[]{Stu_id, Course_code +"-T,P", String.format("%.2f", combinedPercent) + "%", combinedEligibility});

                } else if (hasTheory) {
                    model.addRow(new Object[]{Stu_id, Course_code, String.format("%.2f", theoryPercent) + "%", theoryEligibility});
                } else if (hasPractical) {
                    model.addRow(new Object[]{Stu_id, Course_code, String.format("%.2f", practicalPercent) + "%", practicalEligibility});
                }

            Attendance_table.setModel(model);
            showsingleattendancerecord(Stu_id,Course_code);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }

    private void showsingleattendancerecord(String Stu_id, String Course_code){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/techlms", "root", "");
            PreparedStatement pstm = con.prepareStatement("SELECT * FROM attendance WHERE Stu_id=? AND Course_code=? ORDER BY Stu_id, Course_code, Week_No");
            pstm.setString(1,Stu_id);
            pstm.setString(2,Course_code);
            ResultSet rs = pstm.executeQuery();

            String[] Column =new String[3+15];
            Column[0]="Stu_id";
            Column[1]="Course_code";
            Column[2]="Course_type";
            for (int i = 1; i <= 15; i++) {
                Column[2 + i] = "Week " + i;
            }

            Map<String,String[]>studentmap=new LinkedHashMap<>();

            while (rs.next()) {
//                String Stu_id=rs.getString("Stu_id");
//                String Course_code=rs.getString("Course_code");
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
            String[][] data=new String[studentmap.size()][3+15];
            int i=0;
            for (String[] row:studentmap.values()) {
                data[i++]=row;
            }

            JFrame attendanceFrame = new JFrame("Attendance Records");
            attendanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            attendanceFrame.setSize(600, 400);
//            attendanceFrame.setLocationRelativeTo(null);
            JTable table = new JTable(data, Column);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);
            attendanceFrame.add(scrollPane);
            attendanceFrame.setVisible(true);

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    public String checkMedicalStatus(Connection con, String Stu_id, String Course_code) {
        try {
            PreparedStatement med = con.prepareStatement("SELECT Status FROM medical WHERE Stu_id = ? AND Course_code = ?");
            med.setString(1, Stu_id);
            med.setString(2, Course_code);
            ResultSet rs = med.executeQuery();

            while (rs.next()) {
                if (rs.getString("Status").equalsIgnoreCase("Approved")) {
                    return "Approved";
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
        return "None";
    }


    public static void main(String[] args) {
        new Attendance_Eligibility();
    }
}
