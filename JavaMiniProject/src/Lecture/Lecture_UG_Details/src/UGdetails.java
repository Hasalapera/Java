import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UGdetails extends JFrame {
    private JTextField Stu_number;
    private JButton showButton;
    private JTable Stu_details_table;
    private JPanel MainFrame;
    private JButton showButton2;

    public UGdetails() {
        setContentPane(MainFrame);
        setTitle("Grade and GPA");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stu_number = Stu_number.getText();
                if(!stu_number.isEmpty()) {
                    filterUgraduatesDetails(stu_number);
                }
               else {
                   JOptionPane.showMessageDialog(null, "Please enter a student number");
                }
            }
        });

        showButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               allUgraduatesDetails();
            }
        });
    }

    private void filterUgraduatesDetails(String stu_number) {
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try {
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e){
                JOptionPane.showMessageDialog(MainFrame,e);
            }
            Connection con = DriverManager.getConnection(url, user, password);

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
                Stu_number.setText("");

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
        String url = "jdbc:mysql://localhost:3306/techlms";
        String user = "root";
        String password = "";

        try{
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e){
                JOptionPane.showMessageDialog(MainFrame,e);
            }
            Connection con = DriverManager.getConnection(url, user, password);
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

    public static void main(String[] args) {
        new UGdetails();
    }

}
