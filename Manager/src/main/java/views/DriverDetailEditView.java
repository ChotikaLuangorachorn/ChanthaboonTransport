package views;

import controllers.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Driver;
import models.Van;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class DriverDetailEditView implements Initializable {

    @FXML private TextField tf_fName, tf_lName, tf_nName, tf_phone;
    @FXML private TextArea ta_address;
    @FXML private Button btn_confirm, btn_cancel;
    @FXML private Label lb_license, lb_error, lb_citizenId, lb_birthDay;

    private DriverDetailView dirverDetailView;
    private Driver driver;
    private MainController controller;
    private DriverMenuView driverMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (driver != null)
            showEditForm();
            onClickConcelEdit();
    }

    public void showEditForm(){
        String license = (driver.getDriverLicense()==null)?"":driver.getDriverLicense();
        String fName = (driver.getFirstname()==null)?"":driver.getFirstname();
        String lName = (driver.getLastname()==null)?"":driver.getLastname();
        String nName = (driver.getNickname()==null)?"":driver.getNickname();
        String phone = (driver.getPhone()==null)?"":driver.getPhone();
        String citizenId = driver.getCitizenId();
        String birthDay = ReservationDateFormatter.getInstance().getUiDateFormatter().format(driver.getDateOfBirth());
        String address = (driver.getAddress()==null)?"":driver.getAddress();
        lb_license.setText(license);
        tf_fName.setText(fName);
        tf_lName.setText(lName);
        tf_nName.setText(nName);
        tf_phone.setText(phone);
        lb_citizenId.setText(citizenId);
        lb_birthDay.setText(birthDay);
        ta_address.setText(address);

    }
    public void onClickConfirmEdit(){
        btn_confirm.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String fName = tf_fName.getText();
                String lName = tf_lName.getText();
                String nName = tf_nName.getText();
                String phone = tf_phone.getText();
                String address = ta_address.getText();
                Boolean checkPhone = false;
                try {
                    if (phone.length()==10){
                    int phoneNum = Integer.parseInt(phone);
                    checkPhone = true;}
                } catch (Exception e) {
                    checkPhone = false;
                }
                Boolean checkNull = false;

                if((!fName.equals("")) && (!lName.equals("")) && (!nName.equals("")) && (!address.equals("")) && (!phone.equals(""))){
                    checkNull = true;
                }
                if(!checkNull){
                    lb_error.setText("กรอกข้อมูลไม่ครบถ้วน");
                }
                else if(!checkPhone){
                    lb_error.setText("หมายเลขโทรศัทพ์ไม่ถูกต้อง");
                }
                else {
                    lb_error.setText("");

                    String license = driver.getDriverLicense();
                    String citizenId = driver.getCitizenId();
                    Date birthDay = driver.getDateOfBirth();

                    Driver newDriver = new Driver(citizenId,license,birthDay,fName,lName,nName,phone,address);
                    controller.editDriver(newDriver);

                    dirverDetailView.setLb_fName(newDriver.getFirstname());
                    dirverDetailView.setLb_lName(newDriver.getLastname());
                    dirverDetailView.setLb_nName(newDriver.getNickname());
                    dirverDetailView.setLb_phone(newDriver.getPhone());
                    dirverDetailView.setLb_address(newDriver.getAddress());

                    Stage stage = (Stage) btn_cancel.getScene().getWindow();
                    stage.close();

                    driverMenuView.refreshDriverTable();


                }

            }
        });
    }
    public void onClickConcelEdit(){
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btn_cancel.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        if (tf_fName != null) {
            showEditForm();
            onClickConfirmEdit();
            onClickConcelEdit();
        }
    }
    public void setDriverDetailEditView(DriverDetailView driverDetailView){
        this.dirverDetailView=driverDetailView;
    }
    public void setDriverMenuView(DriverMenuView driverMenuView){
        this.driverMenuView =driverMenuView;
    }
}
