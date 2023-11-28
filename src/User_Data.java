//Demonstrate Abstract
abstract class User_Data{

    private String Username;

    private int Balance;
    private String Unitnum;

    public void setUserDetails(String Username, int balance) {

    }

    public static void setUnitnum(String unitNum) {

    }

    public static void setBalance(int amount){};
    public abstract int setUserID();

    public static String getUnitnum() {
        return null;
    }

    public abstract int getUserID();
    public abstract String getUsername();
    public abstract int getBalance();


}

