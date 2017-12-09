package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import managers.CustomerDatabaseManager;
import models.Destination;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PriceExecutor {
    private String url;

    public PriceExecutor(String url) {
        this.url = url;
    }

    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        System.out.println("request getPrice");
        System.out.println("params " + vanAmt + " startDate " + startDate + " endDate " + endDate);

        int vipAmt = vanAmt.get(CustomerDatabaseManager.VIP);
        int normalAmt = vanAmt.get(CustomerDatabaseManager.NORMAL);
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) diff / (24*60*60*1000);
        String sql = "select * from price_rate where price_rate.reserve_type = \"day\"";

        QueryExecutionAssistant<Double> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, (resultSet -> {
            Map<String, Double> rate = new HashMap<String, Double>();
            Map<String, Double> base = new HashMap<String, Double>();
            Map<String, Double> freeRage = new HashMap<String, Double>();

            while (resultSet.next()){
                rate.put(resultSet.getString("van_type"), resultSet.getDouble("rate"));
                base.put(resultSet.getString("van_type"), resultSet.getDouble("base"));
                freeRage.put(resultSet.getString("van_type"), resultSet.getDouble("free_range"));
            }

            double normalPrice = base.get(CustomerDatabaseManager.NORMAL) + rate.get(CustomerDatabaseManager.NORMAL)*((days < freeRage.get(CustomerDatabaseManager.NORMAL))?0:(days-freeRage.get(CustomerDatabaseManager.NORMAL)));
            double vipPrice = base.get(CustomerDatabaseManager.VIP) + rate.get(CustomerDatabaseManager.VIP)*((days < freeRage.get(CustomerDatabaseManager.VIP))?0:(days-freeRage.get(CustomerDatabaseManager.VIP)));

            return normalPrice*normalAmt + vipPrice*vipAmt;
        }), 0.0);
    }

    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        System.out.println("request getPrice");
        System.out.println("params " + vanAmt + " destination " + destination);
        int vipAmt = vanAmt.get(CustomerDatabaseManager.VIP);
        int normalAmt = vanAmt.get(CustomerDatabaseManager.NORMAL);
        String sql = "select * from price_rate where price_rate.reserve_type = \"distance\"";

        QueryExecutionAssistant<Double> assistant = new QueryExecutionAssistant<>(sql);
        return assistant.execute(sql, (resultSet -> {
            double distance = getDistance(destination);
            Map<String, Double> rate = new HashMap<String, Double>();
            Map<String, Double> base = new HashMap<String, Double>();
            Map<String, Double> freeRage = new HashMap<String, Double>();
            while (resultSet.next()){
                rate.put(resultSet.getString("van_type"), resultSet.getDouble("rate"));
                base.put(resultSet.getString("van_type"), resultSet.getDouble("base"));
                freeRage.put(resultSet.getString("van_type"), resultSet.getDouble("free_range"));
            }

            double normalPrice = base.get(CustomerDatabaseManager.NORMAL) + rate.get(CustomerDatabaseManager.NORMAL)*((distance < freeRage.get(CustomerDatabaseManager.NORMAL))?0:(distance-freeRage.get(CustomerDatabaseManager.NORMAL)));
            double vipPrice = base.get(CustomerDatabaseManager.VIP) + rate.get(CustomerDatabaseManager.VIP)*((distance < freeRage.get(CustomerDatabaseManager.VIP))?0:(distance-freeRage.get(CustomerDatabaseManager.VIP)));
            System.out.println("base = " + base);
            System.out.println("rate = " + rate);
            System.out.println("freeRage = " + freeRage);
            System.out.println("normalPrice = " + normalPrice);
            System.out.println("vipPrice = " + vipPrice);
            return normalPrice*normalAmt + vipPrice*vipAmt;
        }), null);

    }
    private double getDistance(Destination destination){
        String sql = "select distance from distance where province='" + destination.getProvince() + "' and district='" + destination.getDistrict() + "'";
        QueryExecutionAssistant<Double> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {
            if (resultSet.next())
                return resultSet.getDouble("distance");
            return 0.0;
        },0.0);
    }
}
