package views;

import controllers.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Partner;

import java.net.URL;
import java.util.ResourceBundle;

public class PartnerEditView implements Initializable {
    @FXML private Label lb_id;
    @FXML private TextField tf_name, tf_company, tf_phone;
    @FXML private Button btn_cancel, btn_submit;
    private MainController controller;
    private Partner partner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (partner!=null){
            showDetailPartner();
            onClickCancelEdit();
        }
    }
    public void onClickCancelEdit(){
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btn_cancel.getScene().getWindow();
                stage.close();
            }
        });
    }
    public void showDetailPartner(){
        String id = String.format("%05d",partner.getId());
        lb_id.setText(id);
        tf_name.setText(partner.getName());
        tf_company.setText(partner.getCompany());
        tf_phone.setText(partner.getCompanyPhone());
    }
    public void setController(MainController controller){
        this.controller = controller;
    }
    public void setPartner(Partner partner){
        this.partner = partner;
        if (lb_id!=null){
            showDetailPartner();
            onClickCancelEdit();
        }
    }
}
