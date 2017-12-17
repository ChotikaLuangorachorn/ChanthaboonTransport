package controllers;

import models.PriceFactor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PartnerService;
import services.PriceService;

public class PriceController {
    private PriceService priceExecutor;
    public PriceController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        priceExecutor = (PriceService) bf.getBean("PriceService");
    }

    public PriceFactor getPriceFactor(){
        return priceExecutor.getPriceFactor();
    }
    public void updatePriceFactor(PriceFactor priceFactor){
        priceExecutor.updatePriceFactor(priceFactor);
    }
}
