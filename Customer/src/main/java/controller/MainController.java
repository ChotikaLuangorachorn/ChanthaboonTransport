package controller;

import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;

import java.util.Date;
import java.util.Map;

public class MainController implements CustomerDatabaseManager {


    public Customer getCustomer(String citizenId, String pwd) {
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
}
