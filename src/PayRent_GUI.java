import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PayRent_GUI extends JDialog {
    private JPanel contentPane;
    private JButton paybtn;
    private JButton buttonCancel;
    private JComboBox cmbmethod;
    private JTextField txtunitprice;
    private JTextField txtbill;
    private JTextArea receipttxt;
    private JTextField txtCharge;
    private JTextField txt_nextbill;
    private JTextField txtDiscount;
    String mode;

    ArrayList last_transaction = Transaction.getlast_trans();
    ArrayList <String> utilities = new ArrayList<>();
    double utilfee;
    String unit_num= (String) last_transaction.get(1);
    double discount= (double) last_transaction.get(5);
    int unitprice= (int) last_transaction.get(2);

    String duration = (String) last_transaction.get(7);
    String last_mode= (String) last_transaction.get(12);
    Date due_date= (Date) last_transaction.get(4);
    public PayRent_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(paybtn);

        paybtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPay();
            }
        });
        cmbmethod.setSelectedItem("SoulSpace");
        mode = cmbmethod.getSelectedItem().toString(); //Set default value to avoid empty error

        getcharge();
        if((int) last_transaction.get(8)==1){
            utilities.add("Amenities");
        }
        if((int) last_transaction.get(9)==1){
            utilities.add("Wi-Fi");
        }
        if((int) last_transaction.get(10)==1){
            utilities.add("Cable");
        }
        if((int) last_transaction.get(11)==1){
            utilities.add("Water");
        }
        Calculate.setUtilities(utilities);//Display Payment mode
        Calculate.setAdditional();
        getTotal();
        cmbmethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = cmbmethod.getSelectedItem().toString();
                getcharge();
                getTotal();
                  //Display Payment mode

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
        txtDiscount.setText((discount) + "%");
        txtunitprice.setText(String.valueOf(unitprice));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(due_date);
        calendar.add(Calendar.MONTH, 1);// Get the Due date after adding a month
        Date updatedDueDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(updatedDueDate);

        txt_nextbill.setText(formattedDate);







    }
    private void getTotal(){
        double total,price,chargefee,last_chargefee;
        switch (last_mode) {   //Last chargefee
            case "SoulSpace" -> last_chargefee=0;
            case "GCash" -> last_chargefee=100;
            default -> last_chargefee=200;
        }
        chargefee=Double.parseDouble(txtCharge.getText());

        utilfee=Calculate.getAdditional();
        price=unitprice*(discount/100);
        total=(unitprice-price)+chargefee+utilfee-last_chargefee; //Subtract last charge fee
        txtbill.setText(String.valueOf(total));
        Calculate.setTotalprice(total);

    }
    private void getcharge(){

        switch (mode) {
            case "SoulSpace" -> txtCharge.setText("0");
            case "GCash" -> txtCharge.setText("100");
            default -> txtCharge.setText("200");
        }


    }
    Payment pay = new Payment();
    Accessor accessor = new Accessor();
    int status;
    private void onPay() {


        Calculate.setPaymentmod(mode);
        status=pay.confirmPayment(mode);
        //Status 0 = successful payment
        //Status 1 = unsuccessful payment
        //Status -1 = Null payment


        if(status==0){// if Payment successful print receipt update database


            String convert = "";
            for (int i = 0; i < utilities.size(); i++) {
                convert += String.valueOf(utilities.get(i)); //Get all utilities

                if (i < utilities.size() - 1)
                    convert += " ";
            }

            Font font = new Font("Arial", Font.PLAIN, 14);
            receipttxt.setFont(font);



            receipttxt.setText(


                    "\t     ~User Info~" + "\n" +
                            "\tUser ID: " + Accessor.getUserID() + "\n" +
                            "\tUsername: "+ accessor.getUsername() +"\n"+
                            "\tRemaining Balance: "+accessor.getBalance()+"\n"+
                            "\tDate: "+ LocalDate.now() +"\n"+"\n"+

                            "\t     ~Apartment info~" + "\n"+
                            "\tUnit number: "+unit_num+"\n"+
                            "\tUnit Price: " + unitprice + "\n" +
                            "\tDuration of Stay: " + duration + "\n"+
                            "\tUtilities: " + convert +"\n"+"\n"+


                            "\t     ~Total~" + "\n" +
                            "\tUtilities Fee: " + utilfee + "\n"+
                            "\tCharge Fee: " + txtCharge.getText() +"\n"+
                            "\tDiscount: " + discount+"%"+"\n"+
                            "\tTotal: " + Calculate.getTotalprice()


            );


            //Update to Database
            addTransaction();

        }else if (status==1){
            JOptionPane.showMessageDialog(null,"Payment Unsuccessful\nPlease Try again!");
            onPay(); //Call itself
        }



    }

    private void addTransaction(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);




            ResultSet getMaxTranId = state.executeQuery("SELECT MAX(tran_id) as maxTranId FROM apartment.transaction");

            getMaxTranId.next();
            int maxTranId = getMaxTranId.getInt("maxTranId");


            ResultSet result = state.executeQuery("SELECT * FROM apartment.transaction");

            result.moveToInsertRow();


            result.updateInt("tran_id", maxTranId+1);// Increment the max tran_id

            //Date

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(formatter.format(new Date()));
            result.updateDate("Date", new java.sql.Date(date.getTime()));

            //Payment Method
            result.updateString("payment_method",mode);

            result.updateDouble("Discount_code",discount);


            //Rent_total
            result.updateDouble("rent_total",Calculate.getTotalprice()*Transaction.getmonths());
            //monthly due
            result.updateDouble("monthly_due_amount",Calculate.getTotalprice());

            result.updateString("duration",duration);

            //utilities
            result.updateInt("amenities", (int) last_transaction.get(8));
            result.updateInt("wifi", (int) last_transaction.get(9));
            result.updateInt("cable", (int) last_transaction.get(10));
            result.updateInt("water", (int) last_transaction.get(11));
            //Foreign keys
            result.updateInt("user_id",Accessor.getUserID());
            result.updateInt("apart_id", (int) last_transaction.get(0));

            result.insertRow();
            result.beforeFirst();

            JOptionPane.showMessageDialog(null, "Transaction Successful.");

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary

        Calculate cal=new Calculate();
        cal.reset();
        Calculate.resetadditional();
        dispose();
        Dashboard_GUI.Dashboard_GUI();
    }

    public static void main(String[] args) {
        PayRent_GUI.PayRent_GUI();
    }

    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();
    static void PayRent_GUI(){
        PayRent_GUI dialog = new PayRent_GUI();
        dialog.pack();
        dialog.setTitle("SoulSpace | Pay Rent");
        dialog.setIconImage(dialog.imageLogo);
        dialog.setLocationRelativeTo(null);
        dialog.setBounds(650, 250, 700, 450);
        dialog.setResizable(false);

        dialog.setVisible(true);

    }
}
