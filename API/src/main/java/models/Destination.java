package models;

public class Destination {
    private String province;
    private String district;
    private String place;
    private Double distance;

    public Destination(String province, String district, String place) {
        this.province = province;
        this.district = district;
        this.place = place;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getProvince() {
        return province;
    }

    public String getDistrict() {
        return district;
    }

    public String getPlace() {
        return place;
    }

    public Double getDistance() {
        return distance;
    }
}
