import javax.swing.*;

public class Error {


    public void inputInsufficient (){
        JOptionPane.showMessageDialog(null, "Input is insufficient!", "SoulSpace | Warning", JOptionPane.WARNING_MESSAGE);
    }

    public void GcashInsufficient(){
        JOptionPane.showMessageDialog(null, "Input is insufficient!", "Gcash | Warning", JOptionPane.WARNING_MESSAGE);
    }

    public void DebitInsufficient(){
        JOptionPane.showMessageDialog(null, "Input is insufficient!", "Debit | Warning", JOptionPane.WARNING_MESSAGE);
    }


}
