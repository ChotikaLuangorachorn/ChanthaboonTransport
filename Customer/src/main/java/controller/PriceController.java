package controller;

import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PriceService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PriceController {
    private PriceService priceExecutor;
    public PriceController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        priceExecutor = (PriceService) bf.getBean("PriceService");
    }

    public Date getMinimumDate(Destination destination, Date startDate){
        return priceExecutor.getMinimumDate(destination, startDate);
    }

    public double getPrice(Map<String, Integer> vanAmt, Date startDate, Date endDate) {
        return priceExecutor.getPrice(vanAmt, startDate, endDate);
    }

    public double getPrice(Map<String, Integer> vanAmt, Destination destination) {
        return priceExecutor.getPrice(vanAmt, destination);
    }

    public List<String> getProvince(){
        return priceExecutor.getProvinces();
    }

    public List<String> getdistrict(String province){
        return priceExecutor.getDistricts(province);
    }

}
