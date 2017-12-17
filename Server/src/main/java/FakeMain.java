import controllers.SQLiteExecutor;
import models.PriceFactor;
import models.Schedule;
import utils.ReservationDateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FakeMain {
    public static void main(String[] args) throws ParseException {
//        String s = String.format("hello %s", null);
//        System.out.println(s);
//        SQLiteExecutor executor = new SQLiteExecutor();
////        PriceFactor priceFactor = executor.getPriceFactor();
////        System.out.println("priceFactor = " + priceFactor);
//        SimpleDateFormat format = ReservationDateFormatter.getInstance().getDbFormatter();
////        Schedule schedule = new Schedule("กส-3993", format.parse("2017-11-28 09:05:00"), format.parse("2017-11-28 13:00:00"), "ซ่อมบำรุง", Schedule.JOB);
//        Schedule schedule = new Schedule("กค-4983", new Date(), new Date(), "1", Schedule.RESERVE);
//        executor.deleteVanSchedule(schedule);
//        Date date = new Date();
//        System.out.println("date = " + date);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.add(Calendar.DATE, 18);
//        System.out.println(calendar.getTime());
//        getT("name", ArrayList.class, )
    }

    public static <T> T getT(String name, Class<T> type, T defualt){

        return null;
    }
}
