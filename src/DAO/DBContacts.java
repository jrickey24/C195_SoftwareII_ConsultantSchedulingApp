package DAO;

import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.Hashtable;



public class DBContacts {


    private static final ObservableList<String> contactList = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allContactAppointments = FXCollections.observableArrayList();
    public static Dictionary<String, Integer> contactDictionary = new Hashtable<>();

    /**
     *  This method grabs all the Contact IDs and Names from the database
     *  and loads the Contact Dictionary with the (key,value) pair
     *  (key=Contact_Name,value=Contact_ID)-This associates the selected
     *  Contact Names from the UI ComboBoxes with the respective ID.
     *  Invoked in Main as the Contacts aren't updated in the app.
     *  @throws SQLException SQL Exception
     */
    public static void loadContactDictionary() throws SQLException {
        String sqlGetContactInfo = "SELECT Contact_ID, Contact_Name FROM contacts";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGetContactInfo);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            contactDictionary.put(rs.getString("Contact_Name"), rs.getInt("Contact_ID"));
        }
    }

    /**
     *  This method gets all Contact Names from the database
     *  @return Returns a list of Contact Names
     *  @throws SQLException SQL Exception
     */
    public static ObservableList<String> getContacts() throws SQLException {
        contactList.clear();
        String sqlGetContacts = "SELECT Contact_Name FROM contacts";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGetContacts);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            contactList.add(rs.getString("Contact_Name"));
        }
        return contactList;
    }

    /**
     *  This method gets all Appointments for a specific Contact from the database
     *  for use in the ContactScheduleReport.
     *  @param contactId The specified Contact_ID
     *  @return Returns all Appointments for a specified Contact
     *  @throws SQLException SQL Exception
     */
    public static ObservableList<Appointment> getContactAppointments(int contactId) throws SQLException {
       allContactAppointments.clear();
       String sqlGetContactAppointments = "SELECT Appointment_ID, Title, Type, Description, Start, " +
               "End, Customer_ID FROM appointments WHERE Contact_ID = ?";
       PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGetContactAppointments);
       ps.setInt(1, contactId);
       ResultSet rs = ps.executeQuery();
       while (rs.next()) {
           Appointment appointment = new Appointment();
           appointment.setAppointmentId(rs.getInt("Appointment_ID"));
           appointment.setTitle(rs.getString("Title"));
           appointment.setType(rs.getString("Type"));
           appointment.setDescription(rs.getString("Description"));
           appointment.setStartTime(rs.getTimestamp("Start").toLocalDateTime());
           appointment.setEndTime(rs.getTimestamp("End").toLocalDateTime());
           appointment.setCustomerId(rs.getInt("Customer_ID"));
           allContactAppointments.add(appointment);
       }
       return allContactAppointments;
    }
}
