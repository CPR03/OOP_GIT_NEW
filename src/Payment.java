import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Payment extends Error {



    Accessor accessor = new Accessor();
    public boolean confirmPayment(String mode){
        boolean flag=false;
        double input;

        if(mode.equals("GCash" )){
            input=Double.parseDouble(JOptionPane.showInputDialog(null,"Input Amount: ","Gcash",JOptionPane.QUESTION_MESSAGE));
            if(input>=Calculate.getTotalprice()){
                JOptionPane.showMessageDialog(null,"Change: "+ (input - Calculate.getTotalprice()),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                flag=true;
            }
            else{
                GcashInsufficient();
            }
        }
        else if (mode.equals("Debit")) {
            input=Double.parseDouble(JOptionPane.showInputDialog(null,"Input Amount: ","DebitCard",JOptionPane.QUESTION_MESSAGE));
            if(input>=Calculate.getTotalprice()){
                JOptionPane.showMessageDialog(null,"Change: "+ (input - Calculate.getTotalprice()),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                flag=true;
            }
            else {
                DebitInsufficient();
            }

        }

        else{
            if(accessor.checkBalance()>=Calculate.getTotalprice()){
                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apartment", "root", "root");
                    Statement state = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    ResultSet result = state.executeQuery("SELECT Balance FROM apartment.users WHERE user_id ='"+Accessor.getUserID()+"'");

                    result.updateDouble("Balance",accessor.getBalance()-Calculate.getTotalprice());


                } catch (Exception exc) {

                    exc.printStackTrace();
                }


                JOptionPane.showMessageDialog(null,"Remaining Balance "+ accessor.getBalance(),"Payment Successful",JOptionPane.INFORMATION_MESSAGE);
                flag=true;
            }
            else{
                inputInsufficient();

            }

        }





        return flag;
    }
}
