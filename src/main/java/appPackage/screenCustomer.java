package appPackage;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Cursor;


public class screenCustomer extends JPanel {
    
    String name;
    String lastname;
    String date;
    String street;
    String postal;
    String city;
    String mail;
    String phone;
    
    public screenCustomer() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        Box boxName = new Box(BoxLayout.X_AXIS);
        JLabel labelName = new JLabel("Name");
        boxName.add(Box.createHorizontalStrut(5));
        boxName.add(labelName);
        boxName.add(Box.createHorizontalGlue());
        
        Box boxName2 = new Box(BoxLayout.X_AXIS);
        JTextField tfName = new JTextField(16);
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
        JTextField tfLastName = new JTextField(16);
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
        
        Box boxStreet = new Box(BoxLayout.X_AXIS);
        JLabel labelStreet = new JLabel("Street");
        boxStreet.add(Box.createHorizontalStrut(5));
        boxStreet.add(labelStreet);
        boxStreet.add(Box.createHorizontalGlue());
        
        Box boxStreet2 = new Box(BoxLayout.X_AXIS);
        JTextField tfStreet = new JTextField(16);
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
        JTextField tfPostal = new JTextField(16);
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
        JTextField tfCity = new JTextField(16);
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
        JTextField tfMail = new JTextField(16);
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
        JTextField tfPhone = new JTextField(16);
        tfPhone.setMaximumSize(tfPhone.getPreferredSize());
        boxPhone2.add(Box.createHorizontalStrut(5));
        boxPhone2.add(tfPhone);
        boxPhone2.add(Box.createHorizontalGlue());
        
        Box boxButton = new Box(BoxLayout.X_AXIS);
        JButton buttonSubmit = new JButton("Submit");
        boxButton.add(Box.createHorizontalStrut(30));
        boxButton.add(buttonSubmit);
        boxButton.add(Box.createHorizontalGlue());
        
        this.add(boxName);
        this.add(boxName2);
        this.add(boxLastName);
        this.add(boxLastName2);
        this.add(boxDate);
        this.add(boxDate2);
        this.add(boxStreet);
        this.add(boxStreet2);
        this.add(boxPostal);
        this.add(boxPostal2);
        this.add(boxCity);
        this.add(boxCity2);
        this.add(boxMail);
        this.add(boxMail2);
        this.add(boxPhone);
        this.add(boxPhone2);
        this.add(Box.createVerticalStrut(10));
        this.add(boxButton);

        buttonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                name = tfName.getText();
                lastname = tfLastName.getText();
                date = comboYear.getSelectedItem()+"-"+comboMonth.getSelectedItem()
                        +"-"+comboDay.getSelectedItem();
                street = tfStreet.getText();
                postal = tfPostal.getText();
                city = tfCity.getText();
                mail = tfMail.getText();
                phone = tfPhone.getText();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                submitPressed();
            }
        });
    }
    
    public void submitPressed() {
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:"+customerApp.portNumber
                    +"/"+customerApp.dataBase
                    +"?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", 
                    customerApp.userName, customerApp.passWord);
            Statement stmt = conn.createStatement();
            ) {
            
            String sqlCount = "select max(id) from "+customerApp.tableName;
            ResultSet rset = stmt.executeQuery(sqlCount);
            int id = 0;
            while(rset.next()) {
                id = rset.getInt(1)+1;
            }
            String sqlInsert = "insert into "+customerApp.tableName+" values("+id
                    +", '"+lastname+"', '"+name+"', '"+date+"', '"+street+"', '"
                    +postal+"', '"+city+"', '"+mail+"', '"+phone+"')";
            int countInserted = stmt.executeUpdate(sqlInsert);
            setCursor(Cursor.getDefaultCursor());
        } catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
}
