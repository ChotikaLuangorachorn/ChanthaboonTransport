package views;

import controllers.DriverController;
import controllers.MainController;
import controllers.ReservationController;
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
import javafx.scene.layout.AnchorPane;
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
    @FXML private Button btn_editDriver, btn_editSchedule, btn_deleteSchedule, btn_addSchedule;

    private DriverController driverController;
    private ReservationController reservationController;
    private Driver driver;
    private List<Schedule> jobs;
    private DriverMenuView driverMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        if(driver!=null){
        onClickEditDriver();onClickDeleteSchedule();onClickEditSchedule();onClickAddSchedule();}
    }

    public void showDetail(){
        String license = driver.getDriverLicense();
        String fName = driver.getFirstName();
        String lName = driver.getLastName();
        String nName = driver.getNickname();
        String citizenId = driver.getId();
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
                    driverDetailEditView.setDriverController(driverController);
                    driver = new Driver(driver.getId(),lb_fName.getText(),lb_lName.getText(),driver.getDriverLicense(),driver.getDateOfBirth(),lb_nName.getText(),lb_phone.getText(),lb_address.getText());
                    driverDetailEditView.setDriver(driver);
                    driverDetailEditView.setDriverDetailEditView(DriverDetailView.this);
                    driverDetailEditView.setDriverMenuView(driverMenuView);

                    Scene scene = new Scene(detail);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("แก้ไขข้อมูลคนขับ");
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
                if(schedule!=null){
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
                        driverController.deleteDriverSchedule(schedule);
                        refreshScheduleTable();
                    }
                    table_schedule.getSelectionModel().clearSelection();
                }
            }
        });
    }
    public void onClickEditSchedule(){
        btn_editSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Schedule schedule = (Schedule) table_schedule.getSelectionModel().getSelectedItem();
                Date dateNow = new Date();
                if (schedule!=null) {
                    if (schedule.getStartDate().after(dateNow)) {
                        String type = schedule.getType();
                        if ((Schedule.RESERVE).equals(type)) {
                            try {

                                Stage stage = new Stage();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/driver_reservation_edit.fxml"));
                                AnchorPane detail = loader.load();
                                DriverReservationEditView driverReservationEditView = loader.getController();
                                driverReservationEditView.setDriverController(driverController);
                                driverReservationEditView.setReservationController(reservationController);
                                driverReservationEditView.setSchedule(schedule);
                                driverReservationEditView.setDriverDetailView(DriverDetailView.this);
                                driverReservationEditView.setStartDay(schedule.getStartDate());
                                driverReservationEditView.setEndDay(schedule.getEndDate());
                                driverReservationEditView.setReservationId();

                                Scene scene = new Scene(detail);
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.setTitle("แก้ไขตารางงาน");
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.showAndWait();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else if (Schedule.JOB.equals(type)) {
                            try {
                                Stage stage = new Stage();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("/driver_job_edit.fxml"));
                                AnchorPane detail = loader.load();
                                DriverJobEditView driverJobEditView = loader.getController();
                                driverJobEditView.setDriverController(driverController);
                                driverJobEditView.setSchedule(schedule);
                                driverJobEditView.setDriverDetailView(DriverDetailView.this);
                                driverJobEditView.setStartDay(schedule.getStartDate());
                                driverJobEditView.setEndDay(schedule.getEndDate());
                                driverJobEditView.setStatus();

                                Scene scene = new Scene(detail);
                                stage.setScene(scene);
                                stage.setResizable(false);
                                stage.setTitle("แก้ไขตารางงาน");
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.showAndWait();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("ไม่สามารถแก้ไขตารางงานได้");
                        alert.setHeaderText("ไม่สามารถแก้ไขตารางงานได้");
                        String s = "เนื่องจากตารางงานนี้ได้ผ่านมาแล้ว";
                        alert.setContentText(s);
                        Optional<ButtonType> result = alert.showAndWait();
                    }
                }



            }
        });
    }
    public void onClickAddSchedule(){
        btn_addSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                        Stage stage = new Stage();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/driver_job_add.fxml"));
                        AnchorPane detail = loader.load();
                        DriverJobAddView driverJobAddView = loader.getController();
                        driverJobAddView.setDriverController(driverController);
                        driverJobAddView.setDriverDetailView(DriverDetailView.this);
                        driverJobAddView.setStatus();
                        driverJobAddView.setDriver(driver);

                        Scene scene = new Scene(detail);
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.setTitle("เพิ่มงานคนขับ");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
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

    public void refreshScheduleTable(){
        this.jobs = driverController.getDriverSchedule(driver.getId());
        initData();
    }

    public void setDriverController(DriverController driverController){
        this.driverController = driverController;
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
        showDetail();
        if(lb_license!=null){
            onClickEditDriver();
            onClickDeleteSchedule();
            refreshScheduleTable();
            onClickEditSchedule();
            onClickAddSchedule();
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
