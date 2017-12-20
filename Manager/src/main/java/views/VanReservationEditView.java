package views;

import controllers.ReservationController;
import controllers.VanController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Reservation;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class VanReservationEditView implements Initializable{

    @FXML private Label lb_startDate, lb_startTime, lb_endDate, lb_endTime;
    @FXML private ComboBox cb_reserveId;
    @FXML private Button btn_cancel, btn_submit;
    private VanController vanController;
    private ReservationController reservationController;
    private VanDetailView vanDetailView;
    private Schedule schedule;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (schedule!=null){
            onClickCancelEdit();
            onClickSubmit();
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
    public void onClickSubmit(){
        btn_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String vanId = schedule.getId();
                int reserveId = Integer.parseInt(cb_reserveId.getValue().toString());
                Schedule newSchedule = new Schedule(vanId,null,null,reserveId+"",Schedule.RESERVE);
                vanController.editVanSchedule(schedule,newSchedule);
                vanDetailView.refreshScheduleTable();
                Stage stage = (Stage) btn_submit.getScene().getWindow();
                stage.close();
            }
        });
    }

    public void setVanController(VanController vanController) {
        this.vanController = vanController;
        if (lb_startDate!=null){
            onClickCancelEdit();
            onClickSubmit();
        }
    }

    public void setReservationController(ReservationController reservationController) {
        this.reservationController = reservationController;
    }

    public void setVanDetailView(VanDetailView vanDetailView){
        this.vanDetailView =vanDetailView;
    }
    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
    }

    public void setStartDay(Date start) {
        String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(start);
        String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(start)+ " น.";
        this.lb_startDate.setText(date);
        this.lb_startTime.setText(time);
    }

    public void setEndDay(Date end) {
        String date = ReservationDateFormatter.getInstance().getUiDateFormatter().format(end);
        String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(end)+ " น.";
        this.lb_endDate.setText(date);
        this.lb_endTime.setText(time);
    }
    public void setReservationId(){
        List<Reservation> reservations = reservationController.getReservations();
        ObservableList<String> ids = FXCollections.observableArrayList();
        Date dateNow = new Date();
        for (Reservation r: reservations) {
            if(r.getStartDate().after(dateNow)) {
                ids.add(String.format("%05d", Integer.parseInt(r.getReserveId())));
            }
        }
        cb_reserveId.setItems(ids);
        cb_reserveId.setValue(String.format("%05d",Integer.parseInt(schedule.getNote())));
    }
}
