package controllers;

import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.lang.Nullable;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SQLiteExecutor implements CustomerDatabaseManager {
    private String url = "vanScheduler.db";

    @Nullable
    public Customer getCustomer(String citizenId, String pwd) {
        System.out.println("request getCustomer");
        System.out.println("citizenId = " + citizenId);
        System.out.println("pwd = " + pwd);
        Connection connection = null;
        try{
            connection = prepareConnection();

            if (connection != null){
                String sql = "select * from customer where citizen_id = '" + citizenId + "' and pwd = '" + pwd + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()){
                    String id = resultSet.getString("citizen_id");
                    String firstname = resultSet.getString("first_name");
                    String lastname = resultSet.getString("last_name");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    String lineId = resultSet.getString("line_id");
                    int lastReserveId = resultSet.getInt("last_reserve");
                    Customer customer = new Customer(id, firstname, lastname, address, phone, lineId, lastReserveId);
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
    @Nullable
    public Map<String, Integer> getVanAvailable(Destination destination, Date startDate, Date endDate) {
        boolean possible = checkPossibleDay(destination, startDate, endDate);
        if (possible){
            Connection connection = null;
            try {
                connection = prepareConnection();
                if (connection != null){
                    Map<String, Integer> amtMap = new HashMap<String, Integer>();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    String start = formatter.format(startDate);
                    String end = formatter.format(endDate);

                    String sql = String.format("select type, count(*) as amt\n" +
                            "from van\n" +
                            "where van.partner_id is null\n" +
                            "        and van.regis_number not in (select van.regis_number\n" +
                            "                                                                        from van\n" +
                            "                                                                        join van_reserve_schedule\n" +
                            "                                                                        on van.regis_number = van_reserve_schedule.regis_number\n" +
                            "                                                                        join reservation\n" +
                            "                                                                        on reservation.id = van_reserve_schedule.reservation_id\n" +
                            "                                                                        where strftime(\"%%Y-%%m-%%d\", reservation.end_working_date) >= date(\"%s\") \n" +
                            "                                                                                        and strftime(\"%%Y-%%m-%%d\", reservation.start_working_date) <= date(\"%s\"))\n" +
                            "        and van.regis_number not in (select van.regis_number\n" +
                            "                                                                        from van\n" +
                            "                                                                        join van_job_schedule\n" +
                            "                                                                        on van_job_schedule.regis_id = van.regis_number\n" +
                            "                                                                        where strftime(\"%%Y-%%m-%%d\", van_job_schedule.end_date) >= date(\"%s\") \n" +
                            "                                                                                        and strftime(\"%%Y-%%m-%%d\", van_job_schedule.start_date) <= date(\"%s\"))\n" +
                            "group by type", start, end, start, end);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);

                    while (resultSet.next()){
                        amtMap.put(resultSet.getString("type"), resultSet.getInt("amt"));
                    }

                    return amtMap;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
        return null;
    }

    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        int vipAmt = vanAmt.get(VIP);
        int normalAmt = vanAmt.get(NORMAL);
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) diff / (24*60*60*1000);
        Connection connection = null;
        try{
            Map<String, Double> rate = new HashMap<String, Double>();
            Map<String, Double> base = new HashMap<String, Double>();
            Map<String, Double> freeRage = new HashMap<String, Double>();
            connection = prepareConnection();
            if (connection != null){
                String sql = "select * from price_rate where price_rate.reserve_type = \"distance\"";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    rate.put(resultSet.getString("van_type"), resultSet.getDouble("rate"));
                    base.put(resultSet.getString("van_type"), resultSet.getDouble("base"));
                    freeRage.put(resultSet.getString("van_type"), resultSet.getDouble("free_range"));
                }

                double normalPrice = base.get(NORMAL) + rate.get(NORMAL)*((days < freeRage.get(NORMAL))?0:(days-freeRage.get(NORMAL)));
                double vipPrice = base.get(VIP) + rate.get(VIP)*((days < freeRage.get(VIP))?0:(days-freeRage.get(VIP)));

                return normalPrice*normalAmt + vipPrice*vipAmt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return 0;
    }

    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        int vipAmt = vanAmt.get(VIP);
        int normalAmt = vanAmt.get(NORMAL);
        Connection connection = null;
        try{
            Map<String, Double> rate = new HashMap<String, Double>();
            Map<String, Double> base = new HashMap<String, Double>();
            Map<String, Double> freeRage = new HashMap<String, Double>();
            double distance = getDistance(destination);

            connection = prepareConnection();
            if (connection != null){
                String sql = "select * from price_rate where price_rate.reserve_type = \"day\"";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    rate.put(resultSet.getString("van_type"), resultSet.getDouble("rate"));
                    base.put(resultSet.getString("van_type"), resultSet.getDouble("base"));
                    freeRage.put(resultSet.getString("van_type"), resultSet.getDouble("free_range"));
                }

                double normalPrice = base.get(NORMAL) + rate.get(NORMAL)*((distance < freeRage.get(NORMAL))?0:(distance-freeRage.get(NORMAL)));
                double vipPrice = base.get(VIP) + rate.get(VIP)*((distance < freeRage.get(VIP))?0:(distance-freeRage.get(VIP)));

                return normalPrice*normalAmt + vipPrice*vipAmt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return 0;
    }

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, double price) {

    }

    public void editCustomerInfo(Customer customer) {

    }

    public void deleteReservation(Reservation reservation) {

    }

    public List<String> getProvinces() {
        List<String> provinces = new ArrayList<String>();
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "select distinct province from distance";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while(resultSet.next()){
                    String province = resultSet.getString("province");
                    provinces.add(province);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return provinces;
    }

    public List<String> getDistricts(String province) {
        List<String> districts = new ArrayList<String>();
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "select district from distance where province='" + province + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    String district = resultSet.getString("district");
                    districts.add(district);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return districts;
    }

    private double getDistance(Destination destination){
        Connection connection = null;
        try {
            connection = prepareConnection();
            if (connection != null){
                String sql = "select distance from distance where province='" + destination.getProvince() + "' and district='" + destination.getDistrict() + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next())
                    return resultSet.getDouble("distance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return 0;
    }

    private boolean checkPossibleDay(Destination destination, Date startDate, Date endDate){
        double distance = getDistance(destination);
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) diff / (24*60*60*1000);
        if (distance/720 > days)
            return false;
        return true;
    }

    private Connection prepareConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:" + url;
            Connection conn = DriverManager.getConnection(dbURL);

            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("connection Fail cannot find database");
        }
        return null;
    }
}
