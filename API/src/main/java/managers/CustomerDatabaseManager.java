package managers;

import models.Customer;
import models.Destination;
import models.Reservation;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomerDatabaseManager {
    String VIP = "VIP";
    String NORMAL = "NORMAL";

    Customer getCustomer(String citizenId, String pwd);
    Map<String, Integer> getVanAvailable(Destination destination, Date startDate, Date endDate);
    double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate);
    double getPrice(Map<String, Integer> vanAmt, Destination destination);
    void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, double price);
    void editCustomerInfo(Customer customer);
    void deleteReservation(Reservation reservation);
    List<Reservation> getHistoryReservation(String citizenId);
    List<String> getProvinces();
    List<String> getDistricts(String province);
    boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd);

}
