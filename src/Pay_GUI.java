import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Pay_GUI extends JDialog {
    private JPanel contentPane;
    private JButton paybtn;
    private JButton buttonCancel;
    private JComboBox comboBox1;
    private JTextField initialtxt;
    private JTextField dcodetxt;
    private JTextField disctxt;
    private JTextField totaltxt;
    private JTextArea receipttxta;
    private JTextField chargetxt;

    public Pay_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(paybtn);

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

    }

    private void onPay() {
        String ipay = JOptionPane.showInputDialog(null,"Enter Paying Amount: ");
        int pay = Integer.parseInt(ipay);

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
