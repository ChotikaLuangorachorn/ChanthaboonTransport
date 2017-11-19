package controller;

import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainController {

    private CustomerDatabaseManager executor;
    public MainController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        executor = (CustomerDatabaseManager) bf.getBean("CustomerDbManager");
    }

    public Customer getCustomer(String citizenId, String pwd) {
        return executor.getCustomer(citizenId, pwd);
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
        executor.editCustomerInfo(customer);

    }

    public void deleteReservation(Reservation reservation) {

    }

    public List<Reservation> getHistoryReservation(String citizenId){
        return executor.getHistoryReservation(citizenId);
    }
}
