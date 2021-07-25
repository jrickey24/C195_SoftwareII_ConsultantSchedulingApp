package DAO;

import Model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicReference;


public class DBAppointments {

    private static Appointment a;
    public static void setModifyAppointment(Appointment appointment){
        a = appointment;
    }
    public static Appointment getAppointmentToModify(){
        return a;
    }
    private static final ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
    private static final ObservableList<String> months = FXCollections.observableArrayList();
    public static Dictionary<String, String> monthsDictionary = new Hashtable<>();
    public static ObservableList<String> types = FXCollections.observableArrayList();
    public static ObservableList<String> titles = FXCollections.observableArrayList();
    public static ObservableList<String> descriptions = FXCollections.observableArrayList();
    public static ObservableList<String> locations = FXCollections.observableArrayList();
    public static ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

    /**
     *  This method populates the Months Dictionary with the
     *  (key,value) pair (key=monthName,value=monthNumber)-This associates
     *  the selected Month Names from the UI ComboBoxes with the respective Number.
     *  Invoked in Main.
     */
    public static void loadMonthsDictionary() {
        monthsDictionary.put("January", "01");
        monthsDictionary.put("February", "02");
        monthsDictionary.put("March", "03");
        monthsDictionary.put("April", "04");
        monthsDictionary.put("May", "05");
        monthsDictionary.put("June", "06");
        monthsDictionary.put("July", "07");
        monthsDictionary.put("August", "08");
        monthsDictionary.put("September", "09");
        monthsDictionary.put("October", "10");
        monthsDictionary.put("November", "11");
        monthsDictionary.put("December", "12");
    }

    /**
     *  This method grabs all the Title values that can be
     *  select when Adding or Modifying an Appointment for
     *  use in UI ComboBoxes.
     *  Invoked in Main.
     */
    public static void loadTitles() {
        titles.addAll("Potential Client", "Existing Client", "Product Scope", "Product Enhancement");
    }

    /**
     *  This method grabs all the Type values that can be
     *  select when Adding or Modifying an Appointment for
     *  use in UI ComboBoxes.
     *  Invoked in Main.
     */
    public static void loadTypes() {
        types.addAll("Sales/Consulting", "Software Development");
    }

    /**
     *  This method grabs all the Location values that can be
     *  select when Adding or Modifying an Appointment for
     *  use in UI ComboBoxes.
     *  Invoked in Main.
     */
    public static void loadLocations() {
        locations.addAll("Phoenix", "White Plains", "Montreal", "London");
    }

    /**
     *  This method grabs all the Description values that can be
     *  select when Adding or Modifying an Appointment for
     *  use in UI ComboBoxes.
     *  Invoked in Main.
     */
    public static void loadDescriptions() {
        descriptions.addAll("Initial Meeting", "Touch-base", "Advisory Session", "Requirements Gathering",
                "Product Implementation/Go-Live", "Disaster Recovery", "Client Onboarding");
    }

    /**
     *  This method grabs all the Start and End Time values that can be
     *  select when Adding or Modifying an Appointment for use in UI ComboBoxes.
     *  Invoked in Main.
     */
    public static void loadAppointmentTimes() {
        appointmentTimes.addAll("00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00",
                "04:30", "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30",
                "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00",
                "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30",
                "21:00", "21:30", "22:00", "22:30", "23:00", "23:30");
    }

