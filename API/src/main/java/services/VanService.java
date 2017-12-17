package services;

import models.Destination;
import models.JobType;
import models.Schedule;
import models.Van;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VanService {
    List<Van> getVans();
    Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate);
    Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate);
    void editVan(Van van);
    void deleteVan(Van van);
    void deleteVan(String regisNumber);
    Van getVan(String vanId);
    List<Schedule> getVanSchedule(String regisNumber);
    List<Schedule> getVanSchedule(Van van);
    void deleteVanSchedule(Schedule schedule);
    void editVanSchedule(Schedule oldSchedule, Schedule newSchedule);
    List<JobType> getVanJobTypes();
    void addVanJob(Van van, Date startDate, Date endDate, JobType type);
}
