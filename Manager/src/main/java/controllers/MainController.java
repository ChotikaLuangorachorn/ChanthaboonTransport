package controllers;

import javafx.scene.Parent;
import managers.ManagerDatabaseManager;
import models.Driver;
import models.Partner;
import models.Reservation;
import models.Van;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class MainController {
    private ManagerDatabaseManager executor;
    public MainController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        executor = (ManagerDatabaseManager) bf.getBean("ManagerDbManager");
    }

    public void confirmDeposit(String reservationId, Date depositDate){
        executor.confirmDeposit(reservationId, depositDate);
    }

    public List<Reservation> getReservation(){
        return executor.getReservations();
    }


    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate){
        return executor.getVanAvailable(startDate, endDate);
    }

    /**Driver*/
    public List<Driver> getDrivers(){
        return executor.getDrivers();
    }
    public  void deleteDriver(Driver driver){
        executor.deleteDriver(driver);
    }

    /**Partner*/
    public List<Partner> getPartners(){
        return executor.getPartners();
    }
    public void deletePartner(Partner partner){
        executor.deletePartner(partner);
    }

    /**Van*/
    public List<Van> getVans(){
        return executor.getVans();
    }
    public void deleteVan(Van van){
        executor.deleteVan(van);
    }
    public void editVan(Van van){
        executor.editVan(van);
    }
    public Van getVan(String regis){
        return executor.getVan(regis);
    }


}
