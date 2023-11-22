import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Request_Maintenance_GUI extends JDialog{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox cmbReqType;
    private JTextArea areaConcern;

    public Request_Maintenance_GUI() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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
    }

    LogIn_GUI log = new LogIn_GUI();

    private void onOK() {

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            ResultSet getMaxReqId = state.executeQuery("SELECT MAX(req_id) as maxReqId FROM apartment.maintenance_req");
            getMaxReqId.next();

            int maxReqId = getMaxReqId.getInt("maxReqId");

            ResultSet result = state.executeQuery("SELECT * FROM apartment.maintenance_req");

            result.moveToInsertRow();

            result.updateInt("req_id", maxReqId + 1); // Increment the max req_id

            int test = log.getUserID();
            System.out.println(test);

            result.updateInt("user_id", 1); //Error here

            result.updateString("type", cmbReqType.getSelectedItem().toString());
            result.updateString("description", areaConcern.getText());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(formatter.format(new Date()));
            result.updateDate("date_created", new java.sql.Date(date.getTime()));

            result.insertRow();
            result.beforeFirst();

            JOptionPane.showMessageDialog(null, "Request Successfully sent.");

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }


    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    public static void main(String[] args) {
        Request_Maintenance_GUI.Request_Maintenance_GUI();
    }

    //for calling Request Maintenance
    static void Request_Maintenance_GUI() {

        Request_Maintenance_GUI dialog = new Request_Maintenance_GUI();
        dialog.pack();
        dialog.setBounds(300,100,250,250);
        dialog.setTitle("SoulSpace | Request Maintenance");
        dialog.setVisible(true);
        System.exit(0);

    }
}
