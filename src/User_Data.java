//Demonstrate Abstract
abstract class User_Data{



    public void setUserDetails(String Username) {

    }


    public abstract double checkBalance();
    public abstract int setUserID();


    public static int getUserID() {
        return 0;
    }

    public abstract String getUsername();

    public double getBalance() {
        return 0;
    }


}

