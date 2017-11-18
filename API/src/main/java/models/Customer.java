package models;

import java.io.Serializable;

public class Customer implements Serializable{
    private String citizenId;
    private String firstName, lastName;
    private String address, phone, lineId;
    private int lastReserveId;

    public Customer(String citizenId, String firstName, String lastName, String address, String phone, String lineId, int lastReserveId) {
        this.citizenId = citizenId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.lineId = lineId;
        this.lastReserveId = lastReserveId;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getLineId() {
        return lineId;
    }

    public int getLastReserveId() {
        return lastReserveId;
    }

}
