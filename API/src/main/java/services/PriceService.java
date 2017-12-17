package services;

import models.Destination;
import models.PriceFactor;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PriceService {
    PriceFactor getPriceFactor();
    void updatePriceFactor(PriceFactor factor);
    Date getMinimumDate(Destination destination, Date startDate);
    double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate);
    double getPrice(Map<String, Integer> vanAmt, Destination destination);
    List<String> getProvinces();
    List<String> getDistricts(String province);
}
