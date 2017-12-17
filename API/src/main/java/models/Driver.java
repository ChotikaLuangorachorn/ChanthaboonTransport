package models;

import java.io.Serializable;
import java.util.Date;

public class Driver extends Person implements Serializable {
    private String driverLicense;
    private Date dateOfBirth;
    private String nickname;
    private String phone;
    private String address;


    public Driver(String id, String firstName, String lastName, String driverLicense, Date dateOfBirth, String nickname, String phone, String address) {
        super(id, firstName, lastName);
        this.driverLicense = driverLicense;
        this.dateOfBirth = dateOfBirth;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }


    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverLicense='" + driverLicense + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
