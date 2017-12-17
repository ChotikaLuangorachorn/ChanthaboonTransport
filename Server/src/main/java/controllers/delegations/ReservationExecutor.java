package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import managers.CustomerDatabaseManager;
import models.*;
import utils.ReservationDateFormatter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class ReservationExecutor {
    private SimpleDateFormat formatter;
    private String url;

    public ReservationExecutor(String url) {
        this.url = url;
        formatter = ReservationDateFormatter.getInstance().getDbFormatter();
    }

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<Reservation>();
        String sql = "select * \n" +
                "from reservation\n" +
                "join customer\n" +
                "on reservation.customer_id = customer.citizen_id";
        QueryExecutionAssistant<List<Reservation>> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {
            while(resultSet.next()){
                String id = resultSet.getString("id");
                String customerId = resultSet.getString("customer_id");
                Date statDate = formatter.parse(resultSet.getString("start_working_date"));
                Date endDate = formatter.parse(resultSet.getString("end_working_date"));
                Date reserveDate = formatter.parse(resultSet.getString("reserve_date"));
                Date meetingTime = (resultSet.getString("meeting_time")!=null)?formatter.parse(resultSet.getString("meeting_time")):null;
                String province = resultSet.getString("province");
                String district = resultSet.getString("district");
                String place = resultSet.getString("place");
                String meetingPlace = resultSet.getString("meeting_place");
                double fee = resultSet.getDouble("fee");
                int amtVip = resultSet.getInt("amt_vip");
                int amtNormal = resultSet.getInt("amt_normal");
                String isDeposited = resultSet.getString("isDeposited");
                double deposit = resultSet.getDouble("deposit_fee");
                Date depositDate = (resultSet.getString("deposit_date")!=null)?formatter.parse(resultSet.getString("deposit_date")):null;
                Reservation reservation = new Reservation(id, customerId, meetingPlace, amtVip, amtNormal, new Destination(province, district, place), statDate, endDate, reserveDate, meetingTime, fee, isDeposited, deposit, depositDate);


                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String lineId = resultSet.getString("line_id");
                int lastReserveId = resultSet.getInt("last_reserve");
                Customer customer = new Customer(customerId, firstname, lastname, address, phone, lineId, lastReserveId);
                reservation.setCustomer(customer);
                reservations.add(reservation);
            }
            return reservations;
        }, reservations);
    }
    public List<Reservation> getHistoryReservation(String citizenId) {
        List<Reservation> reservations = new ArrayList<Reservation>();
        String sql = "select * from reservation where customer_id = '" + citizenId + "'";
        QueryExecutionAssistant<List<Reservation>> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String customerId = resultSet.getString("customer_id");
                Date statDate = formatter.parse(resultSet.getString("start_working_date"));
                Date endDate = formatter.parse(resultSet.getString("end_working_date"));
                Date reserveDate = formatter.parse(resultSet.getString("reserve_date"));
                Date meetingTime = (resultSet.getString("meeting_time")!=null)?formatter.parse(resultSet.getString("meeting_time")):null;
                String province = resultSet.getString("province");
                String district = resultSet.getString("district");
                String place = resultSet.getString("place");
                String meetingPlace = resultSet.getString("meeting_place");
                double fee = resultSet.getDouble("fee");
                int amtVip = resultSet.getInt("amt_vip");
                int amtNormal = resultSet.getInt("amt_normal");
                String isDeposited = resultSet.getString("isDeposited");
                double deposit = resultSet.getDouble("deposit_fee");
                Date depositDate = (resultSet.getString("deposit_date")!=null)?formatter.parse(resultSet.getString("deposit_date")):null;

                Reservation reservation = new Reservation(id, customerId, meetingPlace, amtVip, amtNormal, new Destination(province, district, place), statDate, endDate, reserveDate, meetingTime, fee, isDeposited, deposit, depositDate);
                reservations.add(reservation);
            }
            return reservations;
        }, reservations);
    }
    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit) {

        System.out.println("request addReservation");
        String reserveDateString = formatter.format(reserveDate);
        String startDateString = formatter.format(startDate);
        String endDateString = formatter.format(endDate);
        String sql = String.format("insert into reservation (customer_id, reserve_date, start_working_date, end_working_date, fee, province, district, place, isDeposit, isDeposited, deposit_fee, amt_vip, amt_normal) values ('%s', '%s', '%s', '%s', %f, '%s', '%s', '%s', '%s', '%s', %f, %d, %d)",
                customerId, reserveDateString, startDateString, endDateString, price, destination.getProvince(), destination.getDistrict(), destination.getPlace(), "true", "false", deposit, vanAmt.get(CustomerDatabaseManager.VIP), vanAmt.get(CustomerDatabaseManager.NORMAL));
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public void deleteReservation(String reservationId){
        System.out.println("request delete reservation");
        System.out.println("reservationId = " + reservationId);
        String sqlr = "delete from reservation where id='" + reservationId + "'";
        String sqlv = "delete from van_reserve_schedule where reservation_id='" + reservationId + "'";
        String sqld = "delete from driver_reserve_schedule where reservation_id='" + reservationId + "'";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sqlr);
        System.out.println("result = " + result);
        if (result > 0){
            assistant.execute(sqlv);
            assistant.execute(sqld);
        }
    }
    public void confirmDeposit(String reservationId, Date depositDate) {
        String sql = "update reservation set isDeposited='true', deposit_date='" + formatter.format(depositDate) + "' where id='" + reservationId + "'";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public void addMeeting(String meetingPlace, Date meetingTime, String reservationId) {
        String sql = String.format("update reservation " +
                        "set meeting_place='%s', meeting_time='%s' " +
                        "where id='%s'",
                meetingPlace, formatter.format(meetingTime), reservationId);
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
    }
    public void assignDriver(List<Driver> drivers, String reservationId) {
        String sql = String.format("insert into driver_reserve_schedule " +
                "select driver.citizen_id, '%s' " +
                "from driver " +
                "where driver.citizen_id in ", reservationId);
        List<String> driverIds = new ArrayList<>();
        for (Driver driver:drivers)
            driverIds.add("'" + driver.getId() + "'");
        sql += "(" + String.join(",", driverIds) + ")";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public void assignVan(List<Van> vans, String reservationId) {
        String sql = String.format("insert into van_reserve_schedule " +
                "select van.regis_number, '%s' " +
                "from van " +
                "where van.regis_number in ", reservationId);
        List<String> vanIds = new ArrayList<>();
        for (Van van:vans)
            vanIds.add("'" + van.getRegisNumber() + "'");
        sql += "(" + String.join(",", vanIds) + ")";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
}
