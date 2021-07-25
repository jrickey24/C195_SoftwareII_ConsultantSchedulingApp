package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBUsers {

    public static ObservableList<Integer> usersList = FXCollections.observableArrayList();

    /**
     *  This method populates the Users list with all
     *  User_IDs in the database. Invoked in Main as
     *  Users aren't updated in the application.
     *  @throws SQLException SQL Exception
     */
    public static void loadUsers() throws SQLException {
        String sqlGetUsers = "SELECT User_ID FROM users ORDER BY User_ID asc";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlGetUsers);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            usersList.add(rs.getInt("User_ID"));
        }
    }
}
