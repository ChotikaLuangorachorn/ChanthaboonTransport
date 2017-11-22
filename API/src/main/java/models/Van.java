package models;

import java.io.Serializable;

public class Van implements Serializable {
    private String regisNumber;
    private String partnerId;
    private String type;
    private String name;

    public Van(String regisNumber, String partnerId, String type) {
        this.regisNumber = regisNumber;
        this.partnerId = partnerId;
        this.type = type;
    }

    public Van(String regisNumber, String partnerId, String type, String name) {
        this.regisNumber = regisNumber;
        this.partnerId = partnerId;
        this.type = type;
        this.name = name;
    }

    public String getRegisNumber() {
        return regisNumber;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
