package utils;

import models.Reservation;

import java.text.SimpleDateFormat;

public class ReservationDateFormatter {
    private static ReservationDateFormatter instance;
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static ReservationDateFormatter getInstance(){
        if (instance == null)
            instance = new ReservationDateFormatter();
        return instance;
    }

    public SimpleDateFormat getFormatter() {
        return formatter;
    }
}
