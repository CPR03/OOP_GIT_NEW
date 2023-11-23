import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignUp_GUI extends JDialog {
    public Image imageLogo;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtUserLog;
    private JPasswordField passfield;
    private JLabel logInPic;
    private JLabel labelLogo;
    private JButton btnLogIn;
    private JButton button1;


    public SignUp_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onOK();

            }
        });

        btnLogIn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onBack();

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

    }

    private void onOK() {

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet getRowCount = state.executeQuery("SELECT COUNT(*) as rowCount FROM apartment.users");

            // Move to the first (and only) row of the result set
            if (getRowCount.next()) {

                // Get the count of rows
                int rowCount = getRowCount.getInt("rowCount");

                String usernameInput = txtUserLog.getText();
                String passText = new String(passfield.getPassword());

                boolean usernameExists = false;

                ResultSet result;

                if (rowCount == 0) {

                    // Insert the new user with user_id 1 if database is empty
                    result = state.executeQuery("SELECT * FROM apartment.users");

                    result.moveToInsertRow();

                    result.updateInt("user_id", 1); // add the newly created ID
                    result.updateString("userName", usernameInput); // add username
                    result.updateString("userPassword", passText); // add password

                    result.insertRow();
                    result.beforeFirst();

                    JOptionPane.showMessageDialog(null, "Account successfully created.");
                    setVisible(false);
                    LogIn_GUI.LogIn_GUI(); // Go back to Login


                } else {

                    // The database is not empty, check for duplicates
                    result = state.executeQuery("SELECT * FROM apartment.users");

                    //Loop through the database to check if user exists
                    while (result.next()) {

                        String userNameFromDatabase = result.getString("userName").toLowerCase();

                        if (userNameFromDatabase.equals(usernameInput.toLowerCase())) {

                            JOptionPane.showMessageDialog(null, "Username already exists","Details not valid.",JOptionPane.ERROR_MESSAGE);
                            usernameExists = true;
                            break;  // exit the loop as soon as a matching username is found

                        }
                    }

                    //if user not exist insert new user
                    if (!usernameExists) {

                        result.last();
                        int id = result.getInt("user_id") + 1; // get current id and ADD 1

                        result.moveToInsertRow();

                        result.updateInt("user_id", id); // add the newly created ID
                        result.updateString("userName", usernameInput); // add username
                        result.updateString("userPassword", passText); // add password

                        result.insertRow();
                        result.beforeFirst();

                        JOptionPane.showMessageDialog(null, "Account successfully created.");
                        setVisible(false);
                        LogIn_GUI.LogIn_GUI(); // Go back to Login

                    }
                }
            }

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }


    }


    //Go back to Login if back button is pressed
    private void onBack(){
        setVisible(false);

        LogIn_GUI.LogIn_GUI();
    }

    private void onCancel() {
        dispose();
    }

    public static void main (String[] args){
        SignUp_GUI.SignUp_GUI();
    }

    Image imagelogo = new ImageIcon("Images/Components/logo.png").getImage();
    static void SignUp_GUI(){
        SignUp_GUI sign = new SignUp_GUI();
        sign.pack();
        sign.setTitle("SoulSpace | Sign up.");
        sign.setIconImage(sign.imagelogo);
        sign.setResizable(false);
        sign.setBounds(600,200,600,380);

        sign.setLocationRelativeTo(null);
        sign.setVisible(true);
    }

}
