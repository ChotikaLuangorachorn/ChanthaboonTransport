package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    private MainController controller;
    private VanDetailView vanDetailView;
    private Schedule schedule;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    public void setController(MainController controller) {
        this.controller = controller;
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
        List<Reservation> reservations = controller.getReservations();
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (Reservation r: reservations) {
            ids.add(String.format("%05d",Integer.parseInt(r.getReserveId())));
        }
        cb_reserveId.setItems(ids);
        cb_reserveId.setValue(String.format("%05d",Integer.parseInt(schedule.getNote())));
    }
}
