package views;

import controllers.MainController;
import controllers.VanController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Driver;
import models.JobType;
import models.Schedule;
import models.Van;
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

public class VanJobAddView implements Initializable {
    @FXML
    private DatePicker dp_startDate, dp_endDate;
    @FXML private Spinner sp_startHr, sp_startMin, sp_endHr, sp_endMin;
    @FXML private ComboBox cb_status;
    @FXML private Button btn_cancel, btn_submit;
    private VanController vanController;
    private VanDetailView vanDetailView;
    private Van van;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void onClickSubmit(){
        btn_submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String startDate = dp_startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                    String startTime = String.format("%02d:%02d:00",sp_startHr.getValue(),sp_startMin.getValue());
                    String endDate = dp_endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
                    String endTime = String.format("%02d:%02d:00",sp_endHr.getValue(),sp_endMin.getValue());
                    Date start = ReservationDateFormatter.getInstance().getDbFormatter().parse(startDate+" "+startTime);
                    Date end = ReservationDateFormatter.getInstance().getDbFormatter().parse(endDate+" "+endTime);
                    JobType status = (JobType) cb_status.getValue();

                    vanController.addVanJob(van,start,end,status);
                    vanDetailView.refreshScheduleTable();
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
    public void setStartDatePicker(){
        dp_startDate.setValue(LocalDate.now());
        dp_startDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }
    public void setEndDatePicker(){
//        dp_endDate.setValue(LocalDate.now());
        LocalDate startLocal = dp_startDate.getValue();
        Date minimumDate = convertToDateStart(startLocal);
        LocalDate minimun = minimumDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dp_endDate.setValue(minimun);
        dp_endDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(minimun));
            }
        });
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
    public void setTimeSpinner(){
        Date dateNow = new Date();
        Spinner<Integer> hr = new Spinner<>(dateNow.getHours(),23,dateNow.getHours());
        sp_startHr.setValueFactory(hr.getValueFactory());
        sp_endHr.setValueFactory(hr.getValueFactory());

        Spinner<Integer> min = new Spinner<>(dateNow.getMinutes(),59,dateNow.getMinutes());
        sp_startMin.setValueFactory(min.getValueFactory());
        sp_endMin.setValueFactory(min.getValueFactory());
    }

    public void setVanController(VanController vanController) {
        this.vanController = vanController;
    }

    public void setVan(Van van){
        this.van = van;
        if(dp_startDate!=null){
            onClickCancelEdit();
            onClickSubmit();
            setStartDatePicker();
            setEndDatePicker();
            onClickStartDatePicker();
            setTimeSpinner();
        }
    }

    public void setVanDetailView(VanDetailView vanDetailView){
        this.vanDetailView = vanDetailView;
    }

    public void setStatus(){
        List<JobType> types = vanController.getVanJobTypes();
//        ObservableList<String> statuses = FXCollections.observableArrayList();
//        for (JobType t: types) {
//            statuses.add(t.getDescription());
//        }
//        cb_status.setItems(statuses);
//        cb_status.setValue(statuses.get(0));
        cb_status.getItems().addAll(types);
        cb_status.setValue(types.get(0));
        cb_status.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                JobType type = (JobType) object;
                return type.getDescription();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });

    }
}
