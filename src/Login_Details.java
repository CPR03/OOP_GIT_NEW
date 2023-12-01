public class Login_Details {
    //Encapsulation
    private static String username;
    private static String userPassword;
    private int Balance=10000;

    //Constructor
    public Login_Details(String user, String pass){
        username = user;
        userPassword = pass;
    }

    public String getUsername(){
        return username;
    }
    public int getBalance(){return this.Balance;}



}
