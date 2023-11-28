import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Selected_Apr_GUI extends JDialog {
    private  JPanel contentPane;
    private  JButton btnRent;
    private JButton buttonCancel;
    private JPanel panel;
    private JTextArea details;
    private JLabel poster;
    private JTabbedPane tabbedPane1;
    private JLabel poster2;
    private JLabel poster3;
    private JLabel poster4;
    private JLabel poster5;

    public Selected_Apr_GUI() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btnRent);

        chosenUnit(Transaction.getUnitnum());


        btnRent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRent();
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


        btnRent.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        btnRent.setHorizontalTextPosition(SwingConstants.CENTER);
        btnRent.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonCancel.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_cancel.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        buttonCancel.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
    String unit_number;
    int unit_price;
    private void chosenUnit(String unit){
        String unitNum=unit.substring(6);

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery("SELECT apartment_unit.*,apartment.unit_photo.*" +
                    "FROM apartment.apartment_unit JOIN apartment.unit_photo " +
                    "ON unit_photo.unit_number = apartment_unit.unit_number");


            while (result.next()) {

                unit_number = result.getString("unit_number");
                int bedcount = result.getInt("bedcount");
                unit_price=result.getInt("unit_price");
                String status = result.getString("status");
                BufferedImage[] image = new BufferedImage[5];
                for(int i=1;i<=5;i++){
                    java.sql.Blob blob = result.getBlob("unit_photo"+i);
                    InputStream in = blob.getBinaryStream();
                    image[i-1]=ImageIO.read(in);
                }
                System.out.println(unit_number+" "+unit);

                if (unitNum.equals(unit_number)) {
                    details.setText("Unit Number: "+unit_number+"\nBedcount: "+bedcount
                    +"\nUnit Price: "+unit_price+"\nStatus: "+status);

                    poster.setIcon(new ImageIcon(resize(image[0],550,300)));
                    poster2.setIcon(new ImageIcon(resize(image[1],550,300)));
                    poster3.setIcon(new ImageIcon(resize(image[2],550,300)));
                    poster4.setIcon(new ImageIcon(resize(image[3],550,300)));
                    poster5.setIcon(new ImageIcon(resize(image[4],550,300)));

                    break;

                }
            }


        } catch (Exception exc) {

            exc.printStackTrace();
        }

    }
    private void onRent() {
        Calculate.setUnit_number(unit_number);
        Calculate.setUnit_price(unit_price);
        setVisible(false);
        Rent_Confirmation_GUI.Rent_Confirmation_GUI();
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }



    public static void main(String[] args) {
            Selected_Apr_GUI.Selected_Apr_GUI();
    }

    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();

    static void Selected_Apr_GUI () {

        Selected_Apr_GUI selectedAprGui = new Selected_Apr_GUI();

        selectedAprGui.pack();
        selectedAprGui.setTitle("SoulSpace | Unit Details.");
        selectedAprGui.setIconImage(selectedAprGui.imageLogo);
        selectedAprGui.setResizable(false);
        selectedAprGui.setBounds(600,200,600,600);
        selectedAprGui.setLocationRelativeTo(null);
        selectedAprGui.setVisible(true);

    }





}
