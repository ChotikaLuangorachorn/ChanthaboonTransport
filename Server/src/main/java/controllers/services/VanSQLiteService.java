package controllers.services;

import models.JobType;
import models.Schedule;
import models.Van;
import services.VanService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class VanSQLiteService extends SQLiteService implements VanService{
    public VanSQLiteService(String url) {
        super(url);
    }

    @Override
    public List<Van> getVans() {
        return null;
    }

    @Override
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public void editVan(Van van) {

    }

    @Override
    public void deleteVan(Van van) {

    }

    @Override
    public void deleteVan(String regisNumber) {

    }

    @Override
    public Van getVan(String vanId) {
        return null;
    }

    @Override
    public List<Schedule> getVanSchedule(String regisNumber) {
        return null;
    }

    @Override
    public List<Schedule> getVanSchedule(Van van) {
        return null;
    }

    @Override
    public void deleteVanSchedule(Schedule schedule) {

    }

    @Override
    public void editVanSchedule(Schedule oldSchedule, Schedule newSchedule) {

    }

    @Override
    public List<JobType> getVanJobTypes() {
        return null;
    }

    @Override
    public void addVanJob(Van van, Date startDate, Date endDate, JobType type) {

    }
}
