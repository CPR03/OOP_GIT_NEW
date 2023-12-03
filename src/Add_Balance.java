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
    private JButton btnCancel;
    private JButton buttonCancel;
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

        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

        btnCashin.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        btnCashin.setHorizontalTextPosition(SwingConstants.CENTER);
        btnCashin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnCancel.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_cancel.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        btnCancel.setHorizontalTextPosition(SwingConstants.CENTER);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }
    Error error = new Error();
    private void getPaymentmode(){

        switch (mode) {
            case "Debit" -> txtcharge.setText("200");
            default -> txtcharge.setText("100");
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

        if (txtamount.getText().equals("") || txtamount.getText().equals(null)){
            error.nullInput();
        }

        else {

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


    }

    private void onCancel() {
        dispose();
        Dashboard_GUI.Dashboard_GUI();
    }

    public static void main(String[] args) {
        Add_Balance.Add_Balance();

    }
    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();

    static void Add_Balance() {

        Add_Balance dialog = new Add_Balance();
        dialog.pack();
        dialog.setBounds(600,200,400,380);
        dialog.setIconImage(dialog.imageLogo);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setTitle("SoulSpace | Add SoulSpace Balance.");
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
