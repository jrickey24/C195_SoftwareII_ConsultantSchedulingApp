package Controller;

import DAO.DBContacts;
import Model.Appointment;
import Utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * Contact Schedule Report screen. */
public class ContactScheduleReport implements Initializable {

    @FXML private TableView<Appointment> contactScheduleTableView;
    private ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
    @FXML private ComboBox<String> contactNameComboBox;
    String contactSelected = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            contactNameComboBox.setItems(DBContacts.getContacts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handleSubmitButton() throws SQLException {
        if (contactNameComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
            Alerts.missingContactSelection();
        }
        contactSelected = contactNameComboBox.getSelectionModel().getSelectedItem();
        int selectedId = DBContacts.contactDictionary.get(contactSelected);
        contactAppointments.setAll(DBContacts.getContactAppointments(selectedId));
        contactScheduleTableView.setItems(contactAppointments);
        contactScheduleTableView.refresh();
    }

    @FXML
    private void handleHomeButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Login.loadMainScreen();
    }
}
