package org.imirsel.nema.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kriswest
 */
public class DatabaseConnector {

    public Connection con;
    private String DBName;
    private String DBLocator;
    private String username;
    private String password;
    
    public DatabaseConnector(String DBName, String DBLocator, String username, String password){
        this.DBName = DBName;
        this.DBLocator = DBLocator;
        this.username = username;
        this.password = password;
    }
    
    /** Closes connection to DB server. 
     */
    public void close() {
        try {
            con.close();
        } catch(SQLException e) {
        } finally {
            con = null;
        }
    }
    
    /** 
     * Opens a connection to the database specific in the constructor.
     * @throws SQLException Thrown if an error occurs during instantion.
     */
    public void connect() throws SQLException{
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("ClassNotFoundException thrown while loading JDBC driver!",e);
        }
        String server = getDBLocator() + getDBName() + "?noAccessToProcedureBodies=true";
        Logger.getLogger(DatabaseConnector.class.getName()).log(Level.INFO, "Connecting to server: "+ server);
        con = DriverManager.getConnection(server, username, password);
        if (con == null){
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, "DB connection is null!");
        }
    }

    /**
     * @return the DBName
     */
    public String getDBName(){
        return DBName;
    }

    /**
     * @return the DBLocator
     */
    public String getDBLocator(){
        return DBLocator;
    }
}
