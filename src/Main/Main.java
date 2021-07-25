package Main;

import DAO.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;


public class Main extends Application {

    /**
     *  This method launches the UI Login screen passing in the Resource Bundle
     *  and the login properties for French or English translation.
     */
    @Override
    public void start(Stage stage) throws Exception{
        ResourceBundle resources = ResourceBundle.getBundle("View/login", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/View/Login.fxml"),resources);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        DBConnection.startConnection();
        DBCountries.loadCountryDictionary();
        DBAppointments.loadMonthsDictionary();
        DBContacts.loadContactDictionary();
        DBAppointments.loadTypes();
        DBAppointments.loadTitles();
        DBAppointments.loadLocations();
        DBAppointments.loadDescriptions();
        DBAppointments.loadAppointmentTimes();
        DBUsers.loadUsers();
        launch(args);
        DBConnection.closeConnection();
    }
}
