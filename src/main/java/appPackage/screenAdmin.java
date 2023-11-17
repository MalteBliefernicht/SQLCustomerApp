package appPackage;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;


public class screenAdmin extends JScrollPane {
    
    JTable table;
    JPanel panel;
    
    public screenAdmin() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        Box boxMain = new Box(BoxLayout.Y_AXIS);
        
        Box boxShow = new Box(BoxLayout.X_AXIS);
        JButton buttonShow = new JButton("Show Database");
        JTextField tfSearch = new JTextField(15);
        tfSearch.setMaximumSize(tfSearch.getPreferredSize());
        JButton buttonSearch = new JButton("Search");
        JButton buttonAdvanced = new JButton("Advanced");
        boxShow.add(Box.createHorizontalStrut(5));
        boxShow.add(buttonShow);
        boxShow.add(Box.createHorizontalStrut(15));
        boxShow.add(tfSearch);
        boxShow.add(Box.createHorizontalStrut(5));
        boxShow.add(buttonSearch);
        boxShow.add(Box.createHorizontalStrut(5));
        boxShow.add(buttonAdvanced);
        boxShow.add(Box.createHorizontalGlue());
        
        
        Box boxTable = new Box(BoxLayout.Y_AXIS);
        String [] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
        Object[][] data = {{"","","","","","","","",""}};
        table = new JTable(data, colNames);
        boxTable.add(table.getTableHeader());
        boxTable.add(table);
        
        boxMain.add(Box.createVerticalStrut(10));
        boxMain.add(boxShow);
        boxMain.add(Box.createVerticalStrut(20));
        boxMain.add(boxTable);
        panel.add(boxMain, BorderLayout.PAGE_START);
        
        buttonShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshTable();
            }
        });
        buttonSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchTable(tfSearch.getText());
            }
        });
        buttonAdvanced.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                advancedSearch();
            }
        });
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == 3) {
                    Point pt = new Point(evt.getX(),evt.getY());
                    int rowIndex = table.rowAtPoint(pt);
                    table.setRowSelectionInterval(rowIndex, rowIndex);
                    JPopupMenu pop = new JPopupMenu();
                    JMenuItem itemEdit = new JMenuItem("Edit");
                    JMenuItem itemDelete = new JMenuItem("Delete");
                    pop.add(itemEdit);
                    pop.add(itemDelete);
                    
                    itemEdit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            popupEdit();
                        }
                    });
                    itemDelete.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                            int delete = JOptionPane.showOptionDialog(null,"Are you sure?",
                                    "Delete Record",JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,null,null,null);
                            if (delete == 0) {
                                popupDelete();
                            }
                        }
                    });

                    pop.show(table,evt.getX(),evt.getY());
                }
            }
        });
        
        this.setViewportView(panel);
    }
    
    public void refreshTable() {
        
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlSelect = "select * from "+customerApp.tableName;
            ResultSet rset = stmt.executeQuery(sqlSelect);
            
            List<List<Object>> data = new ArrayList<List<Object>>();
            int row = 0;
            while(rset.next()) {
                List<Object> rowData = new ArrayList<Object>();
                int id = rset.getInt("id");
                String name = rset.getString("name");
                String lastname = rset.getString("lastname");
                LocalDate date = rset.getObject("birthdate", LocalDate.class);
                String street = rset.getString("street");
                String postal = rset.getString("postal");
                String city = rset.getString("city");
                String mail = rset.getString("email");
                String phone = rset.getString("phone");
                
                rowData.add(id);
                rowData.add(name);
                rowData.add(lastname);
                rowData.add(date);
                rowData.add(street);
                rowData.add(postal);
                rowData.add(city);
                rowData.add(mail);
                rowData.add(phone);
                
                data.add(rowData);
                
                row++;
            }
            
            String [] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
            Object[][] arrayData = new Object[data.size()][];
            for (int i=0; i<data.size(); i++) {
                List<Object> y = data.get(i);
                arrayData[i] = y.toArray(new Object[y.size()]);
            }
            DefaultTableModel myModel = new DefaultTableModel(arrayData, colNames);
            table.setModel(myModel);
            table.setRowSorter(new TableRowSorter(myModel));
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void searchTable(String searchTerm) {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlSelect = "select * from "+customerApp.tableName+
                    " where concat(id,'',lastname,'',name,'',birthdate,'',street,"
                    +"'',postal,'',city,'',email,'',phone) like '%"+searchTerm+"%'";
            ResultSet rset = stmt.executeQuery(sqlSelect);
            
            List<List<Object>> data = new ArrayList<List<Object>>();
            int row = 0;
            while(rset.next()) {
                List<Object> rowData = new ArrayList<Object>();
                int id = rset.getInt("id");
                String name = rset.getString("name");
                String lastname = rset.getString("lastname");
                LocalDate date = rset.getObject("birthdate", LocalDate.class);
                String street = rset.getString("street");
                String postal = rset.getString("postal");
                String city = rset.getString("city");
                String mail = rset.getString("email");
                String phone = rset.getString("phone");
                
                rowData.add(id);
                rowData.add(name);
                rowData.add(lastname);
                rowData.add(date);
                rowData.add(street);
                rowData.add(postal);
                rowData.add(city);
                rowData.add(mail);
                rowData.add(phone);
                
                data.add(rowData);
                
                row++;
            }
            
            String [] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
            Object[][] arrayData = new Object[data.size()][];
            for (int i=0; i<data.size(); i++) {
                List<Object> y = data.get(i);
                arrayData[i] = y.toArray(new Object[y.size()]);
            }
            DefaultTableModel myModel = new DefaultTableModel(arrayData, colNames);
            table.setModel(myModel);
            table.setRowSorter(new TableRowSorter(myModel));
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void advancedSearch() {
        JDialog dbDialog = new JDialog();
        dbDialog.setTitle("Advanced Search");
        dbDialog.setSize(400,200);
        dbDialog.setModal(true);
        dbDialog.setLocationRelativeTo(null);
        dbDialog.setLayout(new BorderLayout());
        
        Box boxMain = new Box(BoxLayout.Y_AXIS);
        ButtonGroup groupRadioButton = new ButtonGroup();
        
        Box boxOne = new Box(BoxLayout.X_AXIS);
        JRadioButton radioOne = new JRadioButton("", true);
        groupRadioButton.add(radioOne);
        JLabel labelOne = new JLabel("Search in");
        String[] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
        String[] tableColNames = {"id","name","lastname","birthdate","street",
            "postal","city","email","phone"};
        JComboBox comboOne = new JComboBox(colNames);
        comboOne.setMaximumSize(comboOne.getPreferredSize());
        JTextField tfOne = new JTextField(15);
        tfOne.setMaximumSize(tfOne.getPreferredSize());
        boxOne.add(Box.createHorizontalStrut(5));
        boxOne.add(radioOne);
        boxOne.add(Box.createHorizontalStrut(5));
        boxOne.add(labelOne);
        boxOne.add(Box.createHorizontalStrut(5));
        boxOne.add(comboOne);
        boxOne.add(Box.createHorizontalStrut(5));
        boxOne.add(tfOne);
        boxOne.add(Box.createHorizontalGlue());
        
        Box boxTwo = new Box(BoxLayout.X_AXIS);
        JRadioButton radioTwo = new JRadioButton();
        groupRadioButton.add(radioTwo);
        JLabel labelTwo = new JLabel("Show ID from");
        int maxValue = getMaxId();
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0,0,maxValue-1,1);
        JSpinner spinnerTwo = new JSpinner(spinnerModel);
        spinnerTwo.setMaximumSize(new Dimension(15,20));
        JLabel labelTwoB = new JLabel("to");
        SpinnerNumberModel spinnerModel2 = new SpinnerNumberModel(maxValue,0,maxValue,1);
        JSpinner spinnerTwoB = new JSpinner(spinnerModel2);
        spinnerTwoB.setMaximumSize(new Dimension(15,20));
        
        boxTwo.add(Box.createHorizontalStrut(5));
        boxTwo.add(radioTwo);
        boxTwo.add(Box.createHorizontalStrut(5));
        boxTwo.add(labelTwo);
        boxTwo.add(Box.createHorizontalStrut(5));
        boxTwo.add(spinnerTwo);
        boxTwo.add(Box.createHorizontalStrut(5));
        boxTwo.add(labelTwoB);
        boxTwo.add(Box.createHorizontalStrut(5));
        boxTwo.add(spinnerTwoB);
        boxTwo.add(Box.createHorizontalGlue());
        
        Box boxThree = new Box(BoxLayout.X_AXIS);
        JRadioButton radioThree = new JRadioButton();
        groupRadioButton.add(radioThree);
        JLabel labelThree = new JLabel("Show Date from");
        SpinnerDateModel spinnerModelB = new SpinnerDateModel();
        JSpinner spinnerThree = new JSpinner(spinnerModelB);
        spinnerThree.setEditor(new JSpinner.DateEditor(spinnerThree,"dd.MM.yyyy"));
        spinnerThree.setMaximumSize(new Dimension(15,20));
        JLabel labelThreeB = new JLabel("to");
        SpinnerDateModel spinnerModelB2 = new SpinnerDateModel();
        JSpinner spinnerThreeB = new JSpinner(spinnerModelB2);
        spinnerThreeB.setEditor(new JSpinner.DateEditor(spinnerThreeB,"dd.MM.yyyy"));
        spinnerThreeB.setMaximumSize(new Dimension(15,20));
        
        boxThree.add(Box.createHorizontalStrut(5));
        boxThree.add(radioThree);
        boxThree.add(Box.createHorizontalStrut(5));
        boxThree.add(labelThree);
        boxThree.add(Box.createHorizontalStrut(5));
        boxThree.add(spinnerThree);
        boxThree.add(Box.createHorizontalStrut(5));
        boxThree.add(labelThreeB);
        boxThree.add(Box.createHorizontalStrut(5));
        boxThree.add(spinnerThreeB);
        boxThree.add(Box.createHorizontalGlue());
        
        boxMain.add(Box.createVerticalStrut(10));
        boxMain.add(boxOne);
        boxMain.add(Box.createVerticalStrut(10));
        boxMain.add(boxTwo);
        boxMain.add(Box.createVerticalStrut(10));
        boxMain.add(boxThree);
        boxMain.add(Box.createVerticalGlue());
        
        Box boxButtonY = new Box(BoxLayout.Y_AXIS);
        Box boxButtonX = new Box(BoxLayout.X_AXIS);
        JButton advancedSearchButton = new JButton("Advanced Search");
        boxButtonX.add(Box.createHorizontalGlue());
        boxButtonX.add(advancedSearchButton);
        boxButtonX.add(Box.createHorizontalGlue());
        boxButtonY.add(boxButtonX);
        boxButtonY.add(Box.createVerticalStrut(10));
        
        dbDialog.add(boxMain, BorderLayout.PAGE_START);
        dbDialog.add(boxButtonY, BorderLayout.PAGE_END);
        
        advancedSearchButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt) {
               ButtonModel model = groupRadioButton.getSelection();
               if (model == radioOne.getModel()) {
                   advancedSearchOne(tableColNames[comboOne.getSelectedIndex()], tfOne.getText());
               } else if (model == radioTwo.getModel()) {
                   int minId = (Integer) spinnerTwo.getValue();
                   int maxId = (Integer) spinnerTwoB.getValue();
                   advancedSearchTwo(minId, maxId);
               } else if (model == radioThree.getModel()) {
                   String minDate = new SimpleDateFormat("yyyy-MM-dd").format(spinnerThree.getValue());
                   String maxDate = new SimpleDateFormat("yyyy-MM-dd").format(spinnerThreeB.getValue());
                   advancedSearchThree(minDate, maxDate);
               }
               dbDialog.dispose();
           } 
        });
        
        dbDialog.setVisible(true);
    }
    
    public int getMaxId() {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlSelect = "select max(id) from "+customerApp.tableName;
            ResultSet rset = stmt.executeQuery(sqlSelect);
            
            int id = 0;
            while(rset.next()) {
                id = rset.getInt(1);
            }
            return id;
            
        } catch(SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    
    public void advancedSearchOne(String column, String searchTerm) {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlSelect = "select * from "+customerApp.tableName+
                    " where "+column+" like '%"+searchTerm+"%'";
            ResultSet rset = stmt.executeQuery(sqlSelect);
            
            List<List<Object>> data = new ArrayList<List<Object>>();
            int row = 0;
            while(rset.next()) {
                List<Object> rowData = new ArrayList<Object>();
                int id = rset.getInt("id");
                String name = rset.getString("name");
                String lastname = rset.getString("lastname");
                LocalDate date = rset.getObject("birthdate", LocalDate.class);
                String street = rset.getString("street");
                String postal = rset.getString("postal");
                String city = rset.getString("city");
                String mail = rset.getString("email");
                String phone = rset.getString("phone");
                
                rowData.add(id);
                rowData.add(name);
                rowData.add(lastname);
                rowData.add(date);
                rowData.add(street);
                rowData.add(postal);
                rowData.add(city);
                rowData.add(mail);
                rowData.add(phone);
                
                data.add(rowData);
                
                row++;
            }
            
            String [] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
            Object[][] arrayData = new Object[data.size()][];
            for (int i=0; i<data.size(); i++) {
                List<Object> y = data.get(i);
                arrayData[i] = y.toArray(new Object[y.size()]);
            }
            DefaultTableModel myModel = new DefaultTableModel(arrayData, colNames);
            table.setModel(myModel);
            table.setRowSorter(new TableRowSorter(myModel));
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void advancedSearchTwo(int min, int max) {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlSelect = "select * from "+customerApp.tableName+
                    " where id >="+min+" and id <="+max;
            ResultSet rset = stmt.executeQuery(sqlSelect);
            
            List<List<Object>> data = new ArrayList<List<Object>>();
            int row = 0;
            while(rset.next()) {
                List<Object> rowData = new ArrayList<Object>();
                int id = rset.getInt("id");
                String name = rset.getString("name");
                String lastname = rset.getString("lastname");
                LocalDate date = rset.getObject("birthdate", LocalDate.class);
                String street = rset.getString("street");
                String postal = rset.getString("postal");
                String city = rset.getString("city");
                String mail = rset.getString("email");
                String phone = rset.getString("phone");
                
                rowData.add(id);
                rowData.add(name);
                rowData.add(lastname);
                rowData.add(date);
                rowData.add(street);
                rowData.add(postal);
                rowData.add(city);
                rowData.add(mail);
                rowData.add(phone);
                
                data.add(rowData);
                
                row++;
            }
            
            String [] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
            Object[][] arrayData = new Object[data.size()][];
            for (int i=0; i<data.size(); i++) {
                List<Object> y = data.get(i);
                arrayData[i] = y.toArray(new Object[y.size()]);
            }
            DefaultTableModel myModel = new DefaultTableModel(arrayData, colNames);
            table.setModel(myModel);
            table.setRowSorter(new TableRowSorter(myModel));
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void advancedSearchThree(String min, String max) {
        
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlSelect = "select * from "+customerApp.tableName+
                    " where birthdate >='"+min+"' and birthdate <='"+max+"'";
            ResultSet rset = stmt.executeQuery(sqlSelect);
            
            List<List<Object>> data = new ArrayList<List<Object>>();
            int row = 0;
            while(rset.next()) {
                List<Object> rowData = new ArrayList<Object>();
                int id = rset.getInt("id");
                String name = rset.getString("name");
                String lastname = rset.getString("lastname");
                LocalDate date = rset.getObject("birthdate", LocalDate.class);
                String street = rset.getString("street");
                String postal = rset.getString("postal");
                String city = rset.getString("city");
                String mail = rset.getString("email");
                String phone = rset.getString("phone");
                
                rowData.add(id);
                rowData.add(name);
                rowData.add(lastname);
                rowData.add(date);
                rowData.add(street);
                rowData.add(postal);
                rowData.add(city);
                rowData.add(mail);
                rowData.add(phone);
                
                data.add(rowData);
                
                row++;
            }
            
            String [] colNames = {"Customer-Id","First Name","Last Name","Birthdate",
            "Street","Postal Code","City","E-Mail","Phone Number"};
            Object[][] arrayData = new Object[data.size()][];
            for (int i=0; i<data.size(); i++) {
                List<Object> y = data.get(i);
                arrayData[i] = y.toArray(new Object[y.size()]);
            }
            DefaultTableModel myModel = new DefaultTableModel(arrayData, colNames);
            table.setModel(myModel);
            table.setRowSorter(new TableRowSorter(myModel));
            
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void popupEdit() {
        JDialog dbDialog = new JDialog();
        dbDialog.setTitle("Edit Record");
        dbDialog.setSize(250,450);
        dbDialog.setModal(true);
        dbDialog.setLocationRelativeTo(null);
        dbDialog.setLayout(new BoxLayout(dbDialog.getContentPane(), BoxLayout.Y_AXIS));
        
        int rowIndex = table.getSelectedRow();
        
        Box boxName = new Box(BoxLayout.X_AXIS);
        JLabel labelName = new JLabel("Name");
        boxName.add(Box.createHorizontalStrut(5));
        boxName.add(labelName);
        boxName.add(Box.createHorizontalGlue());
        
        Box boxName2 = new Box(BoxLayout.X_AXIS);
        JTextField tfName = new JTextField(table.getValueAt(rowIndex,1).toString(),16);
        tfName.setMaximumSize(tfName.getPreferredSize());
        boxName2.add(Box.createHorizontalStrut(5));
        boxName2.add(tfName);
        boxName2.add(Box.createHorizontalGlue());
        
        Box boxLastName = new Box(BoxLayout.X_AXIS);
        JLabel labelLastName = new JLabel("Lastname");
        boxLastName.add(Box.createHorizontalStrut(5));
        boxLastName.add(labelLastName);
        boxLastName.add(Box.createHorizontalGlue());
        
        Box boxLastName2 = new Box(BoxLayout.X_AXIS);
        JTextField tfLastName = new JTextField(table.getValueAt(rowIndex,2).toString(),16);
        tfLastName.setMaximumSize(tfLastName.getPreferredSize());
        boxLastName2.add(Box.createHorizontalStrut(5));
        boxLastName2.add(tfLastName);
        boxLastName2.add(Box.createHorizontalGlue());
        
        Box boxDate = new Box(BoxLayout.X_AXIS);
        JLabel labelDate = new JLabel("Birthdate");
        boxDate.add(Box.createHorizontalStrut(5));
        boxDate.add(labelDate);
        boxDate.add(Box.createHorizontalGlue());
        
        Box boxDate2 = new Box(BoxLayout.X_AXIS);
        String[] listDay = new String[31];
        for (int i=0; i<31; i++) {
            String strNumber = Integer.toString(i+1);
            if (strNumber.length() == 1) {
                listDay[i] = "0"+strNumber;
            } else {
                listDay[i] = strNumber;
            }
        }
        JComboBox comboDay = new JComboBox(listDay);
        comboDay.setMaximumSize(comboDay.getPreferredSize());
        String[] listMonth = new String[12];
        for (int i=0; i<12; i++) {
            String strNumber = Integer.toString(i+1);
            if (strNumber.length() == 1) {
                listMonth[i] = "0"+strNumber;
            } else {
                listMonth[i] = strNumber;
            }
        }
        JComboBox comboMonth = new JComboBox(listMonth);
        comboMonth.setMaximumSize(comboMonth.getPreferredSize());
        String[] listYear = new String[71];
        for (int i=0; i<71; i++) {
            listYear[i] = Integer.toString(1951+i);
        }
        JComboBox comboYear = new JComboBox(listYear);
        comboYear.setMaximumSize(comboYear.getPreferredSize());
        boxDate2.add(Box.createHorizontalStrut(5));
        boxDate2.add(comboDay);
        boxDate2.add(Box.createHorizontalStrut(5));
        boxDate2.add(comboMonth);
        boxDate2.add(Box.createHorizontalStrut(5));
        boxDate2.add(comboYear);
        boxDate2.add(Box.createHorizontalGlue());
        
        String date = table.getValueAt(rowIndex,3).toString();
        String[] splitDate = date.split("-");
        comboYear.setSelectedItem(splitDate[0]);
        comboMonth.setSelectedItem(splitDate[1]);
        comboDay.setSelectedItem(splitDate[2]);
        
        Box boxStreet = new Box(BoxLayout.X_AXIS);
        JLabel labelStreet = new JLabel("Street");
        boxStreet.add(Box.createHorizontalStrut(5));
        boxStreet.add(labelStreet);
        boxStreet.add(Box.createHorizontalGlue());
        
        Box boxStreet2 = new Box(BoxLayout.X_AXIS);
        JTextField tfStreet = new JTextField(table.getValueAt(rowIndex,4).toString(),16);
        tfStreet.setMaximumSize(tfStreet.getPreferredSize());
        boxStreet2.add(Box.createHorizontalStrut(5));
        boxStreet2.add(tfStreet);
        boxStreet2.add(Box.createHorizontalGlue());
        
        Box boxPostal = new Box(BoxLayout.X_AXIS);
        JLabel labelPostal = new JLabel("Postal Code");
        boxPostal.add(Box.createHorizontalStrut(5));
        boxPostal.add(labelPostal);
        boxPostal.add(Box.createHorizontalGlue());
        
        Box boxPostal2 = new Box(BoxLayout.X_AXIS);
        JTextField tfPostal = new JTextField(table.getValueAt(rowIndex,5).toString(),16);
        tfPostal.setMaximumSize(tfPostal.getPreferredSize());
        boxPostal2.add(Box.createHorizontalStrut(5));
        boxPostal2.add(tfPostal);
        boxPostal2.add(Box.createHorizontalGlue());
        
        Box boxCity = new Box(BoxLayout.X_AXIS);
        JLabel labelCity = new JLabel("City");
        boxCity.add(Box.createHorizontalStrut(5));
        boxCity.add(labelCity);
        boxCity.add(Box.createHorizontalGlue());
        
        Box boxCity2 = new Box(BoxLayout.X_AXIS);
        JTextField tfCity = new JTextField(table.getValueAt(rowIndex,6).toString(),16);
        tfCity.setMaximumSize(tfCity.getPreferredSize());
        boxCity2.add(Box.createHorizontalStrut(5));
        boxCity2.add(tfCity);
        boxCity2.add(Box.createHorizontalGlue());
        
        Box boxMail = new Box(BoxLayout.X_AXIS);
        JLabel labelMail = new JLabel("E-Mail");
        boxMail.add(Box.createHorizontalStrut(5));
        boxMail.add(labelMail);
        boxMail.add(Box.createHorizontalGlue());
        
        Box boxMail2 = new Box(BoxLayout.X_AXIS);
        JTextField tfMail = new JTextField(table.getValueAt(rowIndex,7).toString(),16);
        tfMail.setMaximumSize(tfMail.getPreferredSize());
        boxMail2.add(Box.createHorizontalStrut(5));
        boxMail2.add(tfMail);
        boxMail2.add(Box.createHorizontalGlue());
        
        Box boxPhone = new Box(BoxLayout.X_AXIS);
        JLabel labelPhone = new JLabel("Phone Number");
        boxPhone.add(Box.createHorizontalStrut(5));
        boxPhone.add(labelPhone);
        boxPhone.add(Box.createHorizontalGlue());
        
        Box boxPhone2 = new Box(BoxLayout.X_AXIS);
        JTextField tfPhone = new JTextField(table.getValueAt(rowIndex,8).toString(),16);
        tfPhone.setMaximumSize(tfPhone.getPreferredSize());
        boxPhone2.add(Box.createHorizontalStrut(5));
        boxPhone2.add(tfPhone);
        boxPhone2.add(Box.createHorizontalGlue());
        
        Box boxButton = new Box(BoxLayout.X_AXIS);
        JButton buttonSubmitChanges = new JButton("Submit Changes");
        boxButton.add(Box.createHorizontalStrut(30));
        boxButton.add(buttonSubmitChanges);
        boxButton.add(Box.createHorizontalGlue());
        
        dbDialog.add(boxName);
        dbDialog.add(boxName2);
        dbDialog.add(boxLastName);
        dbDialog.add(boxLastName2);
        dbDialog.add(boxDate);
        dbDialog.add(boxDate2);
        dbDialog.add(boxStreet);
        dbDialog.add(boxStreet2);
        dbDialog.add(boxPostal);
        dbDialog.add(boxPostal2);
        dbDialog.add(boxCity);
        dbDialog.add(boxCity2);
        dbDialog.add(boxMail);
        dbDialog.add(boxMail2);
        dbDialog.add(boxPhone);
        dbDialog.add(boxPhone2);
        dbDialog.add(Box.createVerticalStrut(10));
        dbDialog.add(boxButton);
        
        buttonSubmitChanges.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                String id = table.getValueAt(rowIndex,0).toString();
                String name = tfName.getText();
                String lastname = tfLastName.getText();
                String date = comboYear.getSelectedItem()+"-"+comboMonth.getSelectedItem()
                        +"-"+comboDay.getSelectedItem();
                String street = tfStreet.getText();
                String postal = tfPostal.getText();
                String city = tfCity.getText();
                String mail = tfMail.getText();
                String phone = tfPhone.getText();
                dbDialog.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                submitChanges(id,name,lastname,date,street,postal,city,mail,phone,dbDialog);
                table.setValueAt(name,rowIndex,1);
                table.setValueAt(lastname,rowIndex,2);
                table.setValueAt(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")),rowIndex,3);
                table.setValueAt(street,rowIndex,4);
                table.setValueAt(postal,rowIndex,5);
                table.setValueAt(city,rowIndex,6);
                table.setValueAt(mail,rowIndex,7);
                table.setValueAt(phone,rowIndex,8);
            }
        });
        
        dbDialog.setVisible(true);
    }
    
    public void submitChanges(String id,String name,String lastname,String date,
            String street,String postal,String city,String mail,String phone,JDialog dialog) {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlEdit = "update "+customerApp.tableName+" set name='"+name+
                    "', lastname='"+lastname+"', birthdate='"+date+"', street='"+street+
                    "', postal='"+postal+"', city='"+city+"', email='"+mail+"', phone='"+
                    phone+"' where id="+id;
            stmt.executeUpdate(sqlEdit);
            dialog.setCursor(Cursor.getDefaultCursor());
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void popupDelete() {
        int rowIndex = table.getSelectedRow();
        
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlDelete = "delete from "+customerApp.tableName+" where id="+
                    table.getValueAt(rowIndex,0).toString();
            stmt.executeUpdate(sqlDelete);
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        ((DefaultTableModel) table.getModel()).removeRow(rowIndex);
    }
}
