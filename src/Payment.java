import javax.swing.*;
import java.sql.*;

public class Payment extends Error {



    Accessor accessor = new Accessor();
    public int confirmPayment(String mode){
        int stat=10; //initialize not in choices(-1,0,1)
        String text;
        double input;

        if(mode.equals("GCash" )){

            text=JOptionPane.showInputDialog(null,"Input Amount: ","Gcash",JOptionPane.QUESTION_MESSAGE);
            try {
                if(isNumeric(text)){
                    input=Double.parseDouble(text);
                    if(input>=Calculate.getTotalprice()){
                        JOptionPane.showMessageDialog(null,"Change: "+ (input - Calculate.getTotalprice()),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                        stat=0;
                    }
                    else {
                        GcashInsufficient();
                        stat=1;
                    }
                }else{
                    invalidInput();
                    stat=1;
                }
            }catch (NullPointerException e){
                nullInput();
                stat=-1;
            }

        }
        else if (mode.equals("Debit")) {
            text=JOptionPane.showInputDialog(null,"Input Amount: ","DebitCard",JOptionPane.QUESTION_MESSAGE);
            try {
                if(isNumeric(text)){
                    input=Double.parseDouble(text);
                    if(input>=Calculate.getTotalprice()){
                        JOptionPane.showMessageDialog(null,"Change: "+ (input - Calculate.getTotalprice()),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                        stat=0;
                    }
                    else {
                        DebitInsufficient();
                        stat=1;
                    }
                }else {
                    invalidInput();
                    stat=1;
                }
            }catch (NullPointerException e){
                nullInput();
                stat=-1;
            }


        }

        else{  //SoulSpace method
            if(accessor.checkBalance()>=Calculate.getTotalprice()){
                int user = Accessor.getUserID();
                double currentbal=accessor.checkBalance();
                double newbal=currentbal-Calculate.getTotalprice();
                try {

                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
                    con.setAutoCommit(true);
//                    Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
//
                    PreparedStatement stmt = con.prepareStatement("UPDATE users SET Balance = ? WHERE user_id = ?");
                    stmt.setDouble(1,newbal);
                    stmt.setInt(2,user);
                    stmt.executeUpdate();
                    stmt.close();
                    con.close();



//                    result.updateDouble("Balance",accessor.checkBalance()-Calculate.getTotalprice());

                } catch (Exception exc) {

                    exc.printStackTrace();
                }


                JOptionPane.showMessageDialog(null,"Remaining Balance "+ accessor.checkBalance(),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                stat=0;
            }
            else{
                inputInsufficient();

            }

        }






        return stat;
    }

    public static boolean isNumeric(String str) { //check if input is number
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
