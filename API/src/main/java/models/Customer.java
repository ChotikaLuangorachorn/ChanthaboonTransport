package models;

public class Customer {
    private String citizenId;
    private String firstName, lastName;
    private String address, phone, lineId;
    private String lastReserve;

    public Customer(String citizenId, String firstName, String lastName, String address, String phone, String lineId, String lastReserve) {
        this.citizenId = citizenId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.lineId = lineId;
        this.lastReserve = lastReserve;
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

    public String getLastReserve() {
        return lastReserve;
    }

}
