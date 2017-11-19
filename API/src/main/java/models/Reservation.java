package models;

import java.io.Serializable;
import java.util.Date;

public class Reservation implements Serializable{
    private String reserveId,customerId, meetingPlace, status;
    private int amtVip, amtNormal;
    private Destination destination;
    private Date startDate, endDate, reserveDate, meetingDate;
    private double price;

    public Reservation(String reserveId, String customerId, String meetingPlace, int amtVip, int amtNormal, Destination destination, Date startDate, Date endDate, Date reserveDate, Date meetingDate, double price, String status) {
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
        this.status = status;
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

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public Date getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(Date reserveDate) {
        this.reserveDate = reserveDate;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
