package view;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    @FXML private Button btn_editInfo;
    private InformationView informationView;
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

    public void onClickConfirmEdit(){
        btn_editInfo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if ("".equals(tf_fname.getText()) || "".equals(tf_lname.getText()) || "".equals(tf_address.getText()) ||
                "".equals(tf_phone.getText()) || "".equals(tf_line.getText())){
                    showWarningDialog();
                }else{
                    Customer customer = new Customer(lb_citizenId.getText(),
                            tf_fname.getText(),
                            tf_lname.getText(),
                            tf_address.getText(),
                            tf_phone.getText(),
                            tf_line.getText(),
                            CustomerInfoManager.getInstance().getCustomer().getLastReserveId());
                    System.out.println("controller in info edit= " + controller);
                    controller.editCustomerInfo(customer);
                    CustomerInfoManager.getInstance().setCustomer(customer);

                    Stage stage = (Stage) tf_fname.getScene().getWindow();
                    stage.close();
                    informationView.showInformation();
                }

            }
        });
    }
    public void showWarningDialog(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Topic");
        alert.setHeaderText("Look, You have not entered the information text field yet.");
        alert.setContentText("Please enter a message in the information field.");
        alert.showAndWait();
    }


    public void setController(MainController controller) {
        this.controller = controller;
        onClickConfirmEdit();
    }

    public void setInformationView(InformationView informationView){
        this.informationView = informationView;
    }


}
