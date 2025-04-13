import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Eligibility extends JFrame {
    private JPanel MainFrame;
    private JTextField Stu_number;
    private JButton oneshowButton;
    private JTable Attendance_table;
    private JButton AllshowButton;
    private JButton allAttendanceButton;
    private JButton allMedicelsButton;

    public Eligibility() {
        setContentPane(MainFrame);
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Eligibility");
        setLocationRelativeTo(null);
        setResizable(true);
        setVisible(true);

        AllshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        oneshowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
    public static void main(String[] args) {
        new Eligibility();
    }
}
