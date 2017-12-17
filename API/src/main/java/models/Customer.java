package models;

import java.io.Serializable;

public class Customer extends Person implements Serializable{
    private String address, phone, lineId;
    private int lastReserveId;


    public Customer(String id, String firstName, String lastName, String address, String phone, String lineId, int lastReserveId) {
        super(id, firstName, lastName);
        this.address = address;
        this.phone = phone;
        this.lineId = lineId;
        this.lastReserveId = lastReserveId;
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

    @Override
    public String toString() {
        return "Customer{" +
                "address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", lineId='" + lineId + '\'' +
                ", lastReserveId=" + lastReserveId +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
