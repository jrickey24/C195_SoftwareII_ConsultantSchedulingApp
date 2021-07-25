package Controller;

import DAO.DBAppointments;
import Model.Appointment;
import Utils.Config;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;


/** This Controller class Initializes
 *  the Login Screen. */
public class Login implements Initializable {

    String invalidUsernameHeader;
    String invalidUsernameContent;
    String invalidPasswordHeader;
    String invalidPasswordContent;
    String exitConfirmationHeader;
    String exitConfirmationContent;

    @FXML private Label zoneIdLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;


    /**
     *  This method initializes the Login Screen using the login properties resource
     *  to populate labels, Alerts, and ZoneID values based on the
     *  System default Locale fr, or en.
     *  @param url URL
     *  @param resourceBundle RB
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usernameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        invalidUsernameHeader = resourceBundle.getString("invalidUsernameHeader");
        invalidUsernameContent = resourceBundle.getString("invalidUsernameContent");
        invalidPasswordHeader = resourceBundle.getString("invalidPasswordHeader");
        invalidPasswordContent = resourceBundle.getString("invalidPasswordContent");
        exitConfirmationHeader = resourceBundle.getString("exitConfirmationHeader");
        exitConfirmationContent = resourceBundle.getString("exitConfirmationContent");
        zoneIdLabel.setText(ZoneId.systemDefault().toString());
    }

    /**
     *  This method loads the Main/Home
     *  screen of the UI upon successful Login.
     */
    @FXML
    public static void loadMainScreen(){
        try {
            Parent root = FXMLLoader.load(Login.class.getResource("/View/MainScreen.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     *  This method prompts the User to confirm the UI request to Exit the system
     *  & Exits upon confirmation.
     */
    @FXML
    void handleExitButton() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initModality(Modality.NONE);
        alert.setHeaderText(exitConfirmationHeader + "?");
        alert.setContentText(exitConfirmationContent);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     *  This method authenticates the User's Login attempt.
     *  It substrings the Username & Password if longer than 10 digits.
     *  This is done within a Try/Catch block which ignores the null case.
     *  Next, the credentials are verified from the database & logged to
     *  login_activity.txt. If a credential is invalid, the user is notified
     *  and the invalid credential value is logged. In the case where both
     *  credentials are incorrect or null, the incorrect Username takes precedence
     *  as the logged value. If the login is successful, the UI navigates to the
     *  Main/Home screen and the User is notified of either:
     *  1. Appointment scheduled to start within the next 15 minutes of their local time.
     *  2. No upcoming Appointment scheduled.
     *  @param actionEvent Submit button is pressed or clicked
     */
    @FXML
    void handleSubmitButton(ActionEvent actionEvent) {
        String userInputUsername = usernameTextField.getText().trim();
        String userInputPassword = passwordTextField.getText().trim();
        try {
            int usernameInputLength = userInputUsername.length();
            int passwordInputLength = userInputPassword.length();
            if (usernameInputLength > 10) {
                userInputUsername = userInputUsername.substring(0, 9);
            }
            if (passwordInputLength > 10) {
                userInputPassword = userInputPassword.substring(0, 9);
            }
        } catch (Exception ignored) {}
        if (userInputUsername.isEmpty() || !userInputUsername.equals(Config.getAppUsername())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(invalidUsernameHeader);
            alert.setContentText(invalidUsernameContent);
            alert.showAndWait();
            try {
                Config.writeLoginActivity(false, "INVALID USERNAME." +
                        " PROVIDED USERNAME: " + userInputUsername);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        if (userInputPassword.isEmpty() || !userInputPassword.equals(Config.getAppPassword())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(invalidPasswordHeader);
            alert.setContentText(invalidPasswordContent);
            alert.showAndWait();
            try {
                Config.writeLoginActivity(false, "INVALID PASSWORD." +
                        " PROVIDED PASSWORD: " + userInputPassword);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else {
            try {
                Config.writeLoginActivity(true, "NONE.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            try {
                Appointment checkAppointment = DBAppointments.checkUpcomingAppointment();
                if (checkAppointment.getAppointmentId() == 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointment(s)");
                    alert.setHeaderText("No Appointment(s) Found");
                    alert.setContentText("There are no appointments in the next 15 minutes.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        loadMainScreen();
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Upcoming Appointment(s)");
                    alert.setHeaderText("Found Appointment(s)");
                    alert.setContentText(checkAppointment.getType() + " Appointment Scheduled: " +
                            checkAppointment.getStartTime().toLocalDate().getMonthValue() + "-" +
                            checkAppointment.getStartTime().toLocalDate().getDayOfMonth() + "-" +
                            checkAppointment.getStartTime().toLocalDate().getYear() +
                            " From: " + checkAppointment.getStartTime().toLocalTime() +
                                    "-" + checkAppointment.getEndTime().toLocalTime() +
                            ". Appointment ID: " + checkAppointment.getAppointmentId());
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                        loadMainScreen();
                    }
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
