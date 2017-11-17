import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.lang.Nullable;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SQLiteExecutor implements CustomerDatabaseManager {
    private String url = "vanScheduler.db";

    @Nullable
    public Customer getCustomer(String citizenId, String pwd) {
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

                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        return 0;
    }

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, double price) {

    }

    public void editCustomerInfo(Customer customer) {

    }

    public void deleteReservation(Reservation reservation) {

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
