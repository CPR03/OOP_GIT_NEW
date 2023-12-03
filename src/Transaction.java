import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Date;


public class Transaction extends Calculate{ //To view transaction history


    static ArrayList<String> util = new ArrayList<>();


    //Get selected months
    public static int getmonths(){

        return switch (getDuration()) {
            case "3 months" -> 2;
            case "6 months" -> 5;
            case "1 year" -> 11;
            default -> 0;
        };
    }


    //Save transaction
    public static void saveTransaction(){

        String unitNum= getUnitnum().substring(6); //Will get only the number from the "Unit '1'"
        setStatus(unitNum);
        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Check and get the apartment ID
            ResultSet getApartId = state.executeQuery("SELECT apr_id FROM apartment.apartment_unit where unit_number='"+unitNum+"'");

            getApartId.next();
            int apartment_id = getApartId.getInt("apr_id"); //get Apartment_id of chosen unit


            //Get the latest transaction ID
            ResultSet getMaxTranId = state.executeQuery("SELECT MAX(tran_id) as maxTranId FROM apartment.transaction");

            getMaxTranId.next();
            int maxTranId = getMaxTranId.getInt("maxTranId");

            ResultSet result = state.executeQuery("SELECT * FROM apartment.transaction");

            result.moveToInsertRow();

            //Update the newest Transaction ID in the Transaction Table
            int trans_id = maxTranId + 1; // Increment the max tran_id
            result.updateInt("tran_id", trans_id);

            //Insert the Discount Code
            result.updateDouble("Discount_code",Calculate.getDiscountCode()*100);

            //Insert the Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(formatter.format(new Date()));
            result.updateDate("Date", new java.sql.Date(date.getTime()));

            //Insert the Payment Method
            result.updateString("payment_method",getPaymentmod());
            //Amount paid
            result.updateDouble("amount_pay",Double.parseDouble(Payment.getText()));

            //Insert the  Rent_total
            result.updateDouble("rent_total",getTotalprice()*getmonths());
            System.out.println(getTotalprice());
            System.out.println();

            //Insert the  monthly due
            result.updateDouble("monthly_due_amount",getTotalprice());

            //Insert the duration
            result.updateString("duration",getDuration());


            //Insert Utilities (1 = YES / 0 = NONE)
            util=Calculate.getUtilities();

            for(int i=0;i<util.size();i++){

                switch (util.get(i)) {
                    case "Amenities" -> result.updateInt("amenities", 1);
                    case "Cable" -> result.updateInt("Cable", 1);
                    case "Wi-Fi" -> result.updateInt("wifi", 1);
                    default -> result.updateInt("water", 1);
                }

            }

            //Insert Foreign keys (User ID and Apartment ID)
            result.updateInt("user_id",Accessor.getUserID());
            result.updateInt("apart_id", apartment_id);

            result.insertRow();
            result.beforeFirst();

            //Set Status to 1 (Meaning done)
            JOptionPane.showMessageDialog(null, "Transaction Successful.");

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void setStatus(String unitNum){
        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            con.setAutoCommit(true);
            //Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            PreparedStatement stmt = con.prepareStatement("UPDATE apartment_unit SET status = ? WHERE unit_number = ?"); //(Note: ? is a placeholder)
            stmt.setString(1, "Unavailable"); //.set utilizes the '?'
            stmt.setString(2,unitNum);

            stmt.executeUpdate();
            stmt.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


    public static ArrayList getTransaction(){

        Image image; //unit photo
        double remaining; //remaining balance
        String duration; //duration of stay

        ArrayList<Object> dashboard = new ArrayList<>();

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get Unit Photo, Rent Total, Duration of Stay, Transaction ID
            ResultSet result=state.executeQuery(" SELECT apartment_unit.unit_photo, transaction.rent_total, transaction.duration,transaction.user_id,transaction.tran_id\n" +
                    "            FROM apartment.transaction\n" +
                    "            INNER JOIN apartment.apartment_unit ON transaction.apart_id = apartment_unit.apr_id" +
                    " WHERE user_id='"+Accessor.getUserID()+"' order by tran_id DESC ");

            //Put Data a local variable
            while (result.next()){

                java.sql.Blob blob = result.getBlob("unit_photo");

                InputStream in = blob.getBinaryStream();
                image= ImageIO.read(in).getScaledInstance(320,200,Image.SCALE_SMOOTH);
                remaining=result.getDouble("rent_total");
                System.out.println(remaining);
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
        ArrayList<Object> last_transaction = new ArrayList<>();

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //Get Apartment ID, Apartment Unit Number, Unit Price, and all Columns of Transaction Table
            ResultSet result=state.executeQuery(" SELECT apartment_unit.apr_id,apartment_unit.unit_number,apartment_unit.unit_price, transaction.*" +
                    "            FROM apartment.transaction\n" +
                    "            INNER JOIN apartment.apartment_unit ON transaction.apart_id = apartment_unit.apr_id" +
                    " WHERE user_id='"+Accessor.getUserID()+"' order by tran_id DESC");

            //Put Data to the arrayList (last_transaction)
            while (result.next()){

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
                last_transaction.add(result.getDouble("rent_total")); //total payment

            }

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return last_transaction;
    }

}
