import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Lec_Add_marks extends JFrame {
    private JTable marktable;
    private JButton ADDButton;
    private JButton deleteButton;
    private JTextField mark_textField;
    private JTextField student_id_textField;
    private JTextField Mark_id_textfield;
    private JComboBox mark_type_comboBox;
    private JPanel MainFrame;
    private JComboBox coursecodecomboBox;

    public Lec_Add_marks() {

        setContentPane(MainFrame);
        setSize(1024, 768);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Add Marks");
        setResizable(true);
        setLayout(null);

        String Lec_ID="LEC003";

        showmarkstable(Lec_ID);
        coursecodeselection(Lec_ID);

        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addmarks(Lec_ID);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectdeletingrecord(Lec_ID);
            }
        });
    }

    public void showmarkstable(String Lec_ID){
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

            pst.setString(1, Lec_ID);
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

    public void addmarks(String Lecid){
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
                insertStmt.setString(4, Lecid);

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

            showmarkstable(Lecid);

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

    public void selectdeletingrecord(String Lec_ID){
        int selectedRow = marktable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(MainFrame, "Please select a row to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
        else {
            String markID = marktable.getModel().getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(MainFrame, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteRecordFromTable(markID);
                showmarkstable(Lec_ID);
            }}
    }

    public void coursecodeselection(String Lec_ID){
        String url = "jdbc:mysql://localhost:3306/techlms";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT Course_code FROM course WHERE Lec_id = ?");
            ps.setString(1, Lec_ID);
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

    public static void main(String[] args) {
        new Lec_Add_marks();
    }
}
