package Admin;

import javax.swing.*;
import database.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.awt.CardLayout;

public class ViewUserProfiles extends JFrame{
    public JPanel Mainpanel;
    private JPanel userdetailpanel;
    private JTable usertable;
    private JButton deleteButton;
    private JTextField username;
    private JButton clearButton;
//    private JButton exitButton;
    private JPanel parentCardPanel;
    private String returnCardName;


    public ViewUserProfiles(JPanel cardpanel, String returnToCard) {

//        setContentPane(Mainpanel);
//        setTitle("View User Profiles");
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        setLocationRelativeTo(null);
//        setSize(1000, 800);
//        setVisible(true);

        this.parentCardPanel = cardpanel;
        this.returnCardName = returnToCard;

        loadUserTable(); // Load the table when the panel is initialized

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameToDelete = username.getText().trim();
                deleteUser(usernameToDelete);

            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

//        exitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                CardLayout cl = (CardLayout) parentCardPanel.getLayout();
//                cl.show(parentCardPanel, returnCardName);
//                new AdHome(); // go back to admin home
//                dispose();
//            }
//        });
    }

    public void loadUserTable() {
        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            try {
                String sql = "SELECT * FROM user"; // Change 'users' to your actual table name
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                // Get metadata to build column names dynamically
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Table model
                DefaultTableModel model = new DefaultTableModel(){
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };


                // Add column names
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }

                // Add rows
                while (rs.next()) {
                    Object[] row = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        row[i - 1] = rs.getObject(i);
                    }
                    model.addRow(row);
                }

                // Set model to the JTable
                usertable.setModel(model);

                // Optional: auto resize columns
                usertable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                // Close connection
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error loading user data: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Database connection failed.");
        }
    }

    private void selectUserInTable(String usernameToFind) {
        DefaultTableModel model = (DefaultTableModel) usertable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (usernameToFind.equals(model.getValueAt(i, 0))) { // assuming username is in column 0
                usertable.setRowSelectionInterval(i, i);
                break;
            }
        }
    }

    private void deleteUser(String usernameToDelete) {
        if (usernameToDelete == null || usernameToDelete.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a username.");
            return;
        }

        Connection conn = DatabaseConnection.connect();
        if (conn != null) {
            try {
                String sql = "DELETE FROM user WHERE username = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, usernameToDelete);
                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(null, "User '" + usernameToDelete + "' deleted successfully.");
                    loadUserTable(); // Refresh table
                } else {
                    JOptionPane.showMessageDialog(null, "User not found.");
                }

                stmt.close();
                conn.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Database connection failed.");
        }
    }

    private void clearFields() {
        username.setText("");
    }



//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("View User Profiles");
//            ViewUserProfiles view = new ViewUserProfiles();
//            frame.setContentPane(view.Mainpanel); // this must match the GUI-designed panel
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(800, 600);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }


}




