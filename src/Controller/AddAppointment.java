package Controller;

import DAO.*;
import Utils.Alerts;
import Utils.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * Add Appointment screen. */
public class AddAppointment implements Initializable {


    private String type;
    private String title;
    private String contact;
    private String location;
    private String description;
    private Integer customerId;

    private boolean isTypeValid;
    private boolean isDateValid;
    private boolean isTitleValid;
    private boolean isContactValid;
    private boolean isEndTimeValid;
    private boolean isStartTimeValid;
    private boolean isLocationValid;
    private boolean isCustomerIdValid;
    private boolean isDescriptionValid;
    private boolean isWithinBusinessHours;

    private ZonedDateTime startOfDayLocal;
    private ZonedDateTime endOfDayLocal;
    private LocalDate dateSelected;
    private LocalTime aptStartLocalTime;
    private LocalDateTime aptStartLocalDateTime;
    private Timestamp aptStartTimestamp;
    private LocalTime aptEndLocalTime;
    private LocalDateTime aptEndLocalDateTime;
    private Timestamp aptEndTimestamp;

    @FXML private ComboBox<String> titleComboBox;
    @FXML private ComboBox<String> descriptionComboBox;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> contactComboBox;
    @FXML private ComboBox<String> startTimeComboBox;
    @FXML private ComboBox<String> endTimeComboBox;
    @FXML private ComboBox<Integer> customerIdComboBox;
    @FXML private DatePicker datePicker;

    ObservableList<String> timeOptions = FXCollections.observableArrayList();
    ObservableList<String> typeOptions = FXCollections.observableArrayList();
    ObservableList<String> titleOptions = FXCollections.observableArrayList();
    ObservableList<String> locationOptions = FXCollections.observableArrayList();
    ObservableList<String> descriptionOptions = FXCollections.observableArrayList();
    ObservableList<String> contactOptions = FXCollections.observableArrayList();
    ObservableList<Integer> customerIds = FXCollections.observableArrayList();


