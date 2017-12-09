package controllers.delegations;

import controllers.assistants.QueryExecutionAssistant;
import controllers.assistants.UpdateExecutionAssistant;
import models.Driver;
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
        List<Schedule> schedules = new ArrayList<>();
        String sql = "select * " +
                "from driver_job_schedule " +
                "join driver_job_type " +
                "on driver_job_schedule.type_id = driver_job_type.id " +
                "where citizen_id='"+citizenId+"'";
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
                "where citizen_id = '" + citizenId + "'";
        System.out.println(sql2);
        return assistant.execute(sql2, (resultSet -> {
            while(resultSet.next()){
                Date startDate = formatter.parse(resultSet.getString("start_working_date"));
                Date endDate = formatter.parse(resultSet.getString("end_working_date"));
                String note = resultSet.getString("id");
                schedules.add(new Schedule(citizenId, startDate, endDate, note, Schedule.RESERVE));
            }
            return schedules;
        }), schedules);
    }
}
