package services;

import models.Destination;
import models.PriceFactor;

import java.util.Date;

public interface PriceService {
    PriceFactor getPriceFactor();
    void updatePriceFactor(PriceFactor factor);
    Date getMinimumDate(Destination destination, Date startDate);
}
