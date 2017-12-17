package controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PriceService;

public class PriceController {
    private PriceService priceExecutor;
    public PriceController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        priceExecutor = (PriceService) bf.getBean("PriceService");
    }
}
