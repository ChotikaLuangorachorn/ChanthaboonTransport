package view;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;
import models.Destination;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReservationView extends AnchorPane implements Initializable{
    @FXML private ComboBox<String> cbb_province, cbb_district;
    @FXML private TextArea ta_detail;
    @FXML private DatePicker dp_startDate, dp_endStart;
    @FXML private Label lb_amtNormalVan, lb_amtVipVan;
    @FXML private Spinner spn_start_hr, spn_start_min, spn_end_hr, spn_end_min, spn_vip, spn_normal;
    @FXML private RadioButton rd_distance, rd_daily;
    private MainController controller;


    public void initialize(URL location, ResourceBundle resources) {
        setOnActionDatePicker();
    }

    public void setCbb_province(){
        List<String> provinces = controller.getProvince();
        cbb_province.getItems().addAll(provinces);
        cbb_province.getSelectionModel().selectFirst();
        cbb_district.getItems().addAll(controller.getdistrict(cbb_province.getValue()));
        cbb_district.getSelectionModel().selectFirst();
        cbb_province.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                cbb_district.getItems().clear();
                String province = cbb_province.getValue();
                List<String> districts = controller.getdistrict(province);
                cbb_district.getItems().addAll(districts);
                cbb_district.getSelectionModel().selectFirst();
            }
        });
    }

    public void setDatePicker(){
        dp_startDate.setValue(LocalDate.now());
        dp_startDate.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });

        dp_endStart.setValue(LocalDate.now());
        dp_endStart.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(LocalDate.now()));
            }
        });
    }

    public void setOnActionDatePicker(){
        dp_endStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String province = cbb_province.getValue();
                String district = cbb_district.getValue();
                String place = ta_detail.getText();
                Destination destination = new Destination(province, district, place);
                LocalDate startLocal = dp_startDate.getValue();
                LocalDate endLocal = dp_endStart.getValue();
                Map<String, Integer> amtVan = controller.getVanAvailable(destination, convertToDate(startLocal), convertToDate(endLocal));
                System.out.println(amtVan.toString());
                lb_amtNormalVan.setText(amtVan.get(CustomerDatabaseManager.NORMAL).toString());
                lb_amtVipVan.setText(amtVan.get(CustomerDatabaseManager.VIP).toString());
            }
        });
        dp_startDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String province = cbb_province.getValue();
                String district = cbb_district.getValue();
                String place = ta_detail.getText();
                Destination destination = new Destination(province, district, place);
                LocalDate startLocal = dp_startDate.getValue();
                LocalDate endLocal = dp_endStart.getValue();
                Map<String, Integer> amtVan = controller.getVanAvailable(destination, convertToDate(startLocal), convertToDate(endLocal));
                System.out.println(amtVan.toString());
                lb_amtNormalVan.setText(amtVan.get(CustomerDatabaseManager.NORMAL).toString());
                lb_amtVipVan.setText(amtVan.get(CustomerDatabaseManager.VIP).toString());
            }
        });
    }

    public Date convertToDate(LocalDate localDate){
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);

    }


    public void setController(MainController controller) {
        this.controller = controller;
        setCbb_province();
        setDatePicker();
    }

}














