    /**
     *  This method initializes the Add Appointment screen and pre-populates the
     *  ComboBox Items.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startTimeComboBox.setItems(timeOptions = DBAppointments.appointmentTimes);
        endTimeComboBox.setItems(timeOptions);
        typeComboBox.setItems(typeOptions = DBAppointments.types);
        titleComboBox.setItems(titleOptions = DBAppointments.titles);
        descriptionComboBox.setItems(descriptionOptions = DBAppointments.descriptions);
        locationComboBox.setItems(locationOptions = DBAppointments.locations);
        try {
            customerIdComboBox.setItems(customerIds = DBCustomers.getAllCustomerIds());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
           contactComboBox.setItems(contactOptions = DBContacts.getContacts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void verifyLocation() {
        if (locationComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Location");
        } else {
            isLocationValid = true;
            location = locationComboBox.getValue();
        }
    }

    public void verifyTitle() {
        if (titleComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Title");
        } else {
            isTitleValid = true;
            title = titleComboBox.getValue();
        }
    }

    public void verifyType() {
        if (typeComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Type");
        } else {
            isTypeValid = true;
            type = typeComboBox.getValue();
        }
    }

    public void verifyDescription() {
        if (descriptionComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Description");
        } else {
            isDescriptionValid = true;
            description = descriptionComboBox.getValue();
        }
    }

    public void verifyCustomerId() {
        if (customerIdComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Customer ID");
        } else {
            isCustomerIdValid = true;
            customerId = customerIdComboBox.getValue();
        }
    }

    public void verifyContact() {
        if (contactComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Contact");
        } else {
            isContactValid = true;
            contact = contactComboBox.getValue();
        }
    }

    public void verifyDateSelected() {
        if (datePicker.getValue() == null) {
            Alerts.missingAppointmentSelection("Date");
        } else {
            isDateValid = true;
            dateSelected = datePicker.getValue();
            ZonedDateTime startOfDayEST = ZonedDateTime.of(dateSelected.getYear(), dateSelected.getMonthValue(),
                    dateSelected.getDayOfMonth(), 8, 0, 0, 0, ZoneId.of("America/New_York"));
            startOfDayLocal = startOfDayEST.withZoneSameInstant(ZoneId.systemDefault());
            ZonedDateTime endOfDayEST = ZonedDateTime.of(dateSelected.getYear(), dateSelected.getMonthValue(),
                    dateSelected.getDayOfMonth(), 22, 0, 0, 0, ZoneId.of("America/New_York"));
            endOfDayLocal = endOfDayEST.withZoneSameInstant(ZoneId.systemDefault());
        }
    }

    public void verifyStartTimeSelected() {
        if (startTimeComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Start Time");
        } else {
            isStartTimeValid = true;
            // GENERATE TIMESTAMP OF START IN LOCAL TIME
            aptStartLocalTime = LocalTime.parse(startTimeComboBox.getValue());
            aptStartLocalDateTime = LocalDateTime.of(dateSelected, aptStartLocalTime);
            aptStartTimestamp = Timestamp.valueOf(aptStartLocalDateTime);
        }
    }

    public void verifyEndTimeSelected() {
        if (endTimeComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("End Time");
        } else {
            isEndTimeValid = true;
            // GENERATE TIMESTAMP OF END IN LOCAL TIME
            aptEndLocalTime = LocalTime.parse(endTimeComboBox.getValue());
            aptEndLocalDateTime = LocalDateTime.of(dateSelected, aptEndLocalTime);
            aptEndTimestamp = Timestamp.valueOf(aptEndLocalDateTime);
        }
    }

    /**
     *  This method Inserts all of the Appointment information into the database
     *  if all validations are met. Including dates/times are within office hours
     *  and no conflicting appointments are scheduled for the same customer.
     *  Then, returns to the Main/Home screen.
     *  @param actionEvent User clicks the save button
     *  @throws SQLException SQL Exception
     */
    public void handleSaveButton(ActionEvent actionEvent) throws SQLException {
        verifyTitle();
        if (isTitleValid) {
            verifyType();
        }
        if (isTypeValid && isTitleValid) {
            verifyDescription();
        }
        if (isDescriptionValid && isTypeValid && isTitleValid) {
            verifyLocation();
        }
        if (isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyContact();
        }
        if (isContactValid && isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyCustomerId();
        }
        if (isCustomerIdValid && isContactValid && isLocationValid
                && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyDateSelected();
        }
        if (isDateValid && isCustomerIdValid && isContactValid && isLocationValid
                && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyStartTimeSelected();
        }
        if (isStartTimeValid && isDateValid && isCustomerIdValid && isContactValid
                && isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyEndTimeSelected();
        }
        if (isEndTimeValid && isStartTimeValid && isDateValid && isCustomerIdValid && isContactValid
                && isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            if (aptStartLocalTime.isBefore(LocalTime.from(startOfDayLocal)) ||
                    aptEndLocalTime.isAfter(LocalTime.from(endOfDayLocal))) {
                Alerts.outsideOfBusinessHours();
            } else {
                isWithinBusinessHours = true;
            }
        }
        if (isWithinBusinessHours && isEndTimeValid && isStartTimeValid && isDateValid && isCustomerIdValid
                && isContactValid && isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            if (DBAppointments.appointmentConflictExists(customerId,0, aptStartLocalDateTime, aptEndLocalDateTime)) {
                Alerts.appointmentConflict();
            } else {
                int contactId = DBContacts.contactDictionary.get(contact);
                DBAppointments.insertAppointment(title, description, location, type, aptStartTimestamp, aptEndTimestamp,
                        customerId, Config.getCurrentUserId(), contactId);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                Login.loadMainScreen();
            }
        }
    }

    /**
     *  This method prompts the User to confirm the UI request to Cancel
     *  Adding the Appointment and returns to the Main/Home screen upon confirmation.
     *  @param actionEvent User clicks or keys the Cancel button
     */
    public void handleCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Cancel Adding Appointment");
        alert.setContentText("Do You Want To Cancel Adding This Appointment?" +
                " Click 'OK' to Confirm & Return to Main Menu.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Login.loadMainScreen();
        }
    }
}
