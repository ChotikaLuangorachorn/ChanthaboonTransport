package controllers;

import models.JobType;
import models.Reservation;
import models.Schedule;
import models.Van;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.ReservationService;
import services.VanService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class VanController {
    private VanService vanExecutor;
    public VanController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        vanExecutor = (VanService) bf.getBean("VanService");
    }
    public List<Van> getVans()
    {
        return vanExecutor.getVans();
    }
    public void deleteVan(Van van){
        vanExecutor.deleteVan(van);
    }
    public void editVan(Van van){
        vanExecutor.editVan(van);
    }
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate){
        return vanExecutor.getVanAvailable(startDate, endDate);
    }

    public Van getVan(String vanId){
        return vanExecutor.getVan(vanId);
    }
    public List<Schedule> getVanSchedule(String regisNumber){
        return vanExecutor.getVanSchedule(regisNumber);
    }
    public void deleteVanSchedule(Schedule schedule){
        vanExecutor.deleteVanSchedule(schedule);
    }
    public void editVanSchedule(Schedule oldSchedule, Schedule newSchedule){
        vanExecutor.editVanSchedule(oldSchedule,newSchedule);
    }
    public void addVanJob(Van van, Date startDate, Date endDate, JobType jobType){
        vanExecutor.addVanJob(van,startDate,endDate,jobType);
    }
    public List<JobType> getVanJobTypes(){
        return vanExecutor.getVanJobTypes();
    }

}
