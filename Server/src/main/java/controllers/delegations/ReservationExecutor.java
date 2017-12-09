package controllers.delegations;

import controllers.assistants.UpdateExecutionAssistant;
import managers.CustomerDatabaseManager;
import models.Destination;
import utils.ReservationDateFormatter;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.SimpleFormatter;

public class ReservationExecutor {
    private String url;

    public ReservationExecutor(String url) {
        this.url = url;
    }

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit) {
        System.out.println("request addReservation");
        SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();
        String reserveDateString = formatter.format(reserveDate);
        String startDateString = formatter.format(startDate);
        String endDateString = formatter.format(endDate);
        String sql = String.format("insert into reservation (customer_id, reserve_date, start_working_date, end_working_date, fee, province, district, place, isDeposit, isDeposited, deposit_fee, amt_vip, amt_normal) values ('%s', '%s', '%s', '%s', %f, '%s', '%s', '%s', '%s', '%s', %f, %d, %d)",
                customerId, reserveDateString, startDateString, endDateString, price, destination.getProvince(), destination.getDistrict(), destination.getPlace(), "true", "false", deposit, vanAmt.get(CustomerDatabaseManager.VIP), vanAmt.get(CustomerDatabaseManager.NORMAL));
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
}
