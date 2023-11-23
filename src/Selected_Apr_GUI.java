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


        buttonOK.setIcon(new ImageIcon(new ImageIcon("Images/Components/button_red.png").getImage().getScaledInstance(150, 30, Image.SCALE_SMOOTH)));
        buttonOK.setHorizontalTextPosition(SwingConstants.CENTER);
        buttonOK.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    private void chosenUnit(String unit){
         /*
        if(unit.equals("Rent: Unit 1")){


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



        } */


        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery("SELECT apartment_unit.*,apartment.unit_photo.*" +
                    "FROM apartment.apartment_unit JOIN apartment.unit_photo " +
                    "ON unit_photo.unit_number = apartment_unit.unit_number");



            while (result.next()) {

                String unit_number = result.getString("unit_number");
                int bedcount = result.getInt("bedcount");
                int unit_price=result.getInt("unit_price");
                BufferedImage[] image = new BufferedImage[5];
                for(int i=1;i<=5;i++){
                    java.sql.Blob blob = result.getBlob("unit_photo"+i);
                    InputStream in = blob.getBinaryStream();
                    image[i-1]=ImageIO.read(in);
                }

                if (unit.equals("Rent: "+unit_number)) {
                    details.setText("Unit Number: "+unit_number+"\nBedcount: "+bedcount
                    +"\nUnit Price: "+unit_price);

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
    private void onOK() {
        Rent_Confirmation_GUI.Rent_Confirmation_GUI();
    }


    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    Image imageLogo = new ImageIcon("Images/Components/logo.png").getImage();

}