    /**
     *  This method grabs all upcoming Appointments from the database
     *  scheduled to start within 15 minutes of valid User login.
     *  @return Returns all upcoming Appointments within the range
     *  @throws SQLException SQL Exception
     */
    public static Appointment checkUpcomingAppointment() throws SQLException {
        String sqlQryLogin = "SELECT * FROM appointments WHERE (Start BETWEEN ? AND ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryLogin);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        Timestamp timeInFifteen = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));
        ps.setTimestamp(1, currentTime);
        ps.setTimestamp(2, timeInFifteen);
        Appointment appointment = new Appointment();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            appointment.setAppointmentId(rs.getInt("Appointment_ID"));
            appointment.setTitle(rs.getString("Title"));
            appointment.setType(rs.getString("Type"));
            appointment.setLocation(rs.getString("Location"));
            appointment.setStartTime(rs.getTimestamp("Start").toLocalDateTime());
            appointment.setEndTime(rs.getTimestamp("End").toLocalDateTime());
        }
        return appointment;
    }

    /**
     *  This method queries the database for conflicting Customer
     *  Appointments to validate there are no overlaps for a specified
     *  Customer when the user attempts to Add or Modify an Appointment
     *  Start and End time.
     *  @param x Customer_ID
     *  @param y Appointment_ID
     *  @param s Start
     *  @param e End
     *  @throws SQLException SQL Exception
     */
    public static boolean appointmentConflictExists(int x, int y, LocalDateTime s, LocalDateTime e) throws SQLException {
        boolean foundConflict = false;
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        String sqlQryCustomer = "SELECT * FROM appointments WHERE Customer_ID = ? AND Appointment_ID <> ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryCustomer);
        ps.setInt(1, x);
        ps.setInt(2, y);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointment checkAppointment = new Appointment();
            checkAppointment.setAppointmentId(rs.getInt("Appointment_ID"));
            checkAppointment.setStartTime(rs.getTimestamp("Start").toLocalDateTime());
            checkAppointment.setEndTime(rs.getTimestamp("End").toLocalDateTime());
            customerAppointments.add(checkAppointment);
        }
        for (Appointment apt : customerAppointments) {
            if (s.isAfter(apt.getStartTime()) && s.isBefore(apt.getEndTime())
                    || e.isAfter(apt.getStartTime()) && e.isBefore(apt.getEndTime())
                    || s.isBefore(apt.getStartTime()) && e.isAfter(apt.getEndTime())
                    || s.equals(apt.getStartTime()) && e.equals(apt.getEndTime())
                    || s.equals(apt.getStartTime()) || e.equals(apt.getEndTime())) {
                foundConflict = true;
            }
        }
        return foundConflict;
    }

    /**
     *  This method grabs all the Appointments currently
     *  scheduled in the database for populating UI TableView.
     *  @return Returns all appointments
     *  @throws SQLException SQL Exception
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        String sqlQryAppointments = "SELECT a.Appointment_ID, a.Title, a.Description, a.Location," +
                " a.Type, a.Start, a.End, a.Customer_ID, a.User_ID, a.Contact_ID, c.Contact_Name" +
                " FROM appointments a INNER JOIN contacts c ON c.Contact_ID = a.Contact_ID";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryAppointments);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentId = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime endTime = rs.getTimestamp("End").toLocalDateTime();
            int customerId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            Appointment appointment = new Appointment(appointmentId, title, description, location, type,
                    startTime, endTime, customerId, userId, contactId, contactName);
            allAppointments.add(appointment);
        }
        return allAppointments;
    }

    /**
     *  This method is invoked to populate UI ComboBox
     *  with Type values existing in the database for
     *  the AppointmentTypeReport.
     *  @return Returns list of Types
     *  @throws SQLException SQL Exception
     */
    public static ObservableList<String> getAppointmentTypes() throws SQLException {
        appointmentTypes.clear();
        String sqlQryTypes = "SELECT DISTINCT(type) FROM appointments";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryTypes);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            appointmentTypes.add(rs.getString("type"));
        }
        return appointmentTypes;
    }

    /**
     *  This method is invoked to populate UI ComboBox
     *  with month values for user selection.
     *  @return Returns list of months
     */
    public static ObservableList<String> getMonths() {
        if (months.isEmpty()) {
            months.addAll("January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December");
        }
        return months;
    }

    /**
     *  This method queries the database for specific
     *  Appointment Types for a specified month.
     *  @param type Specified Appointment Type
     *  @param month Specified Month
     *  @return Returns the total count found
     *  @throws SQLException SQL Exception
     */
    public static int getAppointmentTypeCountByMonth(String type, String month) throws SQLException {
        int countByMonth = 0;
        String monthStr = "%-" + month + "-%";
        String sqlQryCount = "SELECT COUNT(*) FROM appointments WHERE Type = ? AND Start" +
                " Like ? AND End Like ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryCount);
        ps.setString(1, type);
        ps.setString(2, monthStr);
        ps.setString(3, monthStr);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countByMonth = rs.getInt("COUNT(*)");
        }
        return countByMonth;
    }

    /**
     *  This method Inserts a new Appointment into the database.
     *  The Appointment_ID value is auto-generated upon Insertion.
     *  @param title Title
     *  @param description Description
     *  @param location Location
     *  @param type Type
     *  @param start Start
     *  @param end End
     *  @param customerId Customer_ID
     *  @param userId User_ID
     *  @param contactId Contact_ID
     *  @throws  SQLException SQL Exception
     */
    public static void insertAppointment(String title, String description, String location, String type, Timestamp start,
                                         Timestamp end, int customerId, int userId, int contactId) throws SQLException {
        String insertSql = "INSERT INTO appointments " +
                "(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(insertSql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.execute();
    }

    /**
     *  This method Updates an Appointment in the database
     *  @param title Title
     *  @param description Description
     *  @param location Location
     *  @param type Type
     *  @param start Start
     *  @param end End
     *  @param customerId Customer_ID
     *  @param userId User_ID
     *  @param contactId Contact_ID
     *  @param id Appointment_ID
     *  @throws SQLException SQL Exception
     */
    public static void updateAppointment(String title, String description, String location,
                                         String type, Timestamp start, Timestamp end, int customerId,
                                         int userId, int contactId, int id) throws SQLException {
        String sqlUpdateApt = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?," +
                " Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlUpdateApt);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setInt(7, customerId);
        ps.setInt(8, userId);
        ps.setInt(9, contactId);
        ps.setInt(10, id);
        ps.execute();
    }

    /**
     *  This method Deletes an Appointment in the database
     *  @param x Appointment_ID
     *  @throws SQLException SQL Exception
     */
    public static void deleteAppointment(int x) throws SQLException {
        String sqlDeleteAppointments = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(sqlDeleteAppointments);
        ps1.setInt(1, x);
        ps1.execute();
    }

    /**
     *  LAMBDA - This method generates the Pie Chart Data for Software
     *  type Appointments by Office Location for the third report.
     *  This method includes a for each lambda for looping/filtering
     *  Appointment types to find the Software type which is set
     *  as the SQL query's search parameter.
     *  Lambdas are also used to get the aggregate stream.filter.count() which calculates the
     *  number of appointments for each location.
     *  @return Returns data values to populate the pie chart
     *  @throws SQLException SQL Exception
     */
    public static  ObservableList<PieChart.Data> getPieChartData() throws SQLException {
        int totalCount = 0;
        double countPhoenix;
        double countLondon;
        double countWhitePlains;
        double countMontreal;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        AtomicReference<String> searchType = new AtomicReference<>();

        // FOR EACH LAMBDA
        types.forEach(s -> {if(s.startsWith("Soft")) {searchType.set(s);}});

        String sqlTotalCount = "SELECT COUNT(*) FROM appointments WHERE TYPE = ?";
        PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(sqlTotalCount);
        ps1.setString(1, searchType.get());
        ResultSet rs1 = ps1.executeQuery();
        while (rs1.next()) {
            totalCount = rs1.getInt("COUNT(*)");
        }

        // STREAM.FILTER.COUNT LAMBDA
        countWhitePlains = getAllAppointments().stream().filter(a -> a.getType().equals(searchType.get())
                        && a.getLocation().equals("White Plains")).count();

        // STREAM.FILTER.COUNT LAMBDA
        countPhoenix = getAllAppointments().stream().filter(a -> a.getType().equals(searchType.get())
                && a.getLocation().equals("Phoenix")).count();

        // STREAM.FILTER.COUNT LAMBDA
        countLondon = getAllAppointments().stream().filter(a -> a.getType().equals(searchType.get())
                && a.getLocation().equals("London")).count();

        // STREAM.FILTER.COUNT LAMBDA
        countMontreal = getAllAppointments().stream().filter(a -> a.getType().equals(searchType.get())
                && a.getLocation().equals("Montreal")).count();

        if (countWhitePlains > 0 ) {
            countWhitePlains = (countWhitePlains/totalCount) * 100;
        }
        if (countPhoenix > 0 ) {
            countPhoenix = (countPhoenix/totalCount) * 100;
        }
        if (countLondon > 0 ) {
            countLondon = (countLondon/totalCount) * 100;
        }
        if (countMontreal > 0 ) {
            countMontreal = (countMontreal/totalCount) * 100;
        }
        pieChartData.addAll(new PieChart.Data("Phoenix", countPhoenix),
        new PieChart.Data("White Plains", countWhitePlains),
        new PieChart.Data("Montreal", countMontreal),
        new PieChart.Data("London", countLondon));
        return pieChartData;
    }
}
