package view;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Customer;
import models.CustomerInfoManager;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InformationView extends AnchorPane implements Initializable {
    @FXML private Label lb_fname, lb_lname, lb_citizenId, lb_address, lb_phone, lb_line;
    @FXML private Button btn_editInfo, btn_changePwd;
    private MainController controller;

    public void initialize(URL location, ResourceBundle resources) {
        onClickChangePassword();
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
                    loader.setLocation(getClass().getResource("/information_editTab.fxml"));
                    Pane EditLayout = (AnchorPane) loader.load();
                    InformationEditView informationEditView = loader.getController();
                    System.out.println("controller in info view= " + controller);
                    informationEditView.setController(controller);
                    informationEditView.setInformationView(InformationView.this);

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

    public void onClickChangePassword(){
        btn_changePwd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setHeaderText("เปลี่ยนรหัสผ่านของคุณ");
                Label text1 = new Label("รหัสผ่านเดิม: ");
                Label text2 = new Label("รหัสผ่านใหม่: ");
                Label text3 = new Label("ยืนยันรหัสผ่านใหม่: ");
                Label errorOldPwd = new Label("");
                Label errorNewPwd = new Label("");
                Label errorConfirmPwd = new Label("");
                errorOldPwd.setStyle("-fx-text-fill: Red");
                errorNewPwd.setStyle("-fx-text-fill: Red");
                errorConfirmPwd.setStyle("-fx-text-fill: Red");
                text1.setPrefWidth(120);
                errorOldPwd.setPrefWidth(200);

                PasswordField oldPwd = new PasswordField();
                final PasswordField newPwd = new PasswordField();
                final PasswordField confirmPwd = new PasswordField();

                ButtonType btn_confirmType= new ButtonType("ยืนยัน", ButtonBar.ButtonData.OK_DONE);
                ButtonType btn_cancelType = new ButtonType("ยกเลิก", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(btn_confirmType, btn_cancelType);
                Node btn_confirm = alert.getDialogPane().lookupButton(btn_confirmType);
                if("".equals(oldPwd.getText()) &&( "".equals(newPwd.getText()) &&"".equals(confirmPwd.getText()))){
                    btn_confirm.setDisable(true);
                }

                oldPwd.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("in old pwd");
                        //1
                        if("".equals(oldPwd.getText()) &&( !"".equals(newPwd.getText()) || !"".equals(confirmPwd.getText()))){
                            errorOldPwd.setText("โปรดระบุรหัสผ่านเดิม");
                            oldPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorOldPwd.setText("");
                            oldPwd.setStyle("-fx-border-color: ");
                        }
                        //2
                        if((newPwd.getText().length()<5 && !"".equals(newPwd.getText())) && (!"".equals(oldPwd.getText())|| !"".equals(confirmPwd.getText()))){
                            errorNewPwd.setText("โปรดระบุรหัสผ่านใหม่อย่างน้อย 5 อักขระ");
                            newPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorNewPwd.setText("");
                            newPwd.setStyle("-fx-border-color: ");
                        }
                        //3
                        if ("".equals(newPwd.getText()) && confirmPwd.getText().length()!=0){
                            errorConfirmPwd.setText("โปรดระบุรหัสผ่านใหม่ก่อน");
                            confirmPwd.setStyle("-fx-border-color: Red");
                        }
                        else if((!newPwd.getText().equals(confirmPwd.getText()))){
                            errorConfirmPwd.setText("รหัสผ่านที่ยืนยันไม่ตรงกับรหัสผ่านใหม่");
                            confirmPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorConfirmPwd.setText("");
                            confirmPwd.setStyle("-fx-border-color: ");
                        }
                        // button
                        if(!"".equals(oldPwd.getText()) &&!"".equals(newPwd.getText()) &&!"".equals(confirmPwd.getText())){
                            if("".equals(errorOldPwd.getText()) && "".equals(errorNewPwd.getText()) && "".equals(errorConfirmPwd.getText())){
                            btn_confirm.setDisable(false);}
                        }else btn_confirm.setDisable(true);
                    }

                });

                newPwd.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("in new pwd");
                        //1
                        if("".equals(oldPwd.getText()) &&( !"".equals(newPwd.getText()) || !"".equals(confirmPwd.getText()))){
                            errorOldPwd.setText("โปรดระบุรหัสผ่านเดิม");
                            oldPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorOldPwd.setText("");
                            oldPwd.setStyle("-fx-border-color: ");
                        }
                        //2
                        if((newPwd.getText().length()<5 && !"".equals(newPwd.getText())) && (!"".equals(oldPwd.getText())|| !"".equals(confirmPwd.getText()))){
                            errorNewPwd.setText("โปรดระบุรหัสผ่านใหม่อย่างน้อย 5 อักขระ");
                            newPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorNewPwd.setText("");
                            newPwd.setStyle("-fx-border-color: ");
                        }
                        //3
                        if ("".equals(newPwd.getText()) && confirmPwd.getText().length()!=0){
                            errorConfirmPwd.setText("โปรดระบุรหัสผ่านใหม่ก่อน");
                            confirmPwd.setStyle("-fx-border-color: Red");
                        }
                        else if((!newPwd.getText().equals(confirmPwd.getText()))){
                            errorConfirmPwd.setText("รหัสผ่านที่ยืนยันไม่ตรงกับรหัสผ่านใหม่");
                            confirmPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorConfirmPwd.setText("");
                            confirmPwd.setStyle("-fx-border-color: ");
                        }
                        // button
                        if(!"".equals(oldPwd.getText()) &&!"".equals(newPwd.getText()) &&!"".equals(confirmPwd.getText())){
                            if("".equals(errorOldPwd.getText()) && "".equals(errorNewPwd.getText()) && "".equals(errorConfirmPwd.getText())){
                                btn_confirm.setDisable(false);}
                        }else btn_confirm.setDisable(true);
                    }
                });

                confirmPwd.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("in confirm pwd");

                        //1
                        if("".equals(oldPwd.getText()) &&( !"".equals(newPwd.getText()) || !"".equals(confirmPwd.getText()))){
                            errorOldPwd.setText("โปรดระบุรหัสผ่านเดิม");
                            oldPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorOldPwd.setText("");
                            oldPwd.setStyle("-fx-border-color: ");
                        }
                        //2
                        if((newPwd.getText().length()<5 && !"".equals(newPwd.getText())) && (!"".equals(oldPwd.getText())|| !"".equals(confirmPwd.getText()))){
                            errorNewPwd.setText("โปรดระบุรหัสผ่านใหม่อย่างน้อย 5 อักขระ");
                            newPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorNewPwd.setText("");
                            newPwd.setStyle("-fx-border-color: ");
                        }
                        //3
                        if ("".equals(newPwd.getText()) && confirmPwd.getText().length()!=0){
                            errorConfirmPwd.setText("โปรดระบุรหัสผ่านใหม่ก่อน");
                            confirmPwd.setStyle("-fx-border-color: Red");
                        }
                        else if((!newPwd.getText().equals(confirmPwd.getText()))){
                            errorConfirmPwd.setText("รหัสผ่านที่ยืนยันไม่ตรงกับรหัสผ่านใหม่");
                            confirmPwd.setStyle("-fx-border-color: Red");
                        }
                        else {
                            errorConfirmPwd.setText("");
                            confirmPwd.setStyle("-fx-border-color: ");
                        }
                        // button
                        if(!"".equals(oldPwd.getText()) &&!"".equals(newPwd.getText()) &&!"".equals(confirmPwd.getText())){
                            if("".equals(errorOldPwd.getText()) && "".equals(errorNewPwd.getText()) && "".equals(errorConfirmPwd.getText())){
                                btn_confirm.setDisable(false);}
                        }else btn_confirm.setDisable(true);
                    }
                });


                GridPane grid = new GridPane();

                grid.add(text1, 1, 1);
                grid.add(oldPwd, 2, 1);
                grid.add(errorOldPwd, 2, 2);

                grid.add(text2, 1, 3);
                grid.add(newPwd, 2, 3);
                grid.add(errorNewPwd, 2, 4);

                grid.add(text3, 1, 5);
                grid.add(confirmPwd, 2, 5);
                grid.add(errorConfirmPwd, 2, 6);

                grid.setVgap(10);

                alert.getDialogPane().setContent(grid);

                Optional<ButtonType> result = alert.showAndWait();

                if ((result.isPresent()) && (result.get() == btn_confirmType) &&("".equals(errorOldPwd.getText()) && "".equals(errorNewPwd.getText()) && "".equals(errorConfirmPwd.getText()))){
                    System.out.println("oldPwd = " + oldPwd.getText());
                    System.out.println("newPwd = " + newPwd.getText());
                    controller.changeCustomerPassword(CustomerInfoManager.getInstance().getCustomer().getCitizenId(), oldPwd.getText(), confirmPwd.getText());
                }


            }
        });
    }

    public void setController(MainController controller) {
        this.controller = controller;
        showInformation();
        onClickEdit();
    }
}
