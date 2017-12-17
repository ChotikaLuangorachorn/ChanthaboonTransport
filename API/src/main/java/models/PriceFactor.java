package models;

import java.io.Serializable;

public class PriceFactor implements Serializable{
    public static int DAY = 1;
    public static int DISTANCE = 2;
    public static int VIP = 10;
    public static int NORMAL = 20;
    public static int BASE = 100;
    public static int RATE = 200;
    public static int FREE = 300;

    private double dayVipBase;
    private double dayNormalBase;
    private double dayVipRate;
    private double dayNormalRate;
    private double dayVipFree;
    private double dayNormalFree;

    private double distVipBase;
    private double distNormalBase;
    private double distVipRate;
    private double distNormalRate;
    private double distVipFree;
    private double distNormalFree;

    public PriceFactor() {
    }

    public PriceFactor(double dayVipBase, double dayNormalBase, double dayVipRate, double dayNormalRate, double dayVipFree, double dayNormalFree, double distVipBase, double distNormalBase, double distVipRate, double distNormalRate, double distVipFree, double distNormalFree) {
        this.dayVipBase = dayVipBase;
        this.dayNormalBase = dayNormalBase;
        this.dayVipRate = dayVipRate;
        this.dayNormalRate = dayNormalRate;
        this.dayVipFree = dayVipFree;
        this.dayNormalFree = dayNormalFree;
        this.distVipBase = distVipBase;
        this.distNormalBase = distNormalBase;
        this.distVipRate = distVipRate;
        this.distNormalRate = distNormalRate;
        this.distVipFree = distVipFree;
        this.distNormalFree = distNormalFree;
    }

    public double getFactor(int reserveType, int vanType, int value){
        int factor = reserveType + vanType + value;

        switch (factor){
            case 111:
                return dayVipBase;
            case 112:
                return distVipBase;
            case 121:
                return dayNormalBase;
            case 122:
                return distNormalBase;
            case 211:
                return dayVipRate;
            case 212:
                return distVipRate;
            case 221:
                return dayNormalRate;
            case 222:
                return distNormalRate;
            case 311:
                return dayVipFree;
            case 312:
                return distVipFree;
            case 321:
                return dayNormalFree;
            case 322:
                return distNormalFree;
            default:
                 return 0;
        }
    }
    public void setFactor(int reserveType, int vanType, int value, double factor){
        int factorV = reserveType + vanType + value;
//        System.out.println("setFactor " + factorV + " " + factor);
        switch (factorV){
            case 111:
                dayVipBase = factor;
                break;
            case 112:
                distVipBase = factor;
                break;
            case 121:
                dayNormalBase = factor;
                break;
            case 122:
                distNormalBase = factor;
                break;
            case 211:
                dayVipRate = factor;
//                System.out.println(dayVipBase + " " + factor);
                break;
            case 212:
                distVipRate = factor;
                break;
            case 221:
                dayNormalRate = factor;
                break;
            case 222:
                distNormalRate = factor;
                break;
            case 311:
                dayVipFree = factor;
                break;
            case 312:
                distVipFree = factor;
                break;
            case 321:
                dayNormalFree = factor;
                break;
            case 322:
                distNormalFree = factor;
                break;
        }
    }

    @Override
    public String toString() {
        return "PriceFactor{" +
                "dayVipBase=" + dayVipBase +
                ", dayNormalBase=" + dayNormalBase +
                ", dayVipRate=" + dayVipRate +
                ", dayNormalRate=" + dayNormalRate +
                ", dayVipFree=" + dayVipFree +
                ", dayNormalFree=" + dayNormalFree +
                ", distVipBase=" + distVipBase +
                ", distNormalBase=" + distNormalBase +
                ", distVipRate=" + distVipRate +
                ", distNormalRate=" + distNormalRate +
                ", distVipFree=" + distVipFree +
                ", distNormalFree=" + distNormalFree +
                '}';
    }
}
