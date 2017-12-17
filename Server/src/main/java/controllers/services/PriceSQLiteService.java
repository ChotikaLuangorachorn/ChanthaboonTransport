package controllers.services;

import models.Destination;
import models.PriceFactor;
import services.PriceService;

import java.util.Date;

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
}
