package controllers;

import models.Partner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.PartnerService;

import java.util.List;

public class PartnerController {
    private PartnerService partnerExecutor;
    public PartnerController() {
        ApplicationContext bf = new ClassPathXmlApplicationContext("manager_config.xml");
        partnerExecutor = (PartnerService) bf.getBean("PartnerService");
    }

    public List<Partner> getPartners(){
        return partnerExecutor.getPartners();
    }
    public void deletePartner(Partner partner){
        partnerExecutor.deletePartner(partner);
    }
    public void editPartner(Partner partner){
        partnerExecutor.editPartner(partner);
    }
}
