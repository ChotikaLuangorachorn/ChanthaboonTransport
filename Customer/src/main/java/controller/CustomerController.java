package controller;

import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.CustomerService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomerController {
    private CustomerService customerExecutor;
    public CustomerController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        customerExecutor = (CustomerService) bf.getBean("CustomerService");
    }

    public Customer getCustomer(String citizenId, String pwd) {
        return customerExecutor.getCustomer(citizenId, pwd);
    }

    public void editCustomerInfo(Customer customer) {
        customerExecutor.editCustomerInfo(customer);

    }


    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd){
        return customerExecutor.changeCustomerPassword(citizenId, oldPwd, newPwd);
    }

}
