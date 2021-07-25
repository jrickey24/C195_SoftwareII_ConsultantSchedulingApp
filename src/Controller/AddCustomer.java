package Controller;

import DAO.*;
import Utils.Alerts;
import Utils.Config;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * Add Customer screen. */
public class AddCustomer implements Initializable {

    private boolean isNameValid;
    private boolean isAddressValid;
    private boolean isPostalValid;
    private boolean isPhoneValid;
    private boolean isDivisionValid;
    private boolean customerAdded;
    private String userInputName;
    private String userInputAddress;
    private String userInputPostal;
    private String userInputPhone;

    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField zipCodeTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private ComboBox<String> divisionComboBox;


    /**
     *  This method initializes the Add Customer screen and pre-populates the Division ComboBox selections
     *  with U.S. Divisions and sets the Country RadioButton selected as USA.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTextField.clear();
        addressTextField.clear();
        zipCodeTextField.clear();
        phoneNumberTextField.clear();
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(1));
    }



    public void handleUSASelected() {
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(1));
    }

    public void handleUKSelected() {
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(2));
    }

    public void handleCanadaSelected() {
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(3));
    }

    public void verifyName() {
        if(nameTextField.getText().trim().isEmpty() || "".equals(nameTextField.getText())) {
            Alerts.missingName();
        } else {
            userInputName = nameTextField.getText().trim();
            isNameValid = true;
        }
    }

    public void verifyAddress() {
        if(addressTextField.getText().trim().isEmpty() || "".equals(addressTextField.getText().trim())) {
            Alerts.missingAddress();
        } else{
            userInputAddress = addressTextField.getText().trim();
            isAddressValid = true;
        }
    }

    public void verifyPostal() {
        if(zipCodeTextField.getText().trim().isEmpty() || "".equals(zipCodeTextField.getText().trim())) {
            Alerts.missingPostal();
        } else{
            userInputPostal = zipCodeTextField.getText().trim();
            isPostalValid = true;
        }
    }

    public void verifyPhone() {
        if(phoneNumberTextField.getText().trim().isEmpty() || "".equals(phoneNumberTextField.getText())) {
            Alerts.missingPhone();
        } else {
            userInputPhone = phoneNumberTextField.getText().trim();
            if(!Config.isPhoneNumberValid(userInputPhone)) {
                Alerts.invalidPhoneFormat();
            }
            else {
                isPhoneValid = true;
            }
        }
    }

    public void verifyDivision() {
        if (divisionComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingDivision();
        } else {
            isDivisionValid = true;
        }
    }

    /**
     *  This method Inserts all of the Customer information into the database
     *  if all validations are met. Then, returns to the Main/Home screen.
     *  @param actionEvent User clicks or keys the add/save button
     */
    @FXML
    public void handleAddCustomerButton(ActionEvent actionEvent)  {
        verifyName();
        if (isNameValid) {
            verifyAddress();
        }
        if(isNameValid && isAddressValid) {
            verifyPostal();
        }
        if(isNameValid && isAddressValid && isPostalValid) {
           verifyPhone();
        }
        if(isNameValid && isAddressValid && isPhoneValid && isPostalValid) {
            verifyDivision();
        }
        // ADD THE CUSTOMER IF VALIDATIONS MET & RETURN TO MAIN SCREEN //
        if (isNameValid && isAddressValid && isPhoneValid && isPostalValid && isDivisionValid) {
            String userInputDivision = divisionComboBox.getSelectionModel().getSelectedItem();
            int customerDivision = DBFirstLevelDivisions.getDivisionId(userInputDivision);
            try {
                DBCustomers.insertCustomer(userInputName,userInputAddress,userInputPostal,userInputPhone,customerDivision);
                customerAdded = true;
            } catch(SQLException throwables){
                throwables.printStackTrace();
            }
            if (customerAdded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Customer Added Successfully");
                alert.setContentText(userInputName + " Was Added To Customers. Click 'OK' to Return to Main Menu");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                    Login.loadMainScreen();
                }
            }
        }
    }

    /**
     *  This method prompts the User to confirm the UI request to Cancel
     *  Adding the Customer and returns to the Main/Home screen upon confirmation.
     *  @param actionEvent User clicks or keys the Cancel button
     */
    @FXML
    public void handleCancelAddCustomer(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Cancel Adding Customer");
        alert.setContentText("Do You Want To Cancel Adding Customer? Click 'OK' to Confirm & Return to Main Menu.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Login.loadMainScreen();
        }
    }
}
