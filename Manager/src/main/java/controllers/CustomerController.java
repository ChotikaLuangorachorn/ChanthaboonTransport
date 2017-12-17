package controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.CustomerService;

public class CustomerController {
    private CustomerService customerExecutor;
    public CustomerController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        customerExecutor = (CustomerService) bf.getBean("CustomerService");
    }
}
