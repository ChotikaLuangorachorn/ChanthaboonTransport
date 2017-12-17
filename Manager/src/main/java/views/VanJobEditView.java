package views;

import controllers.MainController;
import controllers.VanController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import models.JobType;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class VanJobEditView implements Initializable {
    @FXML private DatePicker dp_startDate, dp_endDate;
    @FXML private Spinner sp_startHr, sp_startMin, sp_endHr, sp_endMin;
    @FXML private ComboBox cb_status;
    @FXML private Button btn_cancel, btn_submit;
    private VanController vanController;
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
                try {
                String vanId = schedule.getId();
                String startDate = dp_startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                String startTime = String.format("%02d:%02d:00",sp_startHr.getValue(),sp_startMin.getValue());
                String endDate = dp_endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                String endTime = String.format("%02d:%02d:00",sp_endHr.getValue(),sp_endMin.getValue());
                Date start = ReservationDateFormatter.getInstance().getDbFormatter().parse(startDate+" "+startTime);
                Date end = ReservationDateFormatter.getInstance().getDbFormatter().parse(endDate+" "+endTime);
                String status = cb_status.getValue().toString();

                Schedule newSchedule = new Schedule(vanId,start,end,status,Schedule.JOB);
                vanController.editVanSchedule(schedule,newSchedule);
                vanDetailView.refreshScheduleTable();
                Stage stage = (Stage) btn_submit.getScene().getWindow();
                stage.close();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public void setVanController(VanController vanController) {
        this.vanController = vanController;
        if (dp_startDate!=null){
            onClickCancelEdit();
            onClickSubmit();
        }
    }
    public void setVanDetailView(VanDetailView vanDetailView){
        this.vanDetailView =vanDetailView;
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
        List<JobType> types = vanController.getVanJobTypes();
        ObservableList<String> statuses = FXCollections.observableArrayList();
        for (JobType t: types) {
            statuses.add(t.getDescription());
        }
        cb_status.setItems(statuses);
        cb_status.setValue(schedule.getNote());

    }
}
