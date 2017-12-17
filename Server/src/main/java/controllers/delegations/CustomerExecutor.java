package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import models.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerExecutor {
    private String url;

    public CustomerExecutor(String url) {
        this.url = url;
    }

    public Customer getCustomer(String citizenId, String pwd) {
        System.out.println("request getCustomer");
        System.out.println("citizenId = " + citizenId);
        System.out.println("pwd = " + pwd);

        QueryExecutionAssistant<Customer> assistant = new QueryExecutionAssistant<Customer>(url);
        String sql = "select * from customer where citizen_id = '" + citizenId + "' and pwd = '" + pwd + "'";
        return assistant.execute(sql, (resultSet)->{
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
    public void editCustomerInfo(Customer customer) {
        System.out.println("request editCustomerInfo");
        System.out.println("customer = " + customer);
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
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
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public boolean changeCustomerPassword(String citizenId, String oldPwd, String newPwd) {
        String sql = String.format("update customer " +
                "set pwd='%s' " +
                "where citizen_id='%s' and pwd='%s'", newPwd, citizenId, oldPwd);
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        return result > 0;
    }
}
