import java.util.Date;

public class Reservation {
    private String customerId;
    private int amtVip, amtNormal;
    private Destination destination;
    private Date startDate, endDate;
    private double price;

    public Reservation(String customerId, int amtVip, int amtNormal, Destination destination, Date startDate, Date endDate, double price) {
        this.customerId = customerId;
        this.amtVip = amtVip;
        this.amtNormal = amtNormal;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
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
