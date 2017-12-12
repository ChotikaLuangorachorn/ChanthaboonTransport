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
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Schedule;
import models.Van;
import utils.ReservationDateFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class VanDetailView implements Initializable{
    @FXML private Label lb_name, lb_regisNum, lb_type;
    @FXML private TableView table_vanSchedule;
    @FXML private TableColumn col_startDate, col_endDate, col_jobStatus;
    @FXML private Button btn_editVan, btn_deleteSchedule, btn_editSchedule;

    private MainController controller;
    private Van van;
    private List<Schedule> jobs;
    private VanMenuView vanMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        if (van!=null) {
            onClickEditVan();
            onClickEditSchedule();
        }

    }

    public void showDetail(){
        String name = van.getName();
        String regis_Number = van.getRegisNumber();
        String type = (van.getType().equals("VIP"))?"VIP (9 ที่นั่ง)":"ธรรมดา (15 ที่นั่ง)";
        lb_name.setText(name);
        lb_regisNum.setText(regis_Number);
        lb_type.setText(type);

    }
    public void onClickEditVan(){
        btn_editVan.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/van_detail_edit.fxml"));
                    AnchorPane detail = loader.load();
                    VanDetailEditView vanDetailEditView = loader.getController();
                    vanDetailEditView.setController(controller);
                    String type = (lb_name.getText().substring(0,1).equals("V"))?"VIP":"NORMAL";
                    van = new Van(van.getRegisNumber(),null,type,lb_name.getText());
                    vanDetailEditView.setVan(van);
                    vanDetailEditView.setVanDetailView(VanDetailView.this);
                    vanDetailEditView.setVanMenuView(vanMenuView);

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
                Schedule schedule = (Schedule) table_vanSchedule.getSelectionModel().getSelectedItem();
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
                        controller.deleteVanSchedule(schedule);
                        refreshScheduleTable();
                    }
                    table_vanSchedule.getSelectionModel().clearSelection();
                }
            }
        });
    }
    public void onClickEditSchedule(){
        btn_editSchedule.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Schedule schedule = (Schedule) table_vanSchedule.getSelectionModel().getSelectedItem();
                if (schedule!=null) {
                    String type = schedule.getType();
                    if ((Schedule.RESERVE).equals(type)) {
                        try {
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("/van_reservation_edit.fxml"));
                            AnchorPane detail = loader.load();
                            VanReservationEditView vanReservationEditView = loader.getController();
                            vanReservationEditView.setController(controller);
                            vanReservationEditView.setSchedule(schedule);
                            vanReservationEditView.setVanDetailView(VanDetailView.this);
                            vanReservationEditView.setStartDay(schedule.getStartDate());
                            vanReservationEditView.setEndDay(schedule.getEndDate());
                            vanReservationEditView.setReservationId();

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
                            loader.setLocation(getClass().getResource("/van_job_edit.fxml"));
                            AnchorPane detail = loader.load();
                            VanJobEditView vanJobEditView = loader.getController();
                            vanJobEditView.setController(controller);
                            vanJobEditView.setSchedule(schedule);
                            vanJobEditView.setVanDetailView(VanDetailView.this);
                            vanJobEditView.setStartDay(schedule.getStartDate());
                            vanJobEditView.setEndDay(schedule.getEndDate());
                            vanJobEditView.setStatus();

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
        table_vanSchedule.setItems(data);
    }

    public void refreshScheduleTable(){
        this.jobs = controller.getVanSchedule(van.getRegisNumber());
        initData();
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setVan(Van van) {
        this.van = van;
        if (lb_name != null){
        refreshScheduleTable();
        showDetail();
        onClickDeleteSchedule();
        onClickEditSchedule();}
        onClickEditVan();
        onClickEditSchedule();
    }
    public void setVanMenuView(VanMenuView vanMenuView){
        this.vanMenuView = vanMenuView;
    }

    public void setLb_name(String name) {
        this.lb_name.setText(name);
    }

    public void setLb_type(String type) {
        this.lb_type.setText(type);
    }
}
