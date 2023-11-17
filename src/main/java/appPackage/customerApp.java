package appPackage;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class customerApp extends JFrame {
    
    static String dataBase;
    static String tableName;
    static String portNumber;
    static String userName;
    static String passWord;
    boolean connection;
    
    public customerApp() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("SQL-App / Customer Database");
        this.setSize(500, 430);
        
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Data");
        JMenuItem item = new JMenuItem("Login");
        JMenuItem item2 = new JMenuItem("Create Database");
        menu.add(item);
        menu.add(item2);
        bar.add(menu);
        this.setJMenuBar(bar);
        
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                loginDialog();
            }
        });
        
        item2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                createDatabaseDialog();
            }
        });
        
        JPanel customerPanel = new screenCustomer();
        JScrollPane adminPanel = new screenAdmin();
        
        JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT );
        tabPane.addTab("Customer", customerPanel);
        tabPane.addTab("Admin", adminPanel);
        this.add(tabPane);
        this.setLocationRelativeTo(null);
        
        dataBase = "customers";
        tableName = "customerlist";
        portNumber = "3306";
        userName = "root";
        passWord = "xxxx";
    }
    
    public void checkLogin() {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+portNumber+"/", userName, passWord);
            Statement stmt = conn.createStatement();
            ) {
            connection = true;
        } catch(SQLException ex) {
            connection = false;
        }
    }
    
    public void loginDialog() {
        JDialog dbDialog = new JDialog();
        dbDialog.setTitle("Login");
        dbDialog.setSize(350,450);
        dbDialog.setModal(true);
        dbDialog.setLocationRelativeTo(null);
        dbDialog.setLayout(new BoxLayout(dbDialog.getContentPane(), BoxLayout.Y_AXIS));
        
        Box boxUser = new Box(BoxLayout.X_AXIS);
        JLabel labelUser = new JLabel("Username");
        boxUser.add(labelUser);
        boxUser.add(Box.createHorizontalGlue());
        
        Box boxUser2 = new Box(BoxLayout.X_AXIS);
        JTextField tfUser = new JTextField("myuser", 12);
        tfUser.setMaximumSize(tfUser.getPreferredSize());
        boxUser2.add(tfUser);
        boxUser2.add(Box.createHorizontalGlue());
        
        Box boxPW = new Box(BoxLayout.X_AXIS);
        JLabel labelPW = new JLabel("Password");
        boxPW.add(labelPW);
        boxPW.add(Box.createHorizontalGlue());
        
        Box boxPW2 = new Box(BoxLayout.X_AXIS);
        JTextField tfPW = new JTextField("xxxx", 12);
        tfPW.setMaximumSize(tfPW.getPreferredSize());
        boxPW2.add(tfPW);
        boxPW2.add(Box.createHorizontalGlue());
        
        Box boxPort = new Box(BoxLayout.X_AXIS);
        JLabel labelPort = new JLabel("Port Number");
        boxPort.add(labelPort);
        boxPort.add(Box.createHorizontalGlue());
        
        Box boxPort2 = new Box(BoxLayout.X_AXIS);
        JTextField tfPort = new JTextField("3306", 12);
        tfPort.setMaximumSize(tfPort.getPreferredSize());
        boxPort2.add(tfPort);
        boxPort2.add(Box.createHorizontalGlue());
        
        Box boxButtons = new Box(BoxLayout.X_AXIS);
        JButton buttonLogin = new JButton("Login");
        JButton buttonClose = new JButton("Close");
        boxButtons.add(buttonLogin);
        boxButtons.add(buttonClose);
        boxButtons.add(Box.createHorizontalGlue());
        

        dbDialog.add(boxUser);
        dbDialog.add(boxUser2);
        dbDialog.add(boxPW);
        dbDialog.add(boxPW2);
        dbDialog.add(boxPort);
        dbDialog.add(boxPort2);
        dbDialog.add(boxButtons);
        
        buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                userName = tfUser.getText();
                passWord = tfPW.getText();
                portNumber = tfPort.getText();
                checkLogin();
                if (connection) {
                    dbDialog.dispose();
                    chooseDatabaseDialog();
                } else {
                    JOptionPane.showMessageDialog(null, "Can't find user.");
                }
                
                
            }
        });
        
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dbDialog.dispose();
            }
        });
        
        dbDialog.pack();
        dbDialog.setVisible(true);
    }
    
    public void chooseDatabaseDialog() {
        JDialog dbDialog = new JDialog();
        dbDialog.setTitle("Choose Database");
        dbDialog.setSize(350,450);
        dbDialog.setModal(true);
        dbDialog.setLocationRelativeTo(null);
        dbDialog.setLayout(new BoxLayout(dbDialog.getContentPane(), BoxLayout.Y_AXIS));
        
        Box boxDB = new Box(BoxLayout.X_AXIS);
        JLabel labelDB = new JLabel("Databases:");
        boxDB.add(labelDB);
        boxDB.add(Box.createHorizontalGlue());
        
        Box boxButtons = new Box(BoxLayout.X_AXIS);
        JButton buttonSelect = new JButton("Select");
        JButton buttonClose = new JButton("Close");
        boxButtons.add(buttonSelect);
        boxButtons.add(buttonClose);
        boxButtons.add(Box.createHorizontalGlue());
        
        List<String> listDatabases = new ArrayList<>();
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+portNumber+"/", userName, passWord);
            Statement stmt = conn.createStatement();
            ) {
            String sqlDataBase = "show databases";
            ResultSet rset = stmt.executeQuery(sqlDataBase);
            while (rset.next()) {
                listDatabases.add(rset.getString(1));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        JList jlistDatabases = new JList(listDatabases.toArray());
        dbDialog.add(boxDB);
        dbDialog.add(jlistDatabases);
        dbDialog.add(boxButtons);
        
        buttonSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!jlistDatabases.isSelectionEmpty()) {
                    dataBase = String.valueOf(jlistDatabases.getSelectedValue());
                    dbDialog.dispose();
                    chooseTableDialog();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a Database.");
                }
            }
        });
        
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dbDialog.dispose();
            }
        });
        
        dbDialog.pack();
        dbDialog.setVisible(true);
    }
    
    public void chooseTableDialog() {
        JDialog dbDialog = new JDialog();
        dbDialog.setTitle("Choose Table");
        dbDialog.setSize(350,450);
        dbDialog.setModal(true);
        dbDialog.setLocationRelativeTo(null);
        dbDialog.setLayout(new BoxLayout(dbDialog.getContentPane(), BoxLayout.Y_AXIS));
        
        Box boxDB = new Box(BoxLayout.X_AXIS);
        JLabel labelDB = new JLabel("Tables:");
        boxDB.add(labelDB);
        boxDB.add(Box.createHorizontalGlue());
        
        Box boxButtons = new Box(BoxLayout.X_AXIS);
        JButton buttonSelect = new JButton("Select");
        JButton buttonClose = new JButton("Close");
        boxButtons.add(buttonSelect);
        boxButtons.add(buttonClose);
        boxButtons.add(Box.createHorizontalGlue());
        
        List<String> listTables = new ArrayList<>();
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+portNumber+"/"+dataBase+"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", userName, passWord);
            Statement stmt = conn.createStatement();
            ) {
            String sqlTable = "show tables";
            ResultSet rset = stmt.executeQuery(sqlTable);
            while (rset.next()) {
                listTables.add(rset.getString(1));
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        JList jlistTables = new JList(listTables.toArray());
        dbDialog.add(boxDB);
        dbDialog.add(jlistTables);
        dbDialog.add(boxButtons);
        
        buttonSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (!jlistTables.isSelectionEmpty()) {
                    tableName = String.valueOf(jlistTables.getSelectedValue());
                    dbDialog.dispose();
                    JOptionPane.showMessageDialog(null, "Login complete.");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a Table.");
                }
            }
        });
        
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dbDialog.dispose();
            }
        });
        
        dbDialog.pack();
        dbDialog.setVisible(true);
    }
    
    public void createDatabaseDialog() {
        JDialog dbDialog = new JDialog();
        dbDialog.setTitle("Create Database");
        dbDialog.setSize(350,450);
        dbDialog.setModal(true);
        dbDialog.setLocationRelativeTo(null);
        dbDialog.setLayout(new BoxLayout(dbDialog.getContentPane(), BoxLayout.Y_AXIS));
        
        Box boxName = new Box(BoxLayout.X_AXIS);
        JLabel labelName = new JLabel("Database Name");
        boxName.add(labelName);
        boxName.add(Box.createHorizontalGlue());
        
        Box boxName2 = new Box(BoxLayout.X_AXIS);
        JTextField tfName = new JTextField("customers", 12);
        tfName.setMaximumSize(tfName.getPreferredSize());
        boxName2.add(tfName);
        boxName2.add(Box.createHorizontalGlue());
        
        Box boxTable = new Box(BoxLayout.X_AXIS);
        JLabel labelTable = new JLabel("Table Name");
        boxTable.add(labelTable);
        boxTable.add(Box.createHorizontalGlue());
        
        Box boxTable2 = new Box(BoxLayout.X_AXIS);
        JTextField tfTable = new JTextField("customerlist", 12);
        tfTable.setMaximumSize(tfTable.getPreferredSize());
        boxTable2.add(tfTable);
        boxTable2.add(Box.createHorizontalGlue());
        
        Box boxPort = new Box(BoxLayout.X_AXIS);
        JLabel labelPort = new JLabel("Port Number");
        boxPort.add(labelPort);
        boxPort.add(Box.createHorizontalGlue());
        
        Box boxPort2 = new Box(BoxLayout.X_AXIS);
        JTextField tfPort = new JTextField("3306", 12);
        tfPort.setMaximumSize(tfPort.getPreferredSize());
        boxPort2.add(tfPort);
        boxPort2.add(Box.createHorizontalGlue());
        
        Box boxUser = new Box(BoxLayout.X_AXIS);
        JLabel labelUser = new JLabel("Username");
        boxUser.add(labelUser);
        boxUser.add(Box.createHorizontalGlue());
        
        Box boxUser2 = new Box(BoxLayout.X_AXIS);
        JTextField tfUser = new JTextField("myuser", 12);
        tfUser.setMaximumSize(tfUser.getPreferredSize());
        boxUser2.add(tfUser);
        boxUser2.add(Box.createHorizontalGlue());
        
        Box boxPW = new Box(BoxLayout.X_AXIS);
        JLabel labelPW = new JLabel("Password");
        boxPW.add(labelPW);
        boxPW.add(Box.createHorizontalGlue());
        
        Box boxPW2 = new Box(BoxLayout.X_AXIS);
        JTextField tfPW = new JTextField("xxxx", 12);
        tfPW.setMaximumSize(tfPW.getPreferredSize());
        boxPW2.add(tfPW);
        boxPW2.add(Box.createHorizontalGlue());
        
        Box boxButtons = new Box(BoxLayout.X_AXIS);
        JButton buttonCreate = new JButton("Create");
        JButton buttonClose = new JButton("Close");
        boxButtons.add(buttonCreate);
        boxButtons.add(buttonClose);
        boxButtons.add(Box.createHorizontalGlue());
        
        dbDialog.add(boxName);
        dbDialog.add(boxName2);
        dbDialog.add(boxTable);
        dbDialog.add(boxTable2);
        dbDialog.add(boxPort);
        dbDialog.add(boxPort2);
        dbDialog.add(boxUser);
        dbDialog.add(boxUser2);
        dbDialog.add(boxPW);
        dbDialog.add(boxPW2);
        dbDialog.add(boxButtons);
        
        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dataBase = tfName.getText();
                portNumber = tfPort.getText();
                userName = tfUser.getText();
                passWord = tfPW.getText();
                createDatabase();
                createTable();
                dbDialog.dispose();
            }
        });
        
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dbDialog.dispose();
            }
        });
        
        dbDialog.pack();
        dbDialog.setVisible(true);
    }
    
    public void createDatabase() {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+portNumber+"/", userName, passWord);
            Statement stmt = conn.createStatement();
            ) {
            String sqlDataBase = "create database if not exists "+dataBase;
            stmt.executeUpdate(sqlDataBase);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createTable() {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+portNumber+"/"+dataBase+"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", userName, passWord);
            Statement stmt = conn.createStatement();
            ) {
            String sqlTable = "drop table if exists "+tableName;
            stmt.executeUpdate(sqlTable);
            
            sqlTable = "create table "+tableName+" (id INTEGER, lastname VARCHAR(50), name VARCHAR(50), birthdate DATE, street VARCHAR(50), postal VARCHAR(50), city VARCHAR(50), email VARCHAR(50), phone VARCHAR(50), PRIMARY KEY (id))";
            stmt.executeUpdate(sqlTable);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new customerApp().setVisible(true);
    }
}
