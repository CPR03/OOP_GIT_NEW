import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

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
    private JPanel test;
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


    public Dashboard_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        pane.getVerticalScrollBar().setUnitIncrement(16); //set scroll speed




        //Know what button is clicked returns the label
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);//hide dashboard

                Selected_Apr_GUI select = new Selected_Apr_GUI(actionEvent.getActionCommand());// Pass the chosen Apartment Unit to Selected GUI
                select.pack();
                select.setTitle("SoulSpace | Sign up.");

                select.setResizable(false);
                select.setBounds(600,200,600,600);
                select.setVisible(true);

                //make dashboard visible if button cancel is clicked in Selected GUI
                setVisible(true);


            }
        };
        //Store all Buttons to Array
        JButton [] Apart_buttons = {rentUnit1,rentUnit2,rentUnit3,rentUnit4,rentUnit5,
        rentUnit6,rentUnit7,rentUnit8,rentUnit9,rentUnit10,rentUnit11,rentUnit12};

        //Store all label to Array
        JLabel [] Unit_image={unit1,unit2,unit3,unit4,unit5,unit6,unit7,unit8,unit9,unit10,unit11,unit12};

        for(int i=0;i<12;i++){
            Apart_buttons[i].addActionListener(actionListener);//add actionlistener to every Rent button
            //Set image for buttons
            Apart_buttons[i].setIcon(new ImageIcon(new ImageIcon("Images/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
            Apart_buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);//Center text
            Apart_buttons[i].setText("Rent: Unit "+(i+1)); //Set label for buttons
            Apart_buttons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));//Cursor hover

            //Palitan ng Directory pag may Pic na
            //Pang set ng Image to
            Unit_image[i].setIcon(new ImageIcon(new ImageIcon("Images/sample.png").getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH)));

        }
        btnrequest.setCursor(new Cursor(Cursor.HAND_CURSOR));


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

        profilePic.setIcon(new ImageIcon(new ImageIcon("Images/profile.png").getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        homeLogo.setIcon(new ImageIcon(new ImageIcon("Images/home_logo.png").getImage().getScaledInstance(150, 70, Image.SCALE_SMOOTH)));
        dashMan.setIcon(new ImageIcon(new ImageIcon("Images/dash_board_man.png").getImage().getScaledInstance(170, 170, Image.SCALE_SMOOTH)));








    }
    // Method to create an image icon
    protected static ImageIcon createImageIcon(String path) {
        URL imgUrl = Dashboard_GUI.class.getResource(path);
        if (imgUrl != null) {
            return new ImageIcon(imgUrl);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    private void onRequest(){
        Request_Maintenance_GUI req = new Request_Maintenance_GUI();
        req.pack();
        req.setBounds(600,200,600,350);
        req.setTitle("SoulSpace | Request Maintenance");

        req.setVisible(true);

    }

    private void onOK() {
        pane.setVisible(false);
        test.setVisible(true);
    }

    private void onCancel() {
        dispose();
        LogIn_GUI.LogIn_GUI(); //Go back to login if user exit
    }
    Image imageLogo = new ImageIcon("Images/logo.png").getImage();

    public static void main(String[] args) {
        Dashboard_GUI.Dashboard_GUI();
    }

    static void Dashboard_GUI() {

        Dashboard_GUI dialog = new Dashboard_GUI();
        dialog.pack();
        dialog.setBounds(300, 100, 1300, 950);
        dialog.setResizable(false);
        dialog.setTitle("SoulSpace | Dashboard.");
        dialog.setIconImage(dialog.imageLogo);

        dialog.setVisible(true);
    }


}
