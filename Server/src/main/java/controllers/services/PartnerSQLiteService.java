package controllers.services;

import controllers.assistants.UpdateExecutionAssistant;
import models.Partner;
import services.PartnerService;

import java.util.ArrayList;
import java.util.List;

public class PartnerSQLiteService extends SQLiteService implements PartnerService {

    public PartnerSQLiteService(String url) {
        super(url);
    }

    @Override
    public List<Partner> getPartners() {
        List<Partner> partners = new ArrayList<>();
        String sql = "select * from partner";
        return executeQuery(sql, resultSet -> {
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String fname = resultSet.getString("first_name");
                String lname = resultSet.getString("last_name");
                String company = resultSet.getString("company");
                String companyPhone = resultSet.getString("company_phone");
                // TODO partner last name
                Partner partner = new Partner(id, fname, lname, company, companyPhone);
                partners.add(partner);
            }
            return partners;
        }, partners);
    }

    @Override
    public boolean editPartner(Partner partner) {
        // TODO partner last name
        String sql = String.format("update partner " +
                        "set first_name='%s'," +
                        "last_name='%s',"+
                        "company='%s'," +
                        "company_phone='%s' " +
                        "where id='%s'",
                partner.getFirstName(), partner.getLastName(), partner.getCompany(),
                partner.getCompanyPhone(), partner.getId());
        int result = executeUpdate(sql);
        return result > 0;
    }

    @Override
    public boolean deletePartner(Partner partner) {
        return deletePartner(partner.getId());
    }

    @Override
    public boolean deletePartner(String partnerId) {
        System.out.println("request deletePartner");
        String sql = "delete from partner where id='" + partnerId + "'";
        int result = executeUpdate(sql);
        return result > 0;
    }
}
