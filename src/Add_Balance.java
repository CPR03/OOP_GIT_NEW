import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Add_Balance extends JDialog {
    private JPanel contentPane;
    private JButton btnCashin;
    private JComboBox cmbvia;
    private JTextField txtamount;
    private JTextField txttotal;
    private JTextField txtcharge;
    private JTextField txtinput;
    private JButton btnenter;

    String mode;

    public Add_Balance() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnCashin);

        btnCashin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPay();
            }
        });
        cmbvia.setSelectedItem("Gcash");
        mode=cmbvia.getSelectedItem().toString();
        Calculate.setPaymentmod(mode);
        getPaymentmode();
        cmbvia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mode = cmbvia.getSelectedItem().toString();
                Calculate.setPaymentmod(mode);
                getPaymentmode();  //Display Payment mode
            }
        });
        btnenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onEnter();
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
    Error error = new Error();
    private void getPaymentmode(){

        if (mode.equals("Debit")) {
            txtcharge.setText("200");
        } else {
            txtcharge.setText("100");
        }

    }
    int amount=0;
    private void onEnter() {
        String iamount = txtamount.getText();
        if(isNumeric(iamount)){
            amount = Integer.parseInt(iamount);
        }
        else{
            error.invalidInput();
        }
        txtinput.setText(String.valueOf(amount));
        int charge = Integer.parseInt(txtcharge.getText());
        txttotal.setText(String.valueOf(amount-charge));
        }


    User_Data cal = new Accessor();
    private void onPay() {

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            con.setAutoCommit(true);
            //Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            PreparedStatement stmt = con.prepareStatement("UPDATE users SET Balance = ? WHERE user_id = ?"); //(Note: ? is a placeholder)
            stmt.setDouble(1, cal.getBalance()+Double.parseDouble(txttotal.getText())); //.set utilizes the '?'
            stmt.setInt(2,Accessor.getUserID());


            stmt.executeUpdate();
            stmt.close();
            con.close();

            //result.updateDouble("Balance",accessor.checkBalance()-Calculate.getTotalprice());

        } catch (Exception exc) {

            exc.printStackTrace();
        }
        JOptionPane.showMessageDialog(null,"Top up Successfully","Cash-in",JOptionPane.INFORMATION_MESSAGE);
        dispose();
        Dashboard_GUI.Dashboard_GUI();
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Add_Balance.Add_Balance();

    }
    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();


    static void Add_Balance() {

        Add_Balance dialog = new Add_Balance();
        dialog.pack();
        dialog.setBounds(600,200,450,400);
        dialog.setResizable(false);
        dialog.setTitle("SoulSpace | Dashboard.");
        dialog.setIconImage(dialog.imageLogo);

        dialog.setVisible(true);
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
