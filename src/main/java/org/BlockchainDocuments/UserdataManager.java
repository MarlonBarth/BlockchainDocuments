package org.BlockchainDocuments;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserdataManager {
    private final DatabaseManager db;
    private static final String tablename = "Users";
    public UserdataManager(DatabaseManager db){
        this.db = db;
    }

    /**
     * Inserts a new User into the database. It creates a private/public-Key-Pair, that will be saved alongside the Username and the Password.
     * The password will be saved as a hash and the private-Key will be encrypted using the password.
     * @param username The username of the new User
     * @param password The password of the new User
     * @throws SQLException
     */
    public void newUser(String username, String password) throws SQLException {
        String SQL = """
               INSERT INTO %s(Username, PasswordHash, PublicKey, EPrivateKey)
               VALUES ('%s', '%s', '%s', '%s');
               """;
        SQL = String.format(SQL, tablename, username, SecurityManager.hash(password), "PublicKey", "EPrivateKey");
        db.executeStatement(new DatabaseManager.DatabaseQuery(SQL,false));
    }

    /**
     * This Method can verify a password of a given user. It compares the hash of the given password, with the "real" passwordHash saved in the database.
     * It also detects if a username is not yet saved in the database.
     * @param username The password will be compared with that users password-Hash
     * @param passwordTry The password try that should be verified
     * @return 0 if the user exits, but the password is wrong; -1 if the user does not exist; 1 if the password belongs to the user
     * @throws SQLException
     */
    public int verifyPassword(String username, String passwordTry) throws SQLException {
        String SQL = """
               SELECT PasswordHash
               FROM %s
               WHERE Username = '%s';
               """;
        SQL = String.format(SQL,tablename,username);

        //Execution of the SQL-Statement
        DatabaseManager.DatabaseQuery query = new DatabaseManager.DatabaseQuery(SQL,true);
        db.executeStatement(query);

        //The result of the Statement
        ResultSet resultSet = query.getResultSet();

        //If the resultSet is empty, the username is not it the database
        if(!resultSet.first()) return -1;

        //The Hash of the real Password is the only data in the result-Set
        String realHash = resultSet.getString(1);

        //The password-try is hashed and compared to the real hash
        return realHash.equals(SecurityManager.hash(passwordTry)) ? 1 : 0;
    }
}
