package controllers.services;

import models.Destination;
import models.JobType;
import models.Schedule;
import models.Van;
import services.VanService;

import java.util.*;

public class VanSQLiteService extends SQLiteService implements VanService{
    public VanSQLiteService(String url) {
        super(url);
    }

    @Override
    public List<Van> getVans() {
        String sql = "select * from van where partner_id is null";
        List<Van> vans = new ArrayList<>();
        return executeQuery(sql, (resultSet -> {
            while(resultSet.next()){
                String regisId = resultSet.getString("regis_number");
                String type = resultSet.getString("type");
                String name = resultSet.getString("name");
                Van van = new Van(regisId, null, type, name);
                vans.add(van);
            }
            return vans;
        }), vans);
    }

    @Override
    public Map<String, List<Van>> getVanAvailable(Date startDate, Date endDate) {
        Map<String, List<Van>> available = new HashMap<>();
        available.put(VanService.VIP, new ArrayList<>());
        available.put(VanService.NORMAL, new ArrayList<>());
        String start = formatter.format(startDate);
        String end = formatter.format(endDate);
        String sql = String.format("select *\n" +
                        "from van\n" +
                        "where van.partner_id is null\n" +
                        "        and van.regis_number not in (select van.regis_number\n" +
                        "                                                                        from van\n" +
                        "                                                                        join van_reserve_schedule\n" +
                        "                                                                        on van.regis_number = van_reserve_schedule.regis_number\n" +
                        "                                                                        join reservation\n" +
                        "                                                                        on reservation.id = van_reserve_schedule.reservation_id\n" +
                        "                                                                        where strftime(\"%%Y-%%m-%%d\", reservation.end_working_date) >= date(\"%s\") \n" +
                        "                                                                                        and strftime(\"%%Y-%%m-%%d\", reservation.start_working_date) <= date(\"%s\"))\n" +
                        "        and van.regis_number not in (select van.regis_number\n" +
                        "                                                                        from van\n" +
                        "                                                                        join van_job_schedule\n" +
                        "                                                                        on van_job_schedule.regis_number = van.regis_number\n" +
                        "                                                                        where strftime(\"%%Y-%%m-%%d\", van_job_schedule.end_date) >= date(\"%s\") \n" +
                        "                                                                                        and strftime(\"%%Y-%%m-%%d\", van_job_schedule.start_date) <= date(\"%s\"))\n"
                , start, end, start, end);
        return executeQuery(sql, (resultSet -> {
            while (resultSet.next()){
                String id = resultSet.getString("regis_number");
                String type = resultSet.getString("type");
                String name = resultSet.getString("name");
                Van van = new Van(id, null, type, name);
                if (VanService.VIP.equals(type))
                    available.get(VanService.VIP).add(van);
                else
                    available.get(VanService.NORMAL).add(van);
            }
            return available;
        }), available);
    }

    @Override
    public Map<String, Integer> getVanAvailableAmount(Destination destination, Date startDate, Date endDate) {
        System.out.println("request getVanAvailableAmount");
        Map<String, Integer> amtMap = new HashMap<>();
        amtMap.put(VanService.VIP, 0);
        amtMap.put(VanService.NORMAL, 0);
        boolean possible = checkPossibleDay(destination, startDate, endDate);
        if (possible){
            String start = formatter.format(startDate);
            String end = formatter.format(endDate);
            String sql = String.format("select type, count(*) as amt\n" +
                    "from van\n" +
                    "where van.partner_id is null\n" +
                    "        and van.regis_number not in (select van.regis_number\n" +
                    "                                                                        from van\n" +
                    "                                                                        join van_reserve_schedule\n" +
                    "                                                                        on van.regis_number = van_reserve_schedule.regis_number\n" +
                    "                                                                        join reservation\n" +
                    "                                                                        on reservation.id = van_reserve_schedule.reservation_id\n" +
                    "                                                                        where strftime(\"%%Y-%%m-%%d\", reservation.end_working_date) >= date(\"%s\") \n" +
                    "                                                                                        and strftime(\"%%Y-%%m-%%d\", reservation.start_working_date) <= date(\"%s\"))\n" +
                    "        and van.regis_number not in (select van.regis_number\n" +
                    "                                                                        from van\n" +
                    "                                                                        join van_job_schedule\n" +
                    "                                                                        on van_job_schedule.regis_number = van.regis_number\n" +
                    "                                                                        where strftime(\"%%Y-%%m-%%d\", van_job_schedule.end_date) >= date(\"%s\") \n" +
                    "                                                                                        and strftime(\"%%Y-%%m-%%d\", van_job_schedule.start_date) <= date(\"%s\"))\n" +
                    "group by type", start, end, start, end);
            return executeQuery(sql, resultSet -> {
                while (resultSet.next()){
                    amtMap.put(resultSet.getString("type"), resultSet.getInt("amt"));
                }
                return amtMap;
            }, amtMap);
        }
        return null;
    }

    @Override
    public void editVan(Van van) {
        System.out.println("request edit van");
        String sql = String.format("update van " +
                        "set name='%s', partner_id=%s, type='%s' " +
                        "where regis_number='%s'",
                van.getName(),
                (van.getPartnerId()==null)?"null":"'"+van.getPartnerId()+"'",
                van.getType(), van.getRegisNumber());
        int result = executeUpdate(sql);
        System.out.println(result);
    }

