package models;

public class Van {
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
