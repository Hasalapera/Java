package Admin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import database.DatabaseConnection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



public class AddNotices extends JFrame {
    public JPanel MainPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton addNoticeButton;
    private JButton clearButton;
    private JButton exitButton;

    public AddNotices() {
        setContentPane(MainPanel);
        setTitle("Add Notices");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        setLocationRelativeTo(null);
        setSize(2000, 800);
        setVisible(true);


        addNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String noticeId = textField1.getText().trim();
//                String adminId = textField2.getText().trim();
                String title = textField3.getText().trim();
                String date = textField4.getText().trim();
//                String adId = Session.loggedInUsername; // assuming Session class has this
                String adId = "Admin01";



                if (noticeId.isEmpty() || title.isEmpty() || date.isEmpty() ) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                /*if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid date (YYYY-MM-DD)");
                    return;
                }*/

                try {
//                    String trimmedDate = date.trim(); // extra trimming
                    String trimmedDate = date.trim().replaceAll("[\\n\\r\\t]", ""); // strip newlines, tabs
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate parsedDate = LocalDate.parse(trimmedDate, formatter); // this will throw if invalid
                    date = parsedDate.toString();
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date! Please enter a correct date in YYYY-MM-DD format.");
                    return;
                }


                // 1. Insert into database
                try {
                    Connection conn = DatabaseConnection.connect();
                    String sql = "INSERT INTO notice (Notice_id, Ad_id, Title, Date) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, noticeId);
                    stmt.setString(2, adId);
                    stmt.setString(3, title);
                    stmt.setString(4, date);
                    int rows = stmt.executeUpdate();




                    if (rows > 0) {
                        // 2. Create notice file
                        File dir = new File("notices");
                        if (!dir.exists()) dir.mkdirs(); // Create directory if it doesn't exist
                        File file = new File(dir, "notice_" + noticeId + ".txt");
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            writer.write("Title: " + title);
                            writer.newLine();
                            writer.write("Date: " + date);
                            writer.newLine();
                            writer.write("Content:");
                            writer.newLine();
//                            writer.write(content);
                        }

                        JOptionPane.showMessageDialog(null, "Notice added successfully!");
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add notice.");
                    }

                    stmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdHome(); // go back to admin home
                dispose();
            }
        });
    }

    private void clearFields() {
        textField1.setText("");
        textField2.setText("");
        textField3.setText("");
        textField4.setText("");
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }



    public static void main(String[] args) {
        new AddNotices();
    }
}

