package controllers;

import models.Driver;
import models.JobType;
import models.Reservation;
import models.Van;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PriceService;
import services.ReservationService;

import java.util.Date;
import java.util.List;

public class ReservationController{
    private ReservationService reservationExecutor;

    public ReservationController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        reservationExecutor = (ReservationService) bf.getBean("ReservationService");
    }

    public void assignDriver(List<Driver> drivers, Reservation reservation){
        reservationExecutor.assignDriver(drivers, reservation);
    }
    public void assignVan(List<Van> vans, Reservation reservation){
        reservationExecutor.assignVan(vans, reservation);
    }

    public void confirmDeposit(String reservationId, Date depositDate){
        reservationExecutor.confirmDeposit(reservationId, depositDate);
    }

    public List<Reservation> getReservations(){
        return reservationExecutor.getReservations();
    }

    public void deleteReservation(Reservation reservation){
        reservationExecutor.deleteReservation(reservation);
    }


    public void addMeeting(String meetingPlace, Date meetingTime, Reservation reservation){
        reservationExecutor.addMeeting(meetingPlace, meetingTime, reservation);
    }

}
