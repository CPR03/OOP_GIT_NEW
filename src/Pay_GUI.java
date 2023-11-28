import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Pay_GUI extends JDialog {
    private JPanel contentPane;
    private JButton paybtn;
    private JButton buttonCancel;
    private JComboBox cmbPayMethod;
    private JTextField initialtxt;
    private JTextField disctxt;
    private JTextField totaltxt;
    private JTextArea receipttxt;
    private JTextField chargetxt;
    private JComboBox cmbDiscount;
    private JTextField txtDuration;
    private JTextField txtUtil;
    String DiscountCode;
    static ArrayList<String> test = new ArrayList<String>();
    public Pay_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(paybtn);

        test=Calculate.getUtilities();
        for(int i=0;i<test.size();i++){
            System.out.println(test.get(i));
        }



        cmbDiscount.addActionListener(new ActionListener() {//Check selected item from Discount code and set the Discount
            public void actionPerformed(ActionEvent e) {

                DiscountCode=cmbDiscount.getSelectedItem().toString();
                getDiscount();
                getTotal();

            }
        });

        paybtn.addActionListener(new ActionListener() {//Check selected item from Discount code and set the Discount
            public void actionPerformed(ActionEvent e) {

                onPay();

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



    private void getTotal(){
        double discount,total,price;
        discount=Double.parseDouble(disctxt.getText().substring(0,2))/100;
        price=Calculate.getUnit_price()*discount;
        total=Calculate.getUnit_price()-price;
        totaltxt.setText(String.valueOf(total));
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

    Transaction transaction = new Transaction();

    private void onPay() {

        String payMethod = cmbPayMethod.getSelectedItem().toString();

        String ipay = "";
        int pay = 0;

        if (payMethod.equals("GCash")){
            ipay = JOptionPane.showInputDialog(null,"Enter Paying Amount: ", "GCash Method", JOptionPane.INFORMATION_MESSAGE);
            pay = Integer.parseInt(ipay);
        }

        else if (payMethod.equals("Debit")){
             ipay = JOptionPane.showInputDialog(null,"Enter Paying Amount: ", "Debit Method", JOptionPane.INFORMATION_MESSAGE);
             pay = Integer.parseInt(ipay);
        }


        String convert = "";

        for (int i = 0; i < Calculate.getUtilities().size(); i++) {
            convert += String.valueOf(Calculate.getUtilities().get(i));

            if (i < Calculate.getUtilities().size() - 1)
                convert += "\n";
        }

        receipttxt.setText(

                "~User Info~" + "\n " +
                    "User ID: " + transaction.getUserID() + "\n" +
                    "Username: "+ transaction.getUsername() + "\n" + "\n" +

                "~Total~" + "\n" + " "+
                    "Unit Price: " + Calculate.getUnit_price() + "\n" +
                    "Duration of Stay: " + Calculate.getDuration() + "\n" + "\n" +

                "~Utilities~" + "\n " +
                    "Utilities Total: " + Calculate.getAdditional() + "\n" +
                    convert

        );


    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Pay_GUI.Pay_GUI();
    }

    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();
    static void Pay_GUI(){
        Pay_GUI dialog = new Pay_GUI();
        dialog.pack();
        dialog.setTitle("SoulSpace | Payment");
        dialog.setIconImage(dialog.imageLogo);
        dialog.setLocationRelativeTo(null);

        dialog.setVisible(true);

    }
}
