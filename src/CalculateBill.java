import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.util.Random;

public class CalculateBill extends JFrame implements ActionListener {
    JTextField tfname, tfaddress, tfstate,tfunit,tfemail,tfphone;
    JButton next, cancel;
    JLabel lblname,labeladdress;
    Choice meternumber,cmonth;
    CalculateBill(){
        setSize(700,500);
        setLocation(400,150);

        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBackground(new Color(173,216,230));
        add(p);

        JLabel heading = new JLabel("Calculate Electricity Bill");
        heading.setBounds(100, 10,400,20);
        heading.setFont(new Font("Tahoma", Font.PLAIN,24));
        p.add(heading);

        JLabel lblmeternumber = new JLabel("Meter Number");
        lblmeternumber.setBounds(100, 80,100,20);
        p.add(lblmeternumber);

        meternumber = new Choice();
        try{
            conn c = new conn();
           ResultSet rs = c.s.executeQuery("select * from customer");
           while (rs.next()){
               meternumber.add(rs.getString("meter_no"));
           }
        }catch(Exception e){
            e.printStackTrace();
        }
        meternumber.setBounds(240, 80,200,20);
        p.add(meternumber);

        JLabel lblmeterno = new JLabel("Name");
        lblmeterno.setBounds(100, 120,100,20);
        p.add(lblmeterno);

        lblname = new JLabel("");
        lblname.setBounds(240, 120,100,20);
        p.add(lblname);

        JLabel lbladdress = new JLabel("Address");
        lbladdress.setBounds(100, 160,100,20);
        p.add(lbladdress);

        labeladdress = new JLabel();
        labeladdress.setBounds(240,160,200,20);
        p.add(labeladdress);

        try{
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("select * from customer where meter_no='"+meternumber.getSelectedItem()+"'");
            while (rs.next()){
                lblname.setText(rs.getString("name"));
                labeladdress.setText(rs.getString("address"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        meternumber.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                try{
                    conn c = new conn();
                    ResultSet rs = c.s.executeQuery("select * from customer where meter_no='"+meternumber.getSelectedItem()+"'");
                    while (rs.next()){
                        lblname.setText(rs.getString("name"));
                        labeladdress.setText(rs.getString("address"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        JLabel lblcity = new JLabel("Unit Consumed");
        lblcity.setBounds(100, 200,100,20);
        p.add(lblcity);

        tfunit = new JTextField();
        tfunit.setBounds(240,200,200,20);
        p.add(tfunit);

        JLabel lblstate = new JLabel("Month");
        lblstate.setBounds(100, 240,100,20);
        p.add(lblstate);

        cmonth = new Choice();
        cmonth.setBounds(240,240,200,20);
        cmonth.add("January");
        cmonth.add("February");
        cmonth.add("March");
        cmonth.add("April");
        cmonth.add("May");
        cmonth.add("June");
        cmonth.add("July");
        cmonth.add("August");
        cmonth.add("September");
        cmonth.add("October");
        cmonth.add("November");
        cmonth.add("December");
        p.add(cmonth);

        next = new JButton("Submit");
        next.setBounds(120,350,100,25);
        next.setBackground(Color.black);
        next.setForeground(Color.WHITE);
        next.addActionListener(this);
        p.add(next);

        cancel = new JButton("Cancel");
        cancel.setBounds(250,350,100,25);
        cancel.setBackground(Color.black);
        cancel.setForeground(Color.WHITE);
        cancel.addActionListener(this);
        p.add(cancel);

        setLayout(new BorderLayout());

        add(p,"Center");

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/hicon2.jpg"));
        Image i2 = i1.getImage().getScaledInstance(150,300,Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        add(image, "West");

        getContentPane().setBackground(Color.WHITE);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == next){
            String meter = meternumber.getSelectedItem();
            String units = tfunit.getText();
            String month = cmonth.getSelectedItem();

            int totalbill = 0;
            int unit_consumed = Integer.parseInt(units);

            String query ="select * from tax";

            try{
                conn c = new conn();
               ResultSet rs = c.s.executeQuery(query);

               while(rs.next()){
                  totalbill += unit_consumed * Integer.parseInt(rs.getString("cost_per_unit"));
                   totalbill += Integer.parseInt(rs.getString("meter_rent"));
                   totalbill += Integer.parseInt(rs.getString("service_charge"));
                   totalbill += Integer.parseInt(rs.getString("service_tax"));
                   totalbill += Integer.parseInt(rs.getString("swachh_bharat_cess"));
                   totalbill += Integer.parseInt(rs.getString("fix_tax"));

               }
            }catch (Exception e){
                e.printStackTrace();
            }

            String query2 = "insert into bill values('"+meter+"','"+month+"','"+units+"','"+totalbill+"','Not Paid')";

            try{
                conn c = new conn();
                c.s.executeUpdate(query2);

                JOptionPane.showMessageDialog(null,"Customer Bill Updated Successfully");
                setVisible(false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new CalculateBill();
    }
}
