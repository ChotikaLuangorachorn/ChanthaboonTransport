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
    public void assignDriver(List<Driver> drivers, String reservationId) {
        String sql = String.format("insert into driver_reserve_schedule " +
                "select driver.citizen_id, '%s' " +
                "from driver " +
                "where driver.citizen_id in ", reservationId);
        List<String> driverIds = new ArrayList<>();
        for (Driver driver:drivers)
            driverIds.add("'" + driver.getCitizenId() + "'");
        sql += "(" + String.join(",", driverIds) + ")";
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
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
                    "where citizen_id='" + schedule.getId() + "' and " +
                    "start_date='" + startTime + "' and " +
                    "end_date='" + endTime + "' ";
        }else{
            sql = "delete from driver_reserve_schedule " +
                    "where citizen_id='" + schedule.getId() + "' and " +
                    "reservation_id='" + schedule.getNote() + "'";
        }
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }

    public void editDriverSchedule(Schedule oldSchedule, Schedule newSchedule) {
        String sql;
        if (Schedule.JOB.equals(newSchedule.getType())){
            String oldStartTime = formatter.format(oldSchedule.getStartDate());
            String oldEndTime = formatter.format(oldSchedule.getEndDate());
            String newStartTime = formatter.format(newSchedule.getStartDate());
            String newEndTime = formatter.format(newSchedule.getEndDate());
            sql = String.format("update van_job_schedule\n" +
                            "set type_id=(select id\n" +
                            "from van_job_type\n" +
                            "where description='%s'), start_date='%s', end_date='%s'\n" +
                            "where ctizen_id='%s' and start_date='%s' and end_date='%s'",
                    newSchedule.getNote(), newStartTime, newEndTime, oldSchedule.getId(), oldStartTime, oldEndTime);
        }else{
            sql = String.format("update van_reserve_schedule " +
                    "set reservation_id='" + newSchedule.getNote() + "' " +
                    "where citizen_id='" + oldSchedule.getId() + "' and " +
                    "reservation_id='" + oldSchedule.getNote() + "'");
        }
        UpdateExecutionAssistant assistant = new UpdateExecutionAssistant(url);
        int result = assistant.execute(sql);
        System.out.println("result = " + result);
    }
}
