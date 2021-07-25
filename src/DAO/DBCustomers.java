package DAO;

import Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class DBCustomers {


    private static Customer c;
    public static void setModifyCustomer(Customer customer){
        c = customer;
    }
    public static Customer getCustomerToModify(){
        return c;
    }

    /**
     *  This method Inserts a new Customer into the database
     *  @param a Customer_Name
     *  @param b Address
     *  @param c Postal_Code
     *  @param d Phone
     *  @param e Division_ID
     *  @throws SQLException SQL Exception
     */
    public static void insertCustomer(String a, String b, String c, String d, int e ) throws SQLException {
        String insertSql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID)" +
                      "VALUES(?,?,?,?,?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(insertSql);
        ps.setString(1, a);
        ps.setString(2, b);
        ps.setString(3, c);
        ps.setString(4, d);
        ps.setInt(5, e);
        ps.execute();
    }

    /**
     *  This method Updates a Customer in the database
     *  @param n Customer_Name
     *  @param a Address
     *  @param po Postal_Code
     *  @param ph Phone
     *  @param x Division_ID
     *  @param y Customer_ID
     *  @throws SQLException SQL Exception
     */
    public static void updateCustomer(String n, String a, String po, String ph, int x, int y) throws SQLException {
        String updateSql = "UPDATE customers SET Customer_Name = ?, Address = ?, " +
                "Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(updateSql);
        ps.setString(1, n);
        ps.setString(2, a);
        ps.setString(3, po);
        ps.setString(4, ph);
        ps.setInt(5, x);
        ps.setInt(6, y);
        ps.execute();
    }

    /**
     *  This method Deletes a Customer from the database
     *  after deleting any relational appointments
     *  @param x Customer_ID
     *  @throws SQLException SQL Exception
     */
    public static void deleteCustomer(int x) throws SQLException {
        String sqlDeleteAppointments = "DELETE FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(sqlDeleteAppointments);
        ps1.setInt(1, x);
        ps1.execute();
        String sqlDeleteCustomer = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps2 = DBConnection.getConnection().prepareStatement(sqlDeleteCustomer);
        ps2.setInt(1,x);
        ps2.execute();
    }

    /**
     *  This method gets all Customers from the database
     *  @return Returns a list of Customers
     *  @throws SQLException SQL Exception
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        String sqlQryAll =
                "SELECT a.Customer_ID, a.Customer_Name, a.Address, a.Postal_Code, a.Phone, a.Division_ID, b.Division," +
                        " c.Country FROM customers a INNER JOIN first_level_divisions b ON b.Division_ID = a.Division_ID" +
                        " INNER JOIN countries c ON c.Country_ID = b.Country_ID ORDER BY a.Customer_ID";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryAll);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postal = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            int divId = rs.getInt("Division_ID");
            String divName = rs.getString("Division");
            String country = rs.getString("Country");
            Customer customer = new Customer(id, name, address, postal, phone, divId, divName, country);
            allCustomers.add(customer);
        }
        return allCustomers;
    }

    /**
     *  This method gets all Customer IDs from the database
     *  @return Returns a list of Customer IDs
     *  @throws SQLException SQL Exception
     */
    public static ObservableList<Integer> getAllCustomerIds() throws SQLException {
        ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();
        String sqlQryIds = "SELECT Customer_ID FROM customers ORDER BY Customer_ID asc";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQryIds);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            allCustomerIds.add(rs.getInt("Customer_ID"));
        }
        return allCustomerIds;
    }
}
