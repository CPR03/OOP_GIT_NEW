import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Transaction extends Calculate{ //To view transaction history
    private static String Unit_number;
    private static int Unit_price;
    private static int trans_id;
    private static int Apartment_id;
    private static int Status=0;
    private static String Duration;
    private static String Paymentmod;
    private static Double Totalprice;
    private static Double Remaining_balance;
    private static int Remaining_months;
    private static double Additional;
    private static double balance;
    static ArrayList<String> util = new ArrayList<String>();


    //Get selected months
    public static int getmonths(){
        int months=0;
        if(getDuration().equals("3 months")){
            months=3;
        } else if (getDuration().equals("6 months")) {
            months=6;
        } else if (getDuration().equals("1 year")) {
            months=12;
        }else{
            months=1;
        }

        return months;
    }


    //Save transaction
    public static void saveTransaction(){

        String unitNum= getUnitnum().substring(6); //Will get only the number from the "Unit '1'"

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Check and get the apartment ID
            ResultSet getApartId = state.executeQuery("SELECT apr_id FROM apartment.apartment_unit where unit_number='"+unitNum+"'");

            getApartId.next();
            Apartment_id = getApartId.getInt("apr_id"); //get Apartment_id of chosen unit

            //Get the latest transaction ID
            ResultSet getMaxTranId = state.executeQuery("SELECT MAX(tran_id) as maxTranId FROM apartment.transaction");

            getMaxTranId.next();
            int maxTranId = getMaxTranId.getInt("maxTranId");

            ResultSet result = state.executeQuery("SELECT * FROM apartment.transaction");

            result.moveToInsertRow();

            //Update the newest Transaction ID in the Transaction Table
            trans_id = maxTranId+1; // Increment the max tran_id
            result.updateInt("tran_id", trans_id);

            //Insert the Discount Code
            result.updateDouble("Discount_code",Calculate.getDiscountCode()*100);

            //Insert the Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(formatter.format(new Date()));
            result.updateDate("Date", new java.sql.Date(date.getTime()));

            //Insert the Payment Method
            result.updateString("payment_method",getPaymentmod());

            //Insert the  Rent_total
            result.updateDouble("rent_total",getTotalprice()*getmonths());

            //Insert the  monthly due
            result.updateDouble("monthly_due_amount",getTotalprice());

            //Insert the duration
            result.updateString("duration",getDuration());


            //Insert Utilities (1 = YES / 0 = NONE)
            util=Calculate.getUtilities();

            for(int i=0;i<util.size();i++){

                if(util.get(i).equals("Amenities")){
                    result.updateInt("amenities",1);
                }

                else if(util.get(i).equals("Cable")){
                    result.updateInt("Cable",1);
                }

                else if(util.get(i).equals("Wi-Fi")){
                    result.updateInt("wifi",1);
                }

                else{
                    result.updateInt("water",1);
                }

            }

            //Insert Foreign keys (User ID and Apartment ID)
            result.updateInt("user_id",Accessor.getUserID());
            result.updateInt("apart_id",Apartment_id);

            result.insertRow();
            result.beforeFirst();

            //Set Status to 1 (Meaning done)
            Status=1;
            JOptionPane.showMessageDialog(null, "Transaction Successful.");

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static ArrayList getTransaction(){

        Image image; //unit photo
        double remaining; //remaining balance
        String duration; //duration of stay

        ArrayList<Object> dashboard = new ArrayList<Object>();

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get Unit Photo, Rent Total, Duration of Stay, Transaction ID
            ResultSet result=state.executeQuery(" SELECT apartment_unit.unit_photo, transaction.rent_total, transaction.duration,transaction.user_id\n" +
                    "            FROM apartment.transaction\n" +
                    "            INNER JOIN apartment.apartment_unit ON transaction.apart_id = apartment_unit.apr_id" +
                    " WHERE user_id='"+Accessor.getUserID()+"'");

            //Put Data a local variable
            while (result.next()){

                java.sql.Blob blob = result.getBlob("unit_photo");
                InputStream in = blob.getBinaryStream();
                image= ImageIO.read(in).getScaledInstance(320,200,Image.SCALE_SMOOTH);
                remaining=result.getDouble("rent_total");
                duration=result.getString("duration");

                //Add unit photo, duration, and remaining balance to arrayList
                dashboard.add(image);
                dashboard.add(duration);
                dashboard.add(remaining);

            }

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return dashboard;
    }

    //Get Last Transaction Performed
    public static ArrayList getlast_trans(){

        ArrayList<Object> last_transaction = new ArrayList<Object>();

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get Apartment ID, Apartment Unit Number, Unit Price, and all Columns of Transaction Table
            ResultSet result=state.executeQuery(" SELECT apartment_unit.apr_id,apartment_unit.unit_number,apartment_unit.unit_price, transaction.*" +
                    "            FROM apartment.transaction\n" +
                    "            INNER JOIN apartment.apartment_unit ON transaction.apart_id = apartment_unit.apr_id" +
                    " WHERE user_id='"+Accessor.getUserID()+"' order by Date DESC ");

            //Put Data to the arrayList (last_transaction)
            while (result.next()){

//                Calendar cal1 = new GregorianCalendar();
//                cal1.add(Calendar.MONTH, +1);
//                last_transaction.add(result.getDate("Date",cal1));


                last_transaction.add(result.getInt("apr_id")); //apartment ID
                last_transaction.add(result.getString("unit_number")); //unit number
                last_transaction.add(result.getInt("unit_price")); //unit price
                last_transaction.add(result.getInt("tran_id")); //transaction id
                last_transaction.add(result.getDate("Date")); //date created
                last_transaction.add(result.getDouble("Discount_code")); //discount code
                last_transaction.add(result.getDouble("monthly_due_amount")); //rent per month
                last_transaction.add(result.getString("duration")); //duration of stay
                last_transaction.add(result.getInt("amenities")); //utility
                last_transaction.add(result.getInt("wifi")); //utility
                last_transaction.add(result.getInt("cable")); //utility
                last_transaction.add(result.getInt("water")); //utility
                last_transaction.add(result.getString("payment_method")); //payment method

            }

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return last_transaction;
    }

}
