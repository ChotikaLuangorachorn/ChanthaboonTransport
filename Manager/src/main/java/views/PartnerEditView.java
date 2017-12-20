package views;

import controllers.PartnerController;
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
    @FXML private Label lb_id, lb_error;
    @FXML private TextField tf_fName, tf_lName, tf_company, tf_phone;
    @FXML private Button btn_cancel, btn_submit;
    private PartnerController partnerController;
    private Partner partner;
    private PartnerMenuView partnerMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (partner!=null){
            showDetailPartner();
            onClickCancelEdit();
            onClickSubmitEdit();
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
    public void onClickSubmitEdit(){
        btn_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tf_fName.getText().equals("")){
                    tf_fName.setStyle("-fx-border-color: RED");
                }
                else if(!tf_fName.getText().equals("")){
                    tf_fName.setStyle("{-fx-border-color: }");
                }
                if(tf_lName.getText().equals("")){
                    tf_lName.setStyle("-fx-border-color: RED");
                }
                else if(!tf_lName.getText().equals("")){
                    tf_lName.setStyle("{-fx-border-color: }");
                }
                if(tf_company.getText().equals("")){
                    tf_company.setStyle("-fx-border-color: RED");
                }
                else if(!tf_company.getText().equals("")){
                    tf_company.setStyle("{-fx-border-color: }");
                }
                if(tf_phone.getText().equals("")){
                    tf_phone.setStyle("-fx-border-color: RED");
                }
                else if(!tf_phone.getText().equals("")){
                    tf_phone.setStyle("{-fx-border-color: }");
                    lb_error.setText("");
                    try{
                        Integer phone = Integer.parseInt(tf_phone.getText());
                        if(tf_phone.getText().length()!=10){
                            tf_phone.setStyle("-fx-border-color: RED");
                            lb_error.setText("กรุณากรอกหมายเลขโทรศัพท์เป็นตัวเลข10หลัก ");
                        }
                    }catch (Exception e){
                        tf_phone.setStyle("-fx-border-color: RED");
                        lb_error.setText("กรุณากรอกหมายเลขโทรศัพท์เป็นตัวเลข10หลัก ");
                    }
                }

                if(tf_fName.getText().equals("") || tf_lName.getText().equals("") || tf_company.getText().equals("") ||tf_phone.getText().equals("")){
                    lb_error.setText(lb_error.getText() + "กรอกข้อมุลไม่ครบถ้วน");
                }
                else if (lb_error.getText().equals("")){
                    lb_error.setText("");
                    // TODO partner last name
                    Partner newPartner = new Partner(partner.getId(),tf_fName.getText(), tf_lName.getText(), tf_company.getText(),tf_phone.getText());
                    partnerController.editPartner(newPartner);
                    Stage stage = (Stage) btn_submit.getScene().getWindow();
                    stage.close();
                    partnerMenuView.refreshPartnerTable();
                }
            }
        });
    }
    public void showDetailPartner(){
        String id = String.format("%05d",Integer.parseInt(partner.getId()));
        lb_id.setText(id);
        tf_fName.setText(partner.getFirstName());
        tf_lName.setText(partner.getLastName());
        tf_company.setText(partner.getCompany());
        tf_phone.setText(partner.getCompanyPhone());
    }
    public void setPartnerController(PartnerController partnerController){
        this.partnerController = partnerController;
    }
    public void setPartner(Partner partner){
        this.partner = partner;
        if (lb_id!=null){
            showDetailPartner();
            onClickCancelEdit();
            onClickSubmitEdit();
        }
    }
    public void setPartnerMenuView(PartnerMenuView partnerMenuView){
        this.partnerMenuView = partnerMenuView;
    }
}
