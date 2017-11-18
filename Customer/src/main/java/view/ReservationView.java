package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;

import java.net.URL;
import java.util.ResourceBundle;

public class ReservationView extends AnchorPane implements Initializable{
    @FXML private ComboBox<String> cbb_province, cbb_district;
    @FXML private TextArea ta_detail;
    @FXML private DatePicker dp_startDate, dp_endStart;
    @FXML private Spinner spn_start_hr, spn_start_min, spn_end_hr, spn_end_min, spn_vip, spn_normal;
    @FXML private RadioButton rd_distance, rd_daily;
    private CustomerDatabaseManager controller;
    private CustomerInfoManager customer;


    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setController(CustomerDatabaseManager controller) {
        this.controller = controller;
    }

    public void setCustomer(CustomerInfoManager customer) {
        this.customer = customer;
    }
}
