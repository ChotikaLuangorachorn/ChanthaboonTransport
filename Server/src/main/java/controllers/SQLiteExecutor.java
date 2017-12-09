package controllers;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import controllers.delegations.*;
import managers.CustomerDatabaseManager;
import managers.ManagerDatabaseManager;
import models.*;
import models.Driver;
import org.springframework.lang.Nullable;
import utils.ReservationDateFormatter;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SQLiteExecutor implements CustomerDatabaseManager, ManagerDatabaseManager   {
    private String url = "vanScheduler.db";
    private SimpleDateFormat formatter = ReservationDateFormatter.getInstance().getDbFormatter();
    private VanExecutor vanExecutor;
    private PriceExecutor priceExecutor;
    private DriverExecutor driverExecutor;
    private CustomerExecutor customerExecutor;
    private PartnerExecutor partnerExecutor;
    private ReservationExecutor reservationExecutor;

    public SQLiteExecutor() {
        vanExecutor = new VanExecutor(url);
        driverExecutor = new DriverExecutor(url);
        customerExecutor = new CustomerExecutor(url);
        partnerExecutor = new PartnerExecutor(url);
        reservationExecutor = new ReservationExecutor(url);
        priceExecutor = new PriceExecutor(url);
    }

    @Nullable
    public Customer getCustomer(String citizenId, String pwd) {
        return customerExecutor.getCustomer(citizenId, pwd);
    }
    @Nullable
    public Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate) {
        return vanExecutor.getVanAvailableAmount(destination, startDate, endDate);
    }

    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        return priceExecutor.getPrice(vanAmt, startDate, endDate);
    }

    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        return priceExecutor.getPrice(vanAmt, destination);
    }

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit) {
        reservationExecutor.addReservation(customerId, vanAmt, destination, startDate, endDate, reserveDate, price, deposit);
    }

    public Van getVan(String vanId){
        return vanExecutor.getVan(vanId);
    }

    @Override
    public List<Schedule> getVanSchedule(Van van) {
        return getVanSchedule(van.getRegisNumber());
    }

    @Override
    public List<Schedule> getVanSchedule(String regisNumber) {
        return vanExecutor.getVanSchedule(regisNumber);
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
        deleteReservation(reservation.getReserveId());
    }

    public void deleteReservation(String reservationId){
        System.out.println("request delete reservation");
        System.out.println("reservationId = " + reservationId);
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "delete from reservation where id='" + reservationId + "'";
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                System.out.println("result = " + result);
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

    public void assignVan(List<Van> vans, Reservation reservation) {
        assignVan(vans, reservation.getReserveId());
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
    }

    public void assignDriver(List<Driver> drivers, Reservation reservation) {
        assignDriver(drivers, reservation.getReserveId());
    }
    public void assignDriver(List<Driver> drivers, String reservationId) {
        String sql = String.format("insert into driver_reserve_schedule " +
                "select driver.citizen_id, '%s' " +
                "from driver " +
                "where driver.citizen_id in ", reservationId);
        List<String> driverIds = new ArrayList<>();
        for (Driver driver:drivers)
            driverIds.add("'" + driver.getCitizenId() + "'");
        sql += "(" + String.join(",", driverIds) + ")";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
    }

    public List<JobType> getVanJobs() {
        return vanExecutor.getVanJobs();
    }

    public void addVanJob(Van van, Date startDate, Date endDate, JobType type) {
        vanExecutor.addVanJob(van, startDate, endDate, type);
    }

    public void deleteVanJob(Van van, Date startDate, Date endDate) {
        vanExecutor.deleteVanJob(van, startDate, endDate);
    }

    public List<JobType> getDriverJobs() {
        return null;
    }

    public void addDriverJob(Van van, Date startDate, Date endDate, JobType type) {

    }

    public void deleteDriverJob(Van van, Date startDate, Date endDate) {

    }

    public void addMeeting(String meetingPlace, Date meetingTime, Reservation reservation) {
        addMeeting(meetingPlace, meetingTime, reservation.getReserveId());
    }
    public void addMeeting(String meetingPlace, Date meetingTime, String reservationId) {
        String sql = String.format("update reservation " +
                                    "set meeting_place='%s', meeting_time='%s' " +
                                    "where id='%s'",
                                    meetingPlace, formatter.format(meetingTime), reservationId);
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
    }

    public void confirmDeposit(Reservation reservation, Date depositDate) {
        confirmDeposit(reservation.getReserveId(), depositDate);
    }

    public void confirmDeposit(String reservationId, Date depositDate) {
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "update reservation set isDeposited='true', deposit_date='" + formatter.format(depositDate) + "' where id='" + reservationId + "'";
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
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

    public List<Reservation> getReservations() {
        List<Reservation> reservations = new ArrayList<Reservation>();
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "select * \n" +
                        "from reservation\n" +
                        "join customer\n" +
                        "on reservation.customer_id = customer.citizen_id";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

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
                    Reservation reservation = new Reservation(id, customerId, meetingPlace, amtVip, amtNormal, new Destination(province, district, place), statDate, endDate, reserveDate, meetingTime, fee, isDeposited, deposit);


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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public Reservation getReservation(String reserveId) {
        return null;
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

                    Reservation reservation = new Reservation(id, customerId, meetingPlace, amtVip, amtNormal, new Destination(province, district, place), statDate, endDate, reserveDate, meetingTime, fee, isDeposited, deposit);
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

    public List<Van> getVans() {
        return vanExecutor.getVans();
    }

    @Override
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate) {
        return vanExecutor.getVanAvailable(startDate, endDate);
    }

    public void editVan(Van van) {
        vanExecutor.editVan(van);
    }

    public void deleteVan(Van van) {
        deleteVan(van.getRegisNumber());
    }

    public void deleteVan(String regisNumber) {
        vanExecutor.deleteVan(regisNumber);
    }

    public List<Partner> getPartners() {
        List<Partner> partners = new ArrayList<Partner>();
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "select * from partner";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                while (resultSet.next()){
                    int id = Integer.parseInt(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    String company = resultSet.getString("company");
                    String companyPhone = resultSet.getString("company_phone");
                    Partner partner = new Partner(id, name, company, companyPhone);
                    partners.add(partner);
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
        return partners;
    }

    public void editPartner(Partner partner) {
        String sql = String.format("update partner " +
                                    "set name='%s'," +
                                        "company='%s'," +
                                        "company_phone='%s' " +
                                    "where id='%s'",
                                    partner.getName(), partner.getCompany(),
                                    partner.getCompanyPhone(), partner.getId());
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);

    }

    public void deletePartner(Partner partner) {
        deletePartner(partner.getId());
    }

    public void deletePartner(int partnerId) {
        System.out.println("request deletePartner");
        Connection connection = null;
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "delete from partner where id='" + partnerId + "'";
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                System.out.println("result");
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

    @Override
    public List<Driver> getDriverAvailable(Date startDate, Date endDate) {
        List<Driver> available = new ArrayList<>();
        String start = formatter.format(startDate);
        String end = formatter.format(endDate);
        QueryExecutionAssistant<List<Driver>> assistant = new QueryExecutionAssistant<>(url);
        String sql = String.format("select *\n" +
                                    "from driver\n" +
                                    "where driver.citizen_id not in (select driver.citizen_id\n" +
                                    "                                                                        from driver\n" +
                                    "                                                                        join driver_reserve_schedule\n" +
                                    "                                                                        on driver.citizen_id = driver_reserve_schedule.driver_id\n" +
                                    "                                                                        join reservation\n" +
                                    "                                                                        on reservation.id = driver_reserve_schedule.reservation_id\n" +
                                    "                                                                        where strftime(\"%%Y-%%m-%%d\", reservation.end_working_date) >= date(\"%s\") \n" +
                                    "                                                                                        and strftime(\"%%Y-%%m-%%d\", reservation.start_working_date) <= date(\"%s\"))\n" +
                                    "        and driver.citizen_id not in (select driver.citizen_id\n" +
                                    "                                                                        from driver\n" +
                                    "                                                                        join driver_job_schedule\n" +
                                    "                                                                        on driver.citizen_id  = driver_job_schedule.driver_id\n" +
                                    "                                                                        where strftime(\"%%Y-%%m-%%d\", driver_job_schedule.end_date) >= date(\"%s\") \n" +
                                    "                                                                                        and strftime(\"%%Y-%%m-%%d\", driver_job_schedule.start_date) <= date(\"%s\"))"
                , start, end, start, end);
        System.out.println("sql = " + sql);
        return assistant.execute(sql, (resultSet -> {
            while (resultSet.next()){
                String citizenId = resultSet.getString("citizen_id");
                String driverLicense = resultSet.getString("driver_license");
                Date dateOfBirth = (resultSet.getString("date_of_birth")!=null)?formatter.parse(resultSet.getString("date_of_birth")):null;
                String firstname  = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String nickname =  resultSet.getString("nick_name");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                available.add(new Driver(citizenId, driverLicense, dateOfBirth, firstname, lastname, nickname, phone, address));
            }
            return available;
        }), available);
    }

    public List<Driver> getDrivers() {
        Connection connection = null;
        List<Driver> drivers = new ArrayList<Driver>();
        try{
            connection = prepareConnection();
            if (connection != null){
                String sql = "select * from driver";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()){
                    String citizenId = resultSet.getString("citizen_id");
                    String driverLicense = resultSet.getString("driver_license");
                    Date dateOfBirth = formatter.parse(resultSet.getString("date_of_birth"));
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String nickname = resultSet.getString("nick_name");
                    String phone = resultSet.getString("phone");
                    String address = resultSet.getString("address");

                    drivers.add(new Driver(citizenId, driverLicense, dateOfBirth, firstName, lastName, nickname, phone, address));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return drivers;
    }

    public void editDriver(Driver driver) {
        String sql = String.format("update driver " +
                                    "set driver_license='%s'," +
                                        "date_of_birth='%s'," +
                                        "first_name='%s'," +
                                        "last_name='%s'," +
                                        "nick_name='%s'," +
                                        "phone='%s'," +
                                        "address='%s' " +
                                    "where citizen_id='%s'",
                                    driver.getDriverLicense(), formatter.format(driver.getDateOfBirth()),
                                    driver.getFirstname(), driver.getLastname(), driver.getNickname(),
                                    driver.getPhone(), driver.getAddress(), driver.getCitizenId());
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
    }

    public void deleteDriver(Driver driver) {
        deleteDriver(driver.getCitizenId());
    }

    public void deleteDriver(String citizenId) {
        Connection connection = null;
        try {
            connection = prepareConnection();
            if (connection != null){
                String sql = "delete from driver where citizen_id='" + citizenId + "'";
                Statement statement = connection.createStatement();
                int result = statement.executeUpdate(sql);
                System.out.println(result);
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

    public void editReservation(Reservation reservation) {

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

    @Override
    public PriceFactor getPriceFactor() {
        String sql = "select * from price_rate";
        final Map<String, Double> map = new HashMap<>();
        QueryExecutionAssistant<PriceFactor> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {
            PriceFactor factor = new PriceFactor();
            while(resultSet.next()){
                String rtype = resultSet.getString("reserve_type");
                String vtype = resultSet.getString("van_type");
                double rate = resultSet.getDouble("rate");
                double base = resultSet.getDouble("base");
                double free = resultSet.getDouble("free_range");

                if ("day".equals(rtype)){
                    if ("VIP".equals(vtype)){
                        factor.setFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.VIP, PriceFactor.FREE, free);
                    }else{
                        factor.setFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DAY, PriceFactor.NORMAL, PriceFactor.FREE, free);
                    }
                }else{
                    if ("VIP".equals(vtype)){
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.VIP, PriceFactor.FREE, free);
                    }else{
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.RATE, rate);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.BASE, base);
                        factor.setFactor(PriceFactor.DISTANCE, PriceFactor.NORMAL, PriceFactor.FREE, free);
                    }
                }
            }
            return factor;
        }, null);
    }

    @Override
    public void updatePriceFactor(PriceFactor factor) {
        System.out.println("request update price factor");
        String[] rtypes = {"day", "distance"};
        String[] vtype = {"VIP", "NORMAL"};
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++){
                double base = factor.getFactor(i+1, (j+1)*10, 100);
                double rate = factor.getFactor(i+1, (j+1)*10, 200);
                double free = factor.getFactor(i+1, (j+1)*10, 300);
                String sql = createUpdatePriceFactorQuery(rtypes[i], vtype[j], base, rate, free);
                System.out.println(sql);
                assistant.execute(sql);
            }
        }
    }

    private String createUpdatePriceFactorQuery(String rtype, String vtype, double base, double rate,  double free){
        return String.format("update price_rate set rate='%f', base='%f', free_range='%f' where reserve_type='%s' and van_type='%s'",
                            rate, base, free, rtype, vtype);
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
