package DAO;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;


public class DBCountries {

    public static Dictionary<Integer, String> countryDictionary = new Hashtable<>();

    /**
     *  This method is invoked to populate the
     *  Country Dictionary. Invoked in Main.
     */
    public static void loadCountryDictionary(){
        try {
            String sql = "SELECT Country_ID, Country FROM countries ORDER BY Country_ID";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("Country_ID");
                String value = rs.getString("Country");
                countryDictionary.put(key, value);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
