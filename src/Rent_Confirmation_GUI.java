import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Rent_Confirmation_GUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtUnit;
    private JRadioButton btn1month;
    private JRadioButton btn3months;
    private JRadioButton btn1year;
    private JCheckBox amenitiesCheckBox;
    private JCheckBox wiFiCheckBox;
    private JRadioButton halfyear;
    private JCheckBox cableCheckBox;
    private JCheckBox waterCheckBox;
    private JLabel homeLogo;
    Calculate chosen_details = new Calculate();
    private String Duration;
    ArrayList<String> utilities = new ArrayList<String>();
    public Rent_Confirmation_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        ButtonGroup durationgroup = new ButtonGroup();
        durationgroup.add(btn1month);
        durationgroup.add(btn3months);
        durationgroup.add(halfyear);
        durationgroup.add(btn1year);





        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
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

        homeLogo.setIcon(new ImageIcon(new ImageIcon("Images/Components/home_logo.png").getImage().getScaledInstance(200, 70, Image.SCALE_SMOOTH)));

        buttonOK.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        buttonOK.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonOK.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonCancel.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_cancel.png").getImage().getScaledInstance(80, 23, Image.SCALE_SMOOTH)));
        buttonCancel.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        txtUnit.setText(chosen_details.getUnit_number());
        txtUnit.setHorizontalAlignment(SwingConstants.CENTER);
        btn1month.setSelected(true);

    }


    private void onOK() {
        //get selected Utility/s

        if(amenitiesCheckBox.isSelected()){
            utilities.add(amenitiesCheckBox.getText());
        }
        if(cableCheckBox.isSelected()){
            utilities.add(cableCheckBox.getText());
        }
        if(waterCheckBox.isSelected()){
            utilities.add(waterCheckBox.getText());
        }
        if(wiFiCheckBox.isSelected()){
            utilities.add(wiFiCheckBox.getText());
        }

        if(btn1month.isSelected()){
            Duration="1 month";
        } else if (btn3months.isSelected()) {
            Duration="3 months";
        } else if (halfyear.isSelected()) {
            Duration="6 months";
        }else {
            Duration="1 year";
        }


        Calculate.setDuration(Duration);
        Calculate.setUtilities(utilities);

        this.setVisible(false);
        Pay_GUI.pay_GUI();

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        Selected_Apr_GUI.Selected_Apr_GUI();
    }

    Image imagelogo = new ImageIcon("Images/Components/logo.png").getImage();

    public static void main(String[] args) {
        Rent_Confirmation_GUI.Rent_Confirmation_GUI();
    }

    static void Rent_Confirmation_GUI (){
        Rent_Confirmation_GUI dialog = new Rent_Confirmation_GUI();
        dialog.pack();
        dialog.setBounds(600,200,350,490);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setTitle("SoulSpace | Rent Confirmation.");
        dialog.setIconImage(dialog.imagelogo);

        dialog.setVisible(true);

    }


}
