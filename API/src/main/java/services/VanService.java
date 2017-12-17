package services;

import models.Destination;
import models.JobType;
import models.Schedule;
import models.Van;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VanService {
    String VIP = "VIP";
    String NORMAL = "NORMAL";
    List<Van> getVans();
    Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate);
    Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate);
    boolean editVan(Van van);
    boolean deleteVan(Van van);
    boolean deleteVan(String regisNumber);
    Van getVan(String vanId);
    List<Schedule> getVanSchedule(String regisNumber);
    List<Schedule> getVanSchedule(Van van);
    boolean deleteVanSchedule(Schedule schedule);
    boolean editVanSchedule(Schedule oldSchedule, Schedule newSchedule);
    List<JobType> getVanJobTypes();
    boolean addVanJob(Van van, Date startDate, Date endDate, JobType type);
}
