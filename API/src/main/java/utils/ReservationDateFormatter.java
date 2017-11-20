package utils;

import models.Reservation;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReservationDateFormatter {
    private static ReservationDateFormatter instance;
    private SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
    private SimpleDateFormat uiDateFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat uiTimeFormatter = new SimpleDateFormat("hh:mm:ss");
    public static ReservationDateFormatter getInstance(){
        if (instance == null)
            instance = new ReservationDateFormatter();
        return instance;
    }

    public SimpleDateFormat getDbFormatter() {
        return dbFormatter;
    }

    public SimpleDateFormat getUiDateFormatter() {
        return uiDateFormatter;
    }

    public SimpleDateFormat getUiTimeFormatter() {
        return uiTimeFormatter;
    }
}
