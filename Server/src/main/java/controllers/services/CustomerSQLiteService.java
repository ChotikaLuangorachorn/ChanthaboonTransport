package controllers.services;

import models.Customer;
import services.CustomerService;

public class CustomerSQLiteService extends SQLiteService implements CustomerService{
    public CustomerSQLiteService(String url) {
        super(url);
    }

    @Override
    public void editCustomerInfo(Customer customer) {

    }

    @Override
    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd) {
        return false;
    }

    @Override
    public Customer getCustomer(String citizenId, String pwd) {
        return null;
    }
}
