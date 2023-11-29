import javax.swing.*;
import java.sql.*;

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
                flag=true;
            }
            else{
                inputInsufficient();

            }

        }





        return flag;
    }
}
