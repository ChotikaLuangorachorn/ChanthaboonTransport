package models;

import java.io.Serializable;
import java.util.Date;

public class Driver implements Serializable {
    private String citizenId;
    private String driverLicense;
    private Date dateOfBirth;
    private String firstname;
    private String lastname;
    private String nickname;
    private String phone;
    private String address;

    public Driver(String citizenId, String driverLicense, Date dateOfBirth, String firstname, String lastname, String nickname, String phone, String address) {
        this.citizenId = citizenId;
        this.driverLicense = driverLicense;
        this.dateOfBirth = dateOfBirth;
        this.firstname = firstname;
        this.lastname = lastname;
        this.nickname = nickname;
        this.phone = phone;
        this.address = address;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
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
                "citizenId='" + citizenId + '\'' +
                ", driverLicense='" + driverLicense + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
