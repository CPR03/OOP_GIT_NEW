import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Transaction extends Calculate{ //To view transaction history
    private static String Unit_number;
    private static int Unit_price;
    private static int trans_id;
    private static int Apartment_id;
    private static String Duration;
    private static String Paymentmod;
    private static Double Totalprice;
    private static Double Remaining_balance;
    private static int Remaining_months;
    private static double Additional;
    private static double balance;
    static ArrayList<String> util = new ArrayList<String>();


//    public static void setHistory(int Trans_id){
//        trans_id=Trans_id;
//        Unit_number=getUnitnum();
//        Unit_price=getUnit_price();
//        Duration=getDuration();
//        Paymentmod = getPaymentmod();
//        Totalprice = getTotalprice();
//        Utilities= getUtilities();
//        Additional = getAdditional();
//        balance=getBalance();
//        Remaining_months= setmonths();
//        Remaining_balance=Remaining_months*Totalprice;
//
//
//
//    }

    public static int getmonths(){
        int months=0;
        if(getDuration().equals("3months")){
            months=3;
        } else if (getDuration().equals("6months")) {
            months=6;
        } else if (getDuration().equals("1year")) {
            months=12;
        }else{
            months=1;
        }

        return months;
    }

    public static void saveTransaction(){
        String unitNum= getUnitnum().substring(6);

        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);


            ResultSet getApartId=state.executeQuery("SELECT apr_id FROM apartment.apartment_unit where unit_number='"+unitNum+"'");

            getApartId.next();
            Apartment_id=getApartId.getInt("apr_id"); //get Apartment_id of chosen unit


            ResultSet getMaxTranId = state.executeQuery("SELECT MAX(tran_id) as maxTranId FROM apartment.transaction");

            getMaxTranId.next();
            int maxTranId = getMaxTranId.getInt("maxTranId");


            ResultSet result = state.executeQuery("SELECT * FROM apartment.transaction");

            result.moveToInsertRow();

            trans_id=maxTranId+1; // Increment the max tran_id
            result.updateInt("tran_id", trans_id);





            //Date
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = formatter.parse(formatter.format(new Date()));
            result.updateDate("Date", new java.sql.Date(date.getTime()));

            //Payment Method
            result.updateString("payment_method",getPaymentmod());
            //Rent_total
            result.updateDouble("rent_total",getTotalprice()*getmonths());
            //monthly due
            result.updateDouble("monthly_due_amount",getTotalprice());
            result.updateString("duration",getDuration());

            //utilities
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
            //Foreign keys
            result.updateInt("user_id",Accessor.getUserID());
            result.updateInt("apart_id",Apartment_id);

            result.insertRow();
            result.beforeFirst();

            JOptionPane.showMessageDialog(null, "Transaction Successful.");

            state.close();
            con.close();

        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }


//    public static double getTotalprice(){
//        return Totalprice;
//    }
//
//    public String getUnit_number(){
//        return Unit_number;
//    }
//    public static int getUnit_price(){
//        return Unit_price;
//    }
//
//    public static String getDuration() {
//        return Duration;
//    }
//    public static String getPaymentmod() {
//        return Paymentmod;
//    }
//    public static double getAdditional(){
//        return Additional;
//    }
}
