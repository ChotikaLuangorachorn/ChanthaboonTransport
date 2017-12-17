package services;

import models.Destination;
import models.Driver;
import models.Reservation;
import models.Van;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    boolean assignVan(List<Van> vans, Reservation reservation);
    boolean assignVan(List<Van> vans, String reservationId);
    boolean assignDriver(List<Driver> drivers, Reservation reservation);
    boolean assignDriver(List<Driver> drivers, String reservationId);



    boolean addMeeting(String meetingPlace, Date meetingTime, Reservation reservation);
    boolean addMeeting(String meetingPlace, Date meetingTime, String reservationId);
    boolean confirmDeposit(Reservation reservation, Date depositDate);

    boolean confirmDeposit(String reservationId, Date depositDate);
    List<Reservation> getReservations();

    Reservation getReservation(String reserveId);
    boolean addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit);

    boolean deleteReservation(Reservation reservation);
    boolean deleteReservation(String reservationId);
    List<Reservation> getHistoryReservation(String citizenId);
}
