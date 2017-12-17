package controllers.services;

import models.Driver;
import models.JobType;
import models.Reservation;
import models.Schedule;
import services.DriverService;

import java.util.Date;
import java.util.List;

public class DriverSQLiteService extends SQLiteService implements DriverService{
    public DriverSQLiteService(String url) {
        super(url);
    }

    @Override
    public List<Driver> getDriverAvailable(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public List<Driver> getDrivers() {
        return null;
    }

    @Override
    public void editDriver(Driver driver) {

    }

    @Override
    public void deleteDriver(Driver driver) {

    }

    @Override
    public void assignDriver(List<Driver> drivers, Reservation reservation) {

    }

    @Override
    public void assignDriver(List<Driver> drivers, String reservationId) {

    }

    @Override
    public void deleteDriver(String citizenId) {

    }

    @Override
    public List<JobType> getDriverJobTypes() {
        return null;
    }

    @Override
    public List<Schedule> getDriverSchedule(Driver driver) {
        return null;
    }

    @Override
    public List<Schedule> getDriverSchedule(String citizenId) {
        return null;
    }

    @Override
    public void deleteDriverSchedule(Schedule schedule) {

    }

    @Override
    public void editDriverSchedule(Schedule oldSchedule, Schedule newSchedule) {

    }

    @Override
    public void addDriverJob(Driver driver, Date startDate, Date endDate, JobType type) {

    }
}
