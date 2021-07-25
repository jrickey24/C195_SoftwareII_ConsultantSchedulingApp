package Controller;

import DAO.DBCustomers;
import DAO.DBFirstLevelDivisions;
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
 * Modify Customer screen. */
public class ModifyCustomer implements Initializable {


    private boolean isNameValid;
    private boolean isAddressValid;
    private boolean isPostalValid;
    private boolean isPhoneValid;
    private boolean isDivisionValid;
    private boolean customerModified;
    private String userModifiedName;
    private String userModifiedAddress;
    private String userModifiedPostal;
    private String userModifiedPhone;

    @FXML private RadioButton usaRB;
    @FXML private RadioButton ukRB;
    @FXML private RadioButton canadaRB;
    @FXML private TextField customerIdTextField;
    @FXML private TextField modifyNameTextField;
    @FXML private TextField modifyAddressTextField;
    @FXML private TextField modifyPostalTextField;
    @FXML private TextField modifyPhoneTextField;
    @FXML private ComboBox<String> divisionComboBox;

    /**
     *  This method initializes the Modify Customer screen
     *  and pre-populates the fields with the provided Customer's data.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerIdTextField.setText(String.valueOf(DBCustomers.getCustomerToModify().getCustomerId()));
        modifyNameTextField.setText(DBCustomers.getCustomerToModify().getName());
        modifyAddressTextField.setText(DBCustomers.getCustomerToModify().getAddress());
        modifyPostalTextField.setText(DBCustomers.getCustomerToModify().getPostalCode());
        modifyPhoneTextField.setText(DBCustomers.getCustomerToModify().getPhone());
        int divisionId = DBCustomers.getCustomerToModify().getDivisionId();
        int countryId = DBFirstLevelDivisions.getCountryId(divisionId);
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(countryId));
        divisionComboBox.setValue(DBFirstLevelDivisions.getDivision(divisionId));
        switch (countryId) {
            case 1:
                usaRB.setSelected(true);
                break;
            case 2:
                ukRB.setSelected(true);
                break;
            case 3:
                canadaRB.setSelected(true);

                default:
        }
    }



    public void handleUSASelected() {
        divisionComboBox.setValue("");
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(1));
    }

    public void handleUKSelected() {
        divisionComboBox.setValue("");
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(2));
    }

    public void handleCanadaSelected() {
        divisionComboBox.setValue("");
        divisionComboBox.setItems(DBFirstLevelDivisions.getAllDivisions(3));
    }

    public void verifyModifiedName() {
        if(modifyNameTextField.getText().trim().isEmpty() || "".equals(modifyNameTextField.getText())) {
            Alerts.missingName();
        } else {
            userModifiedName = modifyNameTextField.getText().trim();
            isNameValid = true;
        }
    }

    public void verifyModifiedAddress() {
        if(modifyAddressTextField.getText().trim().isEmpty() || "".equals(modifyAddressTextField.getText().trim())) {
            Alerts.missingAddress();
        } else{
            userModifiedAddress = modifyAddressTextField.getText().trim();
            isAddressValid = true;
        }
    }

    public void verifyModifiedPostal() {
        if(modifyPostalTextField.getText().trim().isEmpty() || "".equals(modifyPostalTextField.getText().trim())) {
            Alerts.missingPostal();
        } else{
            userModifiedPostal = modifyPostalTextField.getText().trim();
            isPostalValid = true;
        }
    }

    public void verifyModifiedPhone() {
        if(modifyPhoneTextField.getText().trim().isEmpty() || "".equals(modifyPhoneTextField.getText())) {
            Alerts.missingPhone();
        } else {
            userModifiedPhone = modifyPhoneTextField.getText().trim();
            if(!Config.isPhoneNumberValid(userModifiedPhone)) {
                Alerts.invalidPhoneFormat();
            }
            else {
                isPhoneValid = true;
            }
        }
    }

    public void verifyModifiedDivision() {
        if (divisionComboBox.getSelectionModel().isEmpty()) {
            Alerts.missingDivision();
        } else {
            isDivisionValid = true;
        }
    }

    /**
     *  This method Updates the Customer information in the database
     *  if all validations are met. Then, returns to the Main/Home screen.
     *  @param actionEvent User clicks or keys the save button
     *  @throws SQLException SQL Exception
     */
    @FXML
    public void handleSaveButton(ActionEvent actionEvent) throws SQLException {
        verifyModifiedName();
        if (isNameValid) {
            verifyModifiedAddress();
        }
        if (isNameValid && isAddressValid) {
            verifyModifiedPostal();
        }
        if (isNameValid && isAddressValid && isPostalValid) {
            verifyModifiedPhone();
        }
        if (isNameValid && isAddressValid && isPostalValid && isPhoneValid) {
            verifyModifiedDivision();
        }
        if (isNameValid && isAddressValid && isPhoneValid && isPostalValid && isDivisionValid) {
            String userModifiedDivision = divisionComboBox.getSelectionModel().getSelectedItem();
            int modifiedDivisionId = DBFirstLevelDivisions.getDivisionId(userModifiedDivision);
            try {
                DBCustomers.updateCustomer(userModifiedName, userModifiedAddress, userModifiedPostal,
                        userModifiedPhone, modifiedDivisionId, DBCustomers.getCustomerToModify().getCustomerId());
                customerModified = true;
            } catch(SQLException throwables){
                throwables.printStackTrace();
            }
            if (customerModified) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Customer Modified Successfully");
                alert.setContentText(userModifiedName + " was Updated Successfully. Click 'OK' to Return to Main Menu");
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
     *  Modifying the Customer and returns to the Main/Home screen upon confirmation.
     *  @param actionEvent User clicks or keys the Cancel button
     */
    public void handleCancelButton(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Cancel Modifying Customer");
        alert.setContentText("Do You Want To Cancel Modifying the Customer? Click 'OK' to Confirm & Return to Main Menu.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            Login.loadMainScreen();
        }
    }

}
