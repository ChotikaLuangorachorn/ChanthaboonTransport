package view;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Customer;
import models.CustomerInfoManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InformationView extends AnchorPane implements Initializable {
    @FXML private Label lb_fname, lb_lname, lb_citizenId, lb_address, lb_phone, lb_line;
    @FXML private Button btn_editInfo;
    private MainController controller;

    public void initialize(URL location, ResourceBundle resources) {
        showInformation();
        onClickEdit();

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

    public void onClickEdit(){
        btn_editInfo.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    System.out.println("In onClickNewEvent method in MainView");
                    // Load root layout from fxml file.
                    Stage secondStage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/Information_editTab.fxml"));
                    Pane EditLayout = (AnchorPane) loader.load();
                    InformationEditView informationEditView = loader.getController();
                    informationEditView.setController(controller);

                    // Show the scene containing the root layout.
                    Scene scene = new Scene(EditLayout);
                    secondStage.setScene(scene);
                    secondStage.setResizable(false);
                    secondStage.setTitle("Edit information");
                    secondStage.initModality(Modality.APPLICATION_MODAL);
                    secondStage.showAndWait();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }
}
