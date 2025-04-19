import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Arrays;

public class CA extends JFrame{
    private JPanel MainFrame;
    private JButton AllshowButton;
    private JTextField stu_numbertextField;
    private JButton stu_numbershowButton;
    private JTable Eligibilitytable;

    public CA() {
        setContentPane(MainFrame);
        setSize(1024, 768);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("CA Eligibility");
        setLocationRelativeTo(null);
        setResizable(true);

        String Course_code="ICT2113";

        AllshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
allcamarks(Course_code);
            }
        });
        stu_numbershowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Student_ID=stu_numbertextField.getText();

                if(!Student_ID.isEmpty()){
                    uniqcamarks(Student_ID,Course_code);
                }
else {
    JOptionPane.showMessageDialog(MainFrame, "Please Enter Student ID");
                }
            }
        });
    }

    private double allcamarks(String Course_code){

        double CA_marks=0.0;

        String url="jdbc:mysql://localhost:3306/techlms";
        String user="root";
        String password="";

        try{
            try{
               Class.forName("com.mysql.cj.jdbc.Driver");
            }catch (ClassNotFoundException e){
                JOptionPane.showMessageDialog(MainFrame, e);
            }

            Connection con=DriverManager.getConnection(url, user, password);

            DefaultTableModel model=new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                    "Student_ID","Course Code","Quiz_01", "Quiz_02", "Quiz_03", "Quiz_04",
                    "Assignment_01", "Assignment_02", "Mid_Theory", "Mid_Practical",
                    "CA_Marks", "Eligibility"

            });

            PreparedStatement pstm=con.prepareStatement("select * from marks where course_code=?");
            pstm.setString(1,Course_code);
            ResultSet rs=pstm.executeQuery();

            while(rs.next()){
            String stu_id=rs.getString("Stu_id");
            double assignment1=rs.getDouble("Assignment_01");
            double assignment2=rs.getDouble("Assignment_02");
            double Quiz_01=rs.getDouble("Quiz_01");
            double Quiz_02=rs.getDouble("Quiz_02");
            double Quiz_03=rs.getDouble("Quiz_03");
            double Quiz_04=rs.getDouble("Quiz_04");
            double midtermtheory=rs.getDouble("Mid_theory");
            double midtermpractical=rs.getDouble("Mid_practical");


            double CA_cutoff1=19.5;
            double CA_cutoff2=15.0;
            String Eligibility="";

            double[] Quizzes = {Quiz_01, Quiz_02, Quiz_03, Quiz_04};
            Arrays.sort(Quizzes);
            double[] Assignments = {assignment1, assignment2};

            switch (Course_code){
                case "ICT2113":

                    double quizMark2113 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                    double midtermMark2113 = (midtermpractical + midtermtheory) / 2 * 0.20;
                    CA_marks=quizMark2113+midtermMark2113;
                    if(CA_marks>=CA_cutoff2){
                        Eligibility="Eligible";
                    }
                    else{
                        Eligibility="Not Eligible";
                    }
                    break;
                case "ICT2122":

                    double quizMark2122 = (Quizzes[3] + Quizzes[2]+Quizzes[1]) / 3 * 0.10;
                    CA_marks=quizMark2122+(Assignments[0]*0.10)+(midtermtheory*0.20);
                    if(CA_marks>=CA_cutoff1){
                        Eligibility="Eligible";
                    }
                    else{
                        Eligibility="Not Eligible";
                    }
                    break;
                case "ICT2132":

                    double quizMark2132 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                    double assessmentMark2132 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                    CA_marks=quizMark2132+assessmentMark2132;
                    if(CA_marks>=CA_cutoff2){
                        Eligibility="Eligible";
                    }
                    else{
                        Eligibility="Not Eligible";
                    }
                    break;
                 case "ICT2142":

                     double AssessmentMark2142 = (Assignments[0])* 0.20;
                     double MidtermMark2142 = (midtermpractical)* 0.20;
                     CA_marks=AssessmentMark2142+MidtermMark2142;
                     if(CA_marks>=CA_cutoff1){
                         Eligibility="Eligible";
                     }
                     else{
                         Eligibility="Not Eligible";
                     }
                     break;
                 case "ICT2152":

                     double quizMark2152 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                     double assessmentMark2152 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                     CA_marks=quizMark2152+assessmentMark2152;
                     if(CA_marks>=CA_cutoff2){
                         Eligibility="Eligible";
                     }
                     else{
                         Eligibility="Not Eligible";
                     }
                     break;
                default:
                    JOptionPane.showMessageDialog(MainFrame, "Course Cannot find");
                    break;
            }
                model.addRow(new Object[]{
                        stu_id,Course_code,
                        Quiz_01, Quiz_02, Quiz_03, Quiz_04,
                        assignment1, assignment2,
                        midtermtheory, midtermpractical,
                        String.format("%.2f", CA_marks),
                        Eligibility
                });
            }

            Eligibilitytable.setModel(model);


        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame, e);
        }
        return CA_marks;
    }

    private double uniqcamarks(String stu_id,String Course_code){

        double CA_marks=0.0;

        String url="jdbc:mysql://localhost:3306/techlms";
        String user="root";
        String password="";

        try{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            JOptionPane.showMessageDialog(MainFrame, e);
        }
        Connection con=DriverManager.getConnection(url, user, password);
            DefaultTableModel model=new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{
                    "Student_ID","Course Code", "Quiz_01", "Quiz_02", "Quiz_03", "Quiz_04",
                    "Assignment_01", "Assignment_02", "Mid_Theory", "Mid_Practical",
                    "CA_Marks", "Eligibility"

            });

            PreparedStatement pstm=con.prepareStatement("select * from marks where Stu_id=? AND Course_code=?");
            pstm.setString(1,stu_id);
            pstm.setString(2,Course_code);
            ResultSet rs=pstm.executeQuery();

            while(rs.next()){

                double assignment1=rs.getDouble("Assignment_01");
                double assignment2=rs.getDouble("Assignment_02");
                double Quiz_01=rs.getDouble("Quiz_01");
                double Quiz_02=rs.getDouble("Quiz_02");
                double Quiz_03=rs.getDouble("Quiz_03");
                double Quiz_04=rs.getDouble("Quiz_04");
                double midtermtheory=rs.getDouble("Mid_theory");
                double midtermpractical=rs.getDouble("Mid_practical");


                double CA_cutoff1=19.5;
                double CA_cutoff2=15.0;
                String Eligibility="";

                double[] Quizzes = {Quiz_01, Quiz_02, Quiz_03, Quiz_04};
                double[] Assignments = {assignment1, assignment2};

                switch (Course_code){
                    case "ICT2113":
                        Arrays.sort(Quizzes);
                        double quizMark2113 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                        double midtermMark2113 = (midtermpractical + midtermtheory) / 2 * 0.20;
                        CA_marks=quizMark2113+midtermMark2113;
                        if(CA_marks>=CA_cutoff2){
                            Eligibility="Eligible";
                        }
                        else{
                            Eligibility="Not Eligible";
                        }
                        break;
                    case "ICT2122":
                        Arrays.sort(Quizzes);
                        double quizMark2122 = (Quizzes[3] + Quizzes[2]+Quizzes[1]) / 3 * 0.10;
                        CA_marks=quizMark2122+(Assignments[0]*0.10)+(midtermtheory*0.20);
                        if(CA_marks>=CA_cutoff1){
                            Eligibility="Eligible";
                        }
                        else{
                            Eligibility="Not Eligible";
                        }
                        break;
                    case "ICT2132":
                        Arrays.sort(Quizzes);
                        double quizMark2133 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                        double assessmentMark2133 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                        CA_marks=quizMark2133+assessmentMark2133;
                        if(CA_marks>=CA_cutoff2){
                            Eligibility="Eligible";
                        }
                        else{
                            Eligibility="Not Eligible";
                        }
                        break;
                    case "ICT2142":
                        Arrays.sort(Quizzes);
                        double AssessmentMark2142 = (Assignments[0])* 0.20;
                        double MidtermMark2142 = (midtermpractical)* 0.20;
                        CA_marks=AssessmentMark2142+MidtermMark2142;
                        if(CA_marks>=CA_cutoff1){
                            Eligibility="Eligible";
                        }
                        else{
                            Eligibility="Not Eligible";
                        }
                        break;
                    case "ICT2152":
                        Arrays.sort(Quizzes);
                        double quizMark2152 = (Quizzes[3] + Quizzes[2]) / 2 * 0.10;
                        double assessmentMark2152 = (Assignments[0] + Assignments[1]) / 2 * 0.20;
                        CA_marks=quizMark2152+assessmentMark2152;
                        if(CA_marks>=CA_cutoff2){
                            Eligibility="Eligible";
                        }
                        else{
                            Eligibility="Not Eligible";
                        }
                        break;
                }
                model.addRow(new Object[]{
                        stu_id,Course_code,
                        Quiz_01, Quiz_02, Quiz_03, Quiz_04,
                        assignment1, assignment2,
                        midtermtheory, midtermpractical,
                        String.format("%.2f", CA_marks),
                        Eligibility
                });
            }

            Eligibilitytable.setModel(model);
            stu_numbertextField.setText("");

        }catch (SQLException e){
            JOptionPane.showMessageDialog(MainFrame, e);
        }
        return CA_marks;
    }

    public static void main(String[] args) {
        new CA();
    }
}

