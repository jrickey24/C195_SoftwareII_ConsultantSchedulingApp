package Controller;

import DAO.DBAppointments;
import Utils.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * Appointment Type Report screen. */
public class AppointmentTypeReport implements Initializable {


    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> monthComboBox;
    @FXML private TextField appointmentTotalTextField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthComboBox.setItems(DBAppointments.getMonths());
        try {
            typeComboBox.setItems(DBAppointments.getAppointmentTypes());
        } catch (SQLException throwables) {
                throwables.printStackTrace();
        }
    }

    /**
     *  This method loads the Main/Home
     *  screen of the UI.
     *  @param actionEvent User clicks or keys the Home button
     */
    public void handleHomeButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Login.loadMainScreen();
    }


    public void handleSubmitButton() throws SQLException {
        if (typeComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
            Alerts.missingType();
        }
        else if (monthComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
            Alerts.missingMonth();
        }
        else {
            String type = typeComboBox.getSelectionModel().getSelectedItem();
            String month = DBAppointments.monthsDictionary.get(monthComboBox.getSelectionModel().getSelectedItem());
            int totalCount = DBAppointments.getAppointmentTypeCountByMonth(type, month);
            appointmentTotalTextField.setText(String.valueOf(totalCount));
        }
    }
}
