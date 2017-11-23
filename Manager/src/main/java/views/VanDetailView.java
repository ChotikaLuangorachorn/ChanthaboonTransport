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
    @FXML private TableView table_vanDetail;
    @FXML private TableColumn col_startDate, col_endDate, col_jobStatus;
    @FXML private Button btn_editVan;

    private MainController controller;
    private Van van;
    private List<Schedule> jobs;
    private VanMenuView vanMenuView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
        onClickEditVan();

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

    public void initCol(){
        col_startDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule,String> schedule) {
                Date startDate = schedule.getValue().getStartDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(startDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFullFormatter().format(startDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
        col_endDate.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule,String> schedule) {
                Date endDate = schedule.getValue().getEndDate();
                String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(endDate)+" ";
                String time = ReservationDateFormatter.getInstance().getUiTimeFullFormatter().format(endDate)+ " น.";
                return new SimpleStringProperty(date+time);
            }
        });
        col_jobStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Schedule,String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Schedule,String> schedule) {
                String type = schedule.getValue().getType();
                if (Schedule.RESERVE.equals(type)) {
                    return new SimpleStringProperty(String.format("หมายเลขการจอง: %05d", schedule.getValue().getNote()));
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
        table_vanDetail.setItems(data);
    }

    public void refreshVanTable(){
        this.jobs = controller.getVanSchedule(van.getRegisNumber());
        initData();
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setVan(Van van) {
        this.van = van;
        refreshVanTable();
        showDetail();
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
