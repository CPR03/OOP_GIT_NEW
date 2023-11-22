public class Save_Data {
    private static String username; //save userName
    private static String userPassword; //save userPassword

    public Save_Data(String user, String pass){
        username = user;
        userPassword = pass;
    }

    public String getUsername(){
        return username;
    }

    public String getUserPassword(){
        return userPassword;
    }

}



