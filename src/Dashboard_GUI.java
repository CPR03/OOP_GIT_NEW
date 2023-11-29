import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard_GUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel profilePic;
    private JPanel p1;
    private JLabel homeLogo;
    private JLabel dashMan;
    private JButton btnrequest;
    private JPanel apartment_panel;
    private JScrollPane pane;
    private JLabel unit1;
    private JLabel unit2;
    private JLabel unit3;
    private JLabel unit4;
    private JLabel unit5;
    private JLabel unit6;
    private JLabel unit7;
    private JLabel unit8;
    private JLabel unit9;
    private JPanel scrollPanel;
    private JPanel other_info_panel;
    private JPanel dashPanel;
    private JButton rentUnit1;
    private JButton rentUnit2;
    private JButton rentUnit3;
    private JButton rentUnit4;
    private JButton rentUnit5;
    private JButton rentUnit6;
    private JButton rentUnit7;
    private JButton rentUnit8;
    private JButton rentUnit9;
    private JLabel unit10;
    private JLabel unit11;
    private JLabel unit12;
    private JButton rentUnit10;
    private JButton rentUnit11;
    private JButton rentUnit12;
    private JTable table1;
    private JPanel myInfo_panel;
    private JButton btnPayRent;
    private JLabel txtWelcome;
    private JTextField txtBalance;


    public Dashboard_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        pane.getVerticalScrollBar().setUnitIncrement(16); //set scroll speed


        //Know what button is clicked returns the label
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                Accessor.setUnitnum(actionEvent.getActionCommand());

                Selected_Apr_GUI.Selected_Apr_GUI();
                //make dashboard visible if button cancel is clicked in Selected GUI
                setVisible(true);

            }
        };
        //WindowListerner for PAYGUI

        Display(actionListener);



        btnrequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRequest();
            }
        });


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        btnPayRent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                PayRent_GUI.PayRent_GUI();

            }
        });


        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);



    }

    User_Data cal = new Accessor();
    String type;
    private void Display(ActionListener actionListener){
        checkType();
        if(type.equals("new")){
           btnrequest.setVisible(false);
           btnPayRent.setVisible(false);

        }
        else{

            btnrequest.setVisible(true);
            btnPayRent.setVisible(true);
            scrollPanel.setVisible(false);

            myInfo_panel.setVisible(true);
            history(); //Display Data to table-Now working

        }
        //Store all Buttons to Array
        JButton [] Apart_buttons = {rentUnit1,rentUnit2,rentUnit3,rentUnit4,rentUnit5,
                rentUnit6,rentUnit7,rentUnit8,rentUnit9,rentUnit10,rentUnit11,rentUnit12};

        //Store all label to Array
        JLabel [] Unit_image={unit1,unit2,unit3,unit4,unit5,unit6,unit7,unit8,unit9,unit10,unit11,unit12};

        for(int i=0;i<12;i++){
            Apart_buttons[i].addActionListener(actionListener);//add actionlistener to every Rent button
            //Set image for buttons
            Apart_buttons[i].setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
            Apart_buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);//Center text
            Apart_buttons[i].setText("Rent: Unit "+(i+1)); //Set label for buttons
            Apart_buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));//Cursor hover

            String imagePath = "Images/Apartments/Out1/pngs/unit" + (i + 1) + ".png";

            Unit_image[i].setIcon(new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH)));

        }
        btnrequest.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPayRent.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profilePic.setIcon(new ImageIcon(new ImageIcon("Images/Components/profile.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        homeLogo.setIcon(new ImageIcon(new ImageIcon("Images/Components/home_logo.png").getImage().getScaledInstance(300, 70, Image.SCALE_SMOOTH)));
        dashMan.setIcon(new ImageIcon(new ImageIcon("Images/Components/dash_board_man.png").getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH)));

        table1.setModel(new DefaultTableModel(
                null,
                new String[] {"Name","Current Apartment","Date created","Rent Per Month","Duration of Stay","Amenities","Wi-Fi","Cable","Water"}));


        txtBalance.setText(String.valueOf(cal.getBalance()));
        txtWelcome.setText("WELCOME!  "+cal.getUsername().toUpperCase());

    }

    private void history(){

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement();


            ResultSet result = state.executeQuery(" SELECT apartment.users.userName,apartment.apartment_unit.unit_number,apartment.transaction.Date, apartment.transaction.monthly_due_amount, apartment.transaction.duration,apartment.transaction.amenities,apartment.transaction.wifi,apartment.transaction.cable,apartment.transaction.water\n" +
                    "            FROM apartment.users\n" +
                    "            RIGHT JOIN apartment.transaction ON users.user_id = transaction.user_id\n" +
                    "            LEFT JOIN apartment.apartment_unit ON transaction.apart_id = apartment_unit.apr_id;");
            result.next();

            Object[] row = {result.getString("userName"),result.getString("unit_number"),
                    result.getDate("Date"),result.getDouble("monthly_due_amount"),
                    result.getString("duration"),result.getInt("amenities"),
                    result.getInt("wifi"),result.getInt("cable"),result.getInt("water")
            };
            DefaultTableModel data= (DefaultTableModel)
                    table1.getModel();
            data.addRow(row);


        } catch (Exception exc) {

            exc.printStackTrace();
        }


    }

    private void checkType(){

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery("SELECT COUNT(*) as Rowcount FROM apartment.transaction WHERE user_id ='"+Accessor.getUserID()+"'");
            result.next();
            int count = result.getInt("Rowcount");
            result.close();
            if(count>0){
                type="old";
            }
            else{
                type="new";
            }


        } catch (Exception exc) {

            exc.printStackTrace();
        }

    }

    private void onRequest(){
        Request_Maintenance_GUI.Request_Maintenance_GUI();

    }


    private void onOK() {
        scrollPanel.setVisible(false);
        myInfo_panel.setVisible(true);
    }

    private void onCancel() {
        dispose();
        LogIn_GUI.LogIn_GUI(); //Go back to login if user exit
    }


    public static void main(String[] args) {

        Dashboard_GUI.Dashboard_GUI();



    }


    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();
    static void Dashboard_GUI() {

        Dashboard_GUI dialog = new Dashboard_GUI();
        dialog.pack();
        dialog.setBounds(300, 25, 1300, 950);
        dialog.setResizable(false);
        dialog.setTitle("SoulSpace | Dashboard.");
        dialog.setIconImage(dialog.imageLogo);

        dialog.setVisible(true);
    }

}
