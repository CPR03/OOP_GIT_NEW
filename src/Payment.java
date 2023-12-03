import javax.swing.*;
import java.sql.*;

public class Payment extends Error {

    Accessor accessor = new Accessor();
    static String text;
    public static String getText(){
        return text;
    }
    //Get Payment Method

    public int confirmPayment(String mode){

        int stat=10; //initialize not in choices(-1,0,1)

        double input;

        //GCash
        if(mode.equals("GCash" )){

            text=JOptionPane.showInputDialog(null,"Input Amount: ","Gcash",JOptionPane.QUESTION_MESSAGE);
            try {

                if(isNumeric(text)){
                    input=Double.parseDouble(text);

                    //Display change (if OK)
                    if(input>=Calculate.getTotalprice()){
                        JOptionPane.showMessageDialog(null,"Change: "+ (input - Calculate.getTotalprice()),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                        stat=0;
                    }

                    else {
                        //Display Error (if fund not enough)
                        GcashInsufficient();
                        stat=1;
                    }

                }

                else{

                    //Display Error (If Input is Non numeric)
                    invalidInput();
                    stat=1;

                }

            }catch (NullPointerException e){
                nullInput();
                stat=-1;
            }

        }

        //Debit
        else if (mode.equals("Debit")) {

            text=JOptionPane.showInputDialog(null,"Input Amount: ","DebitCard",JOptionPane.QUESTION_MESSAGE);

            try {

                if(isNumeric(text)){

                    input=Double.parseDouble(text);

                    if(input>=Calculate.getTotalprice()){

                        //Display change
                        JOptionPane.showMessageDialog(null,"Change: "+ (input - Calculate.getTotalprice()),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                        stat=0;

                    }

                    else {
                        //Display Error (If Debit Input is not enough)
                        DebitInsufficient();
                        stat=1;

                    }

                }

                else {
                    //Display Error (If input is Non-numeric)
                    invalidInput();
                    stat=1;

                }

            }catch (NullPointerException e){
                nullInput();
                stat=-1;
            }


        }

        //SoulSpace method
        else{

            //Check if the user input is enough for payment.
            if(accessor.checkBalance()>=Calculate.getTotalprice()){
                text= String.valueOf(Calculate.getTotalprice());

                int user = Accessor.getUserID(); //Get userID
                double currentbal=accessor.checkBalance(); //Check Current balance
                double newbal=currentbal-Calculate.getTotalprice(); //Get New Balance (Note: Total Price is the total expenses of renting the unit)

                try {

                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
                    con.setAutoCommit(true);
                    //Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                    PreparedStatement stmt = con.prepareStatement("UPDATE users SET Balance = ? WHERE user_id = ?"); //(Note: ? is a placeholder)
                    stmt.setDouble(1,newbal); //.set utilizes the '?'
                    stmt.setInt(2,user);

                    stmt.executeUpdate();
                    stmt.close();
                    con.close();

                    //result.updateDouble("Balance",accessor.checkBalance()-Calculate.getTotalprice());

                } catch (Exception exc) {

                    exc.printStackTrace();
                }

                //Display Success Message
                JOptionPane.showMessageDialog(null,"Remaining Balance "+ accessor.checkBalance(),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                stat=0;
            }

            else{

                //Display Error (If SoulSpace Balance is not enough)
                inputInsufficient();

            }

        }

        return stat;
    }


    //check if input is number
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }


}
