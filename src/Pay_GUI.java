import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Pay_GUI extends JDialog {
    private JPanel contentPane;
    private JButton paybtn;
    private JButton buttonCancel;
    private JComboBox cmbPayMethod;
    private JTextField initialtxt;
    private JTextField disctxt;
    private JTextField totaltxt;
    private JTextPane receipttxt;
    private JTextField chargetxt;
    private JComboBox cmbDiscount;
    private JTextField txtDuration;
    private JTextField txtUtil;
    String DiscountCode;
    String mode;
    double discount;
    double total;

    public Pay_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(paybtn);

        cmbDiscount.setSelectedItem("Roland");      //Set default value to avoid empty error
        cmbPayMethod.setSelectedItem("SoulSpace");
        DiscountCode=cmbDiscount.getSelectedItem().toString();
        Calculate.setDiscountCode(discount);
        getDiscount();
        cmbDiscount.addActionListener(new ActionListener() {//Check selected item from Discount code and set the Discount
            public void actionPerformed(ActionEvent e) {

                DiscountCode=cmbDiscount.getSelectedItem().toString();
                Calculate.setDiscountCode(discount);
                getDiscount();  //Display Discount


            }
        });
        mode = cmbPayMethod.getSelectedItem().toString(); //Set default value to avoid empty error
        Calculate.setPaymentmod(mode);
        getPaymentmode();  //Display Payment mode
        Calculate.setAdditional();//Compute Additional fee for Utilities
        getTotal();
        cmbPayMethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = cmbPayMethod.getSelectedItem().toString();
                Calculate.setPaymentmod(mode);
                getPaymentmode();  //Display Payment mode
                getTotal();
            }
        });

        paybtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onPay();

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

        paybtn.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        paybtn.setHorizontalTextPosition(SwingConstants.CENTER);
        paybtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonCancel.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_cancel.png").getImage().getScaledInstance(80, 23, Image.SCALE_SMOOTH)));
        buttonCancel.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        initialtxt.setText(String.valueOf(Calculate.getUnit_price()));
        txtDuration.setText(Calculate.getDuration());



        txtUtil.setText(String.valueOf(Calculate.getAdditional()));






    }
//    if(utilities.get(i).equals("Amenities")){
//        additional=200;
//    }
//            else if (utilities.get(i).equals("Wi-Fi")) {
//        additional=300;
//    }
//            else if (utilities.get(i).equals("Cable")) {
//        additional=400;
//    }
//            else{
//        additional=100; //Water
//    }

    private void getTotal(){
        double total,price,chargefee,utilfee;
        chargefee=Double.parseDouble(chargetxt.getText());
        utilfee=Calculate.getAdditional();
        discount=Double.parseDouble(disctxt.getText().substring(0,2))/100;
        price=Calculate.getUnit_price()*discount;
        total=(Calculate.getUnit_price()-price)+chargefee+utilfee;
        totaltxt.setText(String.valueOf(total));
        Calculate.setTotalprice(total);



    }

    private void getDiscount(){
        switch (DiscountCode) {
            case "Roland" -> disctxt.setText("30%");
            case "Gail" -> disctxt.setText("25%");
            case "King" -> disctxt.setText("20%");
            case "Hakim" -> disctxt.setText("15%");
            default -> disctxt.setText("10%");
        }


    }
    private void getPaymentmode(){

        switch (mode) {
            case "SoulSpace" -> chargetxt.setText("0");
            case "GCash" -> chargetxt.setText("100");
            default -> chargetxt.setText("200");
        }


    }

    Accessor accessor = new Accessor();
    Payment confirm = new Payment();
    int status=1; //Payment status indicator
    private void onPay() {
        //Check if payment is successful
        status=confirm.confirmPayment(mode);
        //Status 0 = successful payment
        //Status 1 = unsuccessful payment
        //Status -1 = Null payment


        Calculate.setDiscountCode(discount);
        if(status==0){// if Payment successful print receipt update database

            String convert = "";
            for (int i = 0; i < Calculate.getUtilities().size(); i++) {
                convert += String.valueOf(Calculate.getUtilities().get(i)); //Get all utilities

                if (i < Calculate.getUtilities().size() - 1)
                    convert += " ";
            }


            Font font = new Font("Arial", Font.PLAIN, 16);
            receipttxt.setFont(font);



            receipttxt.setText(


                    "\t     ~User Info~" + "\n" +
                            "\tUser ID: " + accessor.getUserID() + "\n" +
                            "\tUsername: "+ accessor.getUsername() +"\n"+
                            "\tRemaining Balance: "+accessor.getBalance()+"\n"+
                            "\tDate: "+ LocalDate.now() +"\n"+"\n"+

                            "\t     ~Apartment info~" + "\n"+
                            "\tUnit number: "+Calculate.getUnitnum()+"\n"+
                            "\tUnit Price: " + Calculate.getUnit_price() + "\n" +
                            "\tDuration of Stay: " + Calculate.getDuration() + "\n"+
                            "\tUtilities: " + convert +"\n"+"\n"+


                            "\t     ~Total~" + "\n" +
                            "\tUtilities Fee: " + Calculate.getAdditional() + "\n"+
                            "\tCharge Fee: " + chargetxt.getText() +"\n"+
                            "\tDiscount: " + disctxt.getText()+"\n"+
                            "\tTotal: " + totaltxt.getText()


            );


            //Update to Database
            Transaction.saveTransaction();
        }else if (status==1){
            JOptionPane.showMessageDialog(null,"Payment Unsuccessful\nPlease Try again!");
            onPay(); //Call itself
        }

    }



    private void onCancel() {
        //Clear Utilities
        Calculate cal=new Calculate();

        if(status==0){
            dispose();

            cal.reset();
            Dashboard_GUI.Dashboard_GUI();



           //If payment successful back to Dashboard

        }
        else{          //clear utilities back to Rent confirmation
            Calculate.utilities.clear();
            Calculate.resetadditional();
            dispose();

            Rent_Confirmation_GUI.Rent_Confirmation_GUI();

        }

    }

    public static void main(String[] args) {

        Pay_GUI.pay_GUI();
    }

    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();
     static void pay_GUI(){
        Pay_GUI dialog = new Pay_GUI();
        dialog.pack();
        dialog.setTitle("SoulSpace | Payment");
        dialog.setBounds(300, 25, 700, 450);
        dialog.setResizable(false);
        dialog.setIconImage(dialog.imageLogo);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);

    }
}
