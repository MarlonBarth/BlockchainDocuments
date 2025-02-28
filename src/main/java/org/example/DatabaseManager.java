package org.example;

import java.sql.*;
import java.io.*;

public class DatabaseManager {
    private final String url;
    private final String username;
    private final String password;

    public DatabaseManager(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Executes the SQL-Statement represented by the Input DatabaseQuery-Object.
     * The ResultSet of the SQL-Statement is then written to the Object
     * @param query The SQL-Statement, if the ret-flag is set, the Result-Set can be afterward be found in the Object
     * @throws SQLException
     */
    public void executeStatement(DatabaseQuery query) throws SQLException{
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            Statement statement = con.createStatement();
            if(query.isRet()){
                query.setResultSet(statement.executeQuery(query.getQuery()));
            }else{
                statement.execute(query.getQuery());
            }
        }

    }

    /**
     * This Object represents a SQL-Statement. The Statement itself can be set in the constructor, or via the "setQuery"-Method.
     * The Constructor always expects a flag "ret", that decides if the Statement expects a return. If so, after execution via the DatabaseManager.execute()-Method, the ResultSet
     * can be obtained via the getResultSet()-Method
     */
    public static class DatabaseQuery {
        private String query;
        private boolean ret;
        private ResultSet resultSet;

        /**
         * @param query The SQL-Statement this Object represents
         * @param ret Whether the Statement expects a return
         */
        public DatabaseQuery(String query, boolean ret){
            this.query = query;
            this.ret = ret;
        }

        /**
         * @param ret Whether the Statement expects a return
         */
        public DatabaseQuery(boolean ret){
            this.ret = ret;
        }

        /**
         * This Method can also set the SQL-Statement
         * @param query The SQL-Statement this Object represents
         */
        public void setQuery(String query) {
            this.query = query;
        }

        private String getQuery() {
            return query;
        }

        private boolean isRet() {
            return ret;
        }

        /**
         * If the Object expected a return, the ResultSet can be obtained with this Method
         * @return The ResultSet
         */
        public ResultSet getResultSet() {
            return resultSet;
        }

        private void setResultSet(ResultSet resultSet) {
            this.resultSet = resultSet;
        }
    }

    public static class DatabaseDebug{
        private static void exec(String SQL) throws SQLException {
            DatabaseManager dbM = new DatabaseManager("jdbc:mariadb://localhost:3306/Benutzerdaten","root","PW");
            dbM.executeStatement(new DatabaseManager.DatabaseQuery(SQL,false));
        }

        static void dropTable() throws SQLException {
            exec("DROP TABLE Benutzer");
        }

        static void createTable() throws SQLException {
            exec("""
               CREATE TABLE Benutzer(
               ID int NOT NULL AUTO_INCREMENT,
               Benutzername varchar(255) NOT NULL,
               PasswortHash varchar(255) NOT NULL,
               PublicKey varchar(255) NOT NULL,
               EPrivateKey varchar(255) NOT NULL,
               PRIMARY KEY (ID)
               );
               """);
        }
    }
}
