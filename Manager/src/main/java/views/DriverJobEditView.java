package views;

import controllers.DriverController;
import controllers.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.JobType;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class DriverJobEditView implements Initializable{
    @FXML
    private DatePicker dp_startDate, dp_endDate;
    @FXML private Spinner sp_startHr, sp_startMin, sp_endHr, sp_endMin;
    @FXML private ComboBox cb_status;
    @FXML private Button btn_cancel, btn_submit;
    private DriverController driverController;
    private DriverDetailView driverDetailView;
    private Schedule schedule;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(schedule!=null){
            onClickCancelEdit();
            onClickSubmit();
        }
    }
    public void onClickSubmit(){
        btn_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String driverId = schedule.getId();
                    String startDate = dp_startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                    String startTime = String.format("%02d:%02d:00",sp_startHr.getValue(),sp_startMin.getValue());
                    String endDate = dp_endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                    String endTime = String.format("%02d:%02d:00",sp_endHr.getValue(),sp_endMin.getValue());
                    Date start = ReservationDateFormatter.getInstance().getDbFormatter().parse(startDate+" "+startTime);
                    Date end = ReservationDateFormatter.getInstance().getDbFormatter().parse(endDate+" "+endTime);
                    String status = cb_status.getValue().toString();

                    Schedule newSchedule = new Schedule(driverId,start,end,status,Schedule.JOB);
                    driverController.editDriverSchedule(schedule,newSchedule);
                    driverDetailView.refreshScheduleTable();
                    Stage stage = (Stage) btn_submit.getScene().getWindow();
                    stage.close();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
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
    public void onClickStartDatePicker(){
        dp_startDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setEndDatePicker();
            }
        });
    }
    public void onClickEndDatePicker(){
        dp_endDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!dp_startDate.getValue().isEqual(dp_endDate.getValue())){
                    Spinner<Integer> hr = new Spinner<>(0,23,(Integer)sp_startHr.getValue());
                    sp_endHr.setValueFactory(hr.getValueFactory());

                    Spinner<Integer> min = new Spinner<>(0,59,(Integer)sp_startMin.getValue());
                    sp_endMin.setValueFactory(min.getValueFactory());
                }else {
                    Spinner<Integer> hr = new Spinner<>((Integer)sp_startHr.getValue(),23,(Integer)sp_startHr.getValue());
                    sp_endHr.setValueFactory(hr.getValueFactory());

                    Spinner<Integer> min = new Spinner<>((Integer)sp_startMin.getValue(),59,(Integer)sp_startMin.getValue());
                    sp_endMin.setValueFactory(min.getValueFactory());
                }
            }
        });
    }
    public void onClickSpinnerStartHr(){
        sp_startHr.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (dp_startDate.getValue().isEqual(dp_endDate.getValue())){
                    Spinner<Integer> s = new Spinner<>((Integer)sp_startHr.getValue(),23,(Integer) sp_startHr.getValue());
                    sp_endHr.setValueFactory(s.getValueFactory());
                }
            }
        });
    }
    public void onClickSpinnerStartMin(){
        sp_startMin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (dp_startDate.getValue().isEqual(dp_endDate.getValue())){
                    Spinner<Integer> s = new Spinner<>((Integer)sp_startMin.getValue(),59,(Integer) sp_startMin.getValue());
                    sp_endMin.setValueFactory(s.getValueFactory());
                }
            }
        });
    }
    public void setDriverController(DriverController driverController) {
        this.driverController = driverController;
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
        if(dp_startDate!=null){
            onClickCancelEdit();
            onClickSubmit();
            setTimeSpinner();
            onClickStartDatePicker();
            onClickEndDatePicker();

            onClickSpinnerStartHr();
            onClickSpinnerStartMin();
        }
    }
    public void setStartDay(Date start) {
        LocalDate date = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dp_startDate.setValue(date);
        String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(start);
        int hr = Integer.parseInt(time.split(":")[0]);
        int min = Integer.parseInt(time.split(":")[1]);
        this.sp_startHr.getValueFactory().setValue(hr);
        this.sp_startMin.getValueFactory().setValue(min);
        dp_startDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }

    public void setEndDay(Date end) {
        LocalDate date = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.dp_endDate.setValue(date);
        String time = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(end);
        int hr = Integer.parseInt(time.split(":")[0]);
        int min = Integer.parseInt(time.split(":")[1]);
        this.sp_endHr.getValueFactory().setValue(hr);
        this.sp_endMin.getValueFactory().setValue(min);
        dp_endDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }

    public void setEndDatePicker(){
        LocalDate startLocal = dp_startDate.getValue();
        Date minimumDate = convertToDateStart(startLocal);
        LocalDate minimum = minimumDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(dp_startDate.getValue().isAfter(dp_endDate.getValue())){
            dp_endDate.setValue(minimum);
        }

        dp_endDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(minimum));
            }
        });
        if (!startLocal.isEqual(LocalDate.now())){
            Spinner<Integer> s1 = new Spinner<>(0,23,(Integer)sp_startHr.getValue());
            sp_startHr.setValueFactory(s1.getValueFactory());
            Spinner<Integer> s2 = new Spinner<>(0,59,(Integer)sp_startMin.getValue());
            sp_startMin.setValueFactory(s2.getValueFactory());
        }
    }
    public void setTimeSpinner(){
        sp_startHr.getValueFactory().setValue(schedule.getStartDate().getHours());
        sp_startMin.getValueFactory().setValue(schedule.getStartDate().getMinutes());
        sp_endHr.getValueFactory().setValue(schedule.getEndDate().getHours());
        sp_endMin.getValueFactory().setValue(schedule.getEndDate().getMinutes());
    }
    public Date convertToDateStart(LocalDate localDate){
        try {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            date.setHours(Integer.parseInt(sp_startHr.getValue().toString()));
            date.setMinutes(Integer.parseInt(sp_startMin.getValue().toString()));
            return date;
        }catch (NullPointerException e){
            System.out.println("not set hr or min");
        }
        return null;
    }
    public void setStatus(){
        List<JobType> types = driverController.getDriverJobTypes();
        ObservableList<String> statuses = FXCollections.observableArrayList();
        for (JobType t: types) {
            statuses.add(t.getDescription());
        }
        cb_status.setItems(statuses);
        cb_status.setValue(schedule.getNote());

    }
}
