package Controller;

import DAO.DBCustomers;
import Model.Customer;
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
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * View Customers screen. */
public class ViewCustomers implements Initializable {


    Customer customerToModify;
    @FXML private TableView<Customer> customerTableView;
    private ObservableList<Customer> existingCustomers = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateCustomersTableView();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *  This method populates the Customers TableView
     *  with all Customers in the database.
     */
    @FXML
    private void populateCustomersTableView() throws SQLException {
        existingCustomers.setAll(DBCustomers.getAllCustomers());
        customerTableView.setItems(existingCustomers);
        customerTableView.refresh();
    }

    /**
     *  This method validates the User has selected a Customer from the TableView
     *  to Modify. If a Customer is selected, the Modify Customer screen is populated
     *  with that Customer's information. Else, the User is Alerted.
     *  @param actionEvent User clicks or keys the Modify button
     *  @throws IOException IO Exception
     */
    @FXML
    public void handleModifyButton(ActionEvent actionEvent) throws IOException {
        try{
         customerToModify = customerTableView.getSelectionModel().getSelectedItem();
            if (!existingCustomers.isEmpty() && customerToModify != null) {
                DBCustomers.setModifyCustomer(customerToModify);
                Parent root = FXMLLoader.load(Login.class.getResource("/View/ModifyCustomer.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                stage.setScene(scene);
                stage.show();
            }
            else {
                Alerts.noItemSelectedToModifyOrDelete("Customer", "Modify");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  This method Validates a Customer was selected to be deleted.
     *  If valid, a confirmation is required from the User to proceed.
     *  Otherwise, the user is Alerted no Customer was selected.
     */
    @FXML
    private void handleDeleteButton(ActionEvent actionEvent) throws SQLException {
        Customer customerToDelete = customerTableView.getSelectionModel().getSelectedItem();
        if (customerToDelete != null) {
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setHeaderText("Do You Want to Delete " + customerToDelete.getName() + " From the System?");
            confirmDelete.setContentText("Click 'OK' to Confirm Customer Deletion");
            Optional<ButtonType> result = confirmDelete.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DBCustomers.deleteCustomer(customerToDelete.getCustomerId());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Customer Deleted Successfully");
                alert.setContentText("Customer: " + customerToDelete.getName() + " Deleted");
                alert.show();
                populateCustomersTableView();
            }
        }
        else {
            Alerts.noItemSelectedToModifyOrDelete("Customer", "Delete");
        }
    }

    /**
     *  This method takes the user back to the
     *  Main/Home screen.
     */
    @FXML
    private void handleHomeButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Login.loadMainScreen();
    }
}
