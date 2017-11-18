package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.Customer;
import models.CustomerInfoManager;

import java.net.URL;
import java.util.ResourceBundle;

public class InformationView extends AnchorPane implements Initializable {
    @FXML private Label lb_fname, lb_lname, lb_citizenId, lb_address, lb_phone, lb_line;
    @FXML private Button btn_editInfo;
    private MainController controller;

    public void initialize(URL location, ResourceBundle resources) {
        showInformation();

    }
    public void showInformation(){
        Customer customer = CustomerInfoManager.getInstance().getCustomer();
        lb_fname.setText(customer.getFirstName());
        lb_lname.setText(customer.getLastName());
        lb_citizenId.setText(customer.getCitizenId());
        lb_address.setText(customer.getAddress());
        lb_phone.setText(customer.getPhone());
        lb_line.setText(customer.getLineId());
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
