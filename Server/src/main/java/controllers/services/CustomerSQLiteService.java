package controllers.services;

import models.Customer;
import services.CustomerService;

public class CustomerSQLiteService extends SQLiteService implements CustomerService{
    public CustomerSQLiteService(String url) {
        super(url);
    }

    @Override
    public void editCustomerInfo(Customer customer) {
        System.out.println("request editCustomerInfo");
        System.out.println("customer = " + customer);
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
                customer.getId());
        int result = executeUpdate(sql);
        System.out.println("result = " + result);
    }

    @Override
    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd) {
        String sql = String.format("update customer " +
                "set pwd='%s' " +
                "where citizen_id='%s' and pwd='%s'", newPwd, citizenId, oldPwd);
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public Customer getCustomer(String citizenId, String pwd) {
        System.out.println("request getCustomer");
        System.out.println("citizenId = " + citizenId);
        System.out.println("pwd = " + pwd);


        String sql = "select * from customer where citizen_id = '" + citizenId + "' and pwd = '" + pwd + "'";
        return executeQuery(sql, (resultSet)->{
            if (resultSet.next()){
                String id = resultSet.getString("citizen_id");
                String firstname = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String address = resultSet.getString("address");
                String phone = resultSet.getString("phone");
                String lineId = resultSet.getString("line_id");
                int lastReserveId = resultSet.getInt("last_reserve");
                Customer customer = new Customer(id, firstname, lastname, address, phone, lineId, lastReserveId);
                System.out.println("response");
                System.out.println("customer = " + customer);
                System.out.println();
                return customer;
            }
            return null;
        }, null);
    }
}
