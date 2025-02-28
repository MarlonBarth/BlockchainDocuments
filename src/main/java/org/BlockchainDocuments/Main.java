package org.BlockchainDocuments;

public class Main {
    static final String url = "jdbc:mariadb://localhost:3306/Userdata";
    static final String user = "root";
    static final String password = "PW";

    public static void main(String[] args) throws Exception{
        DatabaseManager db = new DatabaseManager(url,user,password);
        UserdataManager userdataManager = new UserdataManager(db);
        System.out.println(userdataManager.verifyPassword("Marlon Barth","Passwort"));
    }
}