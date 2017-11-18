package models;

import java.util.Date;

public class Reservation {
    private String reserveId,customerId, meetingPlace;
    private int amtVip, amtNormal;
    private Destination destination;
    private Date startDate, endDate, reserveDate, meetingDate;
    private double price;

    public Reservation(String reserveId, String customerId, String meetingPlace, int amtVip, int amtNormal, Destination destination, Date startDate, Date endDate, Date reserveDate, Date meetingDate, double price) {
        this.reserveId = reserveId;
        this.customerId = customerId;
        this.meetingPlace = meetingPlace;
        this.amtVip = amtVip;
        this.amtNormal = amtNormal;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reserveDate = reserveDate;
        this.meetingDate = meetingDate;
        this.price = price;
    }

    public String getReserveId() {
        return reserveId;
    }

    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAmtVip() {
        return amtVip;
    }

    public void setAmtVip(int amtVip) {
        this.amtVip = amtVip;
    }

    public int getAmtNormal() {
        return amtNormal;
    }

    public void setAmtNormal(int amtNormal) {
        this.amtNormal = amtNormal;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
