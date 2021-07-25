package Controller;

import DAO.DBAppointments;
import Model.Appointment;
import Utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * View Appointments screen. */
public class ViewAppointments implements Initializable {


    Appointment appointmentToModify;
    @FXML private ComboBox filterAppointmentsComboBox;
    private static ObservableList<String> filterOptions = FXCollections.observableArrayList();
    @FXML private TableView<Appointment> appointmentTableView;
    private ObservableList<Appointment> existingAppointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        weeklyAppointments.clear();
        monthlyAppointments.clear();
        try {
            populateAppointmentTableView();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (filterOptions.isEmpty()) {
            filterOptions.addAll("All", "Weekly View", "Monthly View");
        }
        filterAppointmentsComboBox.setItems(filterOptions);
        filterAppointmentsComboBox.setValue(filterOptions.get(0));
    }


    /**
     *  This method populates the Appointments TableView
     *  with all Appointments in the database.
     */
    @FXML
    private void populateAppointmentTableView() throws SQLException {
        existingAppointments.setAll(DBAppointments.getAllAppointments());
        appointmentTableView.setItems(existingAppointments);
        appointmentTableView.refresh();
    }

    /**
     *  This method allows the User to filter the Appointments TableView
     *  by Week, Month, or All based on the ComboBox selection.
     */
    public void handleFilterSelection() {
        String filterBy = filterAppointmentsComboBox.getSelectionModel().getSelectedItem().toString();
        switch (filterBy) {
            case "Weekly View":
                weeklyAppointments = existingAppointments.filtered(apt -> apt.getStartTime().isAfter(LocalDateTime.now())
                        && apt.getStartTime().isBefore(LocalDateTime.now().plusDays(7)));
                appointmentTableView.setItems(weeklyAppointments);
                appointmentTableView.refresh();
                break;
            case "Monthly View":
                monthlyAppointments =
                        existingAppointments.filtered(apt -> apt.getStartTime().getMonth() == LocalDateTime.now().getMonth());
                appointmentTableView.setItems(monthlyAppointments);
                appointmentTableView.refresh();
                break;
            case "All":
                appointmentTableView.setItems(existingAppointments);
                appointmentTableView.refresh();
            default:
        }
    }

    /**
     *  This method validates the User has selected an Appointment from the TableView
     *  to Modify. If an Appointment is selected, the Modify Appointment screen is populated
     *  with that Appointment's information. Else, the User is Alerted.
     *  @param actionEvent User clicks or keys the Modify button
     */
    public void handleModifyButton(ActionEvent actionEvent) {
        try{
            appointmentToModify = appointmentTableView.getSelectionModel().getSelectedItem();
            if (!existingAppointments.isEmpty() && appointmentToModify != null) {
                DBAppointments.setModifyAppointment(appointmentToModify);
                Parent root = FXMLLoader.load(Login.class.getResource("/View/ModifyAppointment.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                stage.setScene(scene);
                stage.show();
            }
            else {
                Alerts.noItemSelectedToModifyOrDelete("Appointment", "Modify");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method Validates an Appointment was selected to be deleted.
     *  If valid, a confirmation is required from the User to proceed.
     *  Otherwise, the user is Alerted no Appointment was selected.
     *  @throws SQLException SQL Exception
     */
    public void handleDeleteButton() throws SQLException {
        Appointment appointmentToDelete = appointmentTableView.getSelectionModel().getSelectedItem();
        if (appointmentToDelete != null) {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setHeaderText("Do You Want to Delete Appointment ID: " +
                    appointmentToDelete.getAppointmentId() + " Appointment Type: " +
                    appointmentToDelete.getType() + " From the System?");
            confirmDelete.setContentText("Click 'OK' to Confirm Appointment Deletion");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DBAppointments.deleteAppointment(appointmentToDelete.getAppointmentId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Appointment Deleted Successfully");
                alert.setContentText("Appointment ID: " + appointmentToDelete.getAppointmentId() +
                        " Appointment Type: " + appointmentToDelete.getType() + " Deleted");
                alert.show();
                populateAppointmentTableView();
            }
        }
        else {
            Alerts.noItemSelectedToModifyOrDelete("Appointment","Delete");
        }
    }

    /**
     *  This method takes the user back to the
     *  Main/Home screen.
     *  @param actionEvent Home button pressed or clicked
     */
    @FXML
    private void handleHomeButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Login.loadMainScreen();
    }
}
