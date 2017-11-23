package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Driver;
import utils.ReservationDateFormatter;

import java.io.IOException;
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

    public void onClickEditDriver(){
        btn_editDriver.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/van_detail_edit.fxml"));
                    Pane detail = loader.load();
                    DriverDetailEditView driverDetailEditView = loader.getController();
                    driverDetailEditView.setController(controller);
                    driverDetailEditView.setDriver(driver);
                    driverDetailEditView.setDriverDetailEditView(DriverDetailView.this);

                    Scene scene = new Scene(detail);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("แก้ไขข้อมูลรถตู้");
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
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
