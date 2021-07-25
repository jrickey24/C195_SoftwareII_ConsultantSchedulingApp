package Controller;

import DAO.DBAppointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


/** This Controller class Initializes the
 * Software Appointments By Office Report screen. */
public class SoftwareAppointmentsByOfficeReport implements Initializable {


    @FXML private TextField phoenixTextField;
    @FXML private TextField whiteplainsTextField;
    @FXML private TextField londonTextField;
    @FXML private TextField montrealTextField;
    @FXML PieChart softwareOfficeAptsPieChart = new PieChart();

    /**
     * LAMBDA - This method contains a lambda used to grab the content
     * of the pie chart and format each datapoint into the percentage value
     * for which the corresponding TextField is set.
     * This method also initializes the controller.
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {
            pieChartData = DBAppointments.getPieChartData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        softwareOfficeAptsPieChart.setData(pieChartData);

        softwareOfficeAptsPieChart.getData().forEach(data -> {
            String percentage = String.format("%.2f%%", (data.getPieValue()));
            if (data.getName().equals("White Plains")) {
                whiteplainsTextField.setText(percentage);
            }
            if (data.getName().equals("Phoenix")) {
                phoenixTextField.setText(percentage);
            }
            if (data.getName().equals("London")) {
                londonTextField.setText(percentage);
            }
            if (data.getName().equals("Montreal")) {
                montrealTextField.setText(percentage);
            }
        });
    }

    @FXML
    private void handleHomeButton(ActionEvent actionEvent) {
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        Login.loadMainScreen();
    }
}
