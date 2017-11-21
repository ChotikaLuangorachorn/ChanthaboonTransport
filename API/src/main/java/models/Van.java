package models;

import java.io.Serializable;

public class Van implements Serializable {
    private String regisNumber;
    private String partnerId;
    private String type;

    public Van(String regisNumber, String partnerId, String type) {
        this.regisNumber = regisNumber;
        this.partnerId = partnerId;
        this.type = type;
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
}
