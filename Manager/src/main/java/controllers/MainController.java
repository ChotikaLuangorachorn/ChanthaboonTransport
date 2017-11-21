package controllers;

import managers.ManagerDatabaseManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;


public class MainController {
    private ManagerDatabaseManager executor;
    public MainController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        executor = (ManagerDatabaseManager) bf.getBean("ManagerDbManager");
    }

    public void confirmDeposit(String reservationId, Date depositDate){
        executor.confirmDeposit(reservationId, depositDate);
    }

}
