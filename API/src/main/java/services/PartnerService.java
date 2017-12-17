package services;

import models.Partner;

import java.util.List;

public interface PartnerService {

    List<Partner> getPartners();
    void editPartner(Partner partner);
    void deletePartner(Partner partner);
    void deletePartner(String partnerId);
}
