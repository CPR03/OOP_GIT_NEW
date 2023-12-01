import java.util.ArrayList;

public class Calculate extends Accessor {
    private static String Unit_number;
    private static int Unit_price;
    private static String Duration;
    private static String Paymentmod;
    private static double DiscountCode;
    private static Double totalprice;
    private static double additional;
    static ArrayList<String> utilities = new ArrayList<String>();


    //Add selected utilities to arraylist
    public static void setUtilities(ArrayList utilList){

        for (int i = 0; i < utilList.size(); i++){
            utilities.add(utilList.get(i).toString());
        }
    }

    //Compute selected utilities
    public static void setAdditional(){

        for (int i = 0; i < utilities.size(); i++){
            if(utilities.get(i).equals("Amenities")){
                additional+=200;
            }
            else if (utilities.get(i).equals("Wi-Fi")) {
                additional+=300;
            }
            else if (utilities.get(i).equals("Cable")) {
                additional+=400;
            }
            else{
                additional+=100; //Water
            }
        }
    }

    //Reset Utilities additional variable
    public static void resetadditional(){
        additional=0;
    }

    //Setters

    //Set total price
    public static void setTotalprice(Double num){
        totalprice=num;
    }

    //Set Duration of Stay
    public static void setDuration(String date){

        Duration=date;
    }

    //Set Unit Number
    public static void setUnit_number(String unit){
        Unit_number=unit;
    }

    //Set Unit Price
    public static void setUnit_price(int price){
        Unit_price=price;
    }

    //Set Payment Method
    public static void setPaymentmod(String paymentmod){
        Paymentmod=paymentmod;
    }

    //Set Discount Code
    public static void setDiscountCode(double discountCode){
        DiscountCode=discountCode;
    }

    //Getters

    //Get Discount Code
    public static double getDiscountCode(){
        return DiscountCode;
    }

    //Get Unit Number
    public String getUnit_number(){
        return Unit_number;
    }

    //Get Unit Price
    public static int getUnit_price(){
        return Unit_price;
    }

    //Get Duration
    public static String getDuration() {
        return Duration;
    }

    //Get Payment Method
    public static String getPaymentmod() {
        return Paymentmod;
    }

    //Get Total Price
    public static double getTotalprice(){
        return totalprice;
    }

    //Get Total Utilities
    public static double getAdditional(){
        return additional;
    }

    //Get Selected Utilities
    public static ArrayList<String> getUtilities(){

        return utilities;
    }

    //Reset all
    public void reset(){

        setTotalprice(0.0);
        setDuration("");
        utilities.clear();
        setDiscountCode(0.0);
        setPaymentmod("");
        setUnitnum("");
        setUnit_price(0);
        setUnit_number("");
      
        resetadditional();

    }

}
