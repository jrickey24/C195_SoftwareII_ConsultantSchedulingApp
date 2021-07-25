package DAO;

import Utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String MYSQLJDBCDriver = Config.getDBDriver();
    private static final String ipAddress = Config.getDBIPAddress();
    private static final String dbName = Config.getDBName();
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;
    private static Connection conn = null;


    /**
     *  This method establishes the database connection
     *  and is invoked in Main.
     *  @return Returns the database connection
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, Config.getDBUsername(), Config.getDBPassword());
            System.out.println("SQL Connection Successful");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     *  This method provides access to the established
     *  database connection throughout the application.
     *  @return Returns the database connection
     */
    public static Connection getConnection() {
        return conn;
    }

    /**
     *  This method Closes the database connection
     *  after the user Exits the application.
     */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (Exception e) {
            // Do nothing-this is only called upon system exit
        }
    }
}
