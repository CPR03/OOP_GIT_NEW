import java.util.ArrayList;

public class Calculate extends Transaction{
    private static String Unit_number;
    private static int Unit_price;
    private static String Duration;
    private static Double totalprice;
    private static double additional;
    static ArrayList<String> utilities = new ArrayList<String>();

    public static void setTotalprice(Double num){
        totalprice=num;
    }
    public static void setUtilities(ArrayList utilList){
        for (int i = 0; i < utilList.size(); i++){
            utilities.add(utilList.get(i).toString());
        }
    }
    static ArrayList<String> getUtilities(){

        return utilities;
    }
    public static double getAdditional(){
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
        return additional;
    }
    public static void setDuration(String date){
        Duration=date;
    }
    public static void setUnit_number(String unit){
        Unit_number=unit;
    }
    public static void setUnit_price(int price){
        Unit_price=price;
    }

    public String getUnit_number(){
        return Unit_number;
    }
    public static int getUnit_price(){
        return Unit_price;
    }

    public static String getDuration() {
        return Duration;
    }


}
