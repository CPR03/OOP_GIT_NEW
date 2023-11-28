import java.time.LocalDate;
import java.util.ArrayList;

public class Transaction extends Calculate{ //To view transaction history
    private static String Unit_number;
    private static int Unit_price;
    private static int trans_id;
    private static String Duration;
    private static String Paymentmod;
    private static Double Totalprice;
    private static Double Remaining_balance;
    private static int Remaining_months;
    private static double Additional;
    private static double balance;

    static ArrayList<String> Utilities = new ArrayList<String>();

    public static void setHistory(int Trans_id){
        trans_id=Trans_id;
        Unit_number=getUnitnum();
        Unit_price=getUnit_price();
        Duration=getDuration();
        Paymentmod = getPaymentmod();
        Totalprice = getTotalprice();
        Utilities= getUtilities();
        Additional = getAdditional();
        balance=getBalance();
        Remaining_months= setmonths();
        Remaining_balance=Remaining_months*Totalprice;



    }

    public static int setmonths(){
        int months=0;
        if(Duration.equals("3months")){
            months=2;
        } else if (Duration.equals("6months")) {
            months=5;
        } else if (Duration.equals("1year")) {
            months=11;
        }else{
            months=0;
        }

        return months;
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
