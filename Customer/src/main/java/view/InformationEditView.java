package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Customer;
import models.CustomerInfoManager;

import java.net.URL;
import java.util.ResourceBundle;

public class InformationEditView implements Initializable{
    @FXML private Label lb_citizenId;
    @FXML private TextField tf_fname;
    @FXML private TextField tf_lname;
    @FXML private TextField tf_address;
    @FXML private TextField tf_phone;
    @FXML private TextField tf_line;
    private MainController controller;

    public void initialize(URL location, ResourceBundle resources) {
        showEditInformation();
    }
    public void showEditInformation(){
        Customer customer = CustomerInfoManager.getInstance().getCustomer();
        lb_citizenId.setText(customer.getCitizenId());
        tf_fname.setText(customer.getFirstName());
        tf_lname.setText(customer.getLastName());
        tf_address.setText(customer.getAddress());
        tf_phone.setText(customer.getPhone());
        tf_line.setText(customer.getLineId());
    }


    public void setController(MainController controller) {
        this.controller = controller;
    }
}