    @Override
    public void deleteVan(Van van) {
        deleteVan(van.getRegisNumber());
    }

    @Override
    public void deleteVan(String regisNumber) {
        System.out.println("request deleteVan");
        System.out.println("regisNumber = " + regisNumber);
        String sql = "delete from van where regis_number='" + regisNumber + "'";
        int result = executeUpdate(sql);
        System.out.println(result);
    }

    @Override
    public Van getVan(String vanId) {
        String sql = "select * from van where regis_number='" + vanId + "'";
        return executeQuery(sql, (resultSet -> {
            if (resultSet.next()){
                String regisNumber = resultSet.getString("regis_number");
                String name = resultSet.getString("name");
                String partnerId = resultSet.getString("partner_id");
                String type = resultSet.getString("type");
                return new Van(regisNumber, partnerId, type, name);
            }
            return null;
        }), null);
    }

    @Override
    public List<Schedule> getVanSchedule(String regisNumber) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "select * " +
                "from van_job_schedule " +
                "join van_job_type " +
                "on van_job_schedule.type_id = van_job_type.id " +
                "where regis_number='"+regisNumber+"'";
        System.out.println("sql = " + sql);
        executeQuery(sql, (resultSet -> {
            while(resultSet.next()){
                Date startDate = formatter.parse(resultSet.getString("start_date"));
                Date endDate = formatter.parse(resultSet.getString("end_date"));
                String note = resultSet.getString("description");
                schedules.add(new Schedule(regisNumber, startDate, endDate, note, Schedule.JOB));
            }
            return null;
        }), null);
        String sql2 = "select reservation.id, reservation.start_working_date, reservation.end_working_date\n" +
                "from reservation\n" +
                "join van_reserve_schedule\n" +
                "on reservation.id = van_reserve_schedule.reservation_id\n" +
                "where regis_number = '" + regisNumber + "'";
        System.out.println(sql2);
        return executeQuery(sql2, (resultSet -> {
            while(resultSet.next()){
                Date startDate = formatter.parse(resultSet.getString("start_working_date"));
                Date endDate = formatter.parse(resultSet.getString("end_working_date"));
                String note = resultSet.getString("id");
                schedules.add(new Schedule(regisNumber, startDate, endDate, note, Schedule.RESERVE));
            }
            return schedules;
        }), schedules);
    }

    @Override
    public List<Schedule> getVanSchedule(Van van) {
        return getVanSchedule(van.getRegisNumber());
    }

    @Override
    public void deleteVanSchedule(Schedule schedule) {
        String sql;
        String startTime = formatter.format(schedule.getStartDate());
        String endTime = formatter.format(schedule.getEndDate());
        if (Schedule.JOB.equals(schedule.getType())){
            sql = "delete from van_job_schedule " +
                    "where regis_number='" + schedule.getId() + "' and " +
                    "start_date='" + startTime + "' and " +
                    "end_date='" + endTime + "' ";
        }else{
            sql = "delete from van_reserve_schedule " +
                    "where regis_number='" + schedule.getId() + "' and " +
                    "reservation_id='" + schedule.getNote() + "'";
        }
        int result = executeUpdate(sql);
        System.out.println("result = " + result);
    }

    @Override
    public void editVanSchedule(Schedule oldSchedule, Schedule newSchedule) {
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
                            "where regis_number='%s' and start_date='%s' and end_date='%s'",
                    newSchedule.getNote(), newStartTime, newEndTime, oldSchedule.getId(), oldStartTime, oldEndTime);
        }else{
            sql = String.format("update van_reserve_schedule " +
                    "set reservation_id='" + newSchedule.getNote() + "' " +
                    "where regis_number='" + oldSchedule.getId() + "' and " +
                    "reservation_id='" + oldSchedule.getNote() + "'");
        }
        int result = executeUpdate(sql);
        System.out.println("result = " + result);
    }

    @Override
    public List<JobType> getVanJobTypes() {
        List<JobType> jobTypes = new ArrayList<>();
        String sql = "select * from van_job_type";
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
    public void addVanJob(Van van, Date startDate, Date endDate, JobType type) {
        String startS = formatter.format(startDate);
        String endS = formatter.format(endDate);
        String sql = String.format("insert into van_job_schedule " +
                        "values ('%s', '%s', '%s', '%s')",
                van.getRegisNumber(), startS, endS, type.getId()+"");
        int result = executeUpdate(sql);
        System.out.println("result = " + result);
    }

    private boolean checkPossibleDay(Destination destination, Date startDate, Date endDate){
        double distance = getDistance(destination);
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) diff / (24*60*60*1000);
        if (distance/720 > days)
            return false;
        return true;
    }
    private double getDistance(Destination destination){
        String sql = "select distance from distance where province='" + destination.getProvince() + "' and district='" + destination.getDistrict() + "'";
        return executeQuery(sql, resultSet -> {
            if (resultSet.next())
                return resultSet.getDouble("distance");
            return 0.0;
        }, 0.0);
    }
}
