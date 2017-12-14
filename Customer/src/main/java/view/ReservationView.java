package view;

import controller.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import managers.CustomerDatabaseManager;
import models.CustomerInfoManager;
import models.Destination;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ReservationView extends AnchorPane implements Initializable{
    @FXML private ComboBox<String> cbb_province, cbb_district;
    @FXML private TextArea ta_place;
    @FXML private DatePicker dp_startDate, dp_endStart;
    @FXML private Label lb_amtNormalVan, lb_amtVipVan;
    @FXML private Label lb_dist_normal, lb_dist_vip, lb_day_normal, lb_day_vip;
    @FXML private Spinner spn_start_hr, spn_start_min, spn_end_hr, spn_end_min, spn_vip, spn_normal;
    @FXML private RadioButton rd_distance, rd_daily;
    @FXML private Button btn_calPrice;
    private MainController controller;
    private MyReservationView myReservationView;
    private Map<String, Integer> amtVanTotal;


    public void initialize(URL location, ResourceBundle resources) {
        setOnActionDatePicker();
        onClickCalPrice();
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
        String province = cbb_province.getValue();
        String district = cbb_district.getValue();
        String place = ta_place.getText();
        LocalDate startLocal = dp_startDate.getValue();
        Date minimumDate = controller.getMinimumDate(new Destination(province, district, place),  convertToDateStart(startLocal));
        LocalDate minimun = minimumDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dp_endStart.setValue(minimun);
        dp_endStart.setDayCellFactory(param -> new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(empty || item.isBefore(minimun));
            }
        });
    }



    public void setOnActionDatePicker(){
        dp_endStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String province = cbb_province.getValue();
                    String district = cbb_district.getValue();
                    String place = ta_place.getText();
                    Destination destination = new Destination(province, district, place);
                    LocalDate startLocal = dp_startDate.getValue();
                    LocalDate endLocal = dp_endStart.getValue();
                    Map<String, Integer> amtVan = controller.getVanAvailable(destination, convertToDateStart(startLocal), convertToDateEnd(endLocal));
                    System.out.println(amtVan.toString());
                    lb_amtNormalVan.setText(amtVan.get(CustomerDatabaseManager.NORMAL).toString());
                    lb_amtVipVan.setText(amtVan.get(CustomerDatabaseManager.VIP).toString());
                    spn_normal.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, amtVan.get(CustomerDatabaseManager.NORMAL)));
                    spn_vip.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, amtVan.get(CustomerDatabaseManager.VIP)));
                }catch (NullPointerException e){
                    System.out.println("not set hr or min");
                }

            }
        });
        dp_startDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    setEndDatePicker();
                }catch (NullPointerException e){
                    System.out.println("not set hr or min");
                }

            }
        });
    }

    public Date convertToDateStart(LocalDate localDate){
        try {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            date.setHours(Integer.parseInt(spn_start_hr.getValue().toString()));
            date.setMinutes(Integer.parseInt(spn_start_min.getValue().toString()));
            return date;
        }catch (NullPointerException e){
            System.out.println("not set hr or min");
        }
        return null;
    }
    public Date convertToDateEnd(LocalDate localDate){
        try {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            date.setHours(Integer.parseInt(spn_end_hr.getValue().toString()));
            date.setMinutes(Integer.parseInt(spn_end_min.getValue().toString()));
            return date;
        }catch (NullPointerException e){
            System.out.println("not set hr or min");
        }
        return null;
    }

    public void onClickCalPrice(){
        btn_calPrice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate startLocal = dp_startDate.getValue();
                LocalDate endLocal = dp_endStart.getValue();
                amtVanTotal = new HashMap<>();
                amtVanTotal.put(CustomerDatabaseManager.NORMAL, Integer.parseInt(spn_normal.getValue().toString()));
                amtVanTotal.put(CustomerDatabaseManager.VIP, Integer.parseInt(spn_vip.getValue().toString()));
                double price = 0;
                if (rd_daily.isSelected()){
                    System.out.println("in daily Radio");
                    System.out.println("controller = " + controller);
                    price = controller.getPrice(amtVanTotal, convertToDateStart(startLocal), convertToDateEnd(endLocal));
                }else if(rd_distance.isSelected()){
                    System.out.println("in distance Radio");
                    price = controller.getPrice(amtVanTotal, new Destination(cbb_province.getValue(), cbb_district.getValue(), ta_place.getText()));
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm reservation");
                String s = "ยืนยันการจองรถตู้ของคุณ\n"
                        +"จังหวัด:\t\t" + cbb_province.getValue() + "\n"
                        +"อำเภอ/เขต:\t" + cbb_district.getValue() + "\n"
                        +"จำนวนรถตู้:\tรถธรรมดา(15 ที่นั่ง) " + amtVanTotal.get(CustomerDatabaseManager.NORMAL) + " คัน\n\t\t\tรถVIP(9 ที่นั่ง) " + amtVanTotal.get(CustomerDatabaseManager.VIP) + " คัน\n"
                        +"ราคาทั้งหมด:\t" + String.format("%,.2f",price) +" บาท" + "\n"
                        +"ราคาค่ามัดจำ:\t" + String.format("%,.2f",price/2) +" บาท";
                alert.setContentText(s);
                Optional<ButtonType> result = alert.showAndWait();
                if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                    controller.addReservation(CustomerInfoManager.getInstance().getCustomer().getCitizenId(), amtVanTotal,
                            new Destination(cbb_province.getValue(), cbb_district.getValue(), ta_place.getText()),convertToDateStart(startLocal), convertToDateEnd(endLocal), price);
                    myReservationView.refreshReservationTable();
                }

            }
        });

    }


    public void setController(MainController controller) {
        this.controller = controller;
        setCbb_province();
        setStartDatePicker();
        setEndDatePicker();
        String dist_normal  = "";
        String dist_vip  = "";
        String day_normal  = "";
        String day_vip  = "";
    }

    public void setMyReservationView(MyReservationView myReservationView) {
        this.myReservationView = myReservationView;
    }
}














































