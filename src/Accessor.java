import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Accessor extends User_Data{ //Inheritance


    //Static variable so that value will be the same for every instance
    //Apply Encapsulation
    private static int UserId;
    private static String Username;
    private static double Balance;
    private static String Unitnum;




    //Polymorphism
    public static void setUserDetails(String Username) {
        Accessor.Username=Username;
        UserId=setUserID();

    }




    public double checkBalance(){
        double bal=0;
        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("SELECT Balance FROM apartment.users WHERE userName ='"+Accessor.Username+"'");

            while(result.next())
                bal = result.getDouble("Balance");

        } catch (Exception exc) {

            exc.printStackTrace();
        }
        return bal;
    }
    public static int setUserID(){
        int userID = 0;
        try {


            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet result = state.executeQuery("SELECT user_id FROM apartment.users WHERE userName ='"+Accessor.Username+"'");

            while(result.next())
                userID = result.getInt("user_id");

        } catch (Exception exc) {

            exc.printStackTrace();
        }


        return userID;
    }



    public static void setUnitnum(String unitnum){
        Unitnum=unitnum;

    }


    public static int getUserID() {
        return UserId;
    }


    public String getUsername() {
        return Username;
    }


    public double getBalance() {


        return checkBalance();
    } //override in Userdata


    public static String getUnitnum() {
        return Unitnum;
    } //override in Userdata


}









