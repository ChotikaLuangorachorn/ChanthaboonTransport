import controllers.SQLiteExecutor;
import controllers.services.PriceSQLiteService;
import models.PriceFactor;
import models.Schedule;
import services.PriceService;
import services.VanService;
import utils.ReservationDateFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FakeMain {
    public static void main(String[] args) throws ParseException {
        PriceService priceService = new PriceSQLiteService("vanScheduler.db");
        Map<String, Integer> vamAmt = new HashMap<>();
        vamAmt.put(VanService.VIP, 1);
        vamAmt.put(VanService.NORMAL, 2);
        Date startDate = new Date();
        Date endDate = new Date();
        endDate.setHours(endDate.getHours()+1);
        endDate.setDate(endDate.getDate()+1);
        double price = priceService.getPrice(vamAmt, startDate, endDate);
        System.out.println(price);
    }

    public static <T> T getT(String name, Class<T> type, T defualt){

        return null;
    }
}
