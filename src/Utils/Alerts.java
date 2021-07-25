package Utils;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

/** This class houses a collection of Alert methods
 * that are called from multiple controllers. */
public class Alerts {


    public static void missingName() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Customer Name Not Provided");
        alert.setContentText("Please Provide The Customer's Name.");
        alert.showAndWait();
    }

    public static void missingAddress() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Customer Address Not Provided");
        alert.setContentText("Please Provide Customer's Address.");
        alert.showAndWait();
    }

    public static void missingPostal() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Customer Postal Code Not Provided");
        alert.setContentText("Please Provide Customer's Postal Code.");
        alert.showAndWait();
    }

    public static void missingPhone() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("No Phone Number Provided");
        alert.setContentText("Please Provide Customer's Phone Number. Valid Formats Include: " +
                "123-456-7890 OR 11-222-333-4444");
        alert.showAndWait();
    }

    /**
     * This method informs the user that the phone number provided
     *   is incorrectly formatted based on the RegEx comparison.
     */
    public static void invalidPhoneFormat() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Invalid Phone Number Format");
        alert.setContentText("Phone Number Format Not Recognized. Valid Formats Include: " +
                "123-456-7890 OR 11-222-333-4444");
        alert.showAndWait();
    }

    public static void missingDivision() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Customer Division Not Selected");
        alert.setContentText("Please Select Customer's Division from the Drop-Down.");
        alert.showAndWait();
    }

    public static void missingMonth() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Month Not Selected");
        alert.setContentText("Please Select a Month from the Drop-Down.");
        alert.showAndWait();
    }

    public static void missingContactSelection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("No Contact Selected");
        alert.setContentText("Please Select a Contact from the Drop-Down.");
        alert.showAndWait();
    }

    /**
     *  This method is used to indicate to the user no item was selected
     *   for modification or deletion from the main tableviews.
     *   @param itemType Takes the string values Appointment/Customer
     *   @param attemptedAction Takes the string values Modify/Delete
     */
    public static void noItemSelectedToModifyOrDelete(String itemType, String attemptedAction) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("No " + itemType + " Selected");
        alert.setContentText("Please Select the " + itemType + " You Want to " + attemptedAction);
        alert.showAndWait();
    }

    public static void missingType() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Appointment Type Not Selected");
        alert.setContentText("Please Select an Appointment Type from the Drop-Down.");
        alert.showAndWait();
    }

    /**
     *  This method alerts the user that a required ComboBox
     *  item was not provided for the Appointment.
     * @param missingItem the missing ComboBox item
     */
    public static void missingAppointmentSelection(String missingItem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Appointment " + missingItem + " Not Selected");
        alert.setContentText("Please Select a/an " + missingItem + " from the Drop-Down.");
        alert.showAndWait();
    }

    /**
     *  This method alerts the user that the selected Appointment date/time
     *   isn't within the business hour constraints in EST
     *   and can't be scheduled/re-scheduled for that time.
     */
    public static void outsideOfBusinessHours() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Appointment Time is Outside of Business Hours");
        alert.setContentText("Appointments Must be Scheduled Between the Hours of 8AM - 10PM EST");
        alert.showAndWait();
    }

    /**
     *  This method alerts the user of conflicting appointment times for the customer
     *   when attempting to Add or Modify an appointment.
     */
    public static void appointmentConflict() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.NONE);
        alert.setHeaderText("Appointment Conflict");
        alert.setContentText("Appointment Time Conflicts with Pre-existing Appointment for the Selected Customer");
        alert.showAndWait();
    }
}
