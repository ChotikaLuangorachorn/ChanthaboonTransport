package controllers.services;

import models.*;
import services.ReservationService;
import services.VanService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReservationSQLiteService extends SQLiteService implements ReservationService {
    public ReservationSQLiteService(String url) {
        super(url);
    }

    @Override
    public boolean assignVan(List<Van> vans, Reservation reservation) {
        return assignVan(vans, reservation.getReserveId());
    }

    @Override
    public boolean assignVan(List<Van> vans, String reservationId) {
        String sql = String.format("insert into van_reserve_schedule " +
                "select van.regis_number, '%s' " +
                "from van " +
                "where van.regis_number in ", reservationId);
        List<String> vanIds = new ArrayList<>();
        for (Van van:vans)
            vanIds.add("'" + van.getRegisNumber() + "'");
        sql += "(" + String.join(",", vanIds) + ")";
        int result = executeUpdate(sql);
        return result>0;
    }

    @Override
    public boolean assignDriver(List<Driver> drivers, Reservation reservation) {
        return assignDriver(drivers, reservation.getReserveId());
    }

    @Override
    public boolean assignDriver(List<Driver> drivers, String reservationId) {
        String sql = String.format("insert into driver_reserve_schedule " +
                "select driver.citizen_id, '%s' " +
                "from driver " +
                "where driver.citizen_id in ", reservationId);
        List<String> driverIds = new ArrayList<>();
        for (Driver driver:drivers)
            driverIds.add("'" + driver.getId() + "'");
        sql += "(" + String.join(",", driverIds) + ")";
        int result = executeUpdate(sql);
        return result > 0;
    }



    @Override
    public boolean addMeeting(String meetingPlace, Date meetingTime, Reservation reservation) {
        return addMeeting(meetingPlace, meetingTime, reservation.getReserveId());
    }

    @Override
    public boolean addMeeting(String meetingPlace, Date meetingTime, String reservationId) {
        String sql = String.format("update reservation " +
                        "set meeting_place='%s', meeting_time='%s' " +
                        "where id='%s'",
                meetingPlace, formatter.format(meetingTime), reservationId);
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public boolean confirmDeposit(Reservation reservation, Date depositDate) {
        return confirmDeposit(reservation, depositDate);
    }

    @Override
    public boolean confirmDeposit(String reservationId, Date depositDate) {
        String sql = "update reservation set isDeposited='true', deposit_date='" + formatter.format(depositDate) + "' where id='" + reservationId + "'";
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<Reservation>();
        String sql = "select * \n" +
                "from reservation\n" +
                "join customer\n" +
                "on reservation.customer_id = customer.citizen_id";
        return executeQuery(sql, resultSet -> {
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

    @Override
    public Reservation getReservation(String reserveId) {
        // TODO getReservation
        return null;
    }

    @Override
    public boolean addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit) {
        System.out.println("request addReservation");
        String reserveDateString = formatter.format(reserveDate);
        String startDateString = formatter.format(startDate);
        String endDateString = formatter.format(endDate);
        String sql = String.format("insert into reservation (customer_id, reserve_date, start_working_date, end_working_date, fee, province, district, place, isDeposit, isDeposited, deposit_fee, amt_vip, amt_normal) values ('%s', '%s', '%s', '%s', %f, '%s', '%s', '%s', '%s', '%s', %f, %d, %d)",
                customerId, reserveDateString, startDateString, endDateString, price, destination.getProvince(), destination.getDistrict(), destination.getPlace(), "true", "false", deposit, vanAmt.get(VanService.VIP), vanAmt.get(VanService.NORMAL));
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public boolean deleteReservation(Reservation reservation) {
        return deleteReservation(reservation.getReserveId());
    }

    @Override
    public boolean deleteReservation(String reservationId) {
        System.out.println("request delete reservation");
        System.out.println("reservationId = " + reservationId);
        String sqlr = "delete from reservation where id='" + reservationId + "'";
        String sqlv = "delete from van_reserve_schedule where reservation_id='" + reservationId + "'";
        String sqld = "delete from driver_reserve_schedule where reservation_id='" + reservationId + "'";
        int result = executeUpdate(sqlr);
        System.out.println("result = " + result);
        if (result > 0){
            executeUpdate(sqlv);
            executeUpdate(sqld);
        }
        return result > 0;
    }

    @Override
    public List<Reservation> getHistoryReservation(String citizenId) {
        List<Reservation> reservations = new ArrayList<Reservation>();
        String sql = "select * from reservation where customer_id = '" + citizenId + "'";
        return executeQuery(sql, resultSet -> {
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
}
