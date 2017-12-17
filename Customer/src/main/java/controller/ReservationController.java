package controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.ReservationService;

public class ReservationController {
    private ReservationService reservationExecutor;
    public ReservationController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("customer_config.xml");
        reservationExecutor = (ReservationService) bf.getBean("ReservationService");
    }
}
