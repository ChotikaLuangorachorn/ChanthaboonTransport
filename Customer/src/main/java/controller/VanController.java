package controller;

import models.Customer;
import models.Destination;
import models.Reservation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PriceService;
import services.VanService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VanController {
    private VanService vanExecutor;
    public VanController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        vanExecutor = (VanService) bf.getBean("VanService");
    }

    public Map<String, Integer> getVanAvailable(Destination destination, Date startDate, Date endDate) {
        return vanExecutor.getVanAvailableAmount(destination, startDate, endDate);
    }


}
