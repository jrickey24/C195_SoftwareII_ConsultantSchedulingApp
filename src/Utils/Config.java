package Utils;

import DAO.DBConnection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** This class houses several methods that get invoked when initiating
 *  the application such as Authentication, Login Activity Tracking
 *  and a RegEx function for validating phone numbers.
 */
public class Config {

    private static final String DBDriver = "com.mysql.jdbc.Driver";
    private static final String DBIPAddress = "//wgudb.ucertify.com:3306/";
    private static final String DBName = "WJ05Pmj";
    private static final String DBUsername = "U05Pmj";
    private static final String DBPassword = "53688568029";
    private static final Integer currentUserId = 1;
    private static String appUsername;
    private static String appPassword;

    public static String getDBDriver() { return DBDriver;}
    public static String getDBIPAddress() { return DBIPAddress;}
    public static String getDBName() { return DBName;}
    public static String getDBUsername() { return DBUsername;}
    public static String getDBPassword() { return DBPassword;}
    public static int getCurrentUserId() { return currentUserId;}

    public static String getAppUsername() {
        try {
            String sql = "SELECT User_Name FROM users WHERE User_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                appUsername = rs.getString("User_Name");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appUsername;
    }

    public static String getAppPassword() {
        try {
            String sql = "SELECT Password FROM users WHERE User_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, currentUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                appPassword = rs.getString("Password");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appPassword;
    }

    /**
     *  This method writes the login activity to login_activity.txt file
     *  when a user tries to authenticate a connection to the application.
     *  @param isLoginSuccess outcome of login attempt
     *  @param invalidCredentialMessage the invalid credential provided or "NONE"
     *  @throws IOException IO Exception
     */
    public static void writeLoginActivity(Boolean isLoginSuccess, String invalidCredentialMessage) throws IOException {
        String fileName = "login_activity.txt";
        String str = "SUCCESSFUL LOGIN: " + isLoginSuccess.toString().toUpperCase() + ", INVALID CREDENTIAL: "
                + invalidCredentialMessage + ", LOGIN ATTEMPT DATE: " + LocalDateTime.now().toString();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.newLine();
        writer.append(str);
        writer.close();
    }

    /**
     *  This method validates the provided Customer's phone number
     *  against the given RegEx.
     *  @param phoneNumber provided phone number
     *  @return Returns whether or not the phoneNumber format is valid
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        String regex = "^\\d{1,3}?-\\d{2,5}?-\\d{3,5}$|^\\d{1,3}?-\\d{2,5}?-\\d{2,4}?-\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return(matcher.matches());
    }
}
