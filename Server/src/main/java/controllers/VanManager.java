package controllers;

import managers.CustomerDatabaseManager;
import managers.ManagerDatabaseManager;
import models.Destination;
import models.JobType;
import models.Van;
import org.springframework.lang.Nullable;
import utils.ReservationDateFormatter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class VanManager {
    private String url;
    private SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();

    public VanManager(String url) {
        this.url = url;
    }

    @Nullable
    public Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate) {
        // TODO use assistant
        System.out.println("request getVanAvailableAmount");
        Map<String, Integer> amtMap = new HashMap<>();
        amtMap.put(CustomerDatabaseManager.VIP, 0);
        amtMap.put(CustomerDatabaseManager.NORMAL, 0);
        boolean possible = checkPossibleDay(destination, startDate, endDate);
        if (possible){
            String start = formatter.format(startDate);
            String end = formatter.format(endDate);
            QueryExecutionAssistant<Map<String, Integer>> assistant = new QueryExecutionAssistant<>(url);
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
            return assistant.execute(sql, resultSet -> {
                while (resultSet.next()){
                    amtMap.put(resultSet.getString("type"), resultSet.getInt("amt"));
                }
                return amtMap;
            }, amtMap);
        }
        return null;
    }
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate) {
        Map<String, List<Van>> available = new HashMap<>();
        available.put(ManagerDatabaseManager.VIP, new ArrayList<>());
        available.put(ManagerDatabaseManager.NORMAL, new ArrayList<>());
        String start = formatter.format(startDate);
        String end = formatter.format(endDate);
        QueryExecutionAssistant<Map<String, List<Van>>> assistant = new QueryExecutionAssistant<>(url);
        String sql = String.format("select *\n" +
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
                        "                                                                                        and strftime(\"%%Y-%%m-%%d\", van_job_schedule.start_date) <= date(\"%s\"))\n"
                , start, end, start, end);
        System.out.println(sql);
        return assistant.execute(sql, (resultSet -> {
            while (resultSet.next()){
                String id = resultSet.getString("regis_number");
                String type = resultSet.getString("type");
                String name = resultSet.getString("name");
                Van van = new Van(id, null, type, name);
                if (ManagerDatabaseManager.VIP.equals(type))
                    available.get(ManagerDatabaseManager.VIP).add(van);
                else
                    available.get(ManagerDatabaseManager.NORMAL).add(van);
            }
            return available;
        }), available);
    }
    public Van getVan(String vanId){
        String sql = "select * from van where regis_id='" + vanId + "'";
        QueryExecutionAssistant<Van> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, (resultSet -> {
            if (resultSet.next()){
                String regisNumber = resultSet.getString("regis_id");
                String name = resultSet.getString("name");
                String partnerId = resultSet.getString("partner_id");
                String type = resultSet.getString("type");
                return new Van(regisNumber, partnerId, type, name);
            }
            return null;
        }), null);
    }
    public List<JobType> getVanJobs() {
        return null;
    }
    public void addVanJob(Van van, Date startDate, Date endDate, JobType type) {

    }
    public void deleteVanJob(Van van, Date startDate, Date endDate) {

    }
    public void editVan(Van van) {
        System.out.println("request edit van");
        String sql = String.format("update van " +
                        "set name='%s', partner_id='%s', type=%s " +
                        "where regis_number='%s'",
                van.getName(),
                (van.getPartnerId()==null)?"null":"'"+van.getPartnerId()+"'",
                van.getType(), van.getRegisNumber());
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println(result);
    }
    public void deleteVan(String regisNumber) {
        System.out.println("request deleteVan");
        System.out.println("regisNumber = " + regisNumber);
        String sql = "delete from van where regis_number='" + regisNumber + "'";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println(result);
    }
    public List<Van> getVans() {
        String sql = "select * from van where partner_id is null";
        List<Van> vans = new ArrayList<>();
        QueryExecutionAssistant<List<Van>> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, (resultSet -> {
            while(resultSet.next()){
                String regisId = resultSet.getString("regis_number");
                String type = resultSet.getString("type");
                String name = resultSet.getString("name");
                Van van = new Van(regisId, null, type, name);
                vans.add(van);
            }
            return vans;
        }), vans);
    }

    private boolean checkPossibleDay(Destination destination, Date startDate, Date endDate){
        double distance = getDistance(destination);
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) diff / (24*60*60*1000);
        if (distance/720 > days)
            return false;
        return true;
    }

    private double getDistance(Destination destination){
        // TODO use assistant
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

    private Connection prepareConnection(){
        // TODO remove when refactor all method
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
