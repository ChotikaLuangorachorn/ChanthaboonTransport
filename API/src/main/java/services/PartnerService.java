package services;

import models.Partner;

import java.util.List;

public interface PartnerService {

    List<Partner> getPartners();
    boolean editPartner(Partner partner);
    boolean deletePartner(Partner partner);
    boolean deletePartner(String partnerId);
}
