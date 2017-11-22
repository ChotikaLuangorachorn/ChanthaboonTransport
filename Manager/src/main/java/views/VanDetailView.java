package views;

import controllers.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import models.Van;
import utils.ReservationDateFormatter;

import java.awt.*;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class VanDetailView implements Initializable{
    @FXML private Label lb_name, lb_regisNum, lb_type;
    @FXML private TableView table_vanDetail;
    @FXML private TableColumn col_startDate, col_endDate, col_jobStatus;
    @FXML private Button btn_editVan;

    private MainController controller;
    private Van van;
    private List<Van> jobs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCol();
    }

    public void showDetail(){
        String name = van.getName();
        String regis_Number = van.getRegisNumber();
        String type = (van.getType().equals("VIP"))?"VIP (9 ที่นั่ง)":"ธรรมดา (15 ที่นั่ง)";
        lb_name.setText(name);
        lb_regisNum.setText(regis_Number);
        lb_type.setText(type);

    }


    public void initCol(){
    }
    public void initData(){
        ObservableList<Van> data = FXCollections.observableList(jobs);
        table_vanDetail.setItems(data);
    }

    public void refreshVanTable(){
//        this.jobs = controller.getVans();
        initData();
    }

    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setVan(Van van) {
        this.van = van;
        showDetail();
    }
}
