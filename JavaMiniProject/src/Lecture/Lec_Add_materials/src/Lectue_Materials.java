import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;

public class Lectue_Materials extends JFrame {
    private JButton Deletebutton;
    private JButton Addbutton;
    private JTable Materials_Table;
    private JPanel MainFrame;
    private JComboBox coursecodedropdown;

    public Lectue_Materials() {

        setContentPane(MainFrame);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setTitle("Material Management");
        setLocationRelativeTo(null);
        setVisible(true);

        String Lec_ID="LEC002";
        String Course_Code="ICT2122";
        showcoursetable(Lec_ID);
        populateCourseComboBox(Lec_ID);

        Addbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            uploadfile(Lec_ID);
            }
        });
        Deletebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletematerial(Course_Code,Lec_ID);
            }
        });
    }

    private void showcoursetable(String Lec_ID) {

        String url = "jdbc:mysql://localhost:3306/techlms";
        String username = "root";
        String password = "";

        try{
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e){
                JOptionPane.showMessageDialog(MainFrame,e);
            }
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = con.prepareStatement("select Course_code,Course_name,Lecture_Material from course where lec_id=?");
            pstmt.setString(1, Lec_ID);
            ResultSet rs = pstmt.executeQuery();
            DefaultTableModel model=new DefaultTableModel();
            model.addColumn("Course Code");
            model.addColumn("Course Name");
            model.addColumn("Lecture Material");

            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getString("Course_code"),
                        rs.getString("Course_name"),
                        rs.getString("Lecture_Material")
                });
            }
            Materials_Table.setModel(model);
        }catch (SQLException e){
           JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    private void addmaterials(String Path,String Lec_ID,String Course_Code) {
        String url = "jdbc:mysql://localhost:3306/techlms";
        String username = "root";
        String password = "";

        try{
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e){
                JOptionPane.showMessageDialog(MainFrame,e);
            }
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("UPDATE course SET Lecture_Material = ? WHERE Lec_id = ? AND Course_code = ?");

            ps.setString(1, Path);
            ps.setString(2, Lec_ID);
            ps.setString(3, Course_Code);

            int rowsUpdated = ps.executeUpdate();
            if(rowsUpdated<0){
                JOptionPane.showMessageDialog(null, "Something went wrong");
            }
        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    private void uploadfile(String Lec_ID) {

        String Course_Code = (String) coursecodedropdown.getSelectedItem();
        if (Course_Code == null) {
            JOptionPane.showMessageDialog(this, "Please select a course first!");
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Select File");

        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF, Word, Excel Files", "pdf", "doc", "docx", "xls", "xlsx");
        fc.setFileFilter(filter);

        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            addmaterials(path,Lec_ID,Course_Code);
            showcoursetable(Lec_ID);
        }
    }

    private void deletematerial(String Course_Code,String Lec_ID) {

        String url = "jdbc:mysql://localhost:3306/techlms";
        String username = "root";
        String password = "";

        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e) {
               JOptionPane.showMessageDialog(MainFrame,e);
            }

            int selectedRow = Materials_Table.getSelectedRow();
            if(selectedRow==-1){
                JOptionPane.showMessageDialog(this, "Please select to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            else{
                String Lec_material = Materials_Table.getModel().getValueAt(selectedRow, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                }
            }

            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement pstmt = con.prepareStatement("UPDATE course SET Lecture_Material = NULL WHERE Lec_id = ? AND Course_code =?");
            pstmt.setString(1, Lec_ID);
            pstmt.setString(2, Course_Code);
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected==0){
                JOptionPane.showMessageDialog(this, "No record was deleted", "Error", JOptionPane.ERROR_MESSAGE);
            }
            showcoursetable(Lec_ID);
        }catch (SQLException e) {
           JOptionPane.showMessageDialog(MainFrame,e);
        }
    }

    private void populateCourseComboBox(String Lec_ID) {
        String url = "jdbc:mysql://localhost:3306/techlms";
        String username = "root";
        String password = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement("SELECT Course_code FROM course WHERE Lec_id = ?");
            ps.setString(1, Lec_ID);
            ResultSet rs = ps.executeQuery();

            coursecodedropdown.removeAllItems();
            while (rs.next()) {
                coursecodedropdown.addItem(rs.getString("Course_code"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainFrame, e);
        }
    }


    public static void main(String[] args) {
        new Lectue_Materials();
    }
}
