import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;

import java.sql.*;
import java.util.Date;
import java.util.Map;

public class SQLiteExecutor implements CustomerDatabaseManager {
    private String url = "vanScheduler.db";
    public Customer getCustomer(String citizenId, String pwd) {
        Connection connection;
        try{
            connection = prepareConnection();

            if (connection != null){
                String sql = "select * from customer where citizen_id = '" + citizenId + "' and pwd = '" + pwd + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()){
//                    Customer customer = new Customer()
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, Integer> getVanAvailable(Destination destination, Date startDate, Date endDate) {
        return null;
    }

    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
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
