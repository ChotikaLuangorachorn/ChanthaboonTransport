package controller;

import managers.CustomerDatabaseManager;
import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        return executor.getVanAvailable(destination, startDate, endDate);
    }

    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        return executor.getPrice(vanAmt, startDate, endDate);
    }

    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        return executor.getPrice(vanAmt, destination);
    }

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, double price) {
        LocalDateTime localDate = LocalDateTime.now();
        Instant instant = Instant.from(localDate.atZone(ZoneId.systemDefault()));
        executor.addReservation(customerId, vanAmt, destination, startDate, endDate, Date.from(instant), price, price/2);
    }
    public List<String> getProvince(){
        return executor.getProvinces();
    }

    public List<String> getdistrict(String province){
        return executor.getDistricts(province);
    }

    public void editCustomerInfo(Customer customer) {
        executor.editCustomerInfo(customer);

    }

    public void deleteReservation(String reserveId) {
        executor.deleteReservation(reserveId);
    }

    public List<Reservation> getHistoryReservation(String citizenId){
        return executor.getHistoryReservation(citizenId);
    }

    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd){
        return executor.changeCustomerPassword(citizenId, oldPwd, newPwd);
    }
}
