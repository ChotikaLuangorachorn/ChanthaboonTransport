package controller;

import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.ReservationService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReservationController {
    private ReservationService reservationExecutor;
    public ReservationController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        reservationExecutor = (ReservationService) bf.getBean("ReservationService");
    }

    public void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, double price) {
        LocalDateTime localDate = LocalDateTime.now();
        Instant instant = Instant.from(localDate.atZone(ZoneId.systemDefault()));
        reservationExecutor.addReservation(customerId, vanAmt, destination, startDate, endDate, Date.from(instant), price, price / 2);
    }

    public List<Reservation> getHistoryReservation(String citizenId){
        return reservationExecutor.getHistoryReservation(citizenId);
    }

}
