package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import models.Driver;
import models.JobType;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DriverExecutor {
    private String url;
    private SimpleDateFormat formatter;

    public DriverExecutor(String url) {
        this.url = url;
        formatter = ReservationDateFormatter.getInstance().getDbFormatter();
    }
    public List<Schedule> getDriverSchedule(String citizenId) {
        System.out.println("request get driver schedules");
        System.out.println("citizenId = " + citizenId);
        List<Schedule> schedules = new ArrayList<>();
        String sql = "select * " +
                "from driver_job_schedule " +
                "join driver_job_type " +
                "on driver_job_schedule.type_id = driver_job_type.id " +
                "where driver_id='"+citizenId+"'";
        System.out.println("sql = " + sql);
        QueryExecutionAssistant<List<Schedule>> assistant = new QueryExecutionAssistant<>(url);
        assistant.execute(sql, (resultSet -> {
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
        return assistant.execute(sql2, (resultSet -> {
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
    public List<JobType> getDriverJobTypes() {
        List<JobType> jobTypes = new ArrayList<>();
        String sql = "select * from driver_job_type";
        QueryExecutionAssistant<List<JobType>> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String description = resultSet.getString("description");
                JobType jobType = new JobType(id, description);
                jobTypes.add(jobType);
            }
            return jobTypes;
        }, jobTypes);
    }
    public void deleteDriverSchedule(Schedule schedule) {
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
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public List<Driver> getDriverAvailable(Date startDate, Date endDate) {
        List<Driver> available = new ArrayList<>();
        String start = formatter.format(startDate);
        String end = formatter.format(endDate);
        QueryExecutionAssistant<List<Driver>> assistant = new QueryExecutionAssistant<>(url);
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
        System.out.println("sql = " + sql);
        return assistant.execute(sql, (resultSet -> {
            while (resultSet.next()){
                String citizenId = resultSet.getString("citizen_id");
                String driverLicense = resultSet.getString("driver_license");
                Date dateOfBirth = (resultSet.getString("date_of_birth")!=null)?formatter.parse(resultSet.getString("date_of_birth")):null;
                String firstname  = resultSet.getString("first_name");
                String lastname = resultSet.getString("last_name");
                String nickname =  resultSet.getString("nick_name");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                available.add(new Driver(citizenId, driverLicense, dateOfBirth, firstname, lastname, nickname, phone, address));
            }
            return available;
        }), available);
    }
    public List<Driver> getDrivers() {
        List<Driver> drivers = new ArrayList<Driver>();
        String sql = "select * from driver";
        QueryExecutionAssistant<List<Driver>> assistant = new QueryExecutionAssistant<>(url);
        return assistant.execute(sql, resultSet -> {

            while (resultSet.next()){
                String citizenId = resultSet.getString("citizen_id");
                String driverLicense = resultSet.getString("driver_license");
                Date dateOfBirth = formatter.parse(resultSet.getString("date_of_birth"));
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String nickname = resultSet.getString("nick_name");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");

                drivers.add(new Driver(citizenId, driverLicense, dateOfBirth, firstName, lastName, nickname, phone, address));
            }
            return drivers;
        }, drivers);
    }
    public void editDriverSchedule(Schedule oldSchedule, Schedule newSchedule) {
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
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
    public void editDriver(Driver driver) {
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
                driver.getFirstname(), driver.getLastname(), driver.getNickname(),
                driver.getPhone(), driver.getAddress(), driver.getCitizenId());
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
    }

    public void deleteDriver(String citizenId) {
        String sql = "delete from driver where citizen_id='" + citizenId + "'";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        assistant.execute(sql);
    }

    public void addDriverJob(Driver driver, Date startDate, Date endDate, JobType type) {
        String startS = formatter.format(startDate);
        String endS = formatter.format(endDate);
        String sql = String.format("insert into driver_job_schedule " +
                        "values (%s, %s, %s, %s)",
                driver.getCitizenId(), startS, endS, type.getId()+"");
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }

}
