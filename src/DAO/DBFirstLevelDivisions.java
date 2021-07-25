package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DBFirstLevelDivisions {

    /**
     *  This method queries the database for Divisions associated with a Country.
     *  @param countryId the Country for which to return a list of Divisions
     *  @return returns the list of all Divisions
     */
    public static ObservableList<String> getAllDivisions(int countryId) {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Division FROM first_level_divisions WHERE COUNTRY_ID = ? ORDER BY Division";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, String.valueOf(countryId));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String division = rs.getString("Division");
                divisionList.add(division);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionList;
    }

    /**
     *  This method gets the Division name from the database
     *  @param id the Division ID for which the name is requested
     *  @return the corresponding Division name
     */
    public static String getDivision(int id) {
        String division = null;
        try {
            String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                division = rs.getString("Division");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return division;
    }

    /**
     *  This method gets the Division ID from the database
     *  @param division the Division name for which the ID is requested
     *  @return the corresponding Division ID
     */
    public static int getDivisionId(String division) {
        int id = 0;
        try {
            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setString(1, division);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
              id = rs.getInt("Division_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    /**
     *  This method gets the Country ID from the database
     *  @param divisionId the Division ID for which the Country ID is requested
     *  @return the corresponding Country ID
     */
    public static int getCountryId(int divisionId) {
        int countryId = 0;
        try {
            String sql = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, divisionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                countryId = rs.getInt("COUNTRY_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return countryId;
    }
}

