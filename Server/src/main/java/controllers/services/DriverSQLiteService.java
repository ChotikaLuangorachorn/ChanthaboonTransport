package controllers.services;

import models.Driver;
import models.JobType;
import models.Reservation;
import models.Schedule;
import services.DriverService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DriverSQLiteService extends SQLiteService implements DriverService{
    public DriverSQLiteService(String url) {
        super(url);
    }

    @Override
    public List<Driver> getDriverAvailable(Date startDate, Date endDate) {
        List<Driver> available = new ArrayList<>();
        String start = formatter.format(startDate);
        String end = formatter.format(endDate);
        String sql = String.format("select *\n" +
                        "from driver\n" +
                        "where driver.citizen_id not in (select driver.citizen_id\n" +
                        "                                                                        from driver\n" +
                        "                                                                        join driver_reserve_schedule\n" +
                        "                                                                        on driver.citizen_id = driver_reserve_schedule.driver_id\n" +
                        "                                                                        join reservation\n" +
                        "                                                                        on reservation.id = driver_reserve_schedule.reservation_id\n" +
                        "                                                                        where strftime(\"%%Y-%%m-%%d\", reservation.end_working_date) >= date(\"%s\") \n" +
                        "                                                                                        and strftime(\"%%Y-%%m-%%d\", reservation.start_working_date) <= date(\"%s\"))\n" +
                        "        and driver.citizen_id not in (select driver.citizen_id\n" +
                        "                                                                        from driver\n" +
                        "                                                                        join driver_job_schedule\n" +
                        "                                                                        on driver.citizen_id  = driver_job_schedule.driver_id\n" +
                        "                                                                        where strftime(\"%%Y-%%m-%%d\", driver_job_schedule.end_date) >= date(\"%s\") \n" +
                        "                                                                                        and strftime(\"%%Y-%%m-%%d\", driver_job_schedule.start_date) <= date(\"%s\"))"
                , start, end, start, end);
        return executeQuery(sql, (resultSet -> {
            while (resultSet.next()){
                String citizenId = resultSet.getString("citizen_id");
                String driverLicense = resultSet.getString("driver_license");
                Date dateOfBirth = (resultSet.getString("date_of_birth")!=null)?formatter.parse(resultSet.getString("date_of_birth")):null;
                String firstname  = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String nickname =  resultSet.getString("nick_name");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                available.add(new Driver(citizenId, lastname, nickname, driverLicense, dateOfBirth, firstname,  phone, address));
            }
            return available;
        }), available);
    }

    @Override
    public List<Driver> getDrivers() {
        List<Driver> drivers = new ArrayList<Driver>();
        String sql = "select * from driver";
        return executeQuery(sql, resultSet -> {
            while (resultSet.next()){
                String citizenId = resultSet.getString("citizen_id");
                String driverLicense = resultSet.getString("driver_license");
                Date dateOfBirth = formatter.parse(resultSet.getString("date_of_birth"));
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String nickname = resultSet.getString("nick_name");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");

                drivers.add(new Driver(citizenId, firstName, lastName, driverLicense, dateOfBirth,  nickname, phone, address));
            }
            return drivers;
        }, drivers);
    }

    @Override
    public boolean editDriver(Driver driver) {
        String sql = String.format("update driver " +
                        "set driver_license='%s'," +
                        "date_of_birth='%s'," +
                        "first_name='%s'," +
                        "last_name='%s'," +
                        "nick_name='%s'," +
                        "phone='%s'," +
                        "address='%s' " +
                        "where citizen_id='%s'",
                driver.getDriverLicense(), formatter.format(driver.getDateOfBirth()),
                driver.getFirstName(), driver.getLastName(), driver.getNickname(),
                driver.getPhone(), driver.getAddress(), driver.getId());
        int result = executeUpdate(sql);
        return result>0;
    }

    @Override
    public boolean deleteDriver(Driver driver) {
        return deleteDriver(driver.getId());
    }


    @Override
    public boolean deleteDriver(String citizenId) {
        String sql = "delete from driver where citizen_id='" + citizenId + "'";
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public List<JobType> getDriverJobTypes() {
        List<JobType> jobTypes = new ArrayList<>();
        String sql = "select * from driver_job_type";
        return executeQuery(sql, resultSet -> {
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                JobType jobType = new JobType(id, description);
                jobTypes.add(jobType);
            }
            return jobTypes;
        }, jobTypes);
    }

    @Override
    public List<Schedule> getDriverSchedule(Driver driver) {
        return getDriverSchedule(driver.getId());
    }

    @Override
    public List<Schedule> getDriverSchedule(String citizenId) {
        System.out.println("request get driver schedules");
        System.out.println("citizenId = " + citizenId);
        List<Schedule> schedules = new ArrayList<>();
        String sql = "select * " +
                "from driver_job_schedule " +
                "join driver_job_type " +
                "on driver_job_schedule.type_id = driver_job_type.id " +
                "where driver_id='"+citizenId+"'";
        executeQuery(sql, (resultSet -> {
            while(resultSet.next()){
                Date startDate = formatter.parse(resultSet.getString("start_date"));
                Date endDate = formatter.parse(resultSet.getString("end_date"));
                String note = resultSet.getString("description");
                schedules.add(new Schedule(citizenId, startDate, endDate, note, Schedule.JOB));
            }
            return null;
        }), null);
        String sql2 = "select reservation.id, reservation.start_working_date, reservation.end_working_date\n" +
                "from reservation\n" +
                "join driver_reserve_schedule\n" +
                "on reservation.id = driver_reserve_schedule.reservation_id\n" +
                "where driver_id = '" + citizenId + "'";
        System.out.println(sql2);
        return executeQuery(sql2, (resultSet -> {
            while(resultSet.next()){
                Date startDate = formatter.parse(resultSet.getString("start_working_date"));
                Date endDate = formatter.parse(resultSet.getString("end_working_date"));
                String note = resultSet.getString("id");
                schedules.add(new Schedule(citizenId, startDate, endDate, note, Schedule.RESERVE));
            }
            System.out.println("schedules = " + schedules);
            return schedules;
        }), schedules);
    }

    @Override
    public boolean deleteDriverSchedule(Schedule schedule) {
        String sql;
        String startTime = formatter.format(schedule.getStartDate());
        String endTime = formatter.format(schedule.getEndDate());
        if (Schedule.JOB.equals(schedule.getType())){
            sql = "delete from driver_job_schedule " +
                    "where driver_id='" + schedule.getId() + "' and " +
                    "start_date='" + startTime + "' and " +
                    "end_date='" + endTime + "' ";
        }else{
            sql = "delete from driver_reserve_schedule " +
                    "where driver_id='" + schedule.getId() + "' and " +
                    "reservation_id='" + schedule.getNote() + "'";
        }
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public boolean editDriverSchedule(Schedule oldSchedule, Schedule newSchedule) {
        String sql;
        if (Schedule.JOB.equals(newSchedule.getType())){
            String oldStartTime = formatter.format(oldSchedule.getStartDate());
            String oldEndTime = formatter.format(oldSchedule.getEndDate());
            String newStartTime = formatter.format(newSchedule.getStartDate());
            String newEndTime = formatter.format(newSchedule.getEndDate());
            sql = String.format("update driver_job_schedule\n" +
                            "set type_id=(select id\n" +
                            "from driver_job_type\n" +
                            "where description='%s'), start_date='%s', end_date='%s'\n" +
                            "where driver_id='%s' and start_date='%s' and end_date='%s'",
                    newSchedule.getNote(), newStartTime, newEndTime, oldSchedule.getId(), oldStartTime, oldEndTime);
        }else{
            sql = String.format("update driver_reserve_schedule " +
                    "set reservation_id='" + newSchedule.getNote() + "' " +
                    "where driver_id='" + oldSchedule.getId() + "' and " +
                    "reservation_id='" + oldSchedule.getNote() + "'");
        }
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public boolean addDriverJob(Driver driver, Date startDate, Date endDate, JobType type) {
        String startS = formatter.format(startDate);
        String endS = formatter.format(endDate);
        String sql = String.format("insert into driver_job_schedule " +
                        "values ('%s', '%s', '%s', '%s')",
                driver.getId(), startS, endS, type.getId()+"");
        int result = executeUpdate(sql);
        return result > 0;
    }
}
