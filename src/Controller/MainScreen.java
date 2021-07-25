package Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/** This class Initializes the Main/Home screen and
 * houses a collection of methods used to set the UI stage based on user selection from the Main/Home Menu. */
public class MainScreen implements Initializable {


   @Override
   public void initialize(URL url, ResourceBundle resourceBundle) {}

   /**
    *  This method sets the stage for the Contact Schedule Report
    *  passing in the Resource Bundle.
    *  @param actionEvent User clicks or keys the Contact Schedule Report button
    */
   @FXML
   public void handleContactScheduleReport(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/ContactScheduleReport.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method sets the stage for the Appointment Type Report
    *  passing in the Resource Bundle.
    *  @param actionEvent User clicks or keys the Appointment Type Report button
    */
   @FXML
   public void handleAppointmentTypeReport(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/AppointmentTypeReport.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method sets the stage for the 3rd Report-A Pie Chart
    *  depicting Software-related appointments grouped by Office location.
    *  @param actionEvent User clicks or keys the Software Appointments By Office button
    */
   @FXML
   public void handleSoftwareAppointmentsByOfficeReport(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/SoftwareAppointmentsByOfficeReport.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method sets the stage for the Add Customer screen
    *  passing in the Resource Bundle.
    *  @param actionEvent User clicks or keys the Add Customer button
    */
   @FXML
   public void handleAddCustomer(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/AddCustomer.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method sets the stage for the View Customer screen
    *  passing in the Resource Bundle.
    *  @param actionEvent User clicks or keys the View Customers button
    */
   @FXML
   public void handleViewCustomer(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/ViewCustomers.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method sets the stage for the Add Appointment screen
    *  passing in the Resource Bundle.
    *  @param actionEvent User clicks or keys the Add Appointment button
    */
   @FXML
   public void handleAddAppointment(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/AddAppointment.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method sets the stage for the View Appointment screen
    *  passing in the Resource Bundle.
    *  @param actionEvent User clicks or keys the View Appointments button
    */
   @FXML
   public void handleViewAppointment(ActionEvent actionEvent) {
      try {
         Parent root = FXMLLoader.load(Login.class.getResource("/View/ViewAppointments.fxml"));
         Stage stage = new Stage();
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
         ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
      } catch (Exception exception){
         exception.printStackTrace();
      }
   }

   /**
    *  This method handles the Exit Button invocation
    *  from the Main/Home screen.
    */
   @FXML
   public void handleExitButton() {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.initModality(Modality.NONE);
      alert.setHeaderText("Do You Want To Exit The System?");
      alert.setContentText("Click OK To EXIT");
      Optional<ButtonType> result = alert.showAndWait();
      if (result.isPresent() && result.get() == ButtonType.OK) {
         Platform.exit();
      }
   }
}

