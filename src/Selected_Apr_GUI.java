import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Selected_Apr_GUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel panel;
    private JTextArea details;
    private JLabel poster;
    private JTabbedPane tabbedPane1;
    private JLabel poster2;
    private JLabel poster3;
    private JLabel poster4;
    private JLabel poster5;


    public Selected_Apr_GUI(String unitNum) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        chosenUnit(unitNum);

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

        /*poster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDetails();
            }
        });*/
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






        buttonOK.setIcon(new ImageIcon(new ImageIcon("Images/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        buttonOK.setHorizontalTextPosition(SwingConstants.CENTER);

        buttonCancel.setIcon(new ImageIcon(new ImageIcon("Images/button_cancel.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        buttonCancel.setHorizontalTextPosition(SwingConstants.CENTER);

    }

    private void chosenUnit(String unit){
        if(unit.equals("Rent: Unit 1")){
            poster.setIcon(new ImageIcon(new ImageIcon("Images/Apartments/Apartment In/Apartment 1 Inside/apr_img_in_1.jpg").getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            poster.setHorizontalTextPosition(SwingConstants.CENTER);

            poster2.setIcon(new ImageIcon(new ImageIcon("Images/Apartments/Apartment In/Apartment 1 Inside/apr_img_in_1.1.jpg").getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            poster2.setHorizontalTextPosition(SwingConstants.CENTER);

            poster3.setIcon(new ImageIcon(new ImageIcon("Images/Apartments/Apartment In/Apartment 1 Inside/apr_img_in_1.1.1.jpg").getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            poster3.setHorizontalTextPosition(SwingConstants.CENTER);

            poster4.setIcon(new ImageIcon(new ImageIcon("Images/Apartments/Apartment In/Apartment 1 Inside/apr_img_in_1.1.1.1.jpg").getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            poster4.setHorizontalTextPosition(SwingConstants.CENTER);

            poster5.setIcon(new ImageIcon(new ImageIcon("Images/Apartments/Apartment In/Apartment 1 Inside/apr_img_in_1.1.1.1.1.jpg").getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            poster5.setHorizontalTextPosition(SwingConstants.CENTER);

            details.setText("""
                \s
                House name: Hakim's Crib\s
                Address:  123 Downtown QC\s
                Distance: Tatlo'y Dos\s
                Description: A room with tv, sofa, and picture frames\s
                \s""");

        }

    }
    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
        Dashboard_GUI.Dashboard_GUI();


    }


}
