package services;

import models.Destination;
import models.Driver;
import models.Reservation;
import models.Van;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReservationService {
    void assignVan(List<Van> vans, Reservation reservation);
    void assignVan(List<Van> vans, String reservationId);
    void assignDriver(List<Driver> drivers, Reservation reservation);
    void assignDriver(List<Driver> drivers, String reservationId);



    void addMeeting(String meetingPlace, Date meetingTime, Reservation reservation);
    void addMeeting(String meetingPlace, Date meetingTime, String reservationId);
    void confirmDeposit(Reservation reservation, Date depositDate);

    void confirmDeposit(String reservationId, Date depositDate);
    List<Reservation> getReservations();

    Reservation getReservation(String reserveId);
    void addReservation(String customerId, Map<String, Integer> vanAmt, Destination destination, Date startDate, Date endDate, Date reserveDate, double price, double deposit);

    void deleteReservation(Reservation reservation);
    void deleteReservation(String reservationId);
    List<Reservation> getHistoryReservation(String citizenId);
}
