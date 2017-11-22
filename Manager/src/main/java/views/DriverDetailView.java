package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Driver;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DriverDetailView implements Initializable {
    @FXML private Label lb_license, lb_fName, lb_lName, lb_nName, lb_citizenId, lb_birthDay, lb_phone, lb_address;
    @FXML private TableView table_driverDetail;
    @FXML private TableColumn col_startDatr, col_endDate, col_jobStatus;
    @FXML private Button btn_editDriver, btn_editJob, btn_deleteJob;

    private MainController controller;
    private Driver driver;
    private List<Driver> jobs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
    }

    public void showDetail(){
        String license = driver.getDriverLicense();
        String fName = driver.getFirstname();
        String lName = driver.getLastname();
        String nName = driver.getNickname();
        String citizenId = driver.getCitizenId();
        String birthDay = ReservationDateFormatter.getInstance().getUiDateFormatter().format(driver.getDateOfBirth());
        String phone = driver.getPhone();
        String address = driver.getAddress();

        lb_license.setText(license);
        lb_fName.setText(fName);
        lb_lName.setText(lName);
        lb_nName.setText(nName);
        lb_citizenId.setText(citizenId);
        lb_birthDay.setText(birthDay);
        lb_phone.setText(phone);
        lb_address.setText(address);
    }


    public void initCol(){
    }
    public void initData(){
        ObservableList<Driver> data = FXCollections.observableList(jobs);
        table_driverDetail.setItems(data);
    }

    public void refreshVanTable(){
//        this.jobs = controller.getVans();
        initData();
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        showDetail();
    }
}
