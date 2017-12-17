package controllers.services;

import models.Destination;
import models.PriceFactor;
import services.PriceService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class PriceSQLiteService extends SQLiteService implements PriceService{
    public PriceSQLiteService(String url) {
        super(url);
    }

    @Override
    public PriceFactor getPriceFactor() {
        return null;
    }

    @Override
    public void updatePriceFactor(PriceFactor factor) {

    }

    @Override
    public Date getMinimumDate(Destination destination, Date startDate) {
        return null;
    }

    @Override
    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        return 0;
    }

    @Override
    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        return 0;
    }

    @Override
    public List<String> getProvinces() {
        return null;
    }

    @Override
    public List<String> getDistricts(String province) {
        return null;
    }
}
