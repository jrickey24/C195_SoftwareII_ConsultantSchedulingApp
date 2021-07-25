package Controller;

import DAO.*;
import Utils.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * Modify Appointment screen. */
public class ModifyAppointment implements Initializable {

    private boolean isStartTimeValid;
    private boolean isEndTimeValid;
    private boolean isTitleValid;
    private boolean isTypeValid;
    private boolean isDescriptionValid;
    private boolean isLocationValid;
    private boolean isUserIdValid;
    private boolean isCustomerIdValid;
    private boolean isContactValid;
    private boolean isDateValid;
    private boolean isWithinBusinessHours;

    private String updatedLocation;
    private String updatedType;
    private String updatedTitle;
    private String updatedDescription;
    private String updatedContact;
    private Integer updatedUserId;
    private Integer updatedCustomerId;
    private Integer id;

    private ZonedDateTime startOfDayLocal;
    private ZonedDateTime endOfDayLocal;
    private LocalDate dateSelected;
    private LocalTime aptStartLocalTime;
    private LocalDateTime aptStartLocalDateTime;
    private Timestamp aptStartTimestamp;
    private LocalTime aptEndLocalTime;
    private LocalDateTime aptEndLocalDateTime;
    private Timestamp aptEndTimestamp;

    @FXML private TextField appointmentIdTextField;
    @FXML private ComboBox<String> modifyStartTimeComboBox;
    @FXML private ComboBox<String> modifyEndTimeComboBox;
    @FXML private ComboBox<String> modifyTypeComboBox;
    @FXML private ComboBox<String> modifyLocationComboBox;
    @FXML private ComboBox<String> modifyDescriptionComboBox;
    @FXML private ComboBox<String> modifyTitleComboBox;
    @FXML private ComboBox<String> modifyContactComboBox;
    @FXML private ComboBox<Integer> modifyCustomerIdComboBox;
    @FXML private ComboBox<Integer> modifyUserIdComboBox;
    @FXML private DatePicker datePicker;

    ObservableList<String> timeOptions = FXCollections.observableArrayList();
    ObservableList<String> typeOptions = FXCollections.observableArrayList();
    ObservableList<String> titleOptions = FXCollections.observableArrayList();
    ObservableList<String> locationOptions = FXCollections.observableArrayList();
    ObservableList<String> descriptionOptions = FXCollections.observableArrayList();
    ObservableList<String> contactOptions = FXCollections.observableArrayList();
    ObservableList<Integer> userIds = FXCollections.observableArrayList();
    ObservableList<Integer> customerIds = FXCollections.observableArrayList();

