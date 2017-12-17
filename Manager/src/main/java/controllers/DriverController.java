package controllers;

import models.Driver;
import models.JobType;
import models.Reservation;
import models.Schedule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.CustomerService;
import services.DriverService;

import java.util.Date;
import java.util.List;

public class DriverController {
    private DriverService driverExecutor;
    public DriverController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        driverExecutor = (DriverService) bf.getBean("DriverService");
    }
    public List<Driver> getDrivers(){
        return driverExecutor.getDrivers();
    }
    public  void deleteDriver(Driver driver){
        driverExecutor.deleteDriver(driver);
    }
    public List<Driver> getDriverAvailable(Date startDate, Date endDate){
        return driverExecutor.getDriverAvailable(startDate, endDate);
    }
    public void editDriver(Driver driver){
        driverExecutor.editDriver(driver);
    }

    public List<Schedule> getDriverSchedule(String citizenId){
        return driverExecutor.getDriverSchedule(citizenId);
    }
    public void deleteDriverSchedule(Schedule schedule){
        driverExecutor.deleteDriverSchedule(schedule);
    }
    public void editDriverSchedule(Schedule oldSchedule, Schedule newSchedule){
        driverExecutor.editDriverSchedule(oldSchedule, newSchedule);
    }
    public void addDriverJob(Driver driver, Date startDate, Date endDate, JobType jobType){
        driverExecutor.addDriverJob(driver,startDate,endDate,jobType);
    }
    public List<JobType> getDriverJobTypes(){
        return driverExecutor.getDriverJobTypes();
    }
}
