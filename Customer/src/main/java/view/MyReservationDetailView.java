package view;

import controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import models.Reservation;
import utils.ReservationDateFormatter;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class MyReservationDetailView implements Initializable
{
    @FXML private Label lb_reserveId, lb_province, lb_district, lb_startDate, lb_startTime, lb_endDate, lb_endTime, lb_normalAmt, lb_vipAmt, lb_reserveDate, lb_reserveTime, lb_price,lb_deposit,lb_isDeposited, lb_meetingDate, lb_meetingTime;
    @FXML private TextArea ta_place, ta_meetingPlace;
    private MainController controller;
    private Reservation reservation;

    public void initialize(URL location, ResourceBundle resources) {

    }
    public void showDetail(){
        String id = String.format("%05d",Integer.parseInt(reservation.getReserveId()));
        String province = reservation.getDestination().getProvince();
        String district = reservation.getDestination().getDistrict();

        String startDate = ReservationDateFormatter.getInstance().getUiDateFormatter().format(reservation.getStartDate());
        String endDate = ReservationDateFormatter.getInstance().getUiDateFormatter().format(reservation.getEndDate());
        String reserveDate = ReservationDateFormatter.getInstance().getUiDateFormatter().format(reservation.getReserveDate());
        String meetingDate = (reservation.getMeetingDate()!=null)?ReservationDateFormatter.getInstance().getUiDateFormatter().format(reservation.getMeetingDate()):"--/--/----";

        String startTime = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(reservation.getStartDate());
        String endTime = ReservationDateFormatter.getInstance().getUiTimeFormatter().format(reservation.getEndDate());
        String reserveTime = ReservationDateFormatter.getInstance().getUiTimeFullFormatter().format(reservation.getReserveDate());
        String meetingTime = (reservation.getMeetingDate()!=null)?ReservationDateFormatter.getInstance().getUiTimeFormatter().format(reservation.getMeetingDate()):"--:--";
        String normalAmt = reservation.getAmtNormal() +"";
        String vipAmt = reservation.getAmtVip()+"";
        String price = String.format("%,.2f บาท",reservation.getPrice());
        String deposit = String.format("%,.2f บาท",reservation.getDeposit());
        String isDeposited = (reservation.getIsDeposited().equals("true"))?"ชำระแล้ว":"ยังไม่ชำระ";
        String place = (!reservation.getDestination().getPlace().equals(""))?reservation.getDestination().getPlace():"-";
        String meetingPlace = (reservation.getMeetingPlace()!=null)?reservation.getMeetingPlace():"-";

        lb_reserveId.setText(id);
        lb_province.setText(province);
        lb_district.setText(district);
        ta_place.setText(place);
        lb_startDate.setText(startDate);
        lb_startTime.setText(startTime);
        lb_endDate.setText(endDate);
        lb_endTime.setText(endTime);
        lb_normalAmt.setText(normalAmt);
        lb_vipAmt.setText(vipAmt);
        lb_reserveDate.setText(reserveDate);
        lb_reserveTime.setText(reserveTime);
        lb_price.setText(price);
        lb_deposit.setText(deposit);
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
