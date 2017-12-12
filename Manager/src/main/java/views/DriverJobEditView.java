package views;

import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import models.JobType;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class DriverJobEditView implements Initializable{
    @FXML
    private DatePicker dp_startDate, dp_endDate;
    @FXML private Spinner sp_startHr, sp_startMin, sp_endHr, sp_endMin;
    @FXML private ComboBox cb_status;
    @FXML private Button btn_cancel, btn_submit;
    private MainController controller;
    private DriverDetailView driverDetailView;
    private Schedule schedule;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void setController(MainController controller) {
        this.controller = controller;
        if (dp_startDate!=null){
//            onClickCancelEdit();
//            onClickSubmit();
        }
    }
    public void setDriverDetailView(DriverDetailView driverDetailView){
        this.driverDetailView =driverDetailView;
    }
    public void setSchedule(Schedule schedule){
        this.schedule = schedule;
    }
    public void setStartDay(Date start) {
        LocalDate date = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dp_startDate.setValue(date);
        String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(start);
        int hr = Integer.parseInt(time.split(":")[0]);
        int min = Integer.parseInt(time.split(":")[1]);
        this.sp_startHr.getValueFactory().setValue(hr);
        this.sp_startMin.getValueFactory().setValue(min);
    }

    public void setEndDay(Date end) {
        LocalDate date = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dp_endDate.setValue(date);
        String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(end);
        int hr = Integer.parseInt(time.split(":")[0]);
        int min = Integer.parseInt(time.split(":")[1]);
        this.sp_endHr.getValueFactory().setValue(hr);
        this.sp_endMin.getValueFactory().setValue(min);
    }
    public void setStatus(){
        List<JobType> types = controller.getDriverJobTypes();
        ObservableList<String> statuses = FXCollections.observableArrayList();
        for (JobType t: types) {
            statuses.add(t.getDescription());
        }
        cb_status.setItems(statuses);
        cb_status.setValue(schedule.getNote());

    }
}
