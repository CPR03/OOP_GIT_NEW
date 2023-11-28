//Demonstrate Abstract
abstract class User_Data{

    private String Username;

    private int Balance;

    public void setUserDetails(String Username, int balance) {

    }
    public static void setBalance(int amount){};
    public abstract int setUserID();

    public abstract int getUserID();
    public abstract String getUsername();
    public abstract int getBalance();


}

