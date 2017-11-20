package controllers;

import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.lang.Nullable;
import utils.ReservationDateFormatter;

import java.sql.*;
import java.text.ParseException;
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
                    System.out.println("response");
                    System.out.println("customer = " + customer);
                    System.out.println();
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
        System.out.println("request getVanAvailable");
        Map<String, Integer> amtMap = new HashMap<String, Integer>();
        amtMap.put(CustomerDatabaseManager.VIP, 0);
        amtMap.put(CustomerDatabaseManager.NORMAL, 0);

        boolean possible = checkPossibleDay(destination, startDate, endDate);
        System.out.println("possible = " + possible);
        if (possible){
            Connection connection = null;
            try {
                connection = prepareConnection();
                if (connection != null){
                    SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();
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
                            "                                                                        on van_job_schedule.regis_number = van.regis_number\n" +
                            "                                                                        where strftime(\"%%Y-%%m-%%d\", van_job_schedule.end_date) >= date(\"%s\") \n" +
                            "                                                                                        and strftime(\"%%Y-%%m-%%d\", van_job_schedule.start_date) <= date(\"%s\"))\n" +
                            "group by type", start, end, start, end);
                    System.out.println("sql = " + sql);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);

                    while (resultSet.next()){
                        amtMap.put(resultSet.getString("type"), resultSet.getInt("amt"));
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
        }
        System.out.println("response = " + amtMap);
        return amtMap;
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

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit) {
        System.out.println("request addReservation");
        Connection connection = null;
        SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();
        try{
            String reserveDateString = formatter.format(reserveDate);
            String startDateString = formatter.format(startDate);
            String endDateString = formatter.format(endDate);

            connection = prepareConnection();
            if (connection != null) {
                String sql = String.format("insert into reservation (customer_id, reserve_date, start_working_date, end_working_date, fee, province, district, place, isDeposit, isDeposited, deposit_fee, amt_vip, amt_normal) values ('%s', '%s', '%s', '%s', %f, '%s', '%s', '%s', '%s', '%s', %f, %d, %d)",
                                            customerId, reserveDateString, startDateString, endDateString, price, destination.getProvince(), destination.getDistrict(), destination.getPlace(), "true", "false", deposit, vanAmt.get(CustomerDatabaseManager.VIP), vanAmt.get(CustomerDatabaseManager.NORMAL));
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                System.out.println("result = " + result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCustomerInfo(Customer customer) {
        System.out.println("request editCustomerInfo");
        System.out.println("customer = " + customer);
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = String.format("update customer " +
                                            "set first_name='%s' ," +
                                                "last_name='%s' ," +
                                                "address='%s' ," +
                                                "phone='%s' ," +
                                                "line_id='%s' " +
                                            "where citizen_id='%s'",
                                            customer.getFirstName(),
                                            customer.getLastName(),
                                            customer.getAddress(),
                                            customer.getPhone(),
                                            customer.getLineId(),
                                            customer.getCitizenId());
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                System.out.println("result = " + result);
                System.out.println();
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

    public void deleteReservation(Reservation reservation) {

    }

    public List<Reservation> getHistoryReservation(String citizenId) {
        List<Reservation> reservations = new ArrayList<Reservation>();
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "select * from reservation where customer_id = '" + citizenId + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()){
                    SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();
                    String id = resultSet.getString("id");
                    String customerId = resultSet.getString("customer_id");
                    Date statDate = formatter.parse(resultSet.getString("start_working_date"));
                    Date endDate = formatter.parse(resultSet.getString("end_working_date"));
                    Date reserveDate = formatter.parse(resultSet.getString("reserve_date"));
                    Date meetingTime = formatter.parse(resultSet.getString("meeting_time"));
                    String province = resultSet.getString("province");
                    String district = resultSet.getString("district");
                    String place = resultSet.getString("place");
                    String meetingPlace = resultSet.getString("meeting_place");
                    double fee = resultSet.getDouble("fee");
                    int amtVip = resultSet.getInt("amt_vip");
                    int amtNormal = resultSet.getInt("amt_normal");
                    String isDeposited = resultSet.getString("isDeposited");

                    Reservation reservation = new Reservation(id, customerId, meetingPlace, amtVip, amtNormal, new Destination(province, district, place), statDate, endDate, reserveDate, meetingTime, fee, isDeposited);
                    reservations.add(reservation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            if (connection!=null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return reservations;
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

    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd) {
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = String.format("update customer " +
                                            "set pwd='%s' " +
                                            "where citizen_id='%s' and pwd='%s'", newPwd, citizenId, oldPwd);
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                return result > 0;
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
        return false;
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
