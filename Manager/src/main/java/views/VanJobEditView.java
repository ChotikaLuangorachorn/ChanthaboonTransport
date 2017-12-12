package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import models.Schedule;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class VanJobEditView implements Initializable {
    @FXML private DatePicker dp_startDate, dp_endDate;
    @FXML private Spinner sp_startHr, sp_startMin, sp_endHr, sp_endMin;
    @FXML private ComboBox cb_status;
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

    }

    public void setEndDay(Date end) {
    }
    public void setReservationId(){

    }
}
