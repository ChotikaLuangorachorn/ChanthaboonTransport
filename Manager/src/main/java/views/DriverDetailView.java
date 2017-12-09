package views;

import controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Driver;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DriverDetailView implements Initializable {
    @FXML private Label lb_license, lb_fName, lb_lName, lb_nName, lb_citizenId, lb_birthDay, lb_phone, lb_address;
    @FXML private TableView table_schedule;
    @FXML private TableColumn col_startDate, col_endDate, col_jobStatus;
    @FXML private Button btn_editDriver, btn_editSchedule, btn_deleteSchedule;

    private MainController controller;
    private Driver driver;
    private List<Schedule> jobs;
    private DriverMenuView driverMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        if(driver!=null){
        onClickEditDriver();onClickDeleteSchedule();}
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
                    loader.setLocation(getClass().getResource("/driver_detail_edit.fxml"));
                    Pane detail = loader.load();
                    DriverDetailEditView driverDetailEditView = loader.getController();
                    driverDetailEditView.setController(controller);
                    driver = new Driver(driver.getCitizenId(),driver.getDriverLicense(),driver.getDateOfBirth(),lb_fName.getText(),lb_lName.getText(),lb_nName.getText(),lb_phone.getText(),lb_address.getText());
                    driverDetailEditView.setDriver(driver);
                    driverDetailEditView.setDriverDetailEditView(DriverDetailView.this);
                    driverDetailEditView.setDriverMenuView(driverMenuView);

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
    public void onClickDeleteSchedule(){
        btn_deleteSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Schedule schedule = (Schedule) table_schedule.getSelectionModel().getSelectedItem();
                if(driver!=null){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("ยืนยันการลบตารางงาน");
                    alert.setHeaderText("ยืนยันการลบตารางงาน");

                    Date startDate = schedule.getStartDate();
                    String startDay = ReservationDateFormatter.getInstance().getUiDateFormatter().format(startDate)+" ";
                    String startTime = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(startDate)+ " น.";

                    Date endDate = schedule.getEndDate();
                    String endDay = ReservationDateFormatter.getInstance().getUiDateFormatter().format(endDate)+" ";
                    String endTime = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(endDate)+ " น.";

                    String type = schedule.getType();
                    if (Schedule.RESERVE.equals(type)) {
                        type = String.format("หมายเลขการจอง: %05d", Integer.parseInt(schedule.getNote()));
                    } else if (Schedule.JOB.equals(type)) {
                        type = schedule.getNote();
                    }
                    else{
                        type ="-";
                    }
                    String s = "วันที่ไป:\t\t" + startDay + startTime + "\n"
                            +"วันที่กลับ:\t\t" + endDay + endTime + "\n"
                            +"สถานะ:\t\t" + type;
                    alert.setContentText(s);

                    Optional<ButtonType> result = alert.showAndWait();
                    if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                        controller.deleteDriverSchedule(schedule);
                        table_schedule.getSelectionModel().clearSelection();
                        refreshDriverTable();
                    }
                }
            }
        });
    }

    public void initCol(){
        col_startDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule,String> schedule) {
                Date startDate = schedule.getValue().getStartDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(startDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(startDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
        col_endDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule,String> schedule) {
                Date endDate = schedule.getValue().getEndDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(endDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(endDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
        col_jobStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule,String> schedule) {
                String type = schedule.getValue().getType();
                if (Schedule.RESERVE.equals(type)) {
                    return new SimpleStringProperty(String.format("หมายเลขการจอง: %05d", Integer.parseInt(schedule.getValue().getNote())));
                } else if (Schedule.JOB.equals(type)) {
                    return new SimpleStringProperty(schedule.getValue().getNote());
                }
                else{
                    return new SimpleStringProperty("-");
                }
            }
        });
    }
    public void initData(){
        ObservableList<Schedule> data = FXCollections.observableList(jobs);
        table_schedule.setItems(data);
    }

    public void refreshDriverTable(){
        this.jobs = controller.getDriverSchedule(driver.getCitizenId());
        initData();
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        showDetail();
        if(lb_license!=null){
            onClickEditDriver();
            onClickDeleteSchedule();
            refreshDriverTable();
        }
    }
    public void setDriverMenuView(DriverMenuView driverMenuView){
        this.driverMenuView =driverMenuView;
    }

    public void setLb_fName(String fName) {
        this.lb_fName.setText(fName);
    }

    public void setLb_lName(String lName) {
        this.lb_lName.setText(lName);
    }

    public void setLb_nName(String nName) {
        this.lb_nName.setText(nName);
    }

    public void setLb_phone(String phone) {
        this.lb_phone.setText(phone);
    }

    public void setLb_address(String address) {
        this.lb_address.setText(address);
    }

}
