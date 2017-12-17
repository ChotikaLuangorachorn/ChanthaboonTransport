package services;

import models.Driver;
import models.JobType;
import models.Reservation;
import models.Schedule;

import java.util.Date;
import java.util.List;

public interface DriverService {
    List<Driver> getDriverAvailable(Date startDate, Date endDate);
    List<Driver> getDrivers();
    boolean editDriver(Driver driver);
    boolean deleteDriver(Driver driver);

    boolean deleteDriver(String citizenId);
    List<JobType> getDriverJobTypes();
    List<Schedule> getDriverSchedule(Driver driver);
    List<Schedule> getDriverSchedule(String citizenId);
    boolean deleteDriverSchedule(Schedule schedule);
    boolean editDriverSchedule(Schedule oldSchedule, Schedule newSchedule);
    boolean addDriverJob(Driver driver, Date startDate, Date endDate, JobType type);
}