    /**
     *  This method initializes the Modify Appointment screen
     *  and pre-populates the fields with the provided Appointment's data.
     *  @param url URL
     *  @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyStartTimeComboBox.setItems(timeOptions = DBAppointments.appointmentTimes);
        modifyEndTimeComboBox.setItems(timeOptions);
        modifyTypeComboBox.setItems(typeOptions = DBAppointments.types);
        modifyTitleComboBox.setItems(titleOptions = DBAppointments.titles);
        modifyDescriptionComboBox.setItems(descriptionOptions = DBAppointments.descriptions);
        modifyUserIdComboBox.setItems(userIds = DBUsers.usersList);
        modifyLocationComboBox.setItems(locationOptions = DBAppointments.locations);
        try {
            modifyCustomerIdComboBox.setItems(customerIds = DBCustomers.getAllCustomerIds());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            modifyContactComboBox.setItems(contactOptions = DBContacts.getContacts());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        modifyContactComboBox.setValue(DBAppointments.getAppointmentToModify().getContactName());
        id = DBAppointments.getAppointmentToModify().getAppointmentId();
        appointmentIdTextField.setText(String.valueOf(id));
        modifyTypeComboBox.setValue(DBAppointments.getAppointmentToModify().getType());
        modifyTitleComboBox.setValue(DBAppointments.getAppointmentToModify().getTitle());
        modifyDescriptionComboBox.setValue(DBAppointments.getAppointmentToModify().getDescription());
        modifyLocationComboBox.setValue(DBAppointments.getAppointmentToModify().getLocation());
        modifyUserIdComboBox.setValue(DBAppointments.getAppointmentToModify().getUserId());
        modifyCustomerIdComboBox.setValue(DBAppointments.getAppointmentToModify().getCustomerId());
        modifyStartTimeComboBox.setValue(DBAppointments.getAppointmentToModify().
                getStartTime().toString().substring(11,16));
        modifyEndTimeComboBox.setValue(DBAppointments.getAppointmentToModify().
                getEndTime().toString().substring(11,16));
        datePicker.setValue(DBAppointments.getAppointmentToModify().getStartTime().toLocalDate());
    }

    public void verifyLocation() {
        if (modifyLocationComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Location");
        } else {
            isLocationValid = true;
            updatedLocation = modifyLocationComboBox.getValue();
        }
    }

    public void verifyTitle() {
        if (modifyTitleComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Title");
        } else {
            isTitleValid = true;
            updatedTitle = modifyTitleComboBox.getValue();
        }
    }

    public void verifyType() {
        if (modifyTypeComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Type");
        } else {
            isTypeValid = true;
            updatedType = modifyTypeComboBox.getValue();
        }
    }

    public void verifyDescription() {
        if (modifyDescriptionComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Description");
        } else {
            isDescriptionValid = true;
            updatedDescription = modifyDescriptionComboBox.getValue();
        }
    }

    public void verifyCustomerId() {
        if (modifyCustomerIdComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Customer ID");
        } else {
            isCustomerIdValid = true;
            updatedCustomerId = modifyCustomerIdComboBox.getValue();
        }
    }

    public void verifyUserId() {
        if (modifyUserIdComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("User ID");
        } else {
            isUserIdValid = true;
            updatedUserId = modifyUserIdComboBox.getValue();
        }
    }

    public void verifyContact() {
        if (modifyContactComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Contact");
        } else {
            isContactValid = true;
            updatedContact = modifyContactComboBox.getValue();
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
        if (modifyStartTimeComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("Start Time");
        } else {
            isStartTimeValid = true;
            // GENERATE TIMESTAMP OF START IN LOCAL TIME
            aptStartLocalTime = LocalTime.parse(modifyStartTimeComboBox.getValue());
            aptStartLocalDateTime = LocalDateTime.of(dateSelected, aptStartLocalTime);
            aptStartTimestamp = Timestamp.valueOf(aptStartLocalDateTime);
        }
    }

    public void verifyEndTimeSelected() {
        if (modifyEndTimeComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingAppointmentSelection("End Time");
        } else {
            isEndTimeValid = true;
            // GENERATE TIMESTAMP OF END IN LOCAL TIME
            aptEndLocalTime = LocalTime.parse(modifyEndTimeComboBox.getValue());
            aptEndLocalDateTime = LocalDateTime.of(dateSelected, aptEndLocalTime);
            aptEndTimestamp = Timestamp.valueOf(aptEndLocalDateTime);
        }
    }

    /**
     *  This method Updates the Appointment information in the database
     *  if all validations are met. Then, returns to the Main/Home screen.
     *  @param actionEvent User clicks or keys the save button
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
            verifyUserId();
        }
        if (isUserIdValid && isCustomerIdValid && isContactValid && isLocationValid
                && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyDateSelected();
        }
        if (isDateValid && isUserIdValid && isCustomerIdValid && isContactValid && isLocationValid
                && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyStartTimeSelected();
        }
        if (isStartTimeValid && isDateValid && isUserIdValid && isCustomerIdValid && isContactValid
                && isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            verifyEndTimeSelected();
        }
        if (isEndTimeValid && isStartTimeValid && isDateValid && isUserIdValid && isCustomerIdValid && isContactValid
                && isLocationValid && isDescriptionValid && isTypeValid && isTitleValid) {
            // VERIFY START & END TIMES ARE WITHIN BUSINESS HOURS
            if (aptStartLocalTime.isBefore(LocalTime.from(startOfDayLocal)) ||
                    aptEndLocalTime.isAfter(LocalTime.from(endOfDayLocal))) {
                Alerts.outsideOfBusinessHours();
            } else {
                isWithinBusinessHours = true;
            }
        }
        if (isWithinBusinessHours && isEndTimeValid && isStartTimeValid && isDateValid && isUserIdValid &&
                isCustomerIdValid && isContactValid && isLocationValid && isDescriptionValid
                && isTypeValid && isTitleValid) {
            // VERIFY NO OVERLAPPING APT OPTIONS FOR SELECTED DATE & TIME
            if (DBAppointments.appointmentConflictExists(updatedCustomerId,id,
                    aptStartLocalDateTime, aptEndLocalDateTime)){
                Alerts.appointmentConflict();
            } else {
                int updatedContactId = DBContacts.contactDictionary.get(updatedContact);
                DBAppointments.updateAppointment(updatedTitle, updatedDescription, updatedLocation,
                        updatedType, aptStartTimestamp, aptEndTimestamp, updatedCustomerId,
                        updatedUserId, updatedContactId, id);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                Login.loadMainScreen();
            }
        }
    }

    /**
     *  This method prompts the User to confirm the UI request to Cancel
     *  Modifying the Appointment and returns to the Main/Home screen upon confirmation.
     *  @param actionEvent User clicks or keys the Cancel button
     */
    public void handleCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Cancel Modifying Appointment");
        alert.setContentText("Do You Want To Cancel Modifying the Appointment?" +
                " Click 'OK' to Confirm & Return to Main Menu.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Login.loadMainScreen();
        }
    }
}