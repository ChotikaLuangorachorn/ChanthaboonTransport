package views;

import controllers.MainController;
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
import java.time.LocalDate;
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
    private MainController controller;
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

                    controller.addVanJob(van,start,end,status);
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
        dp_endDate.setValue(LocalDate.now());
        dp_endDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setVan(Van van){
        this.van = van;
        if(dp_startDate!=null){
            onClickCancelEdit();
            onClickSubmit();
            setStartDatePicker();
            setEndDatePicker();
        }
    }

    public void setVanDetailView(VanDetailView vanDetailView){
        this.vanDetailView = vanDetailView;
    }

    public void setStatus(){
        List<JobType> types = controller.getVanJobTypes();
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
