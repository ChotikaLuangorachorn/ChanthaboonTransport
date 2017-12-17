package controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PriceService;
import services.VanService;

public class VanController {
    private VanService vanExecutor;
    public VanController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        vanExecutor = (VanService) bf.getBean("VanService");
    }
}
