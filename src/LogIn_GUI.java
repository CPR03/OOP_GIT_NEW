import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class LogIn_GUI extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    protected JTextField txtUserLog;
    private JPasswordField txtPass;
    private JLabel logInPic;
    private JLabel labelLogo;
    private JButton btnSignUp;
    private JButton button1;

    public LogIn_GUI() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        
        btnSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onSignUp();

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


        //Sign in Picture
        logInPic.setIcon(new ImageIcon(new ImageIcon("Images/Components/sign_in.png").getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        labelLogo.setIcon(new ImageIcon(new ImageIcon("Images/Components/logo.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

        //Rounded Buttons
        buttonOK.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        buttonOK.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonOK.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonCancel.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_cancel.png").getImage().getScaledInstance(80, 23, Image.SCALE_SMOOTH)));
        buttonCancel.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));


    }

    private void onSignUp() {

        setVisible(false);//hide login

        // Create and show the sign-up dialog
        SignUp_GUI.SignUp_GUI();
    }

    private int userID;


    private void onOK() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery("SELECT * FROM apartment.users");

            int flag = 0; //tell if user OK

            while (result.next()) {

                String userNameFromDatabase = result.getString("userName");
                String userPasswordFromDatabase = result.getString("userPassword");

                if (userNameFromDatabase.equals(txtUserLog.getText())) {

                    //if wrong password
                    if (!userPasswordFromDatabase.equals(txtPass.getText())){
                        JOptionPane.showMessageDialog(null, "Wrong Password!", "Error", JOptionPane.WARNING_MESSAGE);
                    }

                    //if password OK Open Dashboard
                    else {
                        //save the login for every object to be accessed
                        String saveUserName = txtUserLog.getText();
                        //save the login for every object to be accessed
                        String saveUserPass = txtPass.getText();
                        Login_Details log = new Login_Details(saveUserName);


                        //Passing User data(Username & Balance) from Login_Details Class to Accessor Class
                        Accessor.setUserDetails(log.getUsername());




                        dispose();//hide login
                        //Run Dashboard
                        Dashboard_GUI.Dashboard_GUI();



                    }

                    flag = 1;
                    break;

                }
            }

            if (flag == 0) {
                JOptionPane.showMessageDialog(null, "User not found or exists.", "Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception exc) {

            exc.printStackTrace();
        }




    }

    private void onCancel() {

        System.exit(0);
    }


    public static void main(String[] args) {

        LogIn_GUI.LogIn_GUI();

    }

    Image imagelogo = new ImageIcon("Images/Components/logo.png").getImage();
    //for calling LogIn_GUI
    static void LogIn_GUI() {

        LogIn_GUI log = new LogIn_GUI();
        log.pack();
        log.setTitle("SoulSpace | Login.");
        log.setIconImage(log.imagelogo);
        log.setResizable(false);
        log.setBounds(600,200,600,380);
        log.setLocationRelativeTo(null);

        log.setVisible(true);
    }

}
