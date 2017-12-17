package controllers.services;

import models.Partner;
import services.PartnerService;

import java.util.List;

public class PartnerSQLiteService extends SQLiteService implements PartnerService {

    public PartnerSQLiteService(String url) {
        super(url);
    }

    @Override
    public List<Partner> getPartners() {
        return null;
    }

    @Override
    public void editPartner(Partner partner) {

    }

    @Override
    public void deletePartner(Partner partner) {

    }

    @Override
    public void deletePartner(int partnerId) {

    }
}
