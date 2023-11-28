import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Transaction extends User_Data{ //Inheritance


    //Static variable so that value will be the same for every instance
    //Apply Encapsulation
    private static int UserId;
    private static String Username;
    private static int Balance;
    private static String Unitnum;




    //Polymorphism
    public void setUserDetails(String Username,int Balance) {
        Transaction.Username=Username;
        Transaction.Balance=Balance;
        Transaction.UserId=setUserID();
    }



    //abstract Methods
    public int setUserID(){
        int userID = 0;
        try {


            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
            Statement state = con.createStatement();
            ResultSet result = state.executeQuery("SELECT user_id FROM apartment.users WHERE userName ='"+this.Username+"'");

            while(result.next())
                userID = result.getInt("user_id");

        } catch (Exception exc) {

            exc.printStackTrace();
        }


        return userID;
    }


    public static void setBalance(int amount){
        Balance = amount;
    }
    public static void setUnitnum(String unitnum){
        Unitnum=unitnum;

    }

    @Override
    public int getUserID() {
        return UserId;
    }

    @Override
    public String getUsername() {
        return Username;
    }

    @Override
    public int getBalance() {
        return Balance;
    }


    public static String getUnitnum() {
        return Unitnum;
    }


}









