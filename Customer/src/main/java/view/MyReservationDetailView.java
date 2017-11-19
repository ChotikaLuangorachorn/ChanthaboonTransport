package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.Reservation;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class MyReservationDetailView implements Initializable
{
    @FXML private Label lb_reserveId, lb_province, lb_district, lb_startDate, lb_startTime, lb_endDate, lb_endTime, lb_normalAmt, lb_vipAmt, lb_reserveDate, lb_reserveTime, lb_price,lb_isDeposited, lb_meetingDate, lb_meetingTime;
    @FXML private TextArea ta_detail, ta_meetingPlace;
    private MainController controller;
    private Reservation reservation;

    public void initialize(URL location, ResourceBundle resources) {

    }
    public void showDetail(){
        String id = reservation.getReserveId();
        String province = reservation.getDestination().getProvince();
        String district = reservation.getDestination().getDistrict();
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = formatDate.format(reservation.getStartDate());
        String endDate = formatDate.format(reservation.getEndDate());
        String reserveDate = formatDate.format(reservation.getReserveDate());
        String meetingDate = formatDate.format(reservation.getMeetingDate());
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        String startTime = formatTime.format(reservation.getStartDate());
        String endTime = formatTime.format(reservation.getEndDate());
        String reserveTime = formatTime.format(reservation.getReserveDate());
        String meetingTime = formatTime.format(reservation.getMeetingDate());
        String normalAmt = reservation.getAmtNormal() +"";
        String vipAmt = reservation.getAmtVip()+"";
        String price = String.format("%,.2f",reservation.getPrice());
        String isDeposited = reservation.getIsDeposited();
        String place = reservation.getDestination().getPlace();
        String meetingPlace = reservation.getMeetingPlace();

        lb_reserveId.setText(id);
        lb_province.setText(province);
        lb_district.setText(district);
        ta_detail.setText(place);
        lb_startDate.setText(startDate);
        lb_startTime.setText(startTime);
        lb_endDate.setText(endDate);
        lb_endTime.setText(endTime);
        lb_normalAmt.setText(normalAmt);
        lb_vipAmt.setText(vipAmt);
        lb_reserveDate.setText(reserveDate);
        lb_reserveTime.setText(reserveTime);
        lb_price.setText(price);
        lb_isDeposited.setText(isDeposited);
        ta_meetingPlace.setText(meetingPlace);
        lb_meetingDate.setText(meetingDate);
        lb_meetingTime.setText(meetingTime);




    }
    public void setController(MainController controller){
        this.controller = controller;

    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        showDetail();
    }
}
