package views;

import controllers.DriverController;
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
    private DriverController driverController;
    private DriverMenuView driverMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (driver != null)
            showEditForm();
            onClickCancelEdit();
    }

    public void showEditForm(){
        String license = (driver.getDriverLicense()==null)?"":driver.getDriverLicense();
        String fName = (driver.getFirstName()==null)?"":driver.getFirstName();
        String lName = (driver.getLastName()==null)?"":driver.getLastName();
        String nName = (driver.getNickname()==null)?"":driver.getNickname();
        String phone = (driver.getPhone()==null)?"":driver.getPhone();
        String citizenId = driver.getId();
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
                    String citizenId = driver.getId();
                    Date birthDay = driver.getDateOfBirth();

                    Driver newDriver = new Driver(citizenId,fName,lName,license,birthDay,nName,phone,address);
                    driverController.editDriver(newDriver);

                    dirverDetailView.setLb_fName(newDriver.getFirstName());
                    dirverDetailView.setLb_lName(newDriver.getLastName());
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
    public void onClickCancelEdit(){
        btn_cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btn_cancel.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void setDriverController(DriverController driverController){
        this.driverController = driverController;

    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        if (tf_fName != null) {
            showEditForm();
            onClickConfirmEdit();
            onClickCancelEdit();
        }
    }
    public void setDriverDetailEditView(DriverDetailView driverDetailView){
        this.dirverDetailView=driverDetailView;
    }
    public void setDriverMenuView(DriverMenuView driverMenuView){
        this.driverMenuView =driverMenuView;
    }
}
