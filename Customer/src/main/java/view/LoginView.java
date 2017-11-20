package view;

import controller.MainController;
import controller.SceneManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import managers.CustomerDatabaseManager;
import models.Customer;
import models.CustomerInfoManager;

public class LoginView {
    @FXML private TextField tf_critizenId;
    @FXML private PasswordField tf_pwd;
    @FXML private Button btn_login;
    @FXML private Label lb_notification;
    private MainController controller;
    private SceneManager sceneManager;

    @FXML
    public void initialize(){

    }

    public void onClickLogin(){
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Customer customer = controller.getCustomer(tf_critizenId.getText().toString(), tf_pwd.getText().toString());
                if( customer != null){
                    CustomerInfoManager customerInfo = CustomerInfoManager.getInstance();
                    customerInfo.setCustomer(customer);
                    sceneManager.showMainView(customerInfo);
                }else{
                    lb_notification.setText("Citizen ID or Password Invalid");
                }
            }
        });
    }

    public void setController(MainController controller) {
        this.controller = controller;
        onClickLogin();
    }

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}
