package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import models.Customer;

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

}
