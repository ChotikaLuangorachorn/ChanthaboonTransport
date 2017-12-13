package utils;

import models.Reservation;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ReservationDateFormatter {
    private static ReservationDateFormatter instance;
    private SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private SimpleDateFormat uiDateFormatter = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
    private SimpleDateFormat uiTimeFullFormatter = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat uiTimeFormatter = new SimpleDateFormat("HH:mm");
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
    public SimpleDateFormat getUiTimeFullFormatter() {
        return uiTimeFullFormatter;
    }
}
