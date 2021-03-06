package forms;

import models.entities.supplier;
import utils.setUIFont;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.sql.*;

public class Editsupplier extends JDialog {
    private JTextField textField_namesp;
    private JTextField textField_phonesp;
    private JTextField textField_emailsp;
    private JTextField textField_addresssp;
    private JTextField textField_deptsp;
    public Editsupplier(String name,String phone, String address,String email, String debt) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setResizable(false);
        setTitle("Edit Supplier");
        setUIFont.Font(new FontUIResource("Arial", Font.PLAIN, 12));
        setBounds(500, 100, 375, 400);
        setLayout(null);
        JLabel lblNewLabel = new JLabel("Edit Supplier");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel.setBounds(138, 10, 98, 36);
        add(lblNewLabel);

        JLabel lblnamesp = new JLabel("Name Supplier");
        lblnamesp.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblnamesp.setBounds(10, 72, 83, 25);
        add(lblnamesp);

        JLabel lblphonesp = new JLabel("Phone Supplier");
        lblphonesp.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblphonesp.setBounds(10, 122, 83, 25);
        add(lblphonesp);

        JLabel lblemailsp = new JLabel("Email Supplier");
        lblemailsp.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblemailsp.setBounds(10, 172, 83, 25);
        add(lblemailsp);

        JLabel lbladdresssp = new JLabel("Address Supplier");
        lbladdresssp.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbladdresssp.setBounds(10, 222, 92, 25);
        add(lbladdresssp);

        JLabel lbldeptsp = new JLabel("Dept");
        lbldeptsp.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lbldeptsp.setBounds(10, 272, 83, 25);
        add(lbldeptsp);

        textField_namesp = new JTextField();
        textField_namesp.setBounds(130, 76, 220, 19);
        add(textField_namesp);
        textField_namesp.setColumns(10);
        textField_namesp.setText(name);
        textField_namesp.setEditable(false);

        textField_phonesp = new JTextField();
        textField_phonesp.setBounds(130, 126, 220, 19);
        add(textField_phonesp);
        textField_phonesp.setColumns(10);
        textField_phonesp.setText(phone);

        textField_emailsp = new JTextField();
        textField_emailsp.setBounds(130, 176, 220, 19);
        add(textField_emailsp);
        textField_emailsp.setColumns(10);
        textField_emailsp.setText(email);

        textField_addresssp = new JTextField();
        textField_addresssp.setBounds(130, 226, 220, 19);
        add(textField_addresssp);
        textField_addresssp.setColumns(10);
        textField_addresssp.setText(address);

        textField_deptsp = new JTextField();
        textField_deptsp.setBounds(130, 276, 220, 19);
        add(textField_deptsp);
        textField_deptsp.setColumns(10);
        textField_deptsp.setText(debt);


        JButton btn_savesp = new JButton("Edit");
        btn_savesp.setFont(new Font("Tahoma", Font.PLAIN, 13));
        btn_savesp.setBounds(197, 319, 85, 21);
        add(btn_savesp);
        btn_savesp.addActionListener(e -> {
            String namesupplier=textField_namesp.getText();
            String phonesupplier=textField_phonesp.getText();
            String emailsupplier=textField_emailsp.getText();
            String addresssupplier=textField_addresssp.getText();
            Float deptsupplier= 0.0f;

            try {
                deptsupplier =Float.parseFloat(textField_deptsp.getText());
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
                JOptionPane.showMessageDialog(rootPane,"Please enter your dept supplier by number");
            }
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLBT;user=sa;password=123456";
                Connection conn = DriverManager.getConnection(connectionUrl);
                String sql = "update supplier set supplierphonenumber = '"+phonesupplier+"' , supplieremail ='"+emailsupplier+"' , supplieraddress ='"+addresssupplier+"' , dept = '"+deptsupplier+"' where suppliername ='"+name+"'";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                if (!namesupplier.equals("")) {
                    if (!phonesupplier.equals("")) {
                        if (!emailsupplier.equals("")) {
                            if (!addresssupplier.equals("")) {
                                if (CheckPhonesp(phonesupplier)){
                                    preparedStatement.executeUpdate();
                                    JOptionPane.showMessageDialog(rootPane, "Update Success");

                                    int row = MainSupplier.ts.row;
                                    int column = 0 ;
                                    for ( int i = 0 ; i<row ;i++)
                                    {
                                        if(MainSupplier.ts.labels.get(0+column).getText().equals(name))
                                        {
                                            MainSupplier.ts.labels.get(1+column).setText(textField_phonesp.getText());
                                            MainSupplier.ts.labels.get(2+column).setText(textField_emailsp.getText());
                                            MainSupplier.ts.labels.get(3+column).setText(textField_addresssp.getText());
                                            MainSupplier.ts.labels.get(4+column).setText(textField_deptsp.getText());
                                        }
                                        column += MainSupplier.ts.column;
                                    }
                                    textField_namesp.setText("");
                                    textField_phonesp.setText("");
                                    textField_emailsp.setText("");
                                    textField_addresssp.setText("");
                                    textField_deptsp.setText("");
                                }else {
                                    JOptionPane.showMessageDialog(rootPane,"Phone was available ");
                                }

                            } else {
                                JOptionPane.showMessageDialog(rootPane, "Please enter your address supplier");
                            }
                        } else {
                            JOptionPane.showMessageDialog(rootPane, "Please enter your email supplier");
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Please enter your phone supplier");
                    }
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Please enter your name supplier");
                }
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        setVisible(true);
    }
    public boolean CheckPhonesp(String phonesupplier) {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLBT;user=sa;password=123456";
        try (Connection con = DriverManager.getConnection(connectionUrl)) {
            Statement stmt = con.createStatement();
            String query = "SELECT supplierphonenumber FROM supplier WHERE supplierphonenumber ='" + phonesupplier + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean CheckNamesp(String namesupplier) {
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=QLBT;user=sa;password=123456";
        try (Connection con = DriverManager.getConnection(connectionUrl)) {
            Statement stmt = con.createStatement();
            String query = "SELECT suppliername FROM supplier WHERE suppliername ='" + namesupplier + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
