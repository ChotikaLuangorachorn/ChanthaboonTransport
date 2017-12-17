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
    void editDriver(Driver driver);
    void deleteDriver(Driver driver);
    void assignDriver(List<Driver> drivers, Reservation reservation);
    void assignDriver(List<Driver> drivers, String reservationId);
    void deleteDriver(String citizenId);
    List<JobType> getDriverJobTypes();
    List<Schedule> getDriverSchedule(Driver driver);
    List<Schedule> getDriverSchedule(String citizenId);
    void deleteDriverSchedule(Schedule schedule);
    void editDriverSchedule(Schedule oldSchedule, Schedule newSchedule);
    void addDriverJob(Driver driver, Date startDate, Date endDate, JobType type);
}
