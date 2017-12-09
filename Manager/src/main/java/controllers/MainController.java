package controllers;

import javafx.scene.Parent;
import managers.ManagerDatabaseManager;
import models.*;
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


    public void addMeeting(String meetingPlace, Date meetingTime, Reservation reservation){
        executor.addMeeting(meetingPlace, meetingTime, reservation);
    }



    /**Driver*/
    public List<Driver> getDrivers(){
        return executor.getDrivers();
    }
    public  void deleteDriver(Driver driver){
        executor.deleteDriver(driver);
    }
    public List<Driver> getDriverAvailable(Date startDate, Date endDate){
        return executor.getDriverAvailable(startDate, endDate);
    }
    public void editDriver(Driver driver){
        executor.editDriver(driver);
    }

    public void assignDriver(List<Driver> drivers, Reservation reservation){
        executor.assignDriver(drivers, reservation);
    }

    /**Partner*/
    public List<Partner> getPartners(){
        return executor.getPartners();
    }
    public void deletePartner(Partner partner){
        executor.deletePartner(partner);
    }
    public void editPartner(Partner partner){
        executor.editPartner(partner);
    }

    /**Van*/
    public List<Van> getVans()
    {
        return executor.getVans();
    }
    public void deleteVan(Van van){
        executor.deleteVan(van);
    }
    public void editVan(Van van){
        executor.editVan(van);
    }
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate){
        return executor.getVanAvailable(startDate, endDate);
    }
    public void assignVan(List<Van> vans, Reservation reservation){
        executor.assignVan(vans, reservation);
    }
    public Van getVan(String vanId){
        return executor.getVan(vanId);
    }
    public List<Schedule> getVanSchedule(String regisNumber){
        return executor.getVanSchedule(regisNumber);
    }

    /**Fee*/
    public PriceFactor getPriceFactor(){
        return executor.getPriceFactor();
    }
    public void updatePriceFactor(PriceFactor priceFactor){
        executor.updatePriceFactor(priceFactor);
    }
}
