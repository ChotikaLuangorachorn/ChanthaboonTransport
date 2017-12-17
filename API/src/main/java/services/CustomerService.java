package services;

import models.Customer;

public interface CustomerService {
    boolean editCustomerInfo(Customer customer);
    boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd);
    Customer getCustomer(String citizenId, String pwd);
}
