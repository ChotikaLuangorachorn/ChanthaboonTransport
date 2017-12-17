package services;

import models.Reservation;
import models.Van;

import java.util.Date;
import java.util.List;

public interface ReservationService {
    void assignVan(List<Van> vans, Reservation reservation);
    void assignVan(List<Van> vans, String reservationId);

    void editReservation(Reservation reservation);


    void addMeeting(String meetingPlace, Date meetingTime, Reservation reservation);
    void addMeeting(String meetingPlace, Date meetingTime, String reservationId);
    void confirmDeposit(Reservation reservation, Date depositDate);

    void confirmDeposit(String reservationId, Date depositDate);
    List<Reservation> getReservations();

    Reservation getReservation(String reserveId);
}
