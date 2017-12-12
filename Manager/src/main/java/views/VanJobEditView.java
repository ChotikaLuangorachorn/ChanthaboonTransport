package views;

import controllers.MainController;
import javafx.fxml.Initializable;
import models.Schedule;

import java.net.URL;
import java.util.ResourceBundle;

public class VanJobEditView implements Initializable {
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
}
